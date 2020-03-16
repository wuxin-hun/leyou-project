package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name="tb_spu_detail")
public class SpuDetail {
    @Id//这个spuId不是自增的而是和Spu中的id关联的，所以不用自增的注解
    private Long spuId;// 对应的SPU的id
    private String description;// 商品描述
    private String special_spec;// 商品特殊规格的名称及可选值模板
    private String generic_spec;// 商品的全局规格属性
    private String packingList;// 包装清单
    private String afterService;// 售后服务

}