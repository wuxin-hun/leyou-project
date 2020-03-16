package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/11/30 23:37
 */
public interface BrandMapper extends Mapper<Brand> {

//    这个的意思是除了通用mapper里面自带的一些api的使用，我们还可以通过注解使用SQL语句
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Select("SELECT b.id,b.name,b.letter,b.image FROM  tb_brand b INNER JOIN tb_category_brand cb WHERE cb.category_id = #{cid}")
    List<Brand> queryByCategoryId(@Param("cid") Long cid);
}