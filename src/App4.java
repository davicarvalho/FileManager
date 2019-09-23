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
   	 	List<Tupla> tuplas = getTuplas(f, def);
   	 	for (Tupla tupla : tuplas) {
			System.out.println(tupla.getTupla());
		}

	}

	public static void prepararArquivo(FileOperations op) throws Exception {
		File file = new File("/Users/davicarvalho/Desktop/teste.txt");
		file.delete();
		op.createFile();
	}

	static String extrairDefinicao(File f) {
		try {
			BufferedReader b = new BufferedReader(new FileReader(f));
			return b.readLine();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static List<Tupla> getTuplas(File f, String def){

        try {
        	 List<Tupla> tuplas = new ArrayList<>();
        	
            BufferedReader b = new BufferedReader(new FileReader(f));
            
            String dados = b.readLine();

            while ((dados = b.readLine()) != null) {
                dados = b.readLine();
                Tupla t = new Tupla(def, dados);
                if(tuplas.size() < 30) {
                	tuplas.add(t);
                }
            }
            return tuplas;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
	}

}
