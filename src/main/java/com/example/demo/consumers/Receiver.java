package com.example.demo.consumers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RabbitListener(queues = "rabbitmq.demoQueue", id = "listener")
@Component
public class Receiver {

    @RabbitHandler
    public void receiver(String data){
        log.info("Rabbit sender object {} received" , data);
        throw new NullPointerException();
    }
}
