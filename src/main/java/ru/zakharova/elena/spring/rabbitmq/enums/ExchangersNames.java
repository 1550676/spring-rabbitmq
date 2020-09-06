package ru.zakharova.elena.spring.rabbitmq.enums;

public enum ExchangersNames {

    EXCHANGER_FOR_SENDING_TO_SUBSCRIBERS("articleSendingExchanger"),
    EXCHANGER_FOR_SENDING_FROM_SUBSCRIBERS("fromSubscribersSendingExchanger");

    final String value;

    ExchangersNames(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
