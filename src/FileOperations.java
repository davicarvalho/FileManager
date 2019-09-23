import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FileOperations {

	String fileName = "/Users/davicarvalho/Desktop/teste.txt";
	Integer tamanhoBlocos = 800;
	String idContainer;
	String definicao;
	Integer qtdColunas;
	
	public FileOperations() {
		this.idContainer = "0";
	}

	public FileOperations(String idContainer, String definicao) {
		this.idContainer = idContainer;
		this.definicao = definicao;
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
		
		raf.close();
	}

	public Integer getTamanhoBlocoDeControle() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		raf.seek(9);
		byte b[] = new byte[2];
		raf.read(b, 0, 2);
		String s = new String(b);
		raf.close();
		return 11 + Integer.parseInt(s);
	}

	public Integer getIdBlocoAtual() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		raf.seek(5);
		byte b[] = new byte[4];
		raf.read(b, 0, 4);
		String s = new String(b);
		raf.close();
		return Integer.parseInt(s);
	}

	public boolean atualizarIdProximoBlocoLivre(Integer tamanhoDaTupla) throws Exception {
		Integer espacoLivreDoBloco = getEspacoLivreDoBloco(getIdBlocoAtual());
		if ((tamanhoDaTupla + 2) > espacoLivreDoBloco) {
			RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
			raf.seek(5);
			byte b[] = new byte[4];
			raf.read(b, 0, 4);
			String s = new String(b);
			Integer id = Integer.parseInt(s) + 1;
			raf.close();
			raf = new RandomAccessFile(fileName, "rw");
			raf.seek(5);
			raf.write(lpad4(id).getBytes());
			System.out.println("id prox bloco livre: " + id);
			raf.close();
			return true;
		}
		return false;
	}

	
	// aqui
	private Integer getEspacoLivreDoBloco(Integer id) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		// 7 eh referente a posicao do ultimo byte da tupla utilizado
		Integer enderecoProxBlocoLivre = getEnderecoProximoBlocoLivre();
		raf.seek(enderecoProxBlocoLivre + 7);
		byte b[] = new byte[2];
		raf.read(b, 0, 2);
