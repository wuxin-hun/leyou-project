package com.leyou.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Description 注意在这个表里的数据都是空的因为视频中没有给数据，我只是单纯的建表了没有完整数据，只有手机一栏才有数据
 * @Author TT Hun
 * @Data 2019/12/8 12:02
 */
@Data
@Table(name = "tb_spec_param")
public class SpecParam {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long cid;
    private Long groupId;
    private String name;
    @Column(name="`numeric`")//这个注解的意思是吧numeric通过转义的方式把它变成普通字符串，中间是飘字符，
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;

}
