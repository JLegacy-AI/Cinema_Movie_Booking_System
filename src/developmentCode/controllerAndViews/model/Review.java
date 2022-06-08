package developmentCode.controllerAndViews.model;

public class Review {
    private String review;
    private int ticket_id;
    private int id;

    public Review(){}

    public Review(String review, int id) {
        this.review = review;
        this.id = id;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
