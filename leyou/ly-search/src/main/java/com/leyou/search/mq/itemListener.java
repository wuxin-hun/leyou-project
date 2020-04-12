package com.leyou.search.mq;

import com.leyou.search.service.SearchService;
import com.rabbitmq.http.client.domain.ExchangeType;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author TT Hun
 * @Data 2020/4/6 0:18
 */
@Component
public class itemListener {

    @Autowired
    private SearchService searchService;

//    新增和修改是一样的逻辑，删除是一个逻辑
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name="search.itme.insert.queue",durable = "true"),
            exchange = @Exchange(name="ly.item.exchange",type= ExchangeTypes.TOPIC),//因为是什么什么.的方式，所以是topic,而不是别的模式
            key={"item.insert","item.update"}
    ))
    public void ListenInsertOrUpdate(Long spuId){
        if(spuId== null){
            return ;//不处理
        }
//        处理消息，对索引库进行新增或者修改。
        searchService.createOrUpdateIndex(spuId);
    }


    //    新增和修改是一样的逻辑，删除是一个逻辑
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name="search.itme.delete.queue",durable = "true"),
            exchange = @Exchange(name="ly.item.exchange",type= ExchangeTypes.TOPIC),//因为是什么什么.的方式，所以是topic,而不是别的模式
            key={"item.insert","item.update"}
    ))
    public void ListenDelete(Long spuId){
        if(spuId== null){
            return ;//不处理
        }
//        处理消息，对索引库进行新增或者修改。
        searchService.deleteIndex(spuId);
    }
}
