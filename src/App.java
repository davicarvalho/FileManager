import java.io.File;

public class App {
	
	public static void main(String[] args) throws Exception {
        FileOperations op = new FileOperations();
		prepararArquivo(op);
//		op.addSomeData("4", "robert");
//		op.addSomeData("5", "smith1");
//		op.addSomeData("6", "planta");
//		
//		
//		op.addSomeData("7", "morris");


		op.printBlocos();
	}
	
	public static void prepararArquivo(FileOperations op) throws Exception {
		File file = new File("/Users/davicarvalho/Desktop/teste.txt"); 
        
        file.delete();
		op.createFile();
		op.addSomeData("1", "hannah");
//		op.addSomeData("2", "jbmmbj");
//		op.addSomeData("3", "wejkbt");
	}
	
	//001101106hannah
	//001101106hannah

}
