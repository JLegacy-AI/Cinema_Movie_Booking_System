package developmentCode.controllerAndViews.model;

import java.sql.Blob;
import java.sql.Timestamp;

public class Movie {
    private int id;
    private String name;
    private int totalTickets;
    private double pricePerTicket;
    private Timestamp startTime;
    private Timestamp endTime;
    private Blob image;

    public Movie(){}

    public Movie(int totalTickets, double pricePerTicket, Timestamp startTime, Timestamp endTime, Blob image) {
        this.totalTickets = totalTickets;
        this.pricePerTicket = pricePerTicket;
        this.startTime = startTime;
        this.endTime = endTime;
        this.image = image;
    }

    public Movie(String name, int totalTickets, double pricePerTicket, Timestamp startTime, Timestamp endTime, Blob image) {
        this.name = name;
        this.totalTickets = totalTickets;
        this.pricePerTicket = pricePerTicket;
        this.startTime = startTime;
        this.endTime = endTime;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public double getPricePerTicket() {
        return pricePerTicket;
    }

    public void setPricePerTicket(double pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
