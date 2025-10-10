package ru.eon.handler;

import ru.eon.listener.Listener;
import ru.eon.model.Message;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
