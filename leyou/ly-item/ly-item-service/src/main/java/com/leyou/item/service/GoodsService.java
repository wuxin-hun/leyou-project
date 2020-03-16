package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 对SPu和Spudetail进行查询
 * @Author TT Hun
 * @Data 2019/12/10 0:40
 */
@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SpuDetailMapper detailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;


    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
//        分页
        PageHelper.startPage(page, rows);
//        过滤
        Example example = new Example(Spu.class);
//        条件过滤，搜索字段过滤
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
//        上下架过滤，因为是布尔值所以只要不等于空，就有值，有值就
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
//        默认根据商品更新时间排序，本来没有，但是可以写
        example.setOrderByClause("last_update_time DESC");
//        进行查询
        List<Spu> spus = spuMapper.selectByExample(example);

//        判断
        if (CollectionUtils.isEmpty(spus)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }

//        解析分类和品牌的名称
        loadCategoryAndBrandName(spus);
//        解析分页结果
        PageInfo<Spu> info = new PageInfo<>(spus);

        return new PageResult<>(info.getTotal(), spus);
    }

    private void loadCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
//            处理分类名称，得到category的集合，但是我们要的不是分类的集合，我们要的是名称所形成的字符串拼接
//            通过流处理字符串拼接节省空间
//            queryByIds是根据几个id查询，也就是根据一个list查询，然后拼接到一起。然后获得List集合用stream流去处理，
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            //join方法告诉一个集合，再传入一个以什么分割。把一个集合拼成一个字符串的效果。
            spu.setCname(StringUtils.join(names, "/"));
//            处理品牌名称
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());

        }
    }

    @Transactional
    public void saveGoods(Spu spu) {
//        新增spu
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());//意思一下随便get一个
        spu.setId(null);//自增逐渐，不让他自己处理
        spu.setSaleable(true);//默认上架
        spu.setValid(false);//是否删除默认不删除

        int count = spuMapper.insert(spu);
        if (count == 1) {
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }

//        定义库存的集合
        List<Stock> stockList = new ArrayList<>();
//        新增spudetail
        SpuDetail detail = spu.getSpuDetail();
        detail.setSpuId(spu.getId());
        detailMapper.insert(detail);
//        新增sku，新增sku这块也可以向新增库存一样使用批量新增，但是批量新增就下面sku.getId就不能直接获得id了。
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getLastUpdateTime());
            sku.setSpuId(spu.getId());

            count = skuMapper.insert(sku);
            if (count != 1) {
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
//        新增库存stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            stockList.add(stock);
        }
//    批量新增库存，调用stockMapper
        stockMapper.insertList(stockList);

    }


    public SpuDetail queryDetailById(Long spuId) {
        SpuDetail detail = detailMapper.selectByPrimaryKey(spuId);
        if (detail == null) {
            throw new LyException(ExceptionEnum.PRICE_COUNT_BE_NULL);
        }
        return null;
    }

    public List<Sku> querySkuBySpuId(Long spuId) {
//        查询sku
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
//        查询库存，根据SKUid来查询。
//        for (Sku s : skuList) {
////            skuid就是stockid，得到一个库存
//            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
//            if(stock==null){
//                throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
//            }
//            s.setStock(stock.getStock());
//        }
//        上面写法可以换成下面来写。

        List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stockList = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnum.GOODS_SKU_NOT_FOUND);
        }
//      把stock变成一个map，其key是:sku的id。取出库存就直接根据Id取出来一条。
        Map<Long, Integer> stockMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skuList.forEach(s -> s.setStock(stockMap.get(s.getId())));//就把库存存进去了

        return skuList;
    }

    @Transactional
    public void updateGoods(Spu spu) {
        if(spu.getId() == null){
            throw  new LyException(ExceptionEnum.GOODS_ID_CANNOT_BE_NULL);
        }
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
//        查询sku
        List<Sku> skuList = skuMapper.select(sku);
        if (!CollectionUtils.isEmpty(skuList)) {
//        删除sku
            skuMapper.delete(sku);
//        删除stock
            List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
            stockMapper.deleteByIdList(ids);
        }
//        修改spu
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        int count = spuMapper.updateByPrimaryKeySelective(spu);
        if(count!=1){
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
//        修改detail
         count = detailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(count!=1){
            throw new LyException(ExceptionEnum.GOODS_UPDATE_ERROR);
        }
//        新增sku和stock
        saveSkuAndStock(spu);
    }

    @Transactional
    public void saveSkuAndStock(Spu spu) {
//        定义库存的集合
        List<Stock> stockList = new ArrayList<>();
//        新增spudetail
        SpuDetail detail = spu.getSpuDetail();
        detail.setSpuId(spu.getId());
        detailMapper.insert(detail);
//        新增sku，新增sku这块也可以向新增库存一样使用批量新增，但是批量新增就下面sku.getId就不能直接获得id了。
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getLastUpdateTime());
            sku.setSpuId(spu.getId());

           int  count = skuMapper.insert(sku);
            if (count != 1) {
                throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
            }
//        新增库存stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            stockList.add(stock);
        }
//    新增sku和stock
        stockMapper.insertList(stockList);

    }

}
