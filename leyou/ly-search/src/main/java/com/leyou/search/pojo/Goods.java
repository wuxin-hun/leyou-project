package com.leyou.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Goods {
    @Id
    private Long id; // spuId
    @Field(type = FieldType.text, analyzer = "ik_max_word")
    private String all; // 所有需要被搜索的信息，包含标题，分类，甚至品牌
    @Field(type = FieldType.keyword, index = false)
    private String subTitle;// 卖点，仅仅用来展示的

    //    下面用来过滤的。
    private Long brandId;// 品牌id
    private Long cid1;// 1级分类id
    private Long cid2;// 2级分类id
    private Long cid3;// 3级分类id
    private Date createTime;// 创建时间有一个发布时间用来过滤的
    // 价格，价格也是过滤之一，价格是一个集合，对应Json是数组，对应elasticsearch也是数组，因为我们存入的是SPU，多个SKU，是一个集合
    private Set<Long> price;

    // sku信息的json结构。因为图片的价格和标题都是SKU只是展示不需要根据价格过滤，标题只是展示字段，既不需要搜索也不需要过滤，只是展示的结果。
//    这样的话，就可以在刚进入界面的时候就可以直接加载商品信息，如果没有这个字段就需要异步加载影响体验。
    @Field(type = FieldType.keyword, index = false)
    private String skus;
    private Map<String, Object> specs;// 可搜索的规格参数，key是参数名，值是参数值
}