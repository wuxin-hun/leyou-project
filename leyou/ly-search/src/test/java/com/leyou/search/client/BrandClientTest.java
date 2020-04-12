package com.leyou.search.client;

import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Brand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BrandClientTest {

    @Autowired
    private BrandClient brandClient;
    @Test
    public void test1(){
        Brand brand = brandClient.queryBrandById(1L);
        System.out.println(JsonUtils.toString(brand));
    }
}