public class BufferManager {
	
	CacheLRU lru;

	FileOperations op;

	Integer miss = 0;

	Integer hit = 0;

	public BufferManager(FileOperations op, Integer tamamhoBuffer) {
		this.op = op;
		lru = new CacheLRU(tamamhoBuffer);
	}
	
	public void procurarVariasTuplas(String[] tuplas) throws Exception {
		for(int i=0; i<tuplas.length; i++) {
			procurarBlocoPorIdDaTupla(tuplas[i]);
		}
	}
	
	public void printHitsAndMisses() {
		System.out.println("Hits: "+hit);
		System.out.println("Misses: "+miss);
		System.out.println("Media: "+ (hit+0.0)/(hit+miss+0.0));
	}

	public void procurarBlocoPorIdDaTupla(String idTupla) throws Exception {
		Bloco b = lru.trazerBlocoPorIdDaTupla(idTupla);
		if(b == null) {
			miss++;
			b = procurarBlocoNoArquivo(idTupla);
			lru.add(b);
		}else {
			hit++;
		}
	}

	private Bloco procurarBlocoNoArquivo(String idTupla) {
		try {
			boolean found = false;
			int idBloco = 2;
			Bloco b = null;
			while (!found) {
				try {
					b = op.lerBloco(idBloco);
					found = b.findTupla(idTupla);
				} catch (Exception e) {

				}
				idBloco++;
			}
			System.out.println("found: " + b.getId());
			return b;
		} catch (Exception e) {
			System.out.println("Id nao encontrado");
			return null;
		}
	}

}
