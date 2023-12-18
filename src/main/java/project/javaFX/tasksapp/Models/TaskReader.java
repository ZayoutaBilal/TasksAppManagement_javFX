package project.javaFX.tasksapp.Models;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import project.javaFX.tasksapp.Utilities.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskReader {

//    public static void main(String[] args) throws FileNotFoundException {
//        List<Task> tasks = readTasksFromJson();
//
//        // Now 'tasks' contains the list of tasks read from the JSON file
//        assert tasks != null;
//        for (Task task : tasks) {
//            System.out.println("Task: " + task.getName() +
//                    ", Description: " + task.getDescription() +
//                    ", Status: " + task.getStatus() +
//                    ", Date Executed: " + task.getDateExecuted() +
//                    ", ID: " + task.getId());
//        }
//    }

    public static List<Task> readTasksFromJson() throws FileNotFoundException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(MapperFeature.AUTO_DETECT_CREATORS);

        try {
            InputStream inputStream = TaskReader.class.getResource(Constants.TASKS_FILE).openStream();
            if (inputStream == null) {
                throw new FileNotFoundException("File not found: " + Constants.TASKS_FILE);
            }


            StringBuilder jsonContent = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonContent.append(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                Task[] taskArray = objectMapper.readValue(jsonContent.toString(), Task[].class);
                return new ArrayList<>(Arrays.asList(taskArray));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}