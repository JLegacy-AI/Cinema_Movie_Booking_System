package developmentCode.controllerAndViews;

import developmentCode.controllerAndViews.model.Database;
import developmentCode.controllerAndViews.model.Person;
import developmentCode.controllerAndViews.model.SharedVariables;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

public class SignInController implements SharedVariables {

    private String uT;
    private String un;

    public TextField uField;
    public PasswordField pField;

    public CheckBox cb_1;
    public CheckBox cb_2;
    public CheckBox cb_3;


    public void signIn(MouseEvent mouseEvent) {
        String u =  uField.getText();
        String p = pField.getText();
        try {
            if(uT.equalsIgnoreCase("guest")){
                Person person = new Person();
                person.setUsername("Guest"+ LocalTime.now().getNano());
                person.setName("Guest");
                person.setUserType("Guest");
                person.setPassword("N/A");
                person.setPhoneNumber("N/A");
                Database.newUserAdd(person);
                un=person.getUsername();
                CustomerScreenController.person = Database.getUser(un);
                screenTransitions();
                return;
            }
            if(Database.userDataCheck(u,p,uT)){
                un=u;
                screenTransitions();
            }else{
                throw new Exception("Invalid Username or Password or selection");
            }
            if(u.isEmpty() || p.isEmpty()){
                throw new Exception("Filled All Required Info:");
            }
        }catch (Exception e){
            alert(e);
        }

    }

    public void backStartUpScreen(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("views/Start_Up_Screen.fxml"));
        stage.setScene(new Scene(root, 600,500));
    }
    public void signUpScreen(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("views/Sign_Up_Screen.fxml"));
        stage.setScene(new Scene(root, 600,300));
    }

    public ArrayList<CheckBox> addSignInCBGroup(){
        ArrayList<CheckBox> cb_group= new ArrayList<>();
        cb_group.add(cb_1);
        cb_group.add(cb_2);
        cb_group.add(cb_3);
        return cb_group;
    }

    public void checkBoxClicked(MouseEvent mouseEvent) {
        System.out.println("Selected");
        CheckBox cb =(CheckBox)mouseEvent.getSource();
        uT = cb.getText();
        for (CheckBox checkBox : addSignInCBGroup()) {
            if(cb!=checkBox){
                checkBox.setSelected(false);
            }
            disableComponent(pField, !cb.getText().equalsIgnoreCase("guest"));
            disableComponent(uField, !cb.getText().equalsIgnoreCase("guest"));
        }
    }

    public void disableComponent(TextField tf, boolean disable){
        if(!disable){
            tf.setText("Not Required You are Guest :)");
        }else{
            tf.setText("");
        }
        tf.setDisable(!disable);
        tf.setEditable(disable);
    }

    public void screenTransitions() throws IOException, SQLException {
        if(uT.equalsIgnoreCase("employee")){
            Parent root = FXMLLoader.load(getClass().getResource("views/Employee_Screen.fxml"));
            stage.setScene(new Scene(root, 600,500));
            stage.setFullScreen(true);

        }else{
            CustomerScreenController.person = Database.getUser(un);
            System.out.println(CustomerScreenController.person.getUsername());
            System.out.println(CustomerScreenController.person.getId());
            Parent root = FXMLLoader.load(getClass().getResource("views/Customer_Screen.fxml"));
            stage.setScene(new Scene(root, 600,500));
            stage.setFullScreen(true);
        }
    }
}
