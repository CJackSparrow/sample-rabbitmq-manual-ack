package com.cjack.ack.two;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Producer
 * @author 30309
 *
 */
@RestController
public class SendMessageController{

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {

        //Send message with binding key value DirectRouting to switch DirectExchange
        rabbitTemplate.convertAndSend("DirectExchange", "DirectRouting", "Hello World");

        return "ok";
    }

}
