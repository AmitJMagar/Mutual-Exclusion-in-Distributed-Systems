/**
 * @author amitjmagar 
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
 * This class implements the Raymonds Algorithm
 */

public class MutualExclusion  implements Mutex {

	private static boolean token = false;
	private static Node provider = new Node();
	private static Node info = new Node();
	static ConcurrentLinkedQueue<Node> que = new ConcurrentLinkedQueue<Node>();

	
	MutualExclusion() throws RemoteException {
		
	}
	// This function executes critical section of 
	private  void critical_section() {
		
		
		System.out.println("Process P" + info.getId() + " has access to critical section");
		System.out.println("Process P" + info.getId() + " processing critical section");
		for (int i = 0; i < 100; i++);
		
		System.out.println("Process P" + info.getId() + " critical section completed");
	}
	
	

	public   void call_provider(Node j, boolean flag) {
	//	synchronized(que){
		
		System.out.println("Queue is ");
		for(Node i:que)
			System.out.print(i);
		System.out.println("");
		System.out.println("Provider is "+provider+"for Process "+info);
			
		String s=(flag==true?j.getIp():provider.getIp());
		String id="Mutex";
		if(flag==true){
			id=id+j.getId();
		}else
			id=id+provider.getId();
		try {
			
			if(flag==true)
				System.out.println("Sending token to process p"+j.getId());
			else
				System.out.println("Requesting token from provider p"+provider.getId());
			
			Registry registry = LocateRegistry.getRegistry(s);
			
			Mutex stub = (Mutex) registry.lookup(id);
			stub.send(new Node(j), flag);
		} catch (RemoteException e) {
			System.out.println("Remote Exception");
		} catch (NotBoundException e) {
			System.out.println("Not Bind Exception at " + s);
		}
	//	}
	}

	@Override
	public void send(Node m, boolean flag) {
		Node temp;
		
		
		if(!m.equals(info)&&!que.contains(m))
			que.add(m);
		
		System.out.println("");
		System.out.print("Queue is ");
		for(Node i:que)
			System.out.print(i);
		System.out.println("");
		
		System.out.println("for process "+info+" provider is "+provider);
		
		
		if(flag==true){
			System.out.println("Received token from process p"+provider.getId());
			token=true;
			if(!que.isEmpty()&&!que.peek().equals(info))
				provider=que.peek();
		}
		else{
			System.out.println("Received Request from process p"+m.getId());
			
		}

		if (token == true) {
			if(!que.isEmpty()){
				temp = que.remove();
			
			if (info.equals(temp))
				critical_section();
			else
				/* send token to its child
				make that child as its provider
				if queue is non empty send request to provider*/
				provider = temp;
				token=false;
				call_provider(new Node(temp),true);
				
				if(!que.isEmpty())
					call_provider(new Node(info),false);
			}
			else
				return;
		} else {
			if(!que.isEmpty())
			call_provider(new Node(info),false);
		}

	}

	public static void main(String args[]) throws IOException, InterruptedException {
		Node n;
		int counter, i = 0, temp;
		Random rand = new Random();
		MutualExclusion me=new MutualExclusion();
		
		
		// Entering information about self
		System.out.println("Intial Configuration info:");
		Scanner sc = new Scanner(System.in);

		System.out.println("Enter node id");
		info.setId(sc.nextInt());
		System.out.println("Enter address of node");
		info.setIp(sc.next()); // can be replaced this code by IP address
		System.out.println("Enter value of token");
		temp=sc.nextInt();
		token=(temp==1?true:false);
		
		if(token==false){
		System.out.println("Enter Provider node id");
		provider.setId(sc.nextInt());
		System.out.println("Enter Provider address of node");
		provider.setIp(sc.next());
		}else
			provider=null;
		// Taking Counter
		System.out.println("For how many time time process should run");
		counter = sc.nextInt();
		
		if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Mutex"+info.getId();
         //  Mutex engine = new MutualExclusion();
            Mutex stub =
                (Mutex) UnicastRemoteObject.exportObject(me, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, me);
            System.out.println("Object Binded");
        } catch (Exception e) {
            System.err.println("Unable to bind object");
            
           e.printStackTrace();
        }
		
		Thread.sleep(3000);
		
		while (i < counter) {

			temp = rand.nextInt(12)*100 + 300;

			try {
				Thread.sleep(temp);
			} catch (InterruptedException e) {
				System.out.println("Interrupted Exception");
			}
			
			
			// add it self to queue to queue and call remote object of provider
			// if it doesnt have the token

			// if it has the token and pass the token to first element in queue
			
			
			if(!que.contains(info))
				que.add(new Node(info));
			
			System.out.println("Queue is ");
			for(Node i2:que)
				System.out.print(i2);
			System.out.println("Provider is "+provider+"for Process "+info);
			
			if(token){
				if(!que.isEmpty()){
					n=que.remove();
					
				if(n.equals(info))
					me.critical_section();
				else{
					token=false;
					provider=n;
					me.call_provider(new Node(n), true);
					if(!que.isEmpty())
						me.call_provider(new Node(info), false);
				}
				}
				}else
				me.call_provider(new Node(info), false);
			i++;
		}
	}

}
