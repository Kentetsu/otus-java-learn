package ru.eon.processor.homework;

import java.time.LocalDateTime;

public interface DateTimeProvider {
    LocalDateTime getCurrentTime();
}
