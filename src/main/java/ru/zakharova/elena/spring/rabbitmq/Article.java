package ru.zakharova.elena.spring.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class Article {
    protected long id;
    protected String key;
    protected String content;
}
