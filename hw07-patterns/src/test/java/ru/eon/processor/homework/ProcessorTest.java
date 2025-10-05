package ru.eon.processor.homework;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ru.eon.model.Message;

public class ProcessorTest {

    @Test
    void shouldThrowExceptionInEvenSecond() {
        try (MockedStatic<LocalDateTime> mockedDateTime = mockStatic(LocalDateTime.class)) {

            LocalDateTime fixedTime = LocalDateTime.of(2024, 1, 1, 12, 0, 2); // секунда = 2 (четная)

            mockedDateTime.when(LocalDateTime::now).thenReturn(fixedTime);
            ProcessorExceptionOnEvenSecond processor = new ProcessorExceptionOnEvenSecond();

            var id = 1;

            var message = new Message.Builder(id).build();
            assertThrows(RuntimeException.class, () -> processor.process(message));
        }
    }
}
