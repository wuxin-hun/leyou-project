package com.leyou.item.api;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2020/3/22 23:30
 */
public interface GoodsApi {
    @GetMapping("spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    );

    /**
     * 商品的新增。
     * 界面在商品管理=》商品列表=》右边界面的新增选项=》最后保存提交
     *
     * @param spu
     * @return
     */
    @PostMapping("goods")
//    传递过来的json结构需要用RequestBody注解
    void saveGoods(@RequestBody Spu spu);

    /**
     * 商品的修改
     * 界面在商品管理=》商品列表=》右边界面的修改选项=》最后保存提交
     * 请求路径：api.leyou.com/api/item/spu/detail/184
     *
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail queryDetailById(@PathVariable("id") Long spuId) ;

    /**
     * 根据spuid查询下面所有的sku
     * 这个界面：商品管理=》商品列表=》右边点击铅笔箭头编辑发出请求回写数据库的参数
     * 请求地址：api.leyou.com/api/item/sku/list?id=184
     *
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long spuId);

    /**
     * 商品的修改请求：api.leyou.com/api/item/goods
     * 这个界面：商品管理=》商品列表=》编辑点击的参数后提交修改或者删除该条数据
     * 这个地方视频是无声的
     * @param spu
     * @return
     */
    @PutMapping("goods")
//    传递过来的json结构需要用RequestBody注解
    Void updateGoods(@RequestBody Spu spu) ;

    /**
     * 用于商品详情页根据Id查询spu的信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Spu querySpuById(@PathVariable("id") Long id);
}