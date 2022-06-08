package developmentCode.controllerAndViews;

import developmentCode.controllerAndViews.model.SharedVariables;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartUpController implements Initializable, SharedVariables {

    public GridPane CUSTOMER_EMPLOYEE;

    public Button employeeSignIn;
    public Button signInButton_1;
    public Button exitButton;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void signInScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("views/Sign_In_Screen.fxml"));
        stage.setScene(new Scene(root, 600,250));
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void backToSignIn(MouseEvent mouseEvent) {

    }
}
