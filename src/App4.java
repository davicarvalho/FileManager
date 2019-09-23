import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App4 {
	
	//id prox bloco livre: 2018
//	011204755318Supplier#00000755310wAU2Lui w902101520-663-409-795651old, stealthy accounts are blithely. fluffily final, tamanho: 116 escrevendo do byte: 1614299 ao byte: 1614415
//	011304822518Supplier#00000822515i7mYUAWhVs EOVc0101510-617-946-431848idle ideas are furiously? packages above the exp, tamanho: 117 escrevendo do byte: 1614181 ao byte: 1614298
//	017204689918Supplier#00000689936Pbpjj0EGV9LcoqhoJT8BhDnDComelq3DiGWF02141524-479-609-111385lyly pending deposits. requests wake. final pinto beans wake across the even instruct, tamanho: 176 escrevendo do byte: 1614004 ao byte: 1614180
//	01040326318Supplier#000000263107Vdou,WjHE02141524-203-272-313944s quickly regular foxes. stealthily ironic p, tamanho: 108 escrevendo do byte: 1613895 ao byte: 1614003
//	013204368618Supplier#000003686199fwXT1RCQohLoNEOiF202141524-873-490-250862 accounts wake furiously blithely bold instructions. unusual, , tamanho: 136 escrevendo do byte: 1613758 ao byte: 1613894


	public static void main(String[] args) throws Exception {

		File f = new File("/Users/davicarvalho/Desktop/forn-tpch.txt");
		String def = extrairDefinicao(f);
		FileOperations op = new FileOperations("0", def);
//		prepararArquivo(f, op);
		
		
		Bloco bloco = op.lerBloco(2018);
		
		bloco.printBloco();

	}

	public static void prepararArquivo(File input, FileOperations op) throws Exception {
		File file = new File("/Users/davicarvalho/Desktop/teste.txt");
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
