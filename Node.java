import java.io.Serializable;

public class Node implements Serializable{
	private int id;
	private String ip;
	Node(){
		
	}
	Node(Node e){
		this.id=e.id;
		this.ip=e.ip;
	}
	@Override
	public String toString() {
		return "[" + id + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	Node(int id,String ip){
		this.id=id;
		this.ip=ip;
	}
	
	@Override
	public boolean equals(Object o){
		Node j=(Node)o;
		if(this.id==j.id)
			return true;
		else
			return false;
	}
	
}
