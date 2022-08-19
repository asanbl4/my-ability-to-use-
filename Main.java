import java.sql.Connection;
import java.util.*;

class Library{
    private static String title;
    private static int age_of_construction;
    public static void setAge_of_construction(int age_of_construction) {Library.age_of_construction = age_of_construction;}
    public static void setTitle(String title) {Library.title = title;}
    public static int getAge_of_construction() {return age_of_construction;}
    public static String getTitle() {return title;}
    public static void getBooks() {
        DbFunctions db = new DbFunctions();
        Connection conn = db.connect_to_db("postgres", "postgres", "mysecretpassword");
        ArrayList<ArrayList<String>> books = db.readData(conn, "books");
    }

}

class Book{
    private String name;
    private String author;
    private int age;
    private int price;
    public void setPrice(int price) {this.price = price;}

    public int getPrice() {return price;}
    public void setAge(int age) {this.age = age;}
    public int getAge() {return age;}
    public void setAuthor(String author) {this.author = author;}
    public String getAuthor() {return author;}
    public void setName(String name) {this.name = name;}

    public String getName() {return name;}
    public void addToTheLibrary(){
        DbFunctions db = new DbFunctions();
        Connection conn = db.connect_to_db("postgres", "postgres", "mysecretpassword");
        db.insert_row(conn, "books", this.name, this.author, this.age, this.price);
    }
}
class Cashier implements Person, Worker{
    private final DbFunctions db = new DbFunctions();
    private final Connection conn = db.connect_to_db("postgres", "postgres", "mysecretpassword");
    private String name;
    private int age;
    private boolean isWorking;
    @Override
    public void setAge(int age) {this.age = age;}
    @Override
    public int getAge() {return age;}
    @Override
    public void setName(String name) {this.name = name;}
    @Override
    public String getName() {return name;}
    @Override
    public void setIsWorking(boolean isWorking) {this.isWorking = isWorking;}
    @Override
    public boolean getIsWorking() {return isWorking;}
    @Override
    public void work(Book book) {
        System.out.printf("The book costs %d", db.readPrice(conn, "books", book.getName()));
        System.out.println();
        isWorking = true;
    }
    @Override
    public void getFree() {
        isWorking = false;
    }
    @Override
    public void helpTheChild(String parent) {
        System.out.println((isWorking) ? "Don't worry, I'll help you to find your parent, " + parent :
                "Sorry, call somebody who is not busy 'cause I'm working right now");
    }
}
class Customer implements Person{
    private int age;
    private String name;
    private int money;
    private final int id;
    public Customer(int money){
        DbFunctions db = new DbFunctions();
        Connection conn = db.connect_to_db("postgres", "postgres", "mysecretpassword");
        db.insertCustomer(conn, "customers", money);
        id = db.readIdCustomer(conn, "customers", money);
        this.money = db.readMoneyCustomer(conn, "customers", id);
        db.createTable(conn, String.format("Customer_%d", id));
    }
    @Override
    public int getAge() {return age;}
    @Override
    public String getName() {return name;}
    @Override
    public void setName(String name) {this.name = name;}
    @Override
    public void setAge(int age) {this.age = age;}
    public void buy(String book, Cashier cashier){
        try{
            DbFunctions db = new DbFunctions();
            Connection conn = db.connect_to_db("postgres", "postgres", "mysecretpassword");

            int price = db.readPrice(conn, "books", book);
            if(money >= price && !cashier.getIsWorking()){
                System.out.printf("You have successfully bought the book for %d at cashier %s", price, cashier.getName());
                System.out.println();
                db.update_money(conn, "customers", id, price);
                money = db.readMoneyCustomer(conn, "customers", id);
                System.out.printf("Customer_%d has %d money left \n", id, money);
                db.insertBookByName(conn, String.format("Customer_%d", id), book);
                db.deleteRowByName(conn, "books", book);
            } else if (money >= price && cashier.getIsWorking()) {
                System.out.println("The cashier is busy. Please, select the one who is free \n");
            } else{
                System.out.println("not enough money you broke ass \n");
            }
        }catch (Exception e){
            System.out.println("Enter the name of the book correctly \n");
        }
    }
}
class CustomersChild extends Customer implements Person{
    private final String parent = super.getName();
    private int age;
    private String name;
    @Override
    public void setName(String name) {this.name = name;}
    @Override
    public String getName() {return name;}
    @Override
    public void setAge(int age) {this.age = age;}
    @Override
    public int getAge() {return age;}

    public CustomersChild(int money) {
        super(money);
    }
    public void askForHelp(Cashier cashier){
        cashier.helpTheChild(super.getName());
    }
}
public class Main {
    public static void create_tables(){
        DbFunctions db = new DbFunctions();
        Connection conn = db.connect_to_db("postgres", "postgres", "mysecretpassword");
//        db.deleteTable(conn, "books");
//        db.deleteTable(conn, "customers");
//        db.deleteTable(conn, "customer_1");
//        db.deleteTable(conn, "customer_2");
        db.createTable(conn, "books");
        db.createTableCustomers(conn, "customers");
    }
    public static void main(String[] args) {
        create_tables();
        Book b = new Book();
        b.setName("Angry birds");
        b.setAuthor("S.Hawking");
        b.setAge(1967);
        b.setPrice(1200);
        b.addToTheLibrary();
        Cashier Alina = new Cashier();
        Alina.setName("Alina");
        Alina.setAge(25);
        Alina.setIsWorking(false);
        Alina.work(b);
        Alina.getFree();
        Book c = new Book();
        c.setName("The history of time");
        c.setAuthor("S.Hawking");
        c.setAge(1967);
        c.setPrice(1300);
        c.addToTheLibrary();
        Book e = new Book();
        e.setName("blabla");
        e.setAuthor("Assanali");
        e.setAge(1995);
        e.setPrice(1);
        e.addToTheLibrary();
        Book g = new Book();
        g.setName("aye");
        g.setAuthor("aye");
        g.setAge(1995);
        g.setPrice(1);
        g.addToTheLibrary();
        Customer d = new Customer(1200);
        Customer f = new Customer(1299);
        f.buy(b.getName(), Alina);
        d.buy(e.getName(), Alina);
        f.buy(g.getName(), Alina);


    }
}
