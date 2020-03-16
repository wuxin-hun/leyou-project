package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2019/11/29 0:50
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(Long pid) {

        Category t = new Category();
        t.setParentId(pid);
//        通用mapperselect方法会把这个对象中的非空字段作为查询条件，也就是会生成一个select ...where parentid=?
        List<Category> list = categoryMapper.select(t);
        System.out.println("查询出来的list是--------------------------" + list);
        if (CollectionUtils.isEmpty(list)) {
//         没查到返回404
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }

//    给定一堆Id，根据这些id查询
    public List<Category> queryByIds(List<Long> ids){
//        根据一堆id去查询
        List<Category> list = categoryMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
            return list;
    }


}
