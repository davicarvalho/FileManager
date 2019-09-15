import java.io.RandomAccessFile;

public class FileOperations {

	String fileName = "/Users/davicarvalho/Desktop/teste.txt";
	Integer tamanhoBlocos = 64;
	String idContainer;

	public FileOperations() {
		idContainer = "0";
	}

	public void createFile() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");

		String strTamanhoBlocos = lpad3(tamanhoBlocos);
		String statusContainer = "0";
		String idProxBloco = "0001";
		String tamanhoDescritor = "05";
		String descritor = "desc1";

		String blocoDeControle = idContainer + strTamanhoBlocos + statusContainer + idProxBloco + tamanhoDescritor
				+ descritor;

		raf.write(blocoDeControle.getBytes());
	}

	public Integer getTamanhoBlocoDeControle() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		raf.seek(9);
		byte b[] = new byte[2];
		raf.read(b, 0, 2);
		String s = new String(b);
		return 11 + Integer.parseInt(s);
	}

	public Integer getIdProximoBloco() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		raf.seek(5);
		byte b[] = new byte[4];
		raf.read(b, 0, 4);
		String s = new String(b);
		return Integer.parseInt(s);
	}

	public Integer getEnderecoProximoBlocoLivre() throws Exception {
		return getTamanhoBlocoDeControle() + tamanhoBlocos * (getIdProximoBloco() - 1);
	}

	public void atualizarIdProximoBlocoLivre(Integer tamanhoDaTupla) throws Exception {
		
		Integer espacoLivreDoBloco = getEspacoLivreDoBloco(getIdProximoBloco());
		if((tamanhoDaTupla + 2) > espacoLivreDoBloco) {
			RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
			raf.seek(5);
			byte b[] = new byte[4];
			raf.read(b, 0, 4);
			String s = new String(b);
			Integer id = Integer.parseInt(s) + 1;
			raf.write(lpad4(id).getBytes());
			System.out.println("id prox bloco livre: " + id);
		}
	}
	
	private Integer getEspacoLivreDoBloco(Integer id) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		// 7 eh referente a posicao do ultimo byte da tupla utilizado
		Integer idProxBlocoLivre = getEnderecoProximoBlocoLivre() ;
		raf.seek(idProxBlocoLivre + 7);
		byte b[] = new byte[2];
		raf.read(b, 0, 2);
		Integer byteFinal = Integer.parseInt(new String(b));
