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
        for (String stringOfList : original.getField13().getData()) {
            copyOfListField13.add(new String(stringOfList));
        }
        copyOfField13.setData(copyOfListField13);

        Message copyMassage = new Message.Builder(original.getId())
                .field1(original.getField1())
                .field2(original.getField2())
                .field3(original.getField3())
                .field4(original.getField4())
                .field5(original.getField5())
                .field6(original.getField6())
                .field7(original.getField7())
                .field8(original.getField8())
                .field9(original.getField9())
                .field10(original.getField10())
                .field11(original.getField11())
                .field12(original.getField12())
                .field13(copyOfField13)
                .build();

        return copyMassage;
    }
}
