package com.cjack.ack.two;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/**
 * Consumer 1
 * @author 30309
 *
 */
@Component
public class DirectReceiver1 {

    @RabbitListener(queues = "DirectQueue")//The name of the listening queue is DirectQueue
    @RabbitHandler
    public void process(String str,Channel channel, Message message) {
        System.out.println("DirectReceiver1 Consumer receives message: " + str );


        try {
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //Deny the news, be denied, then rejoin the team and be consumed again
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            //Reject the message, the message will be discarded and will not be returned to the queue
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}