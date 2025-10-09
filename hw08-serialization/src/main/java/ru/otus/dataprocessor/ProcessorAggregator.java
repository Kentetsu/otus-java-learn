package ru.otus.dataprocessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.otus.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> sumOfMeasurement = new HashMap<>();
        // группирует выходящий список по name, при этом суммирует поля value
        data.forEach((measurement -> {
            sumOfMeasurement.put(
                    measurement.name(), sumOfMeasurement.getOrDefault(measurement.name(), 0.0) + measurement.value());
        }));

        return sumOfMeasurement;
    }
}
