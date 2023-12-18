package project.javaFX.tasksapp.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import javafx.util.StringConverter;
import project.javaFX.tasksapp.Models.Task;
import project.javaFX.tasksapp.Models.TaskReader;
import project.javaFX.tasksapp.Models.TaskWriter;
import project.javaFX.tasksapp.Utilities.Constants;
import project.javaFX.tasksapp.Utilities.Methods;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;


public class HomeController {

    @FXML
    private Label ForLabel;
    @FXML
    private ImageView exitIcon, homeIcon, addIcon,removeAllIcon,rotateIcone,xIcon;
    @FXML
    private ScrollPane taskScrollPane;

    @FXML
    private VBox tasksVBox;

    @FXML
    private Button CanceledButton, UpcomingButton, UncompletedButton, CompletedButton;
    @FXML

    private Label labelmessage;

    @FXML
    private DatePicker datepickerSearch;


    @FXML
    public void initialize() throws FileNotFoundException {


        MouseManager();
        goToHome();
        homeIcon.setOnMouseClicked(event -> {
            try {
                goToHome();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        addIcon.setOnMouseClicked(event -> addTask());
        exitIcon.setOnMouseClicked(event -> exitApp());
        rotateIcone.setOnMouseClicked(mouseEvent -> {
            try {
                rotateTasks();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        });
        removeAllIcon.setOnMouseClicked(mouseEvent -> removeAllTasks(optionForRemoveAll()));
        setDatepickerSearchPro();
        datepickerSearch.setOnAction(event -> {
            searchTasksOfDate();
        });
        xIcon.setOnMouseClicked(mouseEvent -> {

            try {
                goToHome();
                datepickerSearch.getEditor().clear();
                datepickerSearch.getEditor().getOnMouseReleased();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String optionForRemoveAll(){
        return switch (ForLabel.getText()) {
            case "Today" -> "now";
            case "Completed", "Uncompleted", "Upcoming", "Canceled" -> "status";
            default -> "date";
        };
    }
    public void searchTasksOfDate(){
        LocalDate selectedDate = datepickerSearch.getValue();
        tasksVBox.getChildren().clear();
        ForLabel.setText(selectedDate.toString());
        try {
            takeTasks(selectedDate.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void setDatepickerSearchPro(){
        datepickerSearch.setPromptText("Select task date");
        datepickerSearch.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string) : null;
            }
        });
    }

    public void rotateTasks() throws FileNotFoundException{
        String status=ForLabel.getText();
        switch (status) {
            case "Today":
                goToHome();break;
            case "Completed":
                CompletedButtonPressed();break;
            case "Uncompleted":
                UncompletedButtonPressed();break;
            case "Upcoming":
                UpcomingButtonPressed();break;
            case "Canceled":
                CanceledButtonPressed();break;
            default:
                searchTasksOfDate();
                break;
        }
    }

    public void removeAllTasks(String option){


            try {
                List<Task> tasks = TaskReader.readTasksFromJson();
                if (Methods.showConfirmationDialog("Confirmation", "Delete All Tasks", "Do you really want to delete All this tasks ?")) {
                    assert tasks != null;
                    switch (option){
                        case "status":
                            tasks.removeIf(task -> task.getStatus().equals(ForLabel.getText()));
                            break;
                        case "date":
                            tasks.removeIf(task -> task.getDateExecuted().toLocalDate().equals(datepickerSearch.getValue()));
                            break;
                        case "now":
                            tasks.removeIf(task -> task.getDateExecuted().toLocalDate().equals(LocalDate.now()));
                            break;
                    }

                    TaskWriter.writeTasksToJson(tasks);
                    Methods.showAlert(Alert.AlertType.INFORMATION, "Success", "Tasks deleted",
                            "All Tasks deleted successfully.");
                    tasksVBox.getChildren().clear();
                    labelmessage.setText("0 tasks");
                }
            } catch (IOException e) {

                Methods.showAlert(Alert.AlertType.ERROR, "ERROR", "Error in removing",
                        " An error occurred while removing all tasks ! \n"+e.getMessage());
            }

    }
    @FXML
    public void exitApp() {
        boolean isPressed = Methods.showConfirmationDialog("Exit Confirmation",
                "Do you really want to exit the application ?", "");
        if (isPressed) {
            System.exit(0);
        }

    }

    public void goToHome() throws FileNotFoundException {
        tasksVBox.getChildren().clear();
        ForLabel.setText("Today");
        takeTasks("Today");
    }

    public void CompletedButtonPressed() throws FileNotFoundException {
        tasksVBox.getChildren().clear();
        ForLabel.setText("Completed");
        takeTasks("Completed");
    }

    public void UncompletedButtonPressed() throws FileNotFoundException {
        tasksVBox.getChildren().clear();
        ForLabel.setText("Uncompleted");
        takeTasks("Uncompleted");
    }

    public void UpcomingButtonPressed() throws FileNotFoundException {
        tasksVBox.getChildren().clear();
        ForLabel.setText("Upcoming");
        takeTasks("Upcoming");
    }

    public void CanceledButtonPressed() throws FileNotFoundException {
        tasksVBox.getChildren().clear();
        ForLabel.setText("Canceled");
        takeTasks("Canceled");
    }

    public void takeTasks(String For) throws FileNotFoundException {
        int i = 0;
        List<Task> tasks = TaskReader.readTasksFromJson();
        assert tasks != null;
        tasks.sort(Comparator.comparing(Task::getDateExecuted));
        for (Task task : tasks) {
            if (makeConditionDate(For, task)) {
                TaskViewController taskController = new TaskViewController();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_ITEM_TASK));
                loader.setController(taskController);
                try {
                    tasksVBox.getChildren().add(loader.load());
                    taskController.initialize(task);
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        labelmessage.setText(i+" Tasks");

    }

    public boolean makeConditionDate(String status, Task task) {
        switch (status) {
            case "Today":
                return task.getDateExecuted().getDayOfMonth() == LocalDate.now().getDayOfMonth()
                        && task.getDateExecuted().getYear() ==  LocalDate.now().getYear()
                        && task.getDateExecuted().getMonth() == LocalDate.now().getMonth();
            case "Completed":
                return task.getStatus().equals("Completed");
            case "Uncompleted":
                return task.getStatus().equals("Uncompleted");
            case "Upcoming":
                return task.getStatus().equals("Upcoming");
            case "Canceled":
                return task.getStatus().equals("Canceled");
            default:
                LocalDate dateToCompare = LocalDate.parse(status);
                LocalDateTime taskDateTime = task.getDateExecuted();
                LocalDate taskDate = taskDateTime.toLocalDate();
                return taskDate.equals(dateToCompare);
        }
    }

    private void handleMouseEntered(MouseEvent event) {
        exitIcon.getScene().setCursor(Cursor.HAND);
        homeIcon.getScene().setCursor(Cursor.HAND);
        addIcon.getScene().setCursor(Cursor.HAND);
        CanceledButton.getScene().setCursor(Cursor.HAND);
        UpcomingButton.getScene().setCursor(Cursor.HAND);
        UncompletedButton.getScene().setCursor(Cursor.HAND);
        CompletedButton.getScene().setCursor(Cursor.HAND);
        removeAllIcon.getScene().setCursor(Cursor.HAND);
        rotateIcone.getScene().setCursor(Cursor.HAND);
        xIcon.getScene().setCursor(Cursor.HAND);

    }

    private void handleMouseExited(MouseEvent event) {
        exitIcon.getScene().setCursor(Cursor.DEFAULT);
        homeIcon.getScene().setCursor(Cursor.DEFAULT);
        addIcon.getScene().setCursor(Cursor.DEFAULT);
        CanceledButton.getScene().setCursor(Cursor.DEFAULT);
        UpcomingButton.getScene().setCursor(Cursor.DEFAULT);
        UncompletedButton.getScene().setCursor(Cursor.DEFAULT);
        CompletedButton.getScene().setCursor(Cursor.DEFAULT);
        removeAllIcon.getScene().setCursor(Cursor.DEFAULT);
        rotateIcone.getScene().setCursor(Cursor.DEFAULT);
        xIcon.getScene().setCursor(Cursor.DEFAULT);
    }

    private void MouseManager() {
        exitIcon.setOnMouseEntered(this::handleMouseEntered);
        exitIcon.setOnMouseExited(this::handleMouseExited);

        homeIcon.setOnMouseEntered(this::handleMouseEntered);
        homeIcon.setOnMouseExited(this::handleMouseExited);

        addIcon.setOnMouseEntered(this::handleMouseEntered);
        addIcon.setOnMouseExited(this::handleMouseExited);

        CanceledButton.setOnMouseEntered(this::handleMouseEntered);
        CanceledButton.setOnMouseExited(this::handleMouseExited);

        UpcomingButton.setOnMouseEntered(this::handleMouseEntered);
        UpcomingButton.setOnMouseExited(this::handleMouseExited);

        UncompletedButton.setOnMouseEntered(this::handleMouseEntered);
        UncompletedButton.setOnMouseExited(this::handleMouseExited);

        CompletedButton.setOnMouseEntered(this::handleMouseEntered);
        CompletedButton.setOnMouseExited(this::handleMouseExited);

        rotateIcone.setOnMouseEntered(this::handleMouseEntered);
        rotateIcone.setOnMouseExited(this::handleMouseExited);

        removeAllIcon.setOnMouseEntered(this::handleMouseEntered);
        removeAllIcon.setOnMouseExited(this::handleMouseExited);

        xIcon.setOnMouseEntered(this::handleMouseEntered);
        xIcon.setOnMouseExited(this::handleMouseExited);
    }

    public void addTask(){

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("New Task");
        dialog.setHeaderText("Adding new task for work:");

        ButtonType addNewTaskButton = new ButtonType("Add New Task", ButtonBar.ButtonData.OK_DONE);
        ButtonType closeButton = new ButtonType("Close", ButtonType.CANCEL.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(closeButton,addNewTaskButton);

        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(5);

        Label nameLabel = new Label("Task Name:");
        TextField nameTextD = new TextField();
        nameTextD.setPromptText("Enter task name");

        Label descriptionLabel = new Label("Description:");
        TextArea descriptionTextArea = new TextArea();
        descriptionTextArea.setEditable(true);
        descriptionTextArea.setPromptText("Enter task description");

        Label statusLabel = new Label("Status:");
        ChoiceBox<String> statusChoiceBox = new ChoiceBox<>();
        statusChoiceBox.getItems().addAll(Constants.statusOptions);
        statusChoiceBox.setTooltip(new Tooltip("Select task status"));

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select task date");

        TextField timeField = new TextField();
        timeField.setPromptText("HH:mm:ss");
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(date.isBefore(today));
            }
        });
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? date.toString() : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string) : null;
            }
        });

        Label dateExecutedLabel = new Label("Date Executed:");
        Label timeExecutedLabel = new Label("Time Executed:");

        grid.add(nameLabel, 1, 1);
        grid.add(nameTextD, 2, 1);
        grid.add(descriptionLabel, 1, 2);
        grid.add(descriptionTextArea, 2, 2);
        grid.add(statusLabel, 1, 3);
        grid.add(statusChoiceBox, 2, 3);
        grid.add(dateExecutedLabel, 1, 4);
        grid.add(datePicker, 2, 4);
        grid.add(timeExecutedLabel, 1, 5);
        grid.add(timeField, 2, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == closeButton) {
                return null;
            } else if (dialogButton == addNewTaskButton) {
                return new Pair<>("Add New Task", nameTextD.getText());
            }
            return null;
        });

        handleAddTask( dialog, datePicker ,nameTextD, descriptionTextArea,statusChoiceBox,timeField);

    }
    private boolean isValidTask(Task task) {
        return !task.getName().isEmpty() && task.getDateExecuted() != null
                && task.getStatus() !=null;
    }

