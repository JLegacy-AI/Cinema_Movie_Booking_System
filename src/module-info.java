module JavaFxApplication {

    requires java.sql;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.controls;
    requires javafx.graphics;
    requires mysql.connector.java;



    opens developmentCode;
    opens developmentCode.controllerAndViews;
    opens developmentCode.controllerAndViews.views;
    opens developmentCode.controllerAndViews.model;

}

//java.sql,javafx.fxml, javafx.swing, javafx.controls, javafx.graphics, mysql.connector.java