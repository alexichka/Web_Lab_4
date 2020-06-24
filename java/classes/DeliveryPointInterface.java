package classes;
import classes.DeliveryPoint;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DeliveryPointInterface extends Remote {
    public DeliveryPoint sortAndSaveUnique(DeliveryPoint deliveryPoint) throws RemoteException;
}
