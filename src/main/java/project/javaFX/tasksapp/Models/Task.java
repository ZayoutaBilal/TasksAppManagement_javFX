package project.javaFX.tasksapp.Models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

public class Task {

    private String name;
    private String description;
    private String status;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateExecuted;


    private int id;


    public Task() {

    }


    public Task(String name, String description, String status, LocalDateTime dateExecuted, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.dateExecuted = dateExecuted;
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateExecuted() {
        return dateExecuted;
    }

    public void setDateExecuted(LocalDateTime  dateExecuted) {
        this.dateExecuted = dateExecuted ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