//		Integer byteFinal = Integer.parseInt(new String(b));
		Short byteFinal = bytesToShort(b);
		Integer tamTupleDir = getTamanhoTupleDirectory();

		Integer espacoLivre = byteFinal - (tamTupleDir + 9);
		raf.close();
		return espacoLivre;
	}

	public Integer getEnderecoProximoBlocoLivre() throws Exception {
		return getTamanhoBlocoDeControle() + tamanhoBlocos * (getIdBlocoAtual() - 1);
	}

	public Integer getEnderecoBloco(Integer idBloco) throws Exception {
		return getTamanhoBlocoDeControle() + tamanhoBlocos * (idBloco - 1);
	}

	public Integer getTamanhoTupleDirectory() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		if (raf.length() > getEnderecoProximoBlocoLivre() + 5) {
			raf.seek(getEnderecoProximoBlocoLivre() + 5);
			byte b[] = new byte[2];
			raf.read(b, 0, 2);
			raf.close();
			return Integer.parseInt(new String(b));
		}
		raf.close();
		return 0;
	}
	
	public Integer getTamanhoTupleDirectory(Integer idBloco) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer endereco = getTamanhoBlocoDeControle() + (idBloco -1)*tamanhoBlocos;
		raf.seek(endereco + 5);
		byte b[] = new byte[2];
		raf.read(b, 0, 2);
		raf.close();
		return Integer.parseInt(new String(b));
	}

	public void addSomeData(String id, String nome) throws Exception {

		validarTamanhoDaTuplaEmRelacaoAoTamamnhoDoBloco(getTamanhoTupla(id, nome));
		if (getTamanhoTupleDirectory() > 0) {
			boolean houveMudancaDeBloco = atualizarIdProximoBlocoLivre(getTamanhoTupla(id, nome));
			if (houveMudancaDeBloco) {
				escreverHeader();
			}
		} else {
			escreverHeader();
		}

		Integer endereco = escreverTupla(id, nome);

		aumentarTupleDir();
		addEnderecoTupleDir(endereco);
		alterarUltimoByteDaTupla(endereco);
	}
	
	public void addSomeData(TuplaArquivoDeEntrada t) throws Exception {
		
		validarTamanhoDaTuplaEmRelacaoAoTamamnhoDoBloco(t.getTamanhoTupla());
		if (getTamanhoTupleDirectory() > 0) {
			boolean houveMudancaDeBloco = atualizarIdProximoBlocoLivre(t.getTamanhoTupla());
			if (houveMudancaDeBloco) {
				escreverHeader();
			}
		} else {
			escreverHeader();
		}

		Integer endereco = escreverTupla(t);

		aumentarTupleDir();
		addEnderecoTupleDir(endereco);
		alterarUltimoByteDaTupla(endereco);

	}
	
	
	private Integer escreverTupla(TuplaArquivoDeEntrada t) throws Exception {
		this.qtdColunas = t.getColunas().size();
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer comecoDoBloco = getEnderecoComecoBlocoAtual();
		String tupla = t.getTupla();
		Integer enderecoLivre = getEnderecoPrimeiroByteLivreDoBloco();
		
		Integer endereco = enderecoLivre - t.getTamanhoTupla();//+ (getIdBlocoAtual() - 1) * tamanhoBlocos;
		raf.seek(endereco);
		raf.write(tupla.getBytes());

		System.out.println(tupla + ", tamanho: " + t.getTamanhoTupla() + 
				" escrevendo do byte: "+ (endereco ) + 
				" ao byte: " + (t.getTamanhoTupla() + endereco ));

		raf.close();
		return  endereco - comecoDoBloco -1 ;
	}
	
	private Integer escreverTupla(String id, String nome) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer comecoDoBloco = getEnderecoComecoBlocoAtual();
		String tupla = getTupla(id, nome);
		Integer enderecoLivre = getEnderecoPrimeiroByteLivreDoBloco();
		
		Integer endereco = enderecoLivre - getTamanhoTupla(id, nome) ;//+ (getIdBlocoAtual() - 1) * tamanhoBlocos;
		raf.seek(endereco);
		raf.write(tupla.getBytes());

		System.out.println(tupla + ", tamanho: " + getTamanhoTupla(id, nome) + 
				" escrevendo do byte: "+ (endereco ) + 
				" ao byte: " + (getTamanhoTupla(id, nome) + endereco ));
		raf.close();
		return  endereco - comecoDoBloco -1 ;
	}
	
	private Integer getEnderecoComecoBlocoAtual() throws Exception {
		Integer tamBlocoControle = getTamanhoBlocoDeControle();
		return tamBlocoControle + (getIdBlocoAtual() - 1) * tamanhoBlocos;
	}

	//TODO aqui
	public Integer getEnderecoPrimeiroByteLivreDoBloco() throws Exception {
		if (getTamanhoTupleDirectory() == 0) {
			return getEnderecoProximoBlocoLivre() + tamanhoBlocos - 1 ;//- (getIdBlocoAtual() - 1) * tamanhoBlocos;
		} else {
			RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
			Integer comecoDoBloco = getEnderecoComecoBlocoAtual();
			raf.seek(comecoDoBloco + 7);
			byte b[] = new byte[2];
			raf.read(b, 0, 2);
			Short s = bytesToShort(b);
//			Integer data = Integer.parseInt(new String(b));
			raf.close();
			return (s + comecoDoBloco);
		}
	}

	private void validarTamanhoDaTuplaEmRelacaoAoTamamnhoDoBloco(Integer tamanhoTupla) throws Exception {
		if (tamanhoTupla > (tamanhoBlocos - 11)) {
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
		raf.close();
	}

	//TODO aqui
	private void addEnderecoTupleDir(Integer enderecoTupla) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer tamTupleDir = getTamanhoTupleDirectory();
		// 9 se refere ao tamanho do header ate o tuple dir
		// -2 pois o tamanho do tuple dir eh aumentado antes
		raf.seek(getEnderecoComecoBlocoAtual() + 9 + tamTupleDir - 2);
		
		raf.write(shortToBytes(enderecoTupla.shortValue()));
		raf.close();
	}

	//TODO aqui
	private void alterarUltimoByteDaTupla(Integer enderecoTupla) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		raf.seek(getEnderecoComecoBlocoAtual() + 7);
		raf.write(shortToBytes(enderecoTupla.shortValue()));
		raf.close();
	}


	private void escreverHeader() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");

		String idBloco = lpad3(getIdBlocoAtual());
		String tipoBloco = "0";
		String tamanhoTupleDirectory = lpad2(getTamanhoTupleDirectory());
		String enderecoUltimoByteDaTupla = lpad2(getEnderecoPrimeiroByteLivreDoBloco());

		raf.seek(getEnderecoProximoBlocoLivre());

		String header = idContainer + idBloco + tipoBloco + tamanhoTupleDirectory + enderecoUltimoByteDaTupla;

		raf.write(header.getBytes());
		raf.close();
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
	
	private Integer getQuantidadeDeBlocosDeDados() throws Exception {
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer tamFile = (int) raf.length();
		Integer tamBlocoControle = getTamanhoBlocoDeControle();

		Integer quantidadeDeBlocosDeDados = (tamFile - tamBlocoControle + 1) / tamanhoBlocos;
		raf.close();
		return quantidadeDeBlocosDeDados;
	}
	
	//TODO nao pronto, funciona apenas para tupas de tamanho 15
	public void printPrimeiraTuplaDoBloco(Integer idBloco) throws Exception{
		String tp = getTupleDirectory(idBloco);
		Integer primeiroValorTP = Integer.parseInt(tp.substring(0, 2));

		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer tamBlocoControle = getTamanhoBlocoDeControle();
		Integer endereco = tamBlocoControle + (idBloco - 1) * tamanhoBlocos + primeiroValorTP +1 ;
		
		raf.seek(endereco);
		byte b[] = new byte[15];
		raf.read(b, 0, 15);
		raf.close();
	}
	
	public String getTupleDirectory(Integer idBloco) throws Exception{
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer tamBlocoControle = getTamanhoBlocoDeControle();
		Integer endereco = tamBlocoControle + (idBloco - 1) * tamanhoBlocos;
		raf.seek(endereco+ 9);
		byte b[] = new byte[getTamanhoTupleDirectory(idBloco)];
		raf.read(b, 0, 2);
		String tp = new String(b);
		raf.close();
		return tp;
	}
	
	public Bloco lerBloco(Integer idBloco) throws Exception{
		RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
		Integer tamBlocoControle = getTamanhoBlocoDeControle();
		Integer endereco = tamBlocoControle + (idBloco - 1) * tamanhoBlocos;
		raf.seek(endereco);
		byte b[] = new byte[6];
		raf.read(b, 0, 6);
		String primeiros6Bytes = new String(b);
		raf.close();
		
		raf = new RandomAccessFile(fileName, "rw");
		raf.seek(endereco + 7);
		b = new byte[2];
		raf.read(b, 0, 2);
		Short s = bytesToShort(b);
		String enderecoUltimoByteTuplaUtilizadoNoBloco = s.toString();
		raf.close();
		
		raf = new RandomAccessFile(fileName, "rw");
		raf.seek(endereco+9);
		Integer tamTp = getTamanhoTupleDirectory(idBloco);
		b = new byte[tamTp];
		raf.read(b, 0, tamTp);
	//	String tp = "";
		Integer valoresTp[] = new Integer[tamTp/2];
		int j =0;
		for(int i=0; i<b.length; i+=2) {
			byte aux[] = new byte[2];
			aux[0] = b[i];
			aux[1] = b[i+1];
			//tp += bytesToShort(aux);
			valoresTp[j] = new Short(bytesToShort(aux)).intValue();
			j++;
		}
		raf.close();
		
		raf = new RandomAccessFile(fileName, "rw");
		Integer x = endereco+9+tamTp;
		raf.seek(x);
		b = new byte[tamanhoBlocos];
		raf.read(b, 0, tamanhoBlocos - 9 - tamTp);
		String restoDoBloco = new String(b);
		raf.close();
		
//		System.out.println("Primeiros 4 bytes: "+ primeiros6Bytes.substring(0, 5));
//		System.out.println("Tamanho tuple dir: "+ tamTp);
//		System.out.println("Endereco ultimo byte tupla: "+ enderecoUltimoByteTuplaUtilizadoNoBloco);
//		System.out.println("Tuple dir: "+ getTpFormatado(valoresTp));
//		System.out.println("Resto do bloco: "+restoDoBloco);
		
		Bloco bloco = new Bloco(idBloco, tamanhoBlocos, 
				primeiros6Bytes, tamTp, Integer.parseInt(enderecoUltimoByteTuplaUtilizadoNoBloco), 
				valoresTp, restoDoBloco);

		return bloco;
	}

