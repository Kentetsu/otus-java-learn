package ru.eon.listener.homework;

import java.util.Optional;
import ru.eon.model.Message;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
