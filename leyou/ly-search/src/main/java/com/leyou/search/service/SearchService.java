package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.repository.GoodsRepository;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 将查询出的结果封装成Goods对象添加到索引库里面去
 * @Author TT Hun
 * @Data 2020/3/29 22:04
 */
@Service
public class SearchService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsRepository repository;

    /**
     * 给我一个spu商品，根据spu查询商品的其他信息来返回。
     * 为什么是传入一个spu呢，因为索引库的单位是spu，每一个spu对应一个goods。返回一个goods对象
     *
     * @param spu
     * @return
     */
    public Goods buildsGoods(Spu spu) {

        Long spuId = spu.getId();
//        查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categories)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
//        查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        if (brand == null) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
//        搜索字段,中间的空格是分隔符
        String all = spu.getTitle() + StringUtils.join(names, " ") + brand.getName();


//        搜索SKU的价格，需要查询SKU
        List<Sku> skuList = goodsClient.querySkuBySpuId(spu.getId());
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
//        对SKU进行处理，map可以看作一个sku。图省事，将map当成简化的sku
        List<Map<String, Object>> skus = new ArrayList<>();
//        价格集合
        Set<Long> priceList = new HashSet<>();
        for (Sku sku : skuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("images", StringUtils.substringBefore(sku.getImages(), ","));//每个sku只需要一张图片就够了，这边get到的images有很多很图片，
            skus.add(map);
            priceList.add(sku.getPrice());
        }

//        规格参数。分别查询规格参数，和商品详情
        HashMap<String, Object> specs = new HashMap<>();
//        查询规格参数，只需要传入分类绑定的是cid3，能够搜索的规格参数用true。
        List<SpecParam> params = specificationClient.queryParamList(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
//        查询商品详情
        SpuDetail spuDetail = goodsClient.queryDetailById((Long)spu.getId());
//        获取通用规格参数，这个就直接就是一个json结构，但是json不好从里拿到数据，需要转成对象或者map
        Map<Long, String> genericSpec = JsonUtils.toMap(spuDetail.getGeneric_spec(), Long.class, String.class);
//        获取特有规格参数，特有规格参数里的格式是json里面报了一个List。所以可能获得的是一个List,就不能单纯的使用toMap来转换了。
        String json = spuDetail.getSpecial_spec();
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(json, new TypeReference<Map<Long, List<String>>>() {
        });
//        规格参数,key是规格参数的名字，值是规格参数的值
        Map<String, Object> spec = new HashMap<>();
        for (SpecParam param : params) {
//            规格名称
            String key = param.getName();
            Object value = "";
//            值去哪里取到取决于这个参数是通用规格参数还是特有规格参数所以需要做判断
            if (param.getGeneric()) {
                value = genericSpec.get(param.getId());
//                判断是否是数值类型
                if (param.getNumeric()) {
//                    处理成段,覆盖之前的value值，怎么处理成段呢，首先值是object类型，需要把他转成数值类型，然后Param里有segment需要对他以逗号进行分割。变成好多个段，
//                    然后再以-进行切割得到开始值和结束值，然后判断他到底在那个范围内找到了就存进去返回。这地方是直接使用已经写好的方法。
                    value = chooseSegment(value.toString(), param);
//                    这样处理之后就变成了一个段
                }
            } else {
//                特有规格参数就是一个集合，不是字符串，不需要处理了
                value = specialSpec.get(param.getId());
            }
//            存入map
            specs.put(key, value);
//            但是规格参数虽然存进去了，但是如果是电池容量或者屏幕大小，他就是一个段，多少-多少。
//            在搜索的情况下，不是吧所有的段都展示出来，二十在当前搜索情况下的有的分段，用来展示。
//            那么怎么知道呢，要想知道有哪些段。网数据库存的时候就不要存储具体的值了，直接存储段就可以了。点击按钮时候，做不用做范围查询了，做精确匹配就可以了，知道有几个段就是做聚合就可以了


        }


        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setAll(all);//搜索字段，包含标题，分类，品牌，规格等还可以有其他的这里就写这么多
        goods.setPrice(priceList);//所有sku价格的集合
        goods.setSkus(JsonUtils.toString(skus));//所有sku集合的json格式
        goods.setSpecs(specs);//所有可搜索的规格参数
        goods.setSubTitle(spu.getSubTitle());


        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> search(SearchRequest request) {
        int page = request.getPage();
        int size = request.getSize();
//        创建查询构造器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
//        再最开始加上一个结果过滤，只需要下面的不需要别的
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","subTitle","skus"},null));
//            做分页
        queryBuilder.withPageable(PageRequest.of(page,size));

//        坐过滤，所有字段都放到all里面，所以搜索字段是一个all。值是request.getKey()。所有内容都再all里面，
//        值就是key，查询条件也有了。
        queryBuilder.withQuery(QueryBuilders.matchQuery("all",request.getKey()));
//        查询
        Page<Goods> result = repository.search(queryBuilder.build());
//        解析结果
//        获取
        long total = result.getTotalElements();
        int totalPages = result.getTotalPages();
        List<Goods> goodList = result.getContent();//当前页结果

        return new PageResult(total,totalPages,goodList);
    }

    public void createOrUpdateIndex(Long spuId) {
//        这个地方有可能出现异常，这个地方我们不需要try，因为一旦失败了，返回给上一层的消息接受服务，他会触发mq的重试机制。
//        查询spu
//        Spu spu = goodsClient.querySpuById(spuId);
//        构建goods对象
//        Goods goods = buildsGoods(spu);
//        存入索引库
//        repository.save(goods);
    }

    public void deleteIndex(Long spuId) {
        repository.deleteById(spuId);
    }
}
