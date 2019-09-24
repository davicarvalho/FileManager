import java.util.ArrayList;

public class Bloco {
	
	Integer id;
	
	Integer tamBloco;
	
	String primeiros6Bytes;
	
	Integer tamTp;
		
	Integer enderecoUltimoByteTuplaUtilizadoNoBloco;
	
	Integer[] valoresTpDirectory;
	
	ArrayList<Tupla> tuplas;
	
	public Bloco(Integer id, Integer tamBloco) {
		this.id = id;
		this.tamBloco = tamBloco;
	}

	public Bloco(Integer id, Integer tamBloco, String primeiros6Bytes, Integer tamTp,
			Integer enderecoUltimoByteTuplaUtilizadoNoBloco, Integer[] valoresTp, String restoDoBloco) {
		super();
		this.id = id;
		this.tamBloco = tamBloco;
		this.primeiros6Bytes = primeiros6Bytes;
		this.tamTp = tamTp;

		this.enderecoUltimoByteTuplaUtilizadoNoBloco = enderecoUltimoByteTuplaUtilizadoNoBloco;
		this.valoresTpDirectory = valoresTp;
		
		popularTuplas(restoDoBloco);
		
	}

	private void popularTuplas(String restoDoBloco) {
		tuplas = new ArrayList<Tupla>();
		for(int i = 0; i < valoresTpDirectory.length; i++) {
			restoDoBloco = restoDoBloco.trim();
			String s = restoDoBloco.substring(0, 4);
			Integer tamTupla = Integer.parseInt(s);
			restoDoBloco = restoDoBloco.substring(4);
			
			Tupla t = new Tupla();
			String tuplaAtual = restoDoBloco.substring(0, tamTupla);
			while(tuplaAtual.length() > 0) {
				Integer tamColuna = Integer.parseInt(tuplaAtual.substring(0, 2));
				String infoColuna = tuplaAtual.substring(2, 2+tamColuna);
				Coluna c = new Coluna(tamColuna, infoColuna);
				t.addColuna(c);
				tuplaAtual = tuplaAtual.substring(tamColuna+2);
			}
			restoDoBloco = restoDoBloco.substring(tamTupla);
			tuplas.add(t);
		}
	}
	
	private String getTpFormatado(Integer[] valoresTp) {
		String s = "";
		for(int i=0; i<valoresTp.length; i++) {
			s+= valoresTp[i]+",";
		}
		if(s.length()>0) {
			s = s.substring(0, s.length()-1);
		}
		return s;
	}
	
	public void printBloco() {
		System.out.println("Primeiros 4 bytes: "+ primeiros6Bytes.substring(0, 5));
		System.out.println("Tamanho tuple dir: "+ tamTp);
		System.out.println("Endereco ultimo byte tupla: "+ enderecoUltimoByteTuplaUtilizadoNoBloco);
		System.out.println("Tuple dir: "+ getTpFormatado(valoresTpDirectory));
		System.out.println("Resto do bloco: ");
		int i = 1;
		for (Tupla tupla : tuplas) {
			System.out.println("\n\nTupla "+i+": ");
			tupla.printColunas();
			i++;
		}
	}
	
	//tupla.getValores().iterator().next() retorna um objeto Coluna, que por definicao tem seu primeiro valor como id.
	public boolean findTupla(String idTupla) {
		for (Tupla tupla : tuplas) {
			String s = tupla.getValores().iterator().next().getValor();
			if(idTupla.equals(s)) {
				return true;
			}
		}
		return false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTamBloco() {
		return tamBloco;
	}

	public void setTamBloco(Integer tamBloco) {
		this.tamBloco = tamBloco;
	}

	public String getPrimeiros6Bytes() {
		return primeiros6Bytes;
	}

	public void setPrimeiros6Bytes(String primeiros6Bytes) {
		this.primeiros6Bytes = primeiros6Bytes;
	}

	public Integer getTamTp() {
		return tamTp;
	}

	public void setTamTp(Integer tamTp) {
		this.tamTp = tamTp;
	}

	public Integer getEnderecoUltimoByteTuplaUtilizadoNoBloco() {
		return enderecoUltimoByteTuplaUtilizadoNoBloco;
	}

	public void setEnderecoUltimoByteTuplaUtilizadoNoBloco(Integer enderecoUltimoByteTuplaUtilizadoNoBloco) {
		this.enderecoUltimoByteTuplaUtilizadoNoBloco = enderecoUltimoByteTuplaUtilizadoNoBloco;
	}

	public Integer[] getValoresTp() {
		return valoresTpDirectory;
	}

	public void setValoresTp(Integer[] valoresTp) {
		this.valoresTpDirectory = valoresTp;
	}

	public ArrayList<Tupla> getTuplas() {
		return tuplas;
	}

	public void setTuplas(ArrayList<Tupla> tuplas) {
		this.tuplas = tuplas;
	}
	

}
