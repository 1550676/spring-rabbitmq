package ru.zakharova.elena.spring.rabbitmq.enums;

public enum Keys {

    JAVA(".java."),
    PHP(".php."),
    SPRING(".spring."),
    JAVASCRIPT(".javascript");

    final String value;

    Keys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
