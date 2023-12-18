package project.javaFX.tasksapp.Models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import project.javaFX.tasksapp.Utilities.Constants;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String formattedDateTime = localDateTime.format(Constants.formatter);
        jsonGenerator.writeString(formattedDateTime);
    }
}