//	private String getTpFormatado(Integer[] valoresTp) {
//		String s = "";
//		for(int i=0; i<valoresTp.length; i++) {
//			s+= valoresTp[i]+",";
//		}
//		if(s.length()>0) {
//			s = s.substring(0, s.length()-1);
//		}
//		return s;
//	}

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
		raf.close();

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
			Integer n = getEnderecoPrimeiroByteLivreDoBloco() - 16;

			System.out.println("Byte 0 (id container): " + s.substring(0, 1));
			System.out.println("Byte 1-3 (id bloco): " + s.substring(1, 4));
			System.out.println("Byte 4 (tipo): " + s.substring(4, 5));
			System.out.println("Byte 5-6 (tam tuple dir): " + s.substring(5, 7));
			System.out.println("Byte 7-8 (ultimo byte de tupla): " + s.substring(7, 9));
			System.out.println("Byte 9-" + (m + 8) + " (tuple dir): " + s.substring(9, 9 + m + 1));
			System.out.println("Byte " + n + "-" + tamanhoBlocos + " (dados): " + s.substring(m + 9 + 1));
			raf.close();
		}
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
	
	public short bytesToShort(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	public byte[] shortToBytes(short value) {
		byte[] returnByteArray = new byte[2];
		returnByteArray[0] = (byte) (value & 0xff);
		returnByteArray[1] = (byte) ((value >>> 8) & 0xff);
		return returnByteArray;
	}

	public void printBlocos() throws Exception {
		printBlocoDeControle();
		System.out.println("-----");
		printBolocosDeDados();
	}

}
