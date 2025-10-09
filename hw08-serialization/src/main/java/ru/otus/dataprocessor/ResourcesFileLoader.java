package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.*;
import java.io.IOException;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private final String fileNameString;
    private final ObjectMapper mapper;

    public ResourcesFileLoader(String fileName) {
        this.mapper = JsonMapper.builder().build();
        mapper.registerModule(new JavaTimeModule());
        this.fileNameString = fileName;
    }

    @Override
    public List<Measurement> load() throws IOException {
        StringBuilder resultString = new StringBuilder();
        List<Measurement> resultObject = null;
        try (var bufferedReader = new BufferedReader(new FileReader(fileNameString))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultString.append(line);
            }
        }

        resultObject = mapper.readValue(
                resultString.toString(),
                mapper.getTypeFactory().constructCollectionType(List.class, Measurement.class));

        return resultObject;
    }
}
