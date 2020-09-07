package ru.zakharova.elena.spring.rabbitmq;

import org.springframework.stereotype.Component;
import ru.zakharova.elena.spring.rabbitmq.Article;
import ru.zakharova.elena.spring.rabbitmq.enums.Keys;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArticlesRepository {
    private List<Article> articles = new ArrayList<>();

    @PostConstruct
    public void initData() {
        articles.add(new Article(1, Keys.JAVA.getValue() + Keys.PHP.getValue(), "java, php content"));
        articles.add(new Article(2, Keys.JAVA.getValue(), "java content"));
        articles.add(new Article(3, Keys.JAVASCRIPT.getValue(), "javascript content"));
        articles.add(new Article(4, Keys.JAVASCRIPT.getValue() + Keys.PHP.getValue(), "javascript, php content"));
    }

    public Article findById(int i) {
        return articles.get(i);
    }
}
