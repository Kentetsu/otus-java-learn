package ru.calculator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    // !!! эта коллекция должна остаться. Заменять ее на счетчик нельзя.
    private final List<Data> listValues = new ArrayList<>(100_000);
    private final SecureRandom random = new SecureRandom();
    private int listSizeCounter = 0;

    // !!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        listValues.add(data);
        listSizeCounter++;

        if (listValues.size() % 100_000 == 0) {
            listValues.clear();
            listSizeCounter = 0;
        }

        var currentValue = data.getValue();

        sum += data.getValue() + random.nextInt();

        sumLastThreeValues = currentValue + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = currentValue;

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (currentValue + 1) - sum);
            someValue = Math.abs(someValue) + listSizeCounter;
        }
    }

    public int getSum() {
        return sum;
    }

    public int getPrevValue() {
        return prevValue;
    }

    public int getPrevPrevValue() {
        return prevPrevValue;
    }

    public int getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public int getSomeValue() {
        return someValue;
    }
}
