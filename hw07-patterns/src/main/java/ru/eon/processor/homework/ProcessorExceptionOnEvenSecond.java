package ru.eon.processor.homework;

import java.time.LocalDateTime;
import ru.eon.model.Message;
import ru.eon.processor.Processor;

public class ProcessorExceptionOnEvenSecond implements Processor {

    @Override
    public Message process(Message message) {
        DateTimeProvider timeProvider = LocalDateTime::now;
        int currentSecond = timeProvider.getCurrentTime().getSecond();
        if (currentSecond % 2 == 0) {
            throw new RuntimeException("Исключение в четную секунду: " + currentSecond);
        }

        return message;
    }
}
