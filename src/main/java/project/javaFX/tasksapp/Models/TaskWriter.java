package project.javaFX.tasksapp.Models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import project.javaFX.tasksapp.Utilities.Constants;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class TaskWriter {
    public static void writeTasksToJson(List<Task> tasks) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        File file = null;
        try {
            file = new File(Constants.class.getResource(Constants.TASKS_FILE).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try  {
            objectMapper.writeValue(file, tasks);
            objectMapper.clearProblemHandlers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
