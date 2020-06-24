package classes;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;


@Stateless
public class CustomerEJB {
    private HttpSession session;
    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getRequest();
    }


    public HttpServletResponse getResponse() {
        return (HttpServletResponse) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getResponse();
    }

    public CustomerEJB() {
        this.updateSession();
    }


    public Customer getCustomer() {
        if (session != null) {
            return (Customer) session.getAttribute("customer");
        } else return null;
    }


    public void setCustomer(Customer customer) {
        session.setAttribute("customer", customer);
    }


    public Customer validateUserLogin(String login, String password) throws SQLException, ClassNotFoundException, IncorrectConstructorParameters, RemoteException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Customer customer = new ClassDB().getCustomer(login, password);
        this.setCustomer(customer);
        return customer;
    }


    public List<DeliveryPoint> getCustomerProduct() throws SQLException, ClassNotFoundException, IncorrectConstructorParameters, RemoteException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Customer customer = (Customer) session.getAttribute("customer");
        return new ClassDB().productTable2(customer);
    }


    public void addProduct(Product product, int customer_id) throws SQLException, ClassNotFoundException, IncorrectConstructorParameters, RemoteException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        new ClassDB().addProduct(product, customer_id);
    }


    public void updateSession() {
        session = (HttpSession) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSession(false);
    }


    public void invalidateSession() {
        this.updateSession();
        this.getSession().invalidate();
    }


    public void setCustomerId(String author) {
        session.setAttribute("id_customer", author);
    }


    public String getCustomerId() {
        if (session != null) {
            return (String) session.getAttribute("id_customer");
        } else return null;
    }


    public String getUserDataXMLRepresentation() {
        Customer user = (Customer) session.getAttribute("customer");
        try {
            user.setProducts(getCustomerProduct());
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(Customer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(getCustomer(), writer);
            return writer.toString();
        } catch (ClassNotFoundException | SQLException | JAXBException e) {
            e.printStackTrace();
            return "";
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | IncorrectConstructorParameters | RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }
}
