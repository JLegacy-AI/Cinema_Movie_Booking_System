package developmentCode.controllerAndViews.model;

import developmentCode.Main;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public interface SharedVariables {
    Stage stage = Main.stage;           //This Stage is setup Globally and used for transitioning
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, dd-MM , HH:mm");

    default void alert(Exception e){        //This method called for alert when any Exception Generated
        Alert al = new Alert(Alert.AlertType.ERROR, e.getMessage());
        al.setGraphic(null);
        al.setHeaderText(null);
        al.showAndWait();
    }
}
