package ru.zakharova.elena.spring.rabbitmq;

import com.rabbitmq.client.*;
import ru.zakharova.elena.spring.rabbitmq.enums.ExchangersNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubscriberApp {

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(ExchangersNames.EXCHANGER_FOR_SENDING_FROM_SUBSCRIBERS.getValue(), BuiltinExchangeType.DIRECT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ExchangersNames.EXCHANGER_FOR_SENDING_TO_SUBSCRIBERS.getValue(), "changeOfKey");
        System.out.println(" [*] Waiting for tasks");
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery delivery) throws IOException {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [+] Received: " + message);
                if (message.startsWith("deleteKey")){
                queueBindingDelete(message.substring(10), channel, queueName);
                }
                if (message.startsWith("addKey")){
                    queueBindingAdd(message.substring(7), channel, queueName);
                }
                channel.basicPublish(ExchangersNames.EXCHANGER_FOR_SENDING_FROM_SUBSCRIBERS.getValue(), "", null, ("Message was delivered for the consumer " + consumerTag + ": " + message).getBytes());
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });


    }

    private static void queueBindingDelete(String key, Channel channel, String queueName) throws IOException {
        channel.queueUnbind(queueName, ExchangersNames.EXCHANGER_FOR_SENDING_TO_SUBSCRIBERS.getValue(), "#" + key + "#");
    }

    private static void queueBindingAdd(String key, Channel channel, String queueName) throws IOException {
        channel.queueBind(queueName, ExchangersNames.EXCHANGER_FOR_SENDING_TO_SUBSCRIBERS.getValue(), "#" + key + "#");
    }
}