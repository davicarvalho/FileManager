
public class CacheLRU {
	
	Node head;
    Node tail;
    int capacidade;
    int qtdNodes = 0;
    
    public CacheLRU(int capacidade) {
        this.capacidade = capacidade;
        head = new Node(new Bloco(-1, -1));
        tail = new Node(new Bloco(-2, -2));
        head.next = tail;
        tail.prev = head;
    }
    
    public void add(Bloco b) {
    	Node newNode = new Node(b);
    	
    	newNode.next = head.next;
    	newNode.prev = head;
    	
    	head.next.prev = newNode;
    	head.next = newNode;
    	
    	if(qtdNodes >= capacidade) {
    		deletarUltimoNode();
    	}
    	qtdNodes ++;
    }
    
    private void deletarUltimoNode() {
    	tail.prev = tail.prev.prev;
		tail.prev.next = tail;
		qtdNodes --;
	}

	public void printNodesEmOrdem() {
    	Node n = head.next;
    	while(n.getBloco().getId() != tail.getBloco().getId()) {
    		System.out.println(n.getBloco().getId());
    		n = n.next;
    	}
    }
	
    public Bloco trazerBlocoPorIdDaTupla(String idTupla) {
    	Node n = head.next;
    	Node levarParaCima = null;
    	while(n.getBloco().getId() != tail.getBloco().getId()) {
    		Bloco b = n.getBloco();
    		if(b.findTupla(idTupla)) {
    			System.out.println("trazendo bloco: "+ n.getBloco().getId());
    			levarParaCima = n;
    		}
    		n = n.next;
    	}
    	if(levarParaCima != null) {
    		levarPraCima(levarParaCima);
    		return levarParaCima.getBloco();
    	}
    	return null;
    }
    
    public Bloco trazerBloco(Bloco b) {
    	Node n = head.next;
    	Node levarParaCima = null;
    	while(n.getBloco().getId() != tail.getBloco().getId()) {
    		if(b.getId() == n.getBloco().getId()) {
    			System.out.println("trazendo bloco: "+ n.getBloco().getId());
    			levarParaCima = n;
    		}
    		n = n.next;
    	}
    	if(levarParaCima != null) {
    		levarPraCima(levarParaCima);
    		return levarParaCima.getBloco();
    	}
    	return null;
    }
    
    public void levarPraCima(Node n) {
    	deletarNode(n);
    	add(n.getBloco());
    }

	private void deletarNode(Node n) {
		n.prev.next = n.next;
		n.next.prev = n.prev;
		qtdNodes --;
	}
    

}