//		Integer tamanhoBlocosAnteriores = (getIdProximoBloco() - 1) * tamanhoBlocos;
//		Integer tamBlocoDeControle = getTamanhoBlocoDeControle();
//		Integer byteFinalComOffset = byteFinal - tamanhoBlocosAnteriores - tamBlocoDeControle + 1;
		Integer tamTupleDir = getTamanhoTupleDirectory();
		
		Integer espacoLivre = byteFinal - (tamTupleDir + 9);
		return espacoLivre;
	}

	public Integer getTamanhoTupleDirectory() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		if (raf.length() > getEnderecoProximoBlocoLivre() + 5) {
			raf.seek(getEnderecoProximoBlocoLivre() + 5);
			byte b[] = new byte[2];
			raf.read(b, 0, 2);
			return Integer.parseInt(new String(b));
		}
		return 0;
	}

	public Integer getEnderecoPrimeiroByteLivreDoBloco() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		if (getTamanhoTupleDirectory() == 0) {
			return getEnderecoProximoBlocoLivre() + tamanhoBlocos - 1;
		} else {
			Integer comecoDoBloco = getEnderecoComecoBlocoAtual();
			raf.seek(comecoDoBloco + 7);
			byte b[] = new byte[2];
			raf.read(b, 0, 2);
			return Integer.parseInt(new String(b)) ;
		}
	}

	public void addSomeData(String id, String nome) throws Exception {
		
		validarTamanhoDaTuplaEmRelacaoAoTamamnhoDoBloco(getTamanhoTupla(id, nome));
		
		if(getTamanhoTupleDirectory()>0) {
			atualizarIdProximoBlocoLivre(getTamanhoTupla(id, nome));
		}else {
			escreverHeader();
		}

		Integer endereco = escreverTupla(id, nome);

		aumentarTupleDir();
		addEnderecoTupleDir(endereco);
		alterarUltimoByteDaTupla(endereco);
		
	}
	
	private void validarTamanhoDaTuplaEmRelacaoAoTamamnhoDoBloco(Integer tamanhoTupla)  throws Exception {
		if(tamanhoTupla > (tamanhoBlocos - 11)) {
			throw new Exception("Tupla grande demais para o bloco");
		}
	}

	private void aumentarTupleDir() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer endereco = getEnderecoProximoBlocoLivre();
		Integer tamAtual = getTamanhoTupleDirectory();
		tamAtual += 2;
		raf.seek(endereco + 5);
		raf.write(lpad2(tamAtual).getBytes());
	}
	
	private void addEnderecoTupleDir(Integer enderecoTupla) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer tamTupleDir = getTamanhoTupleDirectory();
		//9 se refere ao tamanho do header ate o tuple dir
		//-2 pois o tamanho do tuple dir eh aumentado antes
		raf.seek(getEnderecoComecoBlocoAtual() + 9 + tamTupleDir - 2);
		raf.write(lpad2(enderecoTupla).getBytes());
	}
	
	private void alterarUltimoByteDaTupla(Integer enderecoTupla) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		raf.seek(getEnderecoComecoBlocoAtual() + 7);
		raf.write(lpad2(enderecoTupla).getBytes());
	}
	
	
	private Integer getEnderecoComecoBlocoAtual() throws Exception {
		Integer tamBlocoControle = getTamanhoBlocoDeControle();
		Integer qtdBlocosDeDados = getIdProximoBloco() - 1;
		return tamBlocoControle + qtdBlocosDeDados * tamanhoBlocos;
	}

	private void escreverHeader() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");

		String idBloco = lpad3(getIdProximoBloco());
		String tipoBloco = "0";
		String tamanhoTupleDirectory = lpad2(getTamanhoTupleDirectory());
		String enderecoUltimoByteDaTupla = lpad2(getEnderecoPrimeiroByteLivreDoBloco());

		raf.seek(getEnderecoProximoBlocoLivre());

		String header = idContainer + idBloco + tipoBloco + tamanhoTupleDirectory + enderecoUltimoByteDaTupla;

		raf.write(header.getBytes());
	}

	private Integer escreverTupla(String id, String nome) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		String tupla = getTupla(id, nome);
		Integer enderecoLivre = getEnderecoPrimeiroByteLivreDoBloco();
		Integer endereco = enderecoLivre - tupla.length() + 1;
		raf.seek(endereco);
		raf.write(tupla.getBytes());

		System.out.println(tupla + ", tamanho: "+ getTamanhoTupla(id, nome)
		+" escrevendo do byte: "+enderecoLivre + " ao byte: "+(endereco));

		return endereco;
	}

	private String getTupla(String id, String nome) {
		String col1 = lpad2(id.length()) + id;
		String col2 = lpad2(nome.length()) + nome;
		String tupla = lpad4(col1.length() + col2.length()) + col1 + col2;
		return tupla;
	}

	private Integer getTamanhoTupla(String id, String nome) {
		return getTupla(id, nome).getBytes().length;
	}

	public String lpad2(Integer s) {
		if (s.toString().length() == 1) {
			return "0" + s;
		}
		return s.toString();
	}

	public String lpad3(Integer s) {
		String str = s.toString();
		while (str.length() < 3) {
			str = "0" + str;
		}
		return str;
	}

	public String lpad4(Integer s) {
		String str = s.toString();
		while (str.length() < 4) {
			str = "0" + str;
		}
		return str;
	}

	public void printBlocos() throws Exception {
		printBlocoDeControle();
		System.out.println("-----");
		printBolocosDeDados();
	}

	private void printBlocoDeControle() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		raf.seek(0);
		byte b[] = new byte[getTamanhoBlocoDeControle()];
		raf.read(b, 0, getTamanhoBlocoDeControle());
		String s = new String(b);

		System.out.println("Bloco de controle:");
		System.out.println("Size: " + getTamanhoBlocoDeControle());
		System.out.println("Byte 0 (id container): " + s.substring(0, 1));
		System.out.println("Byte 1-3 (tam blocos): " + s.substring(1, 4));
		System.out.println("Byte 4 (status): " + s.substring(4, 5));
		System.out.println("Byte 5-8 (id prox bloco): " + s.substring(5, 9));
		System.out.println("Byte 9-10 (tam desc): " + s.substring(9, 11));
		System.out.println("Byte 11-?? (desc): " + s.substring(11));

	}

	private void printBolocosDeDados() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer x = getQuantidadeDeBlocosDeDados();

		for (int i = 0; i < x; i++) {
			System.out.println("Bloco de dados #" + (i + 1) + ": ");
			raf.seek(i * tamanhoBlocos + getTamanhoBlocoDeControle());

			byte b[] = new byte[tamanhoBlocos];
			raf.read(b, 0, tamanhoBlocos);
			String s = new String(b);
			Integer m = Integer.parseInt(s.substring(5, 7));

			System.out.println("Byte 0 (id container): " + s.substring(0, 1));
			System.out.println("Byte 1-3 (id bloco): " + s.substring(1, 4));
			System.out.println("Byte 4 (tipo): " + s.substring(4, 5));
			System.out.println("Byte 5-6 (tam tuple dir): " + s.substring(5, 7));
			System.out.println("Byte 7-8 (ultimo byte de tupla): " + s.substring(7, 9));
			System.out.println("Byte 9-" + (m+9) + " (tuple dir): " + s.substring(9, 9 + m + 1));
			System.out.println("Byte ??-" + tamanhoBlocos + " (dados): " + s.substring(m + 9 + 1));
			System.out.println("All-"+ s);
		}

	}

	private Integer getQuantidadeDeBlocosDeDados() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer tamFile = (int) raf.length();
		Integer tamBlocoControle = getTamanhoBlocoDeControle();
		
		Integer quantidadeDeBlocosDeDados = (tamFile - tamBlocoControle + 1) / tamanhoBlocos;
		return quantidadeDeBlocosDeDados;
}

}
