package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

public class ResourcesFileLoader implements Loader {
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        List<Measurement> measurements;
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(in);
            measurements = StreamSupport.stream(node.spliterator(), false)
                    .map(el -> new Measurement(el.get("name").textValue(), Double.parseDouble(String.valueOf(el.get("value")))))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return measurements;
    }
}
