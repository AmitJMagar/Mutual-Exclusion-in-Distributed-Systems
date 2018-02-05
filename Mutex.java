import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Mutex extends Remote{
	public  void send(Node m,boolean flag) throws RemoteException ;	
}
