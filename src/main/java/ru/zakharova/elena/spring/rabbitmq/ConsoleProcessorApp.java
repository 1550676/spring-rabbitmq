package ru.zakharova.elena.spring.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ConsoleProcessorApp {
    public static final String QUEUE_FOR_PROCESSING_NAME = "processingQueue";
    public static final String EXCHANGER_FOR_PROCESSING_RESULTS = "processingResultsExchanger";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //channel.queueDeclare(QUEUE_FOR_PROCESSING_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for tasks");

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery delivery) throws IOException {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received task '" + message + "'");

                channel.basicPublish(EXCHANGER_FOR_PROCESSING_RESULTS, "", null, "Task ready".getBytes());
            }
        };

        channel.basicConsume(QUEUE_FOR_PROCESSING_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}