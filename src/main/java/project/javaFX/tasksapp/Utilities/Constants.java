
package project.javaFX.tasksapp.Utilities;


import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class Constants {

    public static String FXML_HOME = "/project/javaFX/TasksApp/Views/home.fxml";
    public static String FXML_ITEM_TASK = "/project/javaFX/TasksApp/Views/VBox.fxml";
    public static String ICON_CHECK_UNFILL = "/project/javaFX/TasksApp/imgs/uncheckIcon.png";
    public static String ICON_CHECK_FILL = "/project/javaFX/TasksApp/imgs/doneIcon.png";
    public static String ICON_NOT_DONE = "/project/javaFX/TasksApp/imgs/notDoneIcon.png";
    public static String LOGO = "/project/javaFX/TasksApp/imgs/logo.jpg";
    public static String JSON_URL = "https://jsonplaceholder.typicode.com/todos";
    public static String APP_TITLE = "done Task Manager";
    public static String TASKS_FILE = "/project/javaFX/TasksApp/files/DataTasks.json";
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<String> statusOptions = Arrays.asList("Canceled", "Upcoming", "Uncompleted", "Completed");

}
