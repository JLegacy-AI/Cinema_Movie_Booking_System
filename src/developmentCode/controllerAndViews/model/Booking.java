package developmentCode.controllerAndViews.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Booking {
    private int id;
    private Person person;
    private int tickets;
    private Timestamp time;
    private double totalPrice;
    private Movie movie;
    private Discount discount;

    public Booking() { }

    public Booking(int tickets, Movie movie) {
        this.tickets = tickets;
        this.time = Timestamp.valueOf(LocalDateTime.now());
        this.movie = movie;
    }

    public Booking(int tickets, double totalPrice, Movie movie) {
        this(tickets, movie);
        this.totalPrice = totalPrice;
    }

    public Booking(int tickets, double totalPrice, Movie movie, Discount discount) {
        this(tickets, totalPrice, movie);
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}
