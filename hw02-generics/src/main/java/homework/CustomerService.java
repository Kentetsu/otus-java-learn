package homework;

import java.util.*;

public class CustomerService {

    // todo: 3. надо реализовать методы этого класса
    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final TreeMap<Customer, String> treeEntry = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> tempEntry = treeEntry.firstEntry();
        return new AbstractMap.SimpleEntry<>(
                tempEntry.getKey().clone(), tempEntry.getValue()); // это "заглушка, чтобы скомилировать"
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> tempCursor = treeEntry.higherEntry(customer);
        if (tempCursor == null) {
            return null;
        }
        return new AbstractMap.SimpleEntry<>(
                tempCursor.getKey().clone(), tempCursor.getValue()); // это "заглушка, чтобы скомилировать"
    }

    public void add(Customer customer, String data) {
        treeEntry.put(customer, data);
    }
}
