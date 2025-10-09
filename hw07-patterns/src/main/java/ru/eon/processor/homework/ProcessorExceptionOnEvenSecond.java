package ru.eon.processor.homework;

import ru.eon.model.Message;
import ru.eon.processor.Processor;

public class ProcessorExceptionOnEvenSecond implements Processor {
    private final TimeProvider timeProvider;

    public ProcessorExceptionOnEvenSecond(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {

        int currentSecond = timeProvider.getCurrentSecond();
        if (currentSecond % 2 == 0) {
            throw new RuntimeException("Исключение в четную секунду: " + currentSecond);
        }

        return message;
    }
}
