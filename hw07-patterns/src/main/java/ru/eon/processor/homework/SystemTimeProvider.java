package ru.eon.processor.homework;

import java.time.LocalDateTime;

public class SystemTimeProvider implements TimeProvider {
    @Override
    public int getCurrentSecond() {
        return LocalDateTime.now().getSecond();
    }
}
