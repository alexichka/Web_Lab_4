package classes;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "customerBean")
@SessionScoped
public class CustomerBean {
    private Customer customer;
    private CustomerEJB customerEJB;
    private Product product;
    private int id_customer;

    public CustomerBean() {
        customer = new Customer();
        customerEJB = new CustomerEJB();
        customerEJB.updateSession();
        product = new Product();
    }

    public void setCustomerEJB(CustomerEJB customerEJB) {
        this.customerEJB = customerEJB;
    }

    public CustomerEJB getCustomerEJB() {
        return customerEJB;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getCustomeId() {
        return customer.getId();
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public Product getCar() {
        return product;
    }

    public void setCar(Product car) {
        this.product = product;
    }

    public String validateCustomerLogin() throws SQLException, ClassNotFoundException, IncorrectConstructorParameters, RemoteException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Customer cust = customerEJB.validateUserLogin(customer.getLogin(), customer.getPassword());
        if (cust != null) {
            this.customer = cust;
            return "ok";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Такого пользователя не существует"));
            return "no";
        }
    }

    public List<DeliveryPoint> getCustomerCar() throws SQLException, ClassNotFoundException, IncorrectConstructorParameters, RemoteException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return customerEJB.getCustomerProduct();
    }

    public String back() {
        customerEJB.invalidateSession();
        return "back";
    }

    public String validate() {
        return "insert";
    }

    public String seeXmlView() throws SQLException, ClassNotFoundException, IncorrectConstructorParameters, RemoteException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return this.validateCustomerLogin().equals("no") ? "no" : "resultXML";
    }

    public String insertAndGoToResultPage() {
        try {
            customerEJB.addProduct(product, id_customer);
            return "result";
        } catch (SQLException | ClassNotFoundException | IncorrectConstructorParameters | RemoteException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ошибка добавления"));
            return "insert";
        }
    }

    public String getResultXml() {
        return customerEJB.getUserDataXMLRepresentation();
    }
}
