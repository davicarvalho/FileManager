import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App4 {
	
	public static void main(String[] args) throws Exception {

		File f = new File("/Users/davicarvalho/Desktop/forn-tpch.txt");
		String def = extrairDefinicao(f);
		FileOperations op = new FileOperations("1", def);
		
		prepararArquivo(f, op);
		
		int x = 1;
		while(x <= 362) {
			Bloco bloco = op.lerBloco(x);
			bloco.printBloco();
			x++;
		}
		
		
	}

	public static void prepararArquivo(File input, FileOperations op) throws Exception {
		File file = new File("/Users/davicarvalho/Desktop/teste.bin");
		file.delete();
		op.createFile();
		addTuplas(input, op);
	}

	private static void addTuplas(File f, FileOperations op) throws Exception {
		List<TuplaArquivoDeEntrada> tuplas = getTuplas(f);
		for (TuplaArquivoDeEntrada tupla : tuplas) {
			op.addSomeData(tupla);
		}
		
	}

	static String extrairDefinicao(File f) {
		try {
			BufferedReader b = new BufferedReader(new FileReader(f));
			String s = b.readLine();
			b.close();
			return s;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static List<TuplaArquivoDeEntrada> getTuplas(File f){

        try {
        	String def = extrairDefinicao(f);
        	 List<TuplaArquivoDeEntrada> tuplas = new ArrayList<>();
        	
            BufferedReader b = new BufferedReader(new FileReader(f));
            
            String dados = b.readLine();

            while ((dados = b.readLine()) != null) {
                TuplaArquivoDeEntrada t = new TuplaArquivoDeEntrada(def, dados);
                if(tuplas.size() < 10000) {
                	tuplas.add(t);
                }
            }
            b.close();
            return tuplas;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}

}
