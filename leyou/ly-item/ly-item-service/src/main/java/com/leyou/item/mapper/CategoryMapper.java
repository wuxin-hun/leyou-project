package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/11/29 0:48
 */
//继承2个Mapper，一个是为了操作Category，一个是为了批量操作Category，然后第二个参数是主键的类型
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long > {


}