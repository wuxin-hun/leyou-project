package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsService;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2020/4/7 21:20
 */
@Controller
@Api("商品详情页面接口")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     *
     * @param id 占位符
     * @param model 是数据模型类
     * @return
     */
    @ApiOperation(value = "商品详情页对接themeleaf的接口", notes = "根据spuId构建商品详情页需要的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "前端页面url里面的id", type = "Long"),
            @ApiImplicitParam(name = "model", required = true, value = "是页面所需要的数据模型，不包括样式", type = "Model")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "数据库中有用户信息，可以登录"),
            @ApiResponse(code = 404, message = "数据库中有没用户信息，不可以登录"),
            @ApiResponse(code = 500, message = "查询失败")
    })
    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id") Long id, Model model){
        Map<String, Object> map = this.goodsService.loadData(id);
        model.addAllAttributes(map);
//        就会通过视图解析器解析到resource下面的item.html里面
        return "item";

    }
}
