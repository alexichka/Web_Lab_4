package classes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.*;
import java.io.*;

@XmlType(name = "delivery point")
@XmlRootElement
public class DeliveryPoint implements Remote, DeliveryPointInterface, Serializable {

    private String address; // адрес пункта
    private List<Product> products;
    int size;

    //---------------------конструктор-----------------------
    public DeliveryPoint(String address) {
        this.address = address;
        products = new ArrayList<>();
    }


    public DeliveryPoint(String address, List<Product> prod) {
        super();
        this.address = address;
        products = prod;
    }

    @XmlElement(name = "product")
    @XmlElementWrapper(name = "products")
public List<Product> getProducts(){
        return products;
}
    public DeliveryPoint() {
        this.products = new ArrayList<>();
    }

    // ----------------setters and getters----------------
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //----------------methods-------------------------


    @Override
    public DeliveryPoint sortAndSaveUnique(DeliveryPoint deliveryPoint) throws RemoteException {
        List<Product> surfaceCopy = (List<Product>) ((ArrayList<Product>) deliveryPoint.products).clone();
        surfaceCopy.sort(ProductComparator.getInstance());
        List<Product> sortedAndUniq = new ArrayList<>();
        Product prev = surfaceCopy.get(0);
        sortedAndUniq.add(prev);

        for (int i = 0; i < surfaceCopy.size(); ++i) {
            Product cur = surfaceCopy.get(i);
            if (!cur.equals(prev))
                sortedAndUniq.add(cur);
            prev = cur;
        }
        DeliveryPoint ans = new DeliveryPoint(deliveryPoint.getAddress());
        ans.products = sortedAndUniq;
        return ans;
    }

    public Product get(int index) {
        return products.get(index);
    }

    public boolean add(Product product) {
        boolean res = products.add(product);
        size = products.size();
        return res;
    }

    public Product remove(int index) {
        Product temp = products.remove(index);
        size = products.size();
        return temp;
    }

    public int size() {
        return products.size();
    }

    //------------------------чтение и запись-------------------------------
    public static void writeProducts(Writer out, DeliveryPoint deliveryPoint, char delimiter) throws IOException {
        out.write(deliveryPoint.getAddress() + "\n" +
                deliveryPoint.size() + "\n");
        if (deliveryPoint.size() > 0) {
            for (int i = 0; i < deliveryPoint.size(); ++i) {
                Product.writeProduct(out, deliveryPoint.get(i), delimiter);
                out.write("\n");
            }
        }
        out.write("\n");
    }

    public static DeliveryPoint readProducts(Reader in, char delimiter) throws IOException, ParseException {

        BufferedReader reader = new BufferedReader(in);

        String address = reader.readLine();
        int size = Integer.parseInt(reader.readLine());

        DeliveryPoint desired = new DeliveryPoint(address);
       // String products = reader.readLine();

        //StringReader productsReaders = new StringReader(products);
        for (int i = 1; i < size; ++i) {
            String products = reader.readLine();
            StringReader productsReaders = new StringReader(products);
            Product product = Product.readProduct(productsReaders, delimiter);
            desired.add(product);
        }
        return desired;
    }
}
