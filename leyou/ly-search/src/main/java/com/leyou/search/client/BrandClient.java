package com.leyou.search.client;

import com.leyou.item.api.BrandAPi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description
 * @Author TT Hun
 * @Data 2020/3/29 21:57
 */
@FeignClient("item-service")
public interface BrandClient extends BrandAPi {
}
