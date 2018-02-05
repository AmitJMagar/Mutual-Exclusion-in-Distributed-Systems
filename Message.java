
public class Message implements Comparable{
	private boolean token;
	private Node pointer;
	
	Message(boolean value,Node info){
		this.token=value;
		this.pointer=info;
	}

	public boolean isToken() {
		return token;
	}

	public void setToken(boolean token) {
		this.token = token;
	}

	public Node getPointer() {
		return pointer;
	}

	public void setPointer(Node pointer) {
		this.pointer = pointer;
	}

	@Override
	public int compareTo(Object o) {
		Message m=(Message)o;
		if(this.pointer.getId()==m.pointer.getId())
			return 0;
		else 
			return -1;
	}
	
}
