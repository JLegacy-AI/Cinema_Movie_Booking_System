package developmentCode.controllerAndViews;

import developmentCode.controllerAndViews.model.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;


import java.io.*;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EmployeeScreenController implements SharedVariables, Initializable {
    public VBox manipulatorBP;
    public FileInputStream fileInputStream=null;

    public void backToSignIn(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("views/Sign_In_Screen.fxml"));
        stage.setScene(new Scene(root, 600,250));
    }



    //Movies Upload work When Movie Button Clicked
    public void moviesUploads(MouseEvent mouseEvent) throws SQLException {
        manipulatorBP.getChildren().clear();
        manipulatorBP.setSpacing(30);
        manipulatorBP.getChildren().add(moviesSection());
        manipulatorBP.getChildren().add(addMovie());
    }

    public ScrollPane moviesSection() throws SQLException {
        ScrollPane sp = new ScrollPane();
        sp.setPrefHeight(400);

        HBox hb = new HBox();
        hb.setPadding(new Insets(50,10,50,10));
        hb.setSpacing(20);
        hb.setPrefHeight(350);

        ArrayList<Movie> movie = Database.movieArrayList();
        if (movie.size()<=0){
            sp.setContent(new Label("No Movies Available"));
            return sp;
        }

        for (Movie m : movie) {
            hb.getChildren().add(movie(m));
        }
        sp.setContent(hb);
        return sp;
    }

    public BorderPane movie(Movie m) throws SQLException {
        BorderPane bp = new BorderPane();
        bp.setBackground(imageConversion(m.getImage()));

        bp.setPrefHeight(300);


        bp.setOnMouseClicked(e->{
            if(e.isControlDown()){
                try {
                    Database.movieDelete(m.getId());
                    moviesUploads(null);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });



        VBox vb= new VBox();
        vb.getChildren().addAll(
                labelStyle(new Label("ID:"+" ".repeat(30)+m.getId()),"-fx-text-fill:white;"),
                labelStyle(new Label("Id:              "+m.getId()),"-fx-text-fill:white;"),
                labelStyle(new Label("Available Tickets:   "+m.getTotalTickets()),"-fx-text-fill:white;"),
                labelStyle(new Label("Name:                "+m.getName()),"-fx-text-fill:white;"),
                labelStyle(new Label("Start At:            "+m.getStartTime().toLocalDateTime().format(dtf)),"-fx-text-fill:white;"),
                labelStyle(new Label("End At:              "+m.getEndTime().toLocalDateTime().format(dtf)),"-fx-text-fill:white;"),
                labelStyle(new Label("Price of Ticket:     "+m.getPricePerTicket()),"-fx-text-fill:white;")
        );

        vb.setStyle("-fx-background-color: rgba(0,0,0,0.9);" +
                "-fx-background-radius:5;" +
                "-fx-padding: 20;" +
                "-fx-font-family: 'Century Gothic';");

        bp.setBottom(vb);

        return bp;
    }

    public Background imageConversion(Blob blob) throws SQLException {
        InputStream in = blob.getBinaryStream();
        Image img = new Image(in);
        BackgroundImage backgroundImage = new BackgroundImage(img,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(500,500,false,false, false, true));
        return new Background(backgroundImage);
    }


    public VBox addMovie(){
        VBox vb= new VBox();
        vb.setSpacing(10);
        vb.prefHeight(30);
        vb.setPadding(new Insets(10,300,10,300));
        vb.setAlignment(Pos.BASELINE_CENTER);

        TextField nField = new TextField();
        nField.setPromptText("Movie_Name");

        TextField nTField = new TextField();
        nTField.setPromptText("Total_Tickets");

        TextField pTField = new TextField();
        pTField.setPromptText("Ticket_Price");

        DatePicker dp = new DatePicker();
        dp.setPromptText("Select_Date");

        TextField sTField = new TextField();    //Time Should be in 0-24 Hour format
        sTField.setPromptText("Start_Time");    // should write in 00:00 format

        TextField eTField = new TextField();
        eTField.setPromptText("End_Time");

        Button fcButton = new Button("Image_Choose");
        Button addRecord = new Button("    Add    ");

        fcButton.setOnAction(e->{           //Lambda Expression for choosing Image File
            try {
                movieImageChooser();
            } catch (FileNotFoundException fileNotFoundException) {
                alert(fileNotFoundException);
                fileNotFoundException.printStackTrace();
            }
        });



        addRecord.setOnAction(e->{          //Lambda Expression for triggering event and for adding record

            try{
                String name= nField.getText();
                int tickets = Integer.parseInt(nTField.getText());
                double price = Double.parseDouble(pTField.getText());
                Timestamp ts = new Timestamp(dp.getValue().getYear(),
                        dp.getValue().getMonthValue(),
                        dp.getValue().getDayOfMonth(),
                        Integer.parseInt(sTField.getText().split(":")[0]),
                        Integer.parseInt(sTField.getText().split(":")[1]),
                        0,
                        0
                );
                Timestamp te = new Timestamp(dp.getValue().getYear(),
                        dp.getValue().getMonthValue(),
                        dp.getValue().getDayOfMonth(),
                        Integer.parseInt(eTField.getText().split(":")[0]),
                        Integer.parseInt(eTField.getText().split(":")[1]),
                        0,
                        0
                );

                Database.addMovie(new Movie(name, tickets,price, ts, te, null), fileInputStream);
                moviesUploads(null);

            }catch (Exception y){
                alert(y);
            }

        });

        vb.getChildren().addAll(nField, nTField, pTField, dp, sTField, eTField, fcButton, addRecord);

        return vb;
    }




    public void movieImageChooser() throws FileNotFoundException {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        fileInputStream = new FileInputStream(file);
    }


    //After This Discount Button Functions
    //Main Function Triggered
    public void discountUploads(MouseEvent mouseEvent) throws SQLException {
        manipulatorBP.getChildren().clear();                    //Clear All Children
        manipulatorBP.getChildren().add(discountSection());     //add Discount Section Where Discount Displays
        manipulatorBP.getChildren().add(addDiscount());         //add Text-Fields and button to add Discount
    }


    //Method Which Add Show all The Available Discount Offers
    public ScrollPane discountSection() throws SQLException {
        ScrollPane sp = new ScrollPane();
        sp.setPrefHeight(500);

        HBox hb = new HBox();
        hb.setPadding(new Insets(50,10,50,10));
        hb.setSpacing(20);
        ArrayList<Discount> discountOffers = Database.getDiscountOffers();      // A Call to Database Class for Getting all
        if (discountOffers.size()<=0){                                          //Discount Offers from MySQL
            sp.setContent(new Label("No Discount Available"));
            return sp;
        }

        for (Discount d: discountOffers) {
            hb.getChildren().add(discount(d));
        }
        sp.setContent(hb);
        return sp;
    }


    public BorderPane discount(Discount d) throws SQLException {            //This Method Calls For Every Discount Offer
        BorderPane bp = new BorderPane();                                   //And Pack them into Borderpane
        bp.setPrefHeight(300);

        VBox vb= new VBox();
        vb.setPadding(new Insets(5));
        vb.getChildren().addAll(
                                labelStyle(new Label("ID:                      "+d.getId()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Name:                    "+d.getName()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Percentage:              "+d.getPercentage()),"-fx-text-fill:white;")
        );

        vb.setStyle("-fx-background-color: rgba(0,0,0,0.9);" +
                "-fx-background-radius:5;" +
                "-fx-padding: 20;" +
                "-fx-font-family: 'Century Gothic';");

        vb.setOnMouseClicked(e->{
            if(e.isControlDown()){
                try {
                    Database.deleteDiscount(d.getId());
                } catch (SQLException troubles) {
                    troubles.printStackTrace();
                }
            }
        });
        bp.setCenter(vb);
        return bp;
    }

    public VBox addDiscount(){          //This Method used for adding Text Fields
        VBox vb= new VBox();            //to get Input and record them to database
        vb.setSpacing(10);
        vb.prefHeight(30);
        vb.setPadding(new Insets(10,300,10,300));
        vb.setAlignment(Pos.BASELINE_CENTER);

        TextField nField = new TextField();
        nField.setPromptText("Discount_Name");

        TextField pField = new TextField();
        pField.setPromptText("Percentage:");


        Button addRecord = new Button("    Add    ");


        addRecord.setOnAction(e->{
            try {
                String name= nField.getText();
                double percentage = Double.parseDouble(pField.getText());
                Database.addDiscountOffer(new Discount(name,percentage));
                discountUploads(null);
            } catch (Exception exception) {
                alert(exception);
            }
        });
        vb.getChildren().addAll(nField, pField, addRecord);
        return vb;
    }



    public void userUploads(MouseEvent mouseEvent) throws SQLException {            //This is another section for Users
        manipulatorBP.getChildren().clear();
        manipulatorBP.getChildren().add(userSection());
        manipulatorBP.setAlignment(Pos.CENTER);
    }


    public ScrollPane userSection() throws SQLException {               //This Method add Complete ScrollPane to GUI
        ScrollPane sp = new ScrollPane();                               //GUI of Users Information
        sp.setPrefHeight(500);

        HBox hb = new HBox();
        hb.setPadding(new Insets(50,10,50,10));
        hb.setSpacing(20);

        ArrayList<Person> persons = Database.userArrayList();          //Here Database Static Method Call to get User data From
        if (persons.size()<=0){                                        //MySQL Database
            sp.setContent(new Label("No User Available"));
            return sp;
        }

        for (Person p: persons) {
            hb.getChildren().add(users(p));                             //This Piece of Code Same Work and data One By One
        }
        sp.setContent(hb);
        return sp;
    }

    public BorderPane users(Person p) throws SQLException {             //This Method used for adding Users information
        BorderPane bp = new BorderPane();
        bp.setPrefHeight(350);

        VBox vb= new VBox();
        vb.setPadding(new Insets(5));
        vb.getChildren().addAll(labelStyle(new Label("ID:                          "+p.getId()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Name:                        "+p.getName()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Username:                    "+p.getUsername()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Password:                    "+p.getPassword()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Phone-No:                    "+p.getPhoneNumber()),"-fx-text-fill:white;"),
                                labelStyle(new Label("User-Type:                   "+p.getUserType()),"-fx-text-fill:white;")
        );

        vb.setStyle("-fx-background-color: rgba(0,0,0,0.7);" +
                "-fx-background-radius:5;" +
                "-fx-padding: 20;" +
                "-fx-font-family: 'Century Gothic';");

        vb.setOnMouseClicked(e->{
            if(e.isControlDown()){
                try {
                    Database.deleteUser(p.getId());
                    userUploads(null);
                } catch (SQLException troubles) {
                    troubles.printStackTrace();
                }
            }
        });
        bp.setCenter(vb);
        return bp;
    }


    public void ticketsUploads(MouseEvent mouseEvent) throws SQLException {
        manipulatorBP.getChildren().clear();
        manipulatorBP.getChildren().add(ticketsSection());
        manipulatorBP.setAlignment(Pos.CENTER);
    }

    public ScrollPane ticketsSection() throws SQLException {
        ScrollPane sp = new ScrollPane();
        sp.setPrefHeight(500);

        HBox hb = new HBox();
        hb.setPadding(new Insets(50,10,50,10));
        hb.setSpacing(20);
        hb.setPrefHeight(500);
        hb.setAlignment(Pos.CENTER);

        ArrayList<Booking> tickets = Database.getTickets();
        if (tickets.size()<=0){
            sp.setContent(new Label("No Tickets Booked"));
            return sp;
        }

        for (Booking t: tickets) {
            hb.getChildren().add(tickets(t));
        }
        sp.setContent(hb);
        return sp;
    }

    public BorderPane tickets(Booking t) throws SQLException {              //This Method Work For tickets or called Booking
        BorderPane bp = new BorderPane();
        bp.setPrefHeight(350);
        bp.setBackground(imageConversion(t.getMovie().getImage()));

        VBox vb= new VBox();

        vb.setStyle("-fx-background-color: rgba(0,0,0,0.7);" +
                "-fx-background-radius:5;" +
                "-fx-padding: 20;" +
                "-fx-font-family: 'Century Gothic';");

        vb.getChildren().addAll(
                labelStyle(new Label("ID:"+" ".repeat(30)+t.getId()),"-fx-text-fill:white;"),
                labelStyle(new Label("Movie-Name:"+" ".repeat(9)+t.getMovie().getName()),"-fx-text-fill:white; -fx-font-weight:bold;"),
                labelStyle(new Label("Username:"+" ".repeat(11)+t.getPerson().getUsername()),"-fx-text-fill:white;"),
                labelStyle(new Label("User-Name:"+" ".repeat(10)+t.getPerson().getName()),"-fx-text-fill:white;"),
                labelStyle(new Label("Total-Price:"+" ".repeat(8)+t.getTotalPrice()),"-fx-text-fill:white;"),
                labelStyle(new Label("Total-Tickets:"+" ".repeat(6)+t.getTickets()),"-fx-text-fill:white;"),
                labelStyle(new Label("Review:"+" ".repeat(13)+(!Database.checkReview(t.getId()) ?"Not Reviewed":"Done")),(!Database.checkReview(t.getId()) ?"-fx-text-fill:red;":"-fx-text-fill:green;")),
                labelStyle(new Label((Database.checkReview(t.getId()) ?"       "+Database.getReview(t.getId()).getReview():"")),(!Database.checkReview(t.getId()) ?"-fx-text-fill:red;":"-fx-text-fill:blue; -fx-font-weight: bold;"))
        );

        vb.setOnMouseClicked(e->{
            if(e.isControlDown()){
                try {
                    Database.deleteTicket(t.getId());
                    ticketsUploads(null);
                } catch (SQLException troubles) {
                    troubles.printStackTrace();
                }
            }
        });
        bp.setCenter(vb);
        return bp;
    }


    public Label labelStyle(Label label, String style){                     //This Method Get Label and Style
        label.setStyle(style);
        label.setWrapText(true);                                            //Add Style to label and Return label
        return label;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {        //This Method implementation of Initializable Interface
        try {
            ticketsUploads(null);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
