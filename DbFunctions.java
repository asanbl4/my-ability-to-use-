import java.sql.*;
import java.util.ArrayList;

public class DbFunctions {
    public Connection connect_to_db(String dbname, String user, String password){
        Connection conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, password);
            if(conn!=null){
                System.out.println("Connection established");
            }else{
                System.out.println("Connection failed");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return conn;
    }
    public void createTable(Connection conn, String table_name){
        Statement statement;
        try{
            String query = "create table " + table_name + "(id SERIAL primary key, title varchar(200), author varchar(200), year INT, price INT)";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("table created");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void createTableCustomers(Connection conn, String table_name){
        Statement statement;
        try{
            String query = "create table " + table_name + "(id SERIAL primary key, money INT)";
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("table created");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void insertCustomer(Connection conn, String table_name, int money){
        PreparedStatement preparedStatement;
        try{
            String query = String.format("insert into %s(money) values(?)", table_name);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, money);
            preparedStatement.executeUpdate();
            System.out.println("customer added");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void insert_row(Connection conn, String table_name, String title, String author, int year, int price){
        PreparedStatement preparedStatement;
        try{
            String query = String.format("insert into %s(title, author, year, price) values(?, ?, ?, ?)", table_name);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, price);
            preparedStatement.executeUpdate();
            System.out.println("row added");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public ArrayList<ArrayList<String>> readData(Connection conn, String table_name){
        Statement statement;
        ResultSet rs;
        ArrayList<ArrayList<String>> res = new ArrayList<>();
        try{
            String query = String.format("select * from %s", table_name);
            statement = conn.createStatement();
            rs = statement.executeQuery(query);
            while(rs.next()){
                ArrayList<String> temp = new ArrayList<>();
                temp.add(rs.getString("id"));
                temp.add(rs.getString("title"));
                temp.add(rs.getString("author"));
                temp.add(rs.getString("year"));
                temp.add(rs.getString("price"));
                res.add(temp);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return res;
    }
    public int readPrice(Connection conn, String table_name, String title){
        PreparedStatement preparedStatement;
        ResultSet rs;
        int res = 0;
        try{
            String query = String.format("select price from %s where title = ?", table_name);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, title);
            rs = preparedStatement.executeQuery();
            while(rs.next()) {
                res = rs.getInt("price");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return res;
    }
    public ArrayList<Integer> readCustomer(Connection conn, String table_name, int money){
        PreparedStatement preparedStatement;
        ResultSet rs;
        ArrayList<Integer> res = new ArrayList<>();
        try {
            String query = String.format("select * from %s where money = ?", table_name);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, money);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                res.add(rs.getInt("id"));
                res.add(rs.getInt("money"));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return res;
    }
    public int readIdCustomer(Connection conn, String table_name, int money){
        PreparedStatement preparedStatement;
        ResultSet rs;
        int res = 0;
        try {
            String query = String.format("select id from %s where money = ?", table_name);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, money);
            rs = preparedStatement.executeQuery();
            while(rs.next()) {
                res = rs.getInt("id");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return res;
    }
    public int readMoneyCustomer(Connection conn, String table_name, int id){
        PreparedStatement preparedStatement;
        ResultSet rs;
        int res = 0;
        try {
            String query = String.format("select money from %s where id = ?", table_name);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();
            while(rs.next()) {
                res = rs.getInt("money");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return res;
    }
    public void deleteRowByName(Connection conn, String table_name, String title){
        PreparedStatement preparedStatement;
        try{
            String query = String.format("delete from %s where title = ?", table_name);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
            System.out.println("row deleted");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void insertBookByName(Connection conn, String table_name, String title){
        PreparedStatement preparedStatement;
        ResultSet rs;
        String author = "";
        int year = 0;
        int price = 0;
        try {
            String query = "select * from books where title = ?";
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, title);
            rs = preparedStatement.executeQuery();
            while (rs.next()){
                title = rs.getString("title");
                author = rs.getString("author");
                year = rs.getInt("year");
                price = rs.getInt("price");
            }
            insert_row(conn, table_name, title, author, year, price);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void update_money(Connection conn, String table_name, int id, int price){
        PreparedStatement preparedStatement;
        try {
            String query = String.format("update %s set money = money - ? where id = ?", table_name);
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, price);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void deleteTable(Connection conn, String table_name){
        Statement statement;
        try{
            String query = String.format("drop table %s", table_name);
            statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("table deleted");
        }catch (Exception e){
            System.out.println(e);
        }
    }
}


