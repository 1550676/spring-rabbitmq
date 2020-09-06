package ru.zakharova.elena.spring.rabbitmq.repositories;

import org.springframework.stereotype.Component;
import ru.zakharova.elena.spring.rabbitmq.Article;
import ru.zakharova.elena.spring.rabbitmq.enums.Keys;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class QueuesRepository {
    private List<String> queues;

    @PostConstruct
    public void initData() {
        queues = new ArrayList<>();
    }

//    public Article findByName(int i) {
//        return articles.get(i);
//    }
}
