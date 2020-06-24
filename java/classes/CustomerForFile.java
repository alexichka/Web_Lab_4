package classes;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CustomerForFile {
    public static void main(String[] args) {
        Product product0 = new Product("Chair", 400, 4, "11/02/2020");
        Product product1 = new Product("Table", 850, 1, "12/02/2020");
        Product product2 = new Product("Lamp", 100, 2, "12/02/2020");
        Product product3 = new Product("Picture", 370, 1, "13/02/2020");
        Product product4 = new Product("Picture", 370, 1, "13/02/2020");
        DeliveryPoint DP1 = new DeliveryPoint("ul. Tretyakova");
        DeliveryPoint DP2 = new DeliveryPoint("ul. Ulyanova");
        DP2.add(product0);
        DP2.add(product1);
        DP2.add(product2);
        DP2.add(product3);
        DP2.add(product4);

       try {/*
            File file = new File("ord.txt");
            FileReader reader = new FileReader(file);
            DeliveryPoint DP3 = DeliveryPoint.readProducts(reader, ';');
            reader.close();
            */


           File file1 = new File("ord.txt");
           FileWriter writer = new FileWriter(file1);
           DP2.sortAndSaveUnique(DP2);
           DeliveryPoint.writeProducts(writer, DP2, ';');

           writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
       }

           /*try {
               File file = new File("ord.txt");

               FileWriter writer = new FileWriter(file);
               DeliveryPoint.writeProducts(writer, DP3, ';');
               //DeliveryPoint.writeProducts(writer, DP1, ';');

               writer.close();
           } catch (IOException e) {
               e.printStackTrace();*/

            /*try {
                Product product0 = new Product("Chair", 400, 4, "11/02/2020");
                Product product1 = new Product("Table", 850, 1, "12/02/2020");

                File file = new File("ord.txt");
                FileWriter writer = new FileWriter(file);
                Product.writeProduct(writer, product1, '\n');
                writer.close();

                FileReader reader = new FileReader(file);
                Product product5 = Product.readProduct(reader, '\n');
                reader.close();
                //System.out.println(product5.toString());

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
*/
            //System.out.println();
       // DP2.sortAndSaveUnique(pr);
       // System.out.println(DP2.size);
       // for (int i = 0; i < DP2.size; i++){
       //     System.out.println(DP2.get(i));
        }

        }

