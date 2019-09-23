import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App2 {
	
	
	public static void main(String[] args) throws Exception {
				
		String def = "ID[I(6)]|NOOME[A(25)]|";
		FileOperations op = new FileOperations("0", def);
		prepararArquivo(op);
		
		Tupla t1 = new Tupla(def, "1|hannah|");
		System.out.println(t1.getTupla());
		
		
		
		op.addSomeData(t1);
		op.printBlocos();
		
		
	}
	
	public static void prepararArquivo(FileOperations op) throws Exception {
		File file = new File("/Users/davicarvalho/Desktop/teste.txt"); 
        
        file.delete();
		op.createFile();
	}


}
