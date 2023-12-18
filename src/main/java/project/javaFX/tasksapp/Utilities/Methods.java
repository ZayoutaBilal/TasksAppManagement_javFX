package project.javaFX.tasksapp.Utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalTime;
import java.util.Optional;
import java.util.regex.Pattern;

public class Methods {
    public static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static boolean showConfirmationDialog(String title, String header, String content) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle(title);
        confirmationDialog.setHeaderText(header);
        confirmationDialog.setContentText(content);

        Optional<ButtonType> result = confirmationDialog.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static LocalTime parseFlexibleTime(String input) {
        Pattern pattern = Pattern.compile("^\\d{1,2}(:\\d{1,2})?(:\\d{1,2})?$");
        int hours,minutes,seconds;
        if (pattern.matcher(input).matches()) {
            // If input matches the pattern, try parsing
            String[] parts = input.split(":");
            hours = Integer.parseInt(parts[0]);
            if(parts.length==1){
                minutes=seconds=0;
            }else {
                minutes = Integer.parseInt(parts[1]);
                seconds = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
            }


            return LocalTime.of(hours, minutes, seconds);
        } else {

            throw new IllegalArgumentException("Invalid time format");
        }
    }
}
