import java.util.ArrayList;
import java.util.List;

public class Tupla {
	
	private List<Coluna> valores;
	
	public Tupla() {
		valores = new ArrayList<>();
	}

	public List<Coluna> getValores() {
		return valores;
	}
	
	public void addColuna(Coluna c) {
		valores.add(c);
	}
	
	public void printColunas() {
		int i = 1;
		for (Coluna coluna : valores) {
			System.out.println("coulna "+i);
			System.out.println("tamanho = "+coluna.getTamanho());
			System.out.println("valor = "+coluna.getValor());
		}
	}

//	public void setValores(List<Coluna> valores) {
//		this.valores = valores;
//	}
	
}
