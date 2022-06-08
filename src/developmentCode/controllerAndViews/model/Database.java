package developmentCode.controllerAndViews.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Database implements GlobalVariables {


    //Method for adding New User to MySQL
    public static void newUserAdd(Person person) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "INSERT INTO user(name, username, password, phone_number, user_type) VALUES(?,?,?,?,?)";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, person.getName());
        st.setString(2, person.getUsername());
        st.setString(3, person.getPassword());
        st.setString(4, person.getPhoneNumber());
        st.setString(5, person.getUserType());
        st.execute();
    }

    //Method for checking User
    public static boolean userCheck(String uN) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM user WHERE username=?;";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, uN);
        ResultSet rs = st.executeQuery();
        return rs.next();
    }

    //Method for checking particular User with three parameters
    public static boolean userDataCheck(String uN, String p, String user) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM user WHERE username=? AND password=? AND user_type=?;";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, uN);
        st.setString(2, p);
        st.setString(3, user);
        ResultSet rs = st.executeQuery();
        return rs.next();
    }


    //Method for adding Movies
    public static void addMovie(Movie m, FileInputStream fis) throws SQLException, IOException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        Blob blob = con.createBlob();
        blob.setBytes(1, fis.readAllBytes());
        String sql = "INSERT INTO movies(name, av_tickets, price_ticket,time_start, time_end, image)" +
                "VALUES(?,?,?,?,?,?);";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, m.getName());
        st.setInt(2, m.getTotalTickets());
        st.setDouble(3, m.getPricePerTicket());
        st.setTimestamp(4, m.getStartTime());
        st.setTimestamp(5, m.getEndTime());
        st.setBlob(6, blob);
        st.execute();
    }

    //Method for getting Movies List
    public static ArrayList<Movie> movieArrayList() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM movies;";
        return moviesData(con, sql);
    }

    //method for getting future Played Movies
    public static ArrayList<Movie> futureMovieArrayList() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM movies WHERE start_time > NOW();";
        return moviesData(con, sql);
    }


    //Method for getting running Movies
    public static ArrayList<Movie> runningMovieArrayList() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM movies WHERE start_time < NOW() AND end_time > NOW();";
        return moviesData(con, sql);
    }

    //Method for getting Played Movies List
    public static ArrayList<Movie> playedMovieArrayList() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM movies WHERE end_time < NOW();";
        return moviesData(con, sql);
    }

    //Above Methods Used it to retrieve Data
    private static ArrayList<Movie> moviesData(Connection con, String sql) throws SQLException {
        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        ArrayList<Movie> alm = new ArrayList<>();
        while (rs.next()) {
            Movie m = new Movie(rs.getString("name"), rs.getInt("av_tickets"), rs.getDouble("price_ticket"), rs.getTimestamp("time_start"), rs.getTimestamp("time_end"), rs.getBlob("image"));
            m.setId(rs.getInt("id"));
            alm.add(m);
        }
        return alm;
    }

    //Method for deleting Movies From mysql
    public static void movieDelete(int id) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "DELETE FROM movies WHERE id=?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, id);
        st.execute();
    }

    //Method for adding Discounts in mysql
    public static void addDiscountOffer(Discount dis) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "INSERT INTO discount(name, pct) VALUES(?,?)";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, dis.getName());
        st.setDouble(2, dis.getPercentage());
        st.execute();
    }

    //Method for getting discount offers from mysql
    public static ArrayList<Discount> getDiscountOffers() throws SQLException {
        return getDiscounts();
    }

    //Method for deleting Discount Offer from Mysql
    public static void deleteDiscount(int id) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "DELETE FROM discount WHERE id=?;";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, id);
        st.execute();
    }

    //Method for getting Users List
    public static ArrayList<Person> userArrayList() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM user;";
        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        ArrayList<Person> arrayList = new ArrayList<>();
        while (rs.next()) {
            Person ps = new Person(rs.getString("name"), rs.getString("username"), rs.getString("password"), rs.getString("phone_number"), rs.getString("user_type"), rs.getString("user_type").equalsIgnoreCase("guest"));
            ps.setId(rs.getInt("id"));
            arrayList.add(ps);
        }
        return arrayList;
    }

    //method for deleting User from mysql
    public static void deleteUser(int id) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "DELETE FROM user WHERE id=?;";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, id);
        st.execute();
    }

    //Method for getting user by Username
    public static Person getUser(String u) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM user WHERE username=?;";
        PreparedStatement st = con.prepareStatement(sql);
        st.setString(1, u);
        ResultSet rs = st.executeQuery();
        rs.next();
        Person ps = new Person(rs.getString("name"), rs.getString("username"), rs.getString("password"), rs.getString("phone_number"), rs.getString("user_type"), rs.getString("user_type").equalsIgnoreCase("guest"));
        ps.setId(rs.getInt("id"));
        System.out.println(rs.getInt("id"));
        return ps;
    }

    //Getting Discount Offers from mysql
    public static ArrayList<Discount> discountArrayList() throws SQLException {
        return getDiscounts();
    }

    //This method used for method which work for getting discount offers
    private static ArrayList<Discount> getDiscounts() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM discount;";
        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        ArrayList<Discount> arrayList = new ArrayList<>();
        while (rs.next()) {
            Discount dis = new Discount(rs.getString("name"), rs.getDouble("pct"));
            dis.setId(rs.getInt("id"));
            arrayList.add(dis);
        }
        return arrayList;
    }

    //Method for adding Tickets / Booking
    public static void addTickets(Booking ticket) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "";

        if (!ticket.getPerson().isGuest()) {
            sql = "INSERT INTO tickets(date   ," +
                    "quantity," +
                    "total_price ," +
                    "movies_id   ," +
                    "discount_id ," +
                    "user_id) VALUES(?,?,?,?,?,?)";
        } else {
            sql = "INSERT INTO tickets(date   ," +
                    "quantity," +
                    "total_price ," +
                    "movies_id   ," +
                    "user_id) VALUES(?,?,?,?,?)";
        }
        PreparedStatement st = con.prepareStatement(sql);
        st.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        st.setInt(2, ticket.getTickets());
        st.setDouble(3, ticket.getTotalPrice());
        System.out.println(ticket.getTotalPrice() + "   )-(    " + ticket.getTickets());
        st.setInt(4, ticket.getMovie().getId());
        if (!ticket.getPerson().isGuest()) {
            st.setInt(5, ticket.getDiscount().getId());
            st.setInt(6, ticket.getPerson().getId());
        } else {
            st.setInt(5, ticket.getPerson().getId());
        }

        System.out.println(ticket.getPerson().getId());
        System.out.println(ticket.getMovie().getId());
        st.execute();
    }


    //Method work for getting Tickets
    public static ArrayList<Booking> getTickets() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT tickets.id, user.username, user.name, movies.image, tickets.quantity, movies.name,tickets.total_price\n" +
                "FROM tickets\n" +
                "INNER JOIN user ON user.id = tickets.user_id\n" +
                "INNER JOIN movies ON movies.id = tickets.movies_id;";
        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        return getTickets(rs);
    }


    private static ArrayList<Booking> getTickets(ResultSet rs) throws SQLException {
        ArrayList<Booking> tickets = new ArrayList<Booking>();
        while (rs.next()) {
            Person person = new Person();
            person.setName(rs.getString("user.name"));
            person.setUsername(rs.getString("user.username"));
            Movie movie = new Movie();
            movie.setName(rs.getString("movies.name"));
            Booking ticket = new Booking();
            ticket.setId(rs.getInt("tickets.id"));
            ticket.setTickets(rs.getInt("tickets.quantity"));
            ticket.setTotalPrice(rs.getDouble("tickets.total_price"));
            movie.setImage(rs.getBlob("movies.image"));
            ticket.setMovie(movie);
            ticket.setPerson(person);

            tickets.add(ticket);
        }

        return tickets;
    }

    //Method for getting Ticket for particular User
    public static ArrayList<Booking> getUserTickets(int id) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT tickets.id, user.username, user.name, tickets.quantity, movies.name, movies.image,tickets.total_price\n" +
                "FROM tickets\n" +
                "INNER JOIN user ON user.id = tickets.user_id\n" +
                "INNER JOIN movies ON movies.id = tickets.movies_id\n" +
                "WHERE user.id=?;";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        return getTickets(rs);
    }

    //Delete Ticket / Booking
    public static void deleteTicket(int id) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "DELETE FROM tickets WHERE id=?;";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, id);
        st.execute();
    }

    //Getting Reviews
    public static ArrayList<Review> getTicketsReview() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM review;";
        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        ArrayList<Review> reviews = new ArrayList<>();
        while (rs.next()) {
            Review rv = new Review();
            rv.setReview(rs.getString("review"));
            rv.setId(rs.getInt("id"));
            reviews.add(rv);
        }

        return reviews;
    }

    //Adding Reviews TO movies
    public static void addTicketsReview(Review rv) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "INSERT INTO review (tickets_id, review)" +
                "VALUES(?,?);";

        System.out.println(rv.getTicket_id());
        System.out.println(rv.getReview());
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, rv.getTicket_id());
        st.setString(2, rv.getReview());
        st.execute();
    }

    //For Checking either a movie Reviewed
    public static boolean checkReview(int ticket_id) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM review WHERE tickets_id=?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, ticket_id);
        ResultSet rs = st.executeQuery();
        return rs.next();
    }

    public static Review getReview(int ticket_id) throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, admin, pass);
        String sql = "SELECT * FROM review WHERE tickets_id=?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, ticket_id);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            Review rv = new Review();
            rv.setReview(rs.getString("review"));
            return rv;
        }

        return null;
    }


}