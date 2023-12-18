package project.javaFX.tasksapp.Models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import project.javaFX.tasksapp.Utilities.Constants;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        String dateAsString = jsonParser.getText();
        return LocalDateTime.parse(dateAsString, Constants.formatter);
    }
}
