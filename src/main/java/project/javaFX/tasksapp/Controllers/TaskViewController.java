package project.javaFX.tasksapp.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Pair;
import project.javaFX.tasksapp.Models.Task;
import project.javaFX.tasksapp.Models.TaskReader;
import project.javaFX.tasksapp.Models.TaskWriter;
import project.javaFX.tasksapp.Utilities.Constants;
import project.javaFX.tasksapp.Utilities.Methods;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TaskViewController {

    @FXML
    private VBox tasksVBox;
    @FXML
    private AnchorPane VBoxPane;

    @FXML
    private Line line;

    @FXML
    private Label idLabel;

    @FXML
    private ImageView infoIcon,deleteIcon,editIcon,selectIcon;

    @FXML
    private Text taskText,statusText;

    public void initialize(Task task) {
        selectIcon.setOnMouseEntered(this::handleMouseEntered);
        selectIcon.setOnMouseExited(this::handleMouseExited);

        editIcon.setOnMouseEntered(this::handleMouseEntered);
        editIcon.setOnMouseExited(this::handleMouseExited);

        deleteIcon.setOnMouseEntered(this::handleMouseEntered);
        deleteIcon.setOnMouseExited(this::handleMouseExited);

        infoIcon.setOnMouseEntered(this::handleMouseEntered);
        infoIcon.setOnMouseExited(this::handleMouseExited);

        idLabel.setText(String.valueOf(task.getId()));
        taskText.setText(task.getName());
        selectIcon.setImage(selectImage(task.getStatus()));
        statusText.setText(task.getStatus());
        selectColorStatus(task.getStatus(),this.statusText);

        infoIcon.setOnMouseClicked(event -> showTaskDetailsDialog(task));
        deleteIcon.setOnMouseClicked(event -> deleteTask(task.getId()));
        editIcon.setOnMouseClicked(event -> editTask(task.getId()));

        ContextMenu contextMenu = new ContextMenu();
        ArrayList<MenuItem> menuItems=new ArrayList<>();
        for(String status:Constants.statusOptions){
            MenuItem menuItem = new MenuItem(status);
            menuItem.setOnAction(event -> handleMenuItemClick(menuItem,task.getId()));
            menuItems.add(menuItem);
        }
        contextMenu.getItems().addAll(menuItems);
        selectIcon.setOnMouseClicked(mouseEvent -> {
            contextMenu.show(selectIcon, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        });



    }

    private void selectColorStatus(String status,Text text) {
        switch (status) {
            case "Completed":
                text.setFill(Color.GREEN);
                break;
            case "Uncompleted":
                text.setFill(Color.GRAY);
                break;
            case "Upcoming":
                text.setFill(Color.BLUE);
                break;
            case "Canceled":
                text.setFill(Color.RED);
                break;
            default:
                break;
        }
    }
    private Image selectImage(String status){
        Image image;
        String path;
        switch (status){
            case "Completed":
                path= Constants.ICON_CHECK_FILL;
                break;

            case "Uncompleted":
                path= Constants.ICON_CHECK_UNFILL;
                break;

            case "Upcoming" :
                path= Constants.ICON_CHECK_UNFILL;
                break;

            case "Canceled" :
                path= Constants.ICON_NOT_DONE;
                break;

            default:
                path=null;
                break;
        }
        image=new Image(String.valueOf(getClass().getResource(path)));
        return image;
    }

    private void handleMouseEntered(MouseEvent event) {
        editIcon.getScene().setCursor(Cursor.HAND);
        selectIcon.getScene().setCursor(Cursor.HAND);
        deleteIcon.getScene().setCursor(Cursor.HAND);
        infoIcon.getScene().setCursor(Cursor.HAND);
    }

    private void handleMouseExited(MouseEvent event) {
        editIcon.getScene().setCursor(Cursor.DEFAULT);
        selectIcon.getScene().setCursor(Cursor.DEFAULT);
        deleteIcon.getScene().setCursor(Cursor.DEFAULT);
        infoIcon.getScene().setCursor(Cursor.DEFAULT);
    }

    private void showTaskDetailsDialog(Task task) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Task Details");
        dialog.setHeaderText("Details for Task " + task.getName() + " (" + task.getId() + ")");

        ButtonType closeButton = new ButtonType("Close", ButtonType.CANCEL.getButtonData());
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("Task ID:");
        Text idTextD = new Text(task.getId()+"");

        Label nameLabel = new Label("Task Name:");
        Text nameTextD = new Text(task.getName());

        Label descriptionLabel = new Label("Description:");
        TextArea descriptionTextArea = new TextArea(task.getDescription());
        descriptionTextArea.setEditable(false);

        Label statusLabel = new Label("Status:");
        Text statusTextD = new Text(task.getStatus());
        selectColorStatus(task.getStatus(),statusTextD);

        LocalDateTime date_time=task.getDateExecuted();
        Label dateExecutedLabel = new Label("Date Executed:");
        Text dateExecutedTextD = new Text(date_time.getYear() + "-" + date_time.getMonthValue() + "-" + date_time.getDayOfMonth());
        Label timeExecutedLabel = new Label("Time Executed:");
        Text timeExecutedTextD= new Text(date_time.getHour() + ":" + date_time.getMinute() + ":" + date_time.getSecond());

        grid.add(idLabel, 1, 1);
        grid.add(idTextD, 2, 1);
        grid.add(nameLabel, 1, 2);
        grid.add(nameTextD, 2, 2);
        grid.add(descriptionLabel, 1, 3);
        grid.add(descriptionTextArea, 2, 3);
        grid.add(statusLabel, 1, 4);
        grid.add(statusTextD, 2, 4);
        grid.add(dateExecutedLabel, 1, 5);
        grid.add(dateExecutedTextD, 2, 5);
        grid.add(timeExecutedLabel, 1, 6);
        grid.add(timeExecutedTextD, 2, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == closeButton) {
                return null;
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            // Handle any additional actions after the dialog is closed
        });

    }

    private  void deleteTask(int taskIdToDelete) {
        try {
            List<Task> tasks = TaskReader.readTasksFromJson();
            if (Methods.showConfirmationDialog("Confirmation", "Delete Task", "Do you really want to delete this task?")) {
                assert tasks != null;
                tasks.removeIf(task -> task.getId() == taskIdToDelete);
                TaskWriter.writeTasksToJson(tasks);
                Methods.showAlert(Alert.AlertType.INFORMATION, "Success", "Task deleted",
                        "Task with ID " + taskIdToDelete + " deleted successfully.");
                deleteTaskFromPane(VBoxPane,taskIdToDelete);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Methods.showAlert(Alert.AlertType.ERROR, "ERROR", "Error in removing",
                    " An error occurred while removing the task !");
        }
    }

    private void deleteTaskFromPane(AnchorPane taskPane, int IDtask) {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : taskPane.getChildren()) {
            if (node instanceof Label) {
                String labelText = ((Label) node).getText();
                if (labelText.equals(String.valueOf(IDtask))) {
                  nodesToRemove.add(node);
                }
            }
        }
        taskPane.getChildren().removeAll(nodesToRemove);
        Pane parentPane = (Pane) taskPane.getParent();
        parentPane.getChildren().remove(taskPane);
    }

    private void editTask(int taskIdToUpdate) {
        try {
            List<Task> tasks = TaskReader.readTasksFromJson();
            Optional<Task> taskToUpdate = tasks.stream()
                    .filter(task -> task.getId() == taskIdToUpdate)
                    .findFirst();

            if (taskToUpdate.isPresent()) {
                ChoiceDialog<String> statusDialog = new ChoiceDialog<>(taskToUpdate.get().getStatus(), Constants.statusOptions);
                statusDialog.setTitle("Update Task Status");
                statusDialog.setHeaderText("Select the new status for the task:");
                statusDialog.setContentText("Status:");

                Optional<String> statusResult = statusDialog.showAndWait();
                statusResult.ifPresent(newStatus -> {

                    TextInputDialog nameDialog = new TextInputDialog(taskToUpdate.get().getName());
                    nameDialog.setTitle("Update Task Name");
                    nameDialog.setHeaderText("Enter the new name for the task:");
                    Optional<String> nameResult = nameDialog.showAndWait();
                    nameResult.ifPresent(newName -> {

                                TextInputDialog descriptionDialog = new TextInputDialog(taskToUpdate.get().getDescription());
                                descriptionDialog.setTitle("Update Task Description");
                                descriptionDialog.setHeaderText("Enter the new description for the task:");
                                Optional<String> descriptionResult = descriptionDialog.showAndWait();
                                descriptionResult.ifPresent(newDescription -> {

                                    LocalDateTime date_time = taskToUpdate.get().getDateExecuted();
                                    TextField dateField = new TextField(date_time.getYear() + "-" + date_time.getMonthValue() + "-" + date_time.getDayOfMonth());
                                    TextField timeField = new TextField(date_time.getHour() + ":" + date_time.getMinute() + ":" + date_time.getSecond());

                                    Dialog<Pair<String, String>> dateExecutedDialog = new Dialog<>();
                                    dateExecutedDialog.setTitle("Update Task Date Executed");
                                    dateExecutedDialog.setHeaderText("Enter the new dateExecuted for the task:");

                                    ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
                                    dateExecutedDialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

                                    Node updateButton = dateExecutedDialog.getDialogPane().lookupButton(updateButtonType);
                                    updateButton.setDisable(true);

                                    gridpaneDateTime(dateField, timeField, updateButton, updateButtonType, dateExecutedDialog);

                                    dateExecutedDialog.showAndWait().ifPresent(result -> {
                                        LocalDate newDate = LocalDate.parse(result.getKey());
                                        LocalDateTime newDateTime = LocalDateTime.of(newDate,Methods.parseFlexibleTime(result.getValue()));
                                        taskToUpdate.get().setStatus(newStatus);
                                        if(!newName.isEmpty()){
                                            taskToUpdate.get().setName(newName);
                                        }
                                        taskToUpdate.get().setDescription(newDescription);
                                        taskToUpdate.get().setDateExecuted(newDateTime);

                                        TaskWriter.writeTasksToJson(tasks);
                                        Methods.showAlert(Alert.AlertType.INFORMATION, "Success", "Task updated",
                                                "Task with ID " + taskIdToUpdate + " updated successfully.");
                                    });
                                });
                            });

                });
            } else {
                Methods.showAlert(Alert.AlertType.WARNING, "Warning", "Task not found",
                        "Task with ID " + taskIdToUpdate + " not found.");
            }
        } catch (Exception e) {

            Methods.showAlert(Alert.AlertType.ERROR, "Error", "Error updating task",
                    "An error occurred while updating tasks \nBecause: "+e.getMessage());
        }
    }
    private void gridpaneDateTime(TextField dateField,TextField timeField,Node updateButton,
                                 ButtonType updateButtonType,Dialog<Pair<String, String>> dateExecutedDialog){

        dateField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateButton.setDisable(newValue.trim().isEmpty() || timeField.getText().trim().isEmpty());
        });

        timeField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateButton.setDisable(newValue.trim().isEmpty() || dateField.getText().trim().isEmpty());
        });
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        dateField.setPromptText("YYYY-MM-DD");
        timeField.setPromptText("HH:mm:ss");

        grid.add(new Label("Date(yyyy-MM-dd):"), 0, 0);
        grid.add(dateField, 1, 0);
        grid.add(new Label("Time(HH:mm:ss):"), 0, 1);
        grid.add(timeField, 1, 1);

        dateExecutedDialog.getDialogPane().setContent(grid);

        Platform.runLater(dateField::requestFocus);

        dateExecutedDialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return new Pair<>(dateField.getText(), timeField.getText());
            }
            return null;
        });
    }

    private void handleMenuItemClick(MenuItem menuItem,int taskIdToUpdate){
        String newStatus = menuItem.getText();
        try {
            if (Methods.showConfirmationDialog("Confirmation", "Update Task", "Do you really want to change the status of this task to "+newStatus+" ?")) {

                List<Task> tasks = TaskReader.readTasksFromJson();
                assert tasks != null;
                Optional<Task> taskToUpdate = tasks.stream()
                        .filter(task -> task.getId() == taskIdToUpdate)
                        .findFirst();
                taskToUpdate.get().setStatus(newStatus);
                TaskWriter.writeTasksToJson(tasks);
                
                Methods.showAlert(Alert.AlertType.INFORMATION, "Success", "Task updated",
                        "Task's status updated successfully.");
                System.out.println("Selected Status: " + newStatus);
            }
        }catch(Exception ex){
                Methods.showAlert(Alert.AlertType.ERROR, "Error", "Error updating task",
                        "An error occurred while updating task's status \nBecause: "+ex.getMessage());
        }

    }

















}
