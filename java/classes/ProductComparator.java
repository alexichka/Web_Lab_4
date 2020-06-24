package classes;

import java.util.Comparator;

public class ProductComparator implements Comparator<Product>{
    transient static final private ProductComparator comparator = new ProductComparator();

    public static ProductComparator getInstance() {
        return comparator;
    }

    @Override
    public int compare(Product product1, Product product2) {
        return product1.getProductName().compareTo(product2.getProductName());
    }

}
