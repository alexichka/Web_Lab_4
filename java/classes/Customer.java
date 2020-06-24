package classes;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;


public class Customer {

    private int id;
    private String name;
    private String login;
    private String password;
    private List<DeliveryPoint> orders;

    public Customer() {
        orders = new ArrayList<>();
    }

    public Customer(int id, String name,  String login,  String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        orders = new ArrayList<>();
    }

    // -----------SETTERS AND GETTERS------------

    public int getId() {
        return id;
    }

    public void setId(int id) throws IncorrectConstructorParameters {
        if (id >= 0)
            this.id = id;
        else throw new IncorrectConstructorParameters("Incorrect id ");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //----------------методы----------------

    public int size() {
        return orders.size();
    }

    public DeliveryPoint get(int index) {
        return orders.get(index);
    }

    public void add(DeliveryPoint deliveryPoint) {
        orders.add(deliveryPoint);
    }

    public void remove(DeliveryPoint deliveryPoint) {
         orders.remove(deliveryPoint);
    }

    @XmlElementWrapper(nillable = true, name = "name")
    @XmlElement(name = "delivery point")
    public List<DeliveryPoint> getProducts() {
        return this.orders;
    }

    public void setOrders(List<DeliveryPoint> product) {
        this.orders = orders;
    }
}
