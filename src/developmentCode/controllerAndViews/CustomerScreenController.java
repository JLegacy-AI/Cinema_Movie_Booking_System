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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerScreenController implements Initializable, SharedVariables {
    public static Person person= new Person();
    public HBox moviesCollection;
    public ScrollPane scrollPane;
    public ArrayList<CheckBox> cb_list=new ArrayList<>();
    public ArrayList<CheckBox> cb_list_1=new ArrayList<>();
    public Double totalAmount=0.00;
    public Label amount=new Label("$0.00");
    public HBox addTickets;
    public Booking ticket= new Booking();


    public void logOut(MouseEvent mouseEvent) {
    }


    public void makeMoviesList() throws SQLException {
        moviesCollection.setPadding(new Insets(50,10,50,10));
        moviesCollection.setSpacing(20);
        ArrayList<Movie> movie = Database.movieArrayList();
        if (movie.size()<=0){
            scrollPane.setContent(new Label("No Movies Available"));
        }

        for (Movie m : movie) {
            moviesCollection.getChildren().add(movie(m));
        }
    }


    public BorderPane movie(Movie m) throws SQLException {
        BorderPane bp = new BorderPane();
        bp.setStyle(
                "-fx-padding: 10");


        Pane p = new Pane();
        p.setPrefWidth(100);
        p.setPrefHeight(100);
        bp.setBackground(imageConversion(m.getImage()));
        bp.setTop(p);

        p.setOnMouseClicked(e->{
            System.out.println("Selected"+m.getId());
        });

        VBox vb= new VBox();
        vb.getChildren().addAll(
                labelStyle( new Label("Name:"+" ".repeat(15)+m.getName()),"-fx-text-fill:white;"),
                labelStyle(new Label("Start-At:"+" ".repeat(11)+m.getStartTime().toLocalDateTime().format(dtf)),"-fx-text-fill:white;"),
                labelStyle(new Label("End-At:"+" ".repeat(13)+m.getEndTime().toLocalDateTime().format(dtf)),"-fx-text-fill:white;"),
                labelStyle(new Label("Price-Ticket:"+" ".repeat(7)+m.getPricePerTicket()),"-fx-text-fill:white;"),
                labelStyle(new Label("Available-Tickets:"+" ".repeat(9)+m.getTotalTickets()),"-fx-text-fill:white;")
        );


        vb.setStyle("-fx-padding: 10;" +
                "-fx-font-family: 'Century Gothic';" +
                "-fx-background-color: rgba(0,0,0,0.8)");

        CheckBox checkBox= new CheckBox("Select");
        checkBox.setStyle("-fx-text-fill: white;");
        vb.getChildren().add(checkBox);
        bp.setCenter(vb);

        cb_list.add(checkBox);
        checkBox.setOnMouseClicked(e->{
            ticket.setMovie(m);
            System.out.println(ticket.getMovie().getName());
            System.out.println(ticket.getMovie().getId());
            for (CheckBox box : cb_list) {
                if(box!=e.getSource()){
                    box.setSelected(false);
                }
            }
            totalAmount=m.getPricePerTicket();
            amount.setText("$"+m.getPricePerTicket());
        });

        return bp;
    }

    public Background imageConversion(Blob blob) throws SQLException {
        InputStream in = blob.getBinaryStream();
        Image img = new Image(in);
        BackgroundImage backgroundImage = new BackgroundImage(img,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100,100,false,false, false, true));
        return new Background(backgroundImage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            moviesCollection.getChildren().clear();
            addTickets.getChildren().clear();
            makeMoviesList();
            addTicketGUI();
            ticketsSection();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addTicketGUI() throws SQLException {
        VBox vb = new VBox();
        vb.setSpacing(20);
        vb.setPadding(new Insets(50));

        TextField tField = new TextField();
        tField.setPromptText("Total_Tickets");

        Button add = new Button("Add Ticket");

        add.setOnAction(e->{
            try {
                ticket.setTickets(Integer.parseInt(tField.getText()));
                ticket.setPerson(person);
                double discount=0;
                if(!person.isGuest())
                    discount = ticket.getMovie().getPricePerTicket()*ticket.getTickets()*(ticket.getDiscount().getPercentage()/100.0);
                System.out.println(discount);
                ticket.setTotalPrice(ticket.getMovie().getPricePerTicket()*ticket.getTickets()-discount);
                amount.setText("$"+ticket.getTotalPrice());
                System.out.println();
                Database.addTickets(ticket);
                ticketsSection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        vb.getChildren().add(tField);
        if(person.isGuest()){
            vb.getChildren().add(new Label("No Discount Available"));
        }else{
            vb.getChildren().add(discountChoiceBox());
        }
        vb.getChildren().add(add);
        vb.getChildren().add(amount);

        addTickets.getChildren().add(vb);

    }

    public ScrollPane discountChoiceBox() throws SQLException {
        ScrollPane sp = new ScrollPane();
        HBox hb = new HBox();
        hb.setSpacing(10);

        ArrayList<Discount> arrayList = Database.discountArrayList();
        for (Discount discount : arrayList) {
            hb.getChildren().add(checkBoxDiscount(discount));
        }
        sp.setContent(hb);
        return sp;
    }


    public CheckBox checkBoxDiscount(Discount discount){
        CheckBox cb = new CheckBox(discount.getName()+" Pct: "+discount.getPercentage());
        cb.setOnMouseClicked(e->{
            ticket.setDiscount(discount);
            System.out.println(discount.getId()+"   "+discount.getName());
            for (CheckBox box : cb_list_1) {
                if(box!=e.getSource()){
                    box.setSelected(false);
                }
            }
            amount.setText("$"+totalAmount*discount.getPercentage());
        });
        cb_list_1.add(cb);
        return cb;
    }

    public void ticketsSection() throws SQLException {
        ScrollPane sp = new ScrollPane();

        sp.setPrefHeight(500);
        VBox vb= new VBox();
        vb.getChildren().add(new Label("  Tickets You Have Booked"));

        HBox hb = new HBox();
        hb.setPadding(new Insets(50,10,50,10));
        hb.setSpacing(20);
        hb.setPrefHeight(350);
        hb.setAlignment(Pos.CENTER);

        ArrayList<Booking> tickets = Database.getUserTickets(person.getId());

        if (tickets.size()<=0){
            sp.setContent(new Label("   \n      No Tickets Booked"));
        }

        for (Booking t: tickets) {
            hb.getChildren().add(tickets(t));
        }

        vb.getChildren().add(hb);

        sp.setContent(vb);

        addTickets.getChildren().clear();
        addTicketGUI();
        addTickets.getChildren().add(sp);
    }

    public BorderPane tickets(Booking t) throws SQLException {
        BorderPane bp = new BorderPane();
        bp.setBackground(imageConversion(t.getMovie().getImage()));

        VBox vb= new VBox();

        vb.setStyle("-fx-background-color: rgba(0,0,0,0.7);" +
                "-fx-background-radius:5;" +
                "-fx-padding: 20;" +
                "-fx-font-family: 'Century Gothic';");

        vb.getChildren().addAll(
                                labelStyle(new Label("ID:"+" ".repeat(30)+t.getId()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Movie-Name:"+" ".repeat(9)+t.getMovie().getName()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Username:"+" ".repeat(11)+t.getPerson().getUsername()),"-fx-text-fill:white;"),
                                labelStyle(new Label("User-Name:"+" ".repeat(10)+t.getPerson().getName()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Total-Price:"+" ".repeat(8)+t.getTotalPrice()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Total-Tickets:"+" ".repeat(6)+t.getTickets()),"-fx-text-fill:white;"),
                                labelStyle(new Label("Review:"+" ".repeat(13)+(!Database.checkReview(t.getId()) ?"Not Reviewed":"Done")),(!Database.checkReview(t.getId()) ?"-fx-text-fill:red;":"-fx-text-fill:green;"))
        );

        bp.setOnMouseClicked(e->{
            try {
                if(e.getButton().toString().equalsIgnoreCase("SECONDARY") && !Database.checkReview(t.getId())){
                   TextInputDialog tid = new TextInputDialog();
                   tid.setHeaderText(null);
                   tid.setContentText("Review");
                   tid.setTitle("Feedback");
                    if(tid.showAndWait().isPresent()){
                        Review rv= new Review();
                        rv.setTicket_id(t.getId());
                        rv.setReview(tid.getResult());
                        try {
                            Database.addTicketsReview(rv);

                            initialize(null, null);
                        } catch (SQLException throwables) {
                            alert(new Exception(throwables.getMessage()));
                        }
                    }
                }
            } catch (SQLException throwables) {
                alert(new Exception(throwables.getMessage()));
            }
        });
        bp.setCenter(vb);

        return bp;
    }

    public void logout(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("views/Sign_In_Screen.fxml"));
        stage.setScene(new Scene(root, 600,250));
    }

    public Label labelStyle(Label label, String style){
        label.setStyle(style);
        return label;
    }

}
