import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App3 {
	
	
	public static void main(String[] args) throws Exception {
				
		String def = "COD_FORN[I(6)]|NOME_FORN[A(25)]|END_FORN[A(40)]|NASC_FORN[I(4)]|FON_FORN[A(15)]|COM_CLI[A(101)]|";
		FileOperations op = new FileOperations("0", def);
		
		Tupla t1 = new Tupla(def, "4195|Supplier#000004195|NHCu,qYwb21TrXtL8iXEI4QZ|11|21-437-493-6911|s. furiously special requests are. ironically regular packages doubt alongside o|");
		System.out.println(t1.getTamanhoTupla());
//		
//		op.addSomeData(t1);
//		op.printBlocos();
//		
		
	}
	
	public static void prepararArquivo(FileOperations op) throws Exception {
		File file = new File("/Users/davicarvalho/Desktop/teste.txt"); 
        
        file.delete();
		op.createFile();
	}


}