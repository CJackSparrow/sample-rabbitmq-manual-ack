package com.cjack.ack.two;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Direct connected switch
 * @author 30309
 *
 */
@Configuration
public class DirectRabbitConfig {

    //Queue named DirectQueue
    @Bean
    public Queue DirectQueue() {
        return new Queue("DirectQueue",true);  //true indicates whether it is persistent
    }

    //Direct connect switch named DirectExchange
    @Bean
    DirectExchange DirectExchange() {
        return new DirectExchange("DirectExchange");
    }

    //Bind queue to switch and set to match key: DirectRouting
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(DirectQueue()).to(DirectExchange()).with("DirectRouting");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // Message sending failure returned to the queue, publisher returns needs to be configured for the profile: true
        rabbitTemplate.setMandatory(true);

        // Message return, the configuration file needs to configure publisher returns: true
        // The ReturnCallback interface is used to implement the callback when a message is sent to the RabbitMQ switch, but there is no corresponding queue bound to the switch.
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("Message sending failed:No corresponding queue bound to switch");
        });

        // Message confirmation, the configuration file needs to configure publisher confirms: true
        // The ConfirmCallback interface is used to receive ack callbacks after messages are sent to the RabbitMQ exchanger.
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("Message sent successfully:Message sent to RabbitMQ exchanger");
            } else {
                System.out.println("Message sending failed:Message not sent to RabbitMQ exchanger");
            }
        });

        return rabbitTemplate;
    }
}