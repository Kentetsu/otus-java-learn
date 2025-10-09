package ru.eon.listener.homework;

import java.util.*;
import ru.eon.listener.Listener;
import ru.eon.model.Message;
import ru.eon.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageHistory = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Message messageCopy = copyMessage(msg);
        messageHistory.put(messageCopy.getId(), messageCopy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageHistory.get(id));
    }

    private Message copyMessage(Message original) {
        ObjectForMessage copyOfField13 = new ObjectForMessage();
        List<String> copyOfListField13 = new ArrayList<>();
        var copyOfData = original.getField13().getData();
        if (copyOfData != null) {
            for (String stringOfList : original.getField13().getData()) {
                if (stringOfList != null) {
                    copyOfListField13.add(new String(stringOfList));
                } else {
                    copyOfListField13.add(new String());
                }
            }
        }
        copyOfField13.setData(copyOfListField13);

        Message copyMassage = original.toBuilder().field13(copyOfField13).build();

        return copyMassage;
    }
}
