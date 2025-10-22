package ru.eon.processor.homework;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import ru.eon.model.Message;

public class ProcessorExceptionOnEvenSecondTest {

    @Test
    void shouldThrowExceptionInEvenSecond() {
        var mockTimeProvider = mock(TimeProvider.class);
        when(mockTimeProvider.getCurrentSecond()).thenReturn(2);

        ProcessorExceptionOnEvenSecond processor = new ProcessorExceptionOnEvenSecond(mockTimeProvider);

        var id = 1;
        var message = new Message.Builder(id).build();
        assertThrows(RuntimeException.class, () -> processor.process(message));
    }

    @Test
    void shouldNotThrowExceptionInOddSecond() {
        var mockTimeProvider = mock(TimeProvider.class);
        when(mockTimeProvider.getCurrentSecond()).thenReturn(1);

        ProcessorExceptionOnEvenSecond processor = new ProcessorExceptionOnEvenSecond(mockTimeProvider);

        var id = 1;
        var message = new Message.Builder(id).build();
        assertDoesNotThrow(() -> {
            processor.process(message);
        });
    }
}
