package ru.zakharova.elena.spring.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import ru.zakharova.elena.spring.rabbitmq.enums.Exchangers;
import ru.zakharova.elena.spring.rabbitmq.enums.Queues;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@RestController
public class ArticlesSenderApp {
    private ArticlesRepository articlesRepository;
    private RabbitTemplate rabbitTemplate;
    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Autowired
    public void setArticleRepository(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    @PostConstruct
    public void initData() throws IOException {
        com.rabbitmq.client.ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(Exchangers.EXCHANGER_FOR_SENDING_TO_SUBSCRIBERS.getValue(), BuiltinExchangeType.TOPIC);
            channel.queueBind(Queues.QUEUE_FOR_RECEIVING_RESULTS.getValue(), Exchangers.EXCHANGER_FOR_SENDING_FROM_SUBSCRIBERS.getValue(), "");

        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Bean
    public Queue resultsQueue() {
        return new Queue(Queues.QUEUE_FOR_RECEIVING_RESULTS.getValue(), true, false, false);
    }

    @GetMapping("/{id}")
    public String sendReceivedArticle(@PathVariable Integer id) {
        Article article = articlesRepository.findById(id);
        rabbitTemplate.convertAndSend(Exchangers.EXCHANGER_FOR_SENDING_TO_SUBSCRIBERS.getValue(), article.getKey(), "Article with key: " + article.getKey());
        return "The article with content: " + article.getContent() + " was sent";
    }

    // subscribe to
    @GetMapping("/addKey") // http://localhost:8189/spring-rabbit/addKey/?key=.java.&queue=(name of queue)
    public String addKey(@RequestParam String key, @RequestParam String queue) {
        rabbitTemplate.convertAndSend(Exchangers.EXCHANGER_FOR_SENDING_TO_SUBSCRIBERS.getValue(), queue, "addKey " + key);
        return "addKey: " + key;
    }

    // unsubscribe from
    @GetMapping("/deleteKey") // http://localhost:8189/spring-rabbit/deleteKey/?key=.java.&queue=(name of queue)
    public String deleteKey(@RequestParam String key, @RequestParam String queue) {
        rabbitTemplate.convertAndSend(Exchangers.EXCHANGER_FOR_SENDING_TO_SUBSCRIBERS.getValue(), queue, "deleteKey " + key);
        return "deleteKey: " + key;
    }

    @Bean
    public SimpleMessageListenerContainer containerForTopic(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(Queues.QUEUE_FOR_RECEIVING_RESULTS.getValue());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(SimpleMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    public static void main(String[] args) {
        SpringApplication.run(ArticlesSenderApp.class, args);
    }


}