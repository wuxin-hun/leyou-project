package com.leyou.item.web;

import com.leyou.common.utils.JsonUtils;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 写业务分析四个东西，请求方式，请求路径，
 * 该Congroller是商品管理下的商品列表的查询界面，这个查询界面需要从两张表spu和spudetail里面查询，并且有些字段查询出来是
 * 数字需要进行转码。返回结果是Page里面的数据是spu，spu里面装的是cid1 cid2 cid3，需要转换成name信息。
 * @Author TT Hun
 * @Data 2019/12/10 0:42
 */
@RestController
public class GoodsController {


    @Autowired
    private GoodsService goodService;

    /**
     * 分页查询SPU，也就是在商品管理，商品列表的查询界面
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key
    ) {

        return ResponseEntity.ok(goodService.querySpuByPage(page, rows, saleable, key));
    }

    /**
     * 商品的新增。
     * 界面在商品管理=》商品列表=》右边界面的新增选项=》最后保存提交
     *
     * @param spu
     * @return
     */
    @PostMapping("goods")
//    传递过来的json结构需要用RequestBody注解
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu) {
        goodService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 商品的修改
     * 界面在商品管理=》商品列表=》右边界面的修改选项=》最后保存提交
     * 请求路径：api.leyou.com/api/item/spu/detail/184
     * 根据spuId查询spuDetail
     *
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> queryDetailById(@PathVariable("id") Long spuId) {
//        根据spu的id查询详情Detail
        System.out.println("--------------------------------------");
        System.out.println(JsonUtils.toString(goodService.queryDetailById(spuId)));
        return ResponseEntity.ok(goodService.queryDetailById(spuId));
    }

    /**
     * 根据spuid查询下面所有的sku
     * 这个界面：商品管理=》商品列表=》右边点击铅笔箭头编辑发出请求回写数据库的参数
     * 请求地址：api.leyou.com/api/item/sku/list?id=184
     *
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long spuId) {
        return ResponseEntity.ok(goodService.querySkuBySpuId(spuId));
    }

    /**
     * 商品的修改请求：api.leyou.com/api/item/goods
     * 这个界面：商品管理=》商品列表=》编辑点击的参数后提交修改或者删除该条数据
     * 这个地方视频是无声的
     * @param spu
     * @return
     */
    @PutMapping("goods")
//    传递过来的json结构需要用RequestBody注解
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu) {
        goodService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 通过skuId查询Spu的信息。在商品详情页使用。
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id")Long id){
        Spu spu = this.goodService.querySpuById(id);
        if(spu==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }

}
