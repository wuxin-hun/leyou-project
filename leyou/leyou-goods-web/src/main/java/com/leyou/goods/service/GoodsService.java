package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2020/4/8 15:22
 */
@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;

    /**
     * 构建一个通过spuId查询出来的的静态页面需要的内容的集合
     * @param spuId
     * @return
     */
    public Map<String,Object> loadData(Long spuId){
        Map<String ,Object> model = new HashMap<>();
//        根据spuid查询spu
        Spu spu = goodsClient.querySpuById(spuId);

//        查询spuDetail
        SpuDetail spuDetail = this.goodsClient.queryDetailById(spuId);

//        查询分类Map<String,Object>
        List<Long> cids = Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3());
        List<Category> names = this.categoryClient.queryCategoryByIds(cids);
//        初始化一个分类的map
        List<Map<String,Object>> categories = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < cids.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }

//        查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());


//        查询skus
//        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);

        List<Sku> skus = new ArrayList<>();

//        查询规格参数组
        ResponseEntity<List<SpecGroup>> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());

//        查询特殊规格参数，处理成一个map结构
//        List<SpecParam> params = this.specificationClient.queryParams(null,spu.getCid3(),false,null);

        ArrayList<SpecParam> params = new ArrayList<>();

//        初始化特殊规格参数的map
        Map<Long,String> paramMap = new HashMap<>();
        params.forEach(param->{
            paramMap.put(param.getId(),param.getName());
        });


        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("categories",categories);
        model.put("brand",brand);
        model.put("skus",skus);
        model.put("groups",groups);
        model.put("paramMap",paramMap);

        return model;



    }

}
