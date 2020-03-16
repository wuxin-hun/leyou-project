package com.leyou.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Description 这个pojo主要用来接受前台发送过来的json串。
 * @Author TT Hun
 * @Data 2019/12/9 23:51
 */
@Data
@Table(name = "tb_spu")
public class Spu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long brandId;
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private String title;// 标题
    private String subTitle;// 子标题
    private Boolean saleable;// 是否上架

    @JsonIgnore//在json序列化时将java bean中的一些属性忽略掉，序列化和反序列化都受影响。使用方法：
    // 一般标记在属性或者方法上，返回的json数据即不包含该属性。
//    不想返回的字段就是用这个JsonIgnore,当返回界面的时候可以忽略这个字段
    private Boolean valid; //是否有效，逻辑删除用
    private Date createTime;//创建时间
    @JsonIgnore
    private Date lastUpdateTime;

    //这个地方为了图简便，在使用过程中直接添加了bname和cname两个字段，其实在使用过程中应该是VO和pojo相互转换的。
//    PO也就是Pojo是和数据库一一对应的管理，而VO是和前台页面一一对应的关系。由于下面2个字段不是数据库字段，所以在使用过程
//    中使用了Transient这个注解，告诉通用Mapper它不是数据库的字段
    @Transient
    private String bname;
    @Transient
    private String cname;

//这个地方有2个List原因是因为需要接收前台传来的json串，前台传过来的json串里面包含了2个数组，到后台接受就需要用集合来接收
    @Transient
    private List<Sku> skus;
    @Transient
    private SpuDetail spuDetail;
}
