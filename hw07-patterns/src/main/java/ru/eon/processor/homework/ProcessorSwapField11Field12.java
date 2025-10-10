package ru.eon.processor.homework;

import ru.eon.model.Message;
import ru.eon.processor.Processor;

public class ProcessorSwapField11Field12 implements Processor {

    @Override
    public Message process(Message message) {
        var tempField = message.getField11();
        return message.toBuilder()
                .field11(message.getField12())
                .field12(tempField)
                .build();
    }
}
