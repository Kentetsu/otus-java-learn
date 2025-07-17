package homework;

import java.util.*;

public class CustomerService {

    // todo: 3. надо реализовать методы этого класса
    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    TreeMap<Customer, String> TreeEntry = new TreeMap<>();
    Map.Entry<Customer, String> currentCursor = null;

    public Map.Entry<Customer, String> getSmallest() {
        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        return new AbstractMap.SimpleEntry<>(
                TreeEntry.firstEntry().getKey().clone(),
                TreeEntry.firstEntry().getValue()); // это "заглушка, чтобы скомилировать"
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        currentCursor = TreeEntry.higherEntry(currentCursor.getKey());
        if (currentCursor == null) {
            return null;
        }
        return new AbstractMap.SimpleEntry<>(
                currentCursor.getKey().clone(), currentCursor.getValue()); // это "заглушка, чтобы скомилировать"
    }

    public void add(Customer customer, String data) {
        TreeEntry.put(customer, data);
        currentCursor = TreeEntry.firstEntry();
    }
}
