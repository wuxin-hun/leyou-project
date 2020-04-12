package com.leyou.search.client;

import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsClientTest {
    @Autowired
    private GoodsClient goodsClient;

    @Test
    public void goodClient1Test(){
        SpuDetail spuDetail = goodsClient.queryDetailById(215L);
        System.out.println(JsonUtils.toString(spuDetail));
    }

    @Test
    public void test2(){
        List<Sku> skus = goodsClient.querySkuBySpuId(1L);
        System.err.println(skus);
    }

}