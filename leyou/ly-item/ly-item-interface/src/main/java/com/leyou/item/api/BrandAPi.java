package com.leyou.item.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2020/3/22 23:33
 */
public interface BrandAPi {

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("brand/{id}")
    Brand queryBrandById(@PathVariable("id") Long id);
}