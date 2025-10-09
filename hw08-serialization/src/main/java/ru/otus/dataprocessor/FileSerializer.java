package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final ObjectMapper mapper = JsonMapper.builder()
            .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .build();
    ;
    private final String fileNameString;

    public FileSerializer(String fileName) {
        mapper.registerModule(new JavaTimeModule());
        this.fileNameString = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) throws IOException {
        // формирует результирующий json и сохраняет его в файл
        var file = new File(fileNameString);
        mapper.writeValue(file, data);
    }
}
