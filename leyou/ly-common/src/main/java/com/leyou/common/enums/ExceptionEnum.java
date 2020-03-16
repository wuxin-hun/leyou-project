package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/11/27 1:11
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
//枚举类，指的是具有固定实例个数的类，正常类可以创建无数个实例，这些实例是提前创建好的
//类似于单例，别人不许new只允许自己New，枚举构造函数默认是私有别人不能访问
public enum ExceptionEnum {
//    这个就是私有的静态的构造多个对象构造以逗号隔开
    PRICE_COUNT_BE_NULL(400,"价格不能为空"),

    CATEGORY_NOT_FOUND(404,"商品分类没有查询到"),
    //    这个分号必须写
    BRAND_NOT_FOUND(404,"品牌没有查询到"),
    BRAND_SAVE_ERROR(500,"服务器新增品牌失败"),
    CATEGORY_BRAND_SAVE_ERROR(500,"新增品牌分类中间表失败"),
    UPLOAD_FILE_ERRO(500,"文件上传失败"),
    INVALID_FILE_TYPE(400,"文件上传失败"),
    SPEC_GROUP_NOT_FOUND(404,"商品规格组不存在"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数不存在"),
    GOODS_NOT_FOUND(404,"商品不存在"),
    GOODS_SAVE_ERROR(500,"新增商品失败"),
    GOODS_DETAIL_NOT_FOUND(404,"商品不存在"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU不存在"),
    GOODS_STOCK_NOT_FOUND(404,"商品STOCK不存在"),
    GOODS_UPDATE_ERROR(500,"商品SKU修改失败"),
    GOODS_ID_CANNOT_BE_NULL(400,"商品ID不能为空"),


    ;
    private int code;
    private String msg;
}
