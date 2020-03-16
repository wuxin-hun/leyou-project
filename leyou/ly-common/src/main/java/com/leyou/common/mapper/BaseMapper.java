package com.leyou.common.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Description 这个接口是用来继承一些Mapper，然后在下面项目里面继承就不需要一次写三个Mapper了。
 * 但是下面这个的InsertListMapper：批量插入有2个。一个是主键名字只能是ID，一个是可以自己设置。
 * @Author TT Hun
 * @Data 2019/12/14 18:02
 */
//这个注解的意思是加了这个注解之后可以扫描的到
@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, IdListMapper<T,Long>, InsertListMapper<T> {
}