    private int generateUniqueIntId() {
        UUID uuid = UUID.randomUUID();
        return Math.abs(uuid.hashCode());
    }
    private void handleAddTask(Dialog dialog,DatePicker datePicker,TextField nameTextD,
                               TextArea descriptionTextArea, ChoiceBox statusChoiceBox,TextField timeField){
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {

            if ("Add New Task".equals(pair.getKey())) {
                try {
                    LocalDateTime newTaskDateTime = LocalDateTime.of(datePicker.getValue(), Methods.parseFlexibleTime(timeField.getText()));
                    Task newTask = new Task();
                    newTask.setId(generateUniqueIntId());
                    newTask.setName(nameTextD.getText());
                    newTask.setDescription(descriptionTextArea.getText());
                    newTask.setStatus(statusChoiceBox.getValue().toString());
                    newTask.setDateExecuted(newTaskDateTime);

                    if (isValidTask(newTask)) {
                        List<Task> tasks = TaskReader.readTasksFromJson();
                        assert tasks != null;
                        tasks.add(newTask);
                        TaskWriter.writeTasksToJson(tasks);
                        dialog.close();
                        Methods.showAlert(Alert.AlertType.INFORMATION, "Success", "Operation Successful",
                                "Your task has been added successfully.");

                    } else {

                        Methods.showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid Input",
                                "Please check your input and try again.");
                        handleAddTask( dialog, datePicker ,nameTextD, descriptionTextArea,statusChoiceBox,timeField);

                    }

                } catch (Exception e) {
                    // Handle exceptions
                    Methods.showAlert(Alert.AlertType.ERROR, "ERROR", "Error in adding",
                            "An error occurred while adding the task! \nBecause: " + e.getMessage());
                    handleAddTask( dialog, datePicker ,nameTextD, descriptionTextArea,statusChoiceBox,timeField);
                }
            }
        });
    }


}
