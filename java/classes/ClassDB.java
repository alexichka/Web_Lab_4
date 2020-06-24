package classes;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class ClassDB {
    private static Product product;
    private static DeliveryPoint deliveryPoint;
    private static Customer customer;

    /*static {
        try {
            product = new Product("Chair", 400, 4, "11/02/2020");
            deliveryPoint = new DeliveryPoint("ul. Tretyakova");
            customer = new Customer(1, "Sasha", "alex", "1111");

        } сatch(IncorrectConstructorParameters | RemoteException incorrectConstructorParameters) {
            incorrectConstructorParameters.printStackTrace();
        }
    }*/

    // соединение с базой данных через JDBC URL
    String URL = "jdbc:mysql://localhost:3306/delivery_point?serverTimezone=Europe/Moscow&useSSL=false";
    String user = "root";
    String password = "0000";

    public ClassDB() throws IncorrectConstructorParameters, RemoteException {
    }

    private Connection connect() throws ClassNotFoundException, SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // для загрузки драйвера
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        // для подключения к базе данных
        return DriverManager.getConnection(URL, user, password);
    }

    public void createTable() throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connection = this.connect();
        // для взаимодействия с базой данных
        Statement statement = connection.createStatement();
        // execute выполняет любые команды и возвращает значение boolean
        statement.execute("CREATE TABLE product (product_name VARCHAR(30) PRIMARY KEY not null, price BIGINT not null, number INT not null, date_of_delivery VARCHAR(30) not null)");
        statement.execute("CREATE TABLE delivery_point (address VARCHAR(30) PRIMARY KEY not null)");
        statement.execute("CREATE TABLE order_products(address VARCHAR(30) not null, product_name VARCHAR(30) not null, PRIMARY KEY (address, product_name), FOREIGN KEY (address) REFERENCES delivery_point (address), FOREIGN KEY (product_name) REFERENCES product (product_name))");
        statement.execute("CREATE TABLE customer (customer_id INT PRIMARY KEY not null, name VARCHAR(30) not null, login VARCHAR(30) not null, password VARCHAR(30) not null)");
        statement.execute("CREATE TABLE point_and_customer (address VARCHAR(30) not null, customer_id INT not null, PRIMARY KEY (address, customer_id), FOREIGN KEY (address) REFERENCES delivery_point (address), FOREIGN KEY (customer_id) REFERENCES customer (customer_id))");
        System.out.println("Tables created");
        statement.close();
        connection.close();
    }

    public void insertTable() throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connect = this.connect();
        // классы для выполнения запросов
        PreparedStatement insert_product = connect.prepareStatement("INSERT product (product_name, price, number, date_of_delivery) VALUES (?,?,?,?)");
        insert_product.setString(1, "Lamp");
        insert_product.setInt(2, 1200);
        insert_product.setInt(3, 2);
        insert_product.setString(4, "17/05/20");

        PreparedStatement insert_order_products = connect.prepareStatement("INSERT  order_products (address, product_name) VALUES (?,?)");
        insert_order_products.setString(1, "ul. Pervomayskaya");
        insert_order_products.setString(2, "Lamp 'Moon'");

        PreparedStatement insert_delivery_point = connect.prepareStatement("INSERT delivery_point(address) VALUES (?)");
        insert_delivery_point.setString(1, "ul. Pervomayskaya");

        PreparedStatement insert_point_and_customer = connect.prepareStatement("INSERT  point_and_customer (address, customer_id) VALUES (?,?)");
        insert_point_and_customer.setString(1, "ul. Pervomayskaya");
        insert_point_and_customer.setInt(2, 1);

        PreparedStatement insert_customer = connect.prepareStatement("INSERT customer(customer_id, name, login, password) VALUES (?,?,?,?)");
        insert_customer.setInt(1, 1);
        insert_customer.setString(2, "Ivan");
        insert_customer.setString(3, "esquire01");
        insert_customer.setString(4, "780");

        System.out.printf("\n" +  " %d rows added in table PRODUCT", insert_product.executeUpdate());
        System.out.printf("\n" + " %d rows added in table ORDER_PRODUCTS", insert_order_products.executeUpdate());
        System.out.printf("\n" + " %d rows added in table DELIVERY_POINT", insert_delivery_point.executeUpdate());
        System.out.printf("\n" + " %d rows added in table DELIVERY_POINT", insert_point_and_customer.executeUpdate());
        System.out.printf("\n" + " %d rows added in table CUSTOMER", insert_customer.executeUpdate());

        connect.close();
    }

    public void addCustomer() throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IncorrectConstructorParameters {
        Connection connect = this.connect();

        PreparedStatement insert_customer = connect.prepareStatement("INSERT customer(customer_id, name, login, password) VALUES (?,?,?,?)");
        insert_customer.setInt(1, customer.getId());
        insert_customer.setString(2, customer.getName());
        insert_customer.setString(3, customer.getLogin());
        insert_customer.setString(4, customer.getPassword());

        Customer cu = new Customer(customer.getId(), customer.getName(), customer.getLogin(), customer.getPassword());

        System.out.printf("%d added rows in table CUSTOMER", insert_customer.executeUpdate());

        connect.close();
    }
    public void addDeliveryPoint(Customer cust) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        Connection connect = this.connect();

        PreparedStatement insert_delivery_point = connect.prepareStatement("INSERT delivery_point(address) VALUES (?)");
        insert_delivery_point.setString(1, deliveryPoint.getAddress());

        DeliveryPoint dp = new DeliveryPoint(deliveryPoint.getAddress());
        cust.add(dp);

        System.out.printf("%d rows added in table DELIVERY_POINT", insert_delivery_point.executeUpdate());

        connect.close();
    }

    public void addProduct(DeliveryPoint del) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IncorrectConstructorParameters {
        Connection connection = this.connect();

        PreparedStatement insert_product = connection.prepareStatement("INSERT product(product_name, price, number, date_of_delivery) VALUES (?,?,?,?)");
        insert_product.setString(1, product.getProductName());
        insert_product.setDouble(2, product.getProductPrice());
        insert_product.setInt(3, product.getNumberOfProduct());
        insert_product.setString(4, product.getDateOfDelivery());

        Product prod = new Product(product.getProductName(), product.getProductPrice(), product.getNumberOfProduct(), product.getDateOfDelivery());
        del.add(prod);
        connection.setAutoCommit(false);
        System.out.printf(" %d rows added in table PRODUCT", insert_product.executeUpdate());

        connection.close();
    }

    public void deleteDeliverYPoint(DeliveryPoint dp) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        Connection connect = this.connect();

        PreparedStatement delete_delivery_point = connect.prepareStatement("DELETE FROM delivery_point WHERE address = ?");

        delete_delivery_point.setString(1, dp.getAddress());
        System.out.println(delete_delivery_point.executeUpdate() + "%d rows deleted in table DELIVERY_POINTS");
        connect.setAutoCommit(false);
        connect.close();
    }

    public void deleteProduct(Product prod) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IncorrectConstructorParameters {
        Connection connect = this.connect();


        PreparedStatement delete_product = connect.prepareStatement("DELETE FROM product WHERE product_name = ?");
        delete_product.setString(1, prod.getProductName());

        System.out.println(delete_product.executeUpdate() + "%d rows deleted in table PRODUCT" );
        connect.setAutoCommit(false);
        connect.close();
    }

    //проверка существования клиента
    public void controlCustomers(Customer cust) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connect = this.connect();
        PreparedStatement select_customers = connect.prepareStatement("SELECT customer_id FROM customer WHERE login = ? AND password = ?");
        select_customers.setString(1, cust.getLogin());
        select_customers.setString(2, cust.getPassword());
        ResultSet resultSet = select_customers.executeQuery();
        while (resultSet.next()) {
            String customer_id = resultSet.getString("customer_id");
            System.out.println(customer_id);
        }
        connect.close();

    }

    //выбор всех списков, соответстующих заданному пользователю
      public DeliveryPoint get_point_by_customer_id(Customer cust) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException, IncorrectConstructorParameters {
        Connection connect = this.connect();

        PreparedStatement select_address = connect.prepareStatement("SELECT address FROM point_and_customer where customer_id = ? ");
        select_address.setObject(1, cust.getId());
        ResultSet resultSet = select_address.executeQuery();
        DeliveryPoint dp = null;
        while (resultSet.next()) {
            String address = resultSet.getString("address");
           dp = new DeliveryPoint(address);
        }
        connect.close();
        return dp;
    }

    //выбор всех элементов заданного списка
    public Product get_product_by_point(DeliveryPoint dp) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException, IncorrectConstructorParameters {
        Connection connect = this.connect();

        PreparedStatement select_product_name = connect.prepareStatement("SELECT product_name FROM order_products where address=? ; ");
        select_product_name.setObject(1, dp.getAddress());
        ResultSet resultSet = select_product_name.executeQuery();
        Product prod = null;
        while (resultSet.next()) {
            String product_name = resultSet.getString(1);
            int price = resultSet.getInt(2);
            int number = resultSet.getInt(3);
            String date_of_delivery = resultSet.getString(4);
            prod = new Product(product_name, price, number, date_of_delivery);
        }

        connect.close();
        return prod;
    }


    public Customer getCustomer(String login, String password) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connect = this.connect();
        PreparedStatement statement = connect.prepareStatement("SELECT * FROM customer WHERE login = ? AND password = ?");
        statement.setString(1, login);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        Customer customer = null;
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String login2 = resultSet.getString(3);
            String password2 = resultSet.getString(4);
            customer = new Customer(id, name, login2, password2);
        }
        statement.close();
        connect.close();
        return customer;
    }

    public DeliveryPoint getProductByAddress(String address) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection connect = this.connect();
        PreparedStatement statement = connect.prepareStatement("SELECT product_name FROM order_products where address=? ; ");
        statement.setObject(1, address);
        ResultSet resultSet = statement.executeQuery();
        DeliveryPoint dp = new DeliveryPoint();
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            int number = resultSet.getInt(2);
            int price = resultSet.getInt(3);
            String date = resultSet.getString(4);
            dp.add(new Product(name, number, price, date));
        }
        connect.close();
        return dp;
    }

    public List<DeliveryPoint> getCustomersProduct(Customer customer) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        int customerId = customer.getId();
        Connection connect = this.connect();
        PreparedStatement statement = connect.prepareStatement("SELECT distinct product_name FROM order_products,  delivery_point.point_and_customer \n" +
                "where order_products.address = point_and_customer.address and\n" +
                "customer_id=?");
        statement.setObject(1, customerId );
        ResultSet resultSet = statement.executeQuery();
        List<DeliveryPoint> prod = new LinkedList<>();
        while (resultSet.next()) {
           prod.add(this.getProductByAddress(resultSet.getString(1)));
        }
        connect.close();
        return prod;
    }

    public List<DeliveryPoint> productTable2(Customer customer) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, RemoteException, IncorrectConstructorParameters {
        int id_customer = customer.getId();
        Connection connection = this.connect();
        PreparedStatement statement = connection.prepareStatement("SELECT distinct product_name FROM delivery_point.order_products,  delivery_point.point_and_customer where delivery_point.order_products.address = delivery_point.point_and_customer.address and customer_id=?;");
        statement.setObject(1, id_customer);
        ResultSet resultSet = statement.executeQuery();
        List<DeliveryPoint> spisok = new LinkedList<>();
        List<Product> prod = new LinkedList<>();
        while (resultSet.next()) {
            String address = resultSet.getString("address");
            String name = resultSet.getString("name");
            int number = resultSet.getInt("number");
            int price = resultSet.getInt("price");
            String date = resultSet.getString("date");
            prod.add(new Product(name, number, price, date));
            spisok.add(new DeliveryPoint(address, prod));
        }
        connection.close();
        return spisok;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IncorrectConstructorParameters, IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ClassDB cl = new ClassDB();
        //cl.createTable();
        //cl.insertTable();
        //cl.controlCustomers();
        Customer one = new Customer(1, "Ivan", "esquire01", "780");
        //cl.get_point_by_customer_id(one);
        DeliveryPoint DP1 = new DeliveryPoint("ul. Tatiany");
        //cl.get_product_by_point(DP1);
        Customer Cu1 = new Customer(4, "Rodion", "love11", "000");
        //cl.controlCustomers(Cu1);
        Product product4 = new Product("Nosok", 150, 6, "21/05/20");
        //cl.deleteProduct(product4);
        //cl.deleteDeliverYPoint(one);
        cl.addProduct(DP1);
    }
}