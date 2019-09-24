
public class Node {
	
	private Bloco bloco;
	
	Node next;
	
	Node prev;
	
	public Node(Bloco b) {
		this.bloco = b;
	}

	public Bloco getBloco() {
		return bloco;
	}

	public void setBloco(Bloco bloco) {
		this.bloco = bloco;
	}

}
