package ru.zakharova.elena.spring.rabbitmq.enums;

public enum Queues {
    QUEUE_FOR_RECEIVING_RESULTS("receivingResultsQueue");

    final String value;

    Queues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
