package ru.zakharova.elena.spring.rabbitmq.enums;

public enum QueueNames {

    QUEUE_FOR_JAVA_ARTICLES("javaArticlesQueue"),
    QUEUE_FOR_PHP_ARTICLES("phpArticlesQueue"),
    QUEUE_FOR_RECEIVING_RESULTS("receivingResultsQueue");

    final String value;

    QueueNames(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
