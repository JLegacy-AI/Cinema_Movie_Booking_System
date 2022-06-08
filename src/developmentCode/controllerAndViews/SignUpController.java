package developmentCode.controllerAndViews;

import developmentCode.controllerAndViews.model.Database;
import developmentCode.controllerAndViews.model.Person;
import developmentCode.controllerAndViews.model.SharedVariables;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;

public class SignUpController implements SharedVariables {


    public CheckBox cb_1;
    public CheckBox cb_2;

    public TextField uNField, nField, pNField, pField;
    public String uT="";

    public void backSignInScreen(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("views/Sign_In_Screen.fxml"));
        stage.setScene(new Scene(root, 600,250));
    }


    public void addNewUser(MouseEvent mouseEvent) {
        String uN = uNField.getText();
        String n = nField.getText();
        String pN = pNField.getText();
        String p = pField.getText();

        try{
            if(uN.isEmpty() || n.isEmpty() || pN.isEmpty() || p.isEmpty()){
                throw new Exception("Please complete all Fields:");
            }
            if(userAlready(uN)){
                throw new Exception("User Already Present Use Different Username:");
            }
            addUser(new Person(n, uN, p, pN, uT, false));
        }catch (Exception e){
            alert(e);
        }
        uNField.setText(null);
        nField.setText(null);
        pField.setText(null);
        pNField.setText(null);
    }

    public void checkBoxSelect(MouseEvent mouseEvent) {
        CheckBox cb =(CheckBox)mouseEvent.getSource();
        uT = cb.getText();
        for (CheckBox checkBox : addSignInCBGroup()) {
            if(cb!=checkBox){
                checkBox.setSelected(false);
            }
        }
    }

    public ArrayList<CheckBox> addSignInCBGroup(){
        ArrayList<CheckBox> cb_group= new ArrayList<>();
        cb_group.add(cb_1);
        cb_group.add(cb_2);
        return cb_group;
    }

    public void addUser(Person person){
        try {
            Database.newUserAdd(person);
        }catch (Exception e){
            alert(e);
        }
    }

    public boolean userAlready(String uN){
        try{
            if(Database.userCheck(uN)){
                throw new Exception("Username Already Taken Use Different");
            }
        }catch (Exception e){
            alert(e);
        }
        return false;
    }
}
