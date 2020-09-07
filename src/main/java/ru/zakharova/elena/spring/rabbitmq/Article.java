package ru.zakharova.elena.spring.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Article {
    protected long id;
    protected String key;
    protected String content;
}
