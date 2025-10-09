package ru.eon;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.eon.handler.ComplexProcessor;
import ru.eon.model.Message;
import ru.eon.model.ObjectForMessage;
import ru.eon.processor.homework.ProcessorExceptionOnEvenSecond;
import ru.eon.processor.homework.ProcessorSwapField11Field12;
import ru.eon.processor.homework.SystemTimeProvider;
import ru.eon.processor.homework.TimeProvider;

public class HomeWork {
    private final List<String> listForField13 = new ArrayList<>(List.of("F13"));
    private final ObjectForMessage objectForField13 = new ObjectForMessage();
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    {
        objectForField13.setData(listForField13);

        Message message = new Message.Builder(2)
                .field11("F11")
                .field12("F12")
                .field13(objectForField13)
                .build();
    }

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    public static void main(String[] args) {
        /*
          по аналогии с Demo.class
          из элеменов "to do" создать new ComplexProcessor и обработать сообщение
        */
        var id = 1L;
        var data = "1";
        var objectForField13 = new ObjectForMessage();
        var field13Data = new ArrayList<String>();
        field13Data.add(data);
        objectForField13.setData(field13Data);

        TimeProvider DateTimeProvider = new SystemTimeProvider();

        var processors =
                List.of(new ProcessorSwapField11Field12(), new ProcessorExceptionOnEvenSecond(DateTimeProvider));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .field13(objectForField13)
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);
    }
}
