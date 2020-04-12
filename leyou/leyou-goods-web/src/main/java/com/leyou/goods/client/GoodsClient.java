package com.leyou.goods.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2020/3/29 21:48
 */

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}