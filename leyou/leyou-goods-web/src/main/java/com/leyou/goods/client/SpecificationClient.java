package com.leyou.goods.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2020/3/29 21:57
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
