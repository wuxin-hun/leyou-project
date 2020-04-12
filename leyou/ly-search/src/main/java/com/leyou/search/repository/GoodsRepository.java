package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Description 用这个实现简单的增删改查
 * @Author TT Hun
 * @Data 2020/3/29 21:59
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
