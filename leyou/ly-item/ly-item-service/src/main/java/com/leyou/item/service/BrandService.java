package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/11/30 23:37
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
//        分页tk.mapper写了一个分页助手，有个Pagehelper的依赖，没有通用Mapper一样可以使用
//        对分页进行拦截，然后自动的在后面拼上Limit语句，分页就不需要我们去处理了。
        PageHelper.startPage(page, rows);
//        过滤
        /**
         *  where 'name' like '%x%' or letter = 'x' order by desc
         */
//        过滤条件,传入Brand类的字节码就可以通过反射得到这个表的名字主键等各个信息。
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
//            通过example实例创建条件
            example.createCriteria().orLike("name", "%" + key + "%").orEqualTo("letter", key.toUpperCase());
        }
//        排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        System.out.println("example拼接的是==========================" + example);
//        查询，在查询之后
        List<Brand> list = brandMapper.selectByExample(example);
        System.out.println("查询出来的list=======================" + list.toString());
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
//        PageResult需要2个参数，总条数，当前页数据，list是当前页数据，总条数
        PageInfo<Brand> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), list);
    }

    @Transactional//因为太复杂了所以加上事务
    public void saveBrand(Brand brand, List<Long> cids){
//        insert有2个api，一个是insert一个是selectInsert，这个是有选择的插入，也就是如果有空的就不插入。
        brand.setId(null);
//        这个insert是插入成功返回1失败返回0
        int count = brandMapper.insert(brand);
        if(count!=1){
            throw new LyException(ExceptionEnum.BRAND_SAVE_ERROR);
        }
//       新增中间表，中间表没有实体类，就没有通用Mapper，cids中id不止一个，所以就要新增多个。
        for(Long cid : cids){
//            这个地方的id不是Null，因为是自增的所以新增之后自动回写
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if(count!=1){
                throw new LyException(ExceptionEnum.CATEGORY_BRAND_SAVE_ERROR);
            }
        }

    }

    /**
     * 查询当前分类下的品牌
     * @param cid
     * @return
     */
    public List<Brand> queryBrandByCid(Long cid) {
        //因为category和brand是作为中间表存在的。这张表没有cid字段，需要多表关联查询，不能用通用Mapper里面的。
//        所以这个地方我们使用自己写sql，2表关联查询

        List<Brand> list = brandMapper.queryByCategoryId(cid);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return list;

    }

//    传入id
    public Brand queryById(Long id){
        System.out.println("-------------------------查询的id是"+id);
        Brand brand = brandMapper.selectByPrimaryKey(id);
        System.out.println("查询的brand是"+brand.toString());
        if(brand==null){
            throw  new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }
}
