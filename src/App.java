import java.io.File;

public class App {
	
	public static void main(String[] args) throws Exception {
        FileOperations op = new FileOperations();
		prepararArquivo(op);
//		op.addSomeData("4", "robert");
//		System.out.println(op.getEnderecoPrimeiroByteLivreDoBloco());
//		op.addSomeData("5", "smith1");
//		System.out.println(op.getEnderecoPrimeiroByteLivreDoBloco());

		op.printBlocos();
	}
	
	public static void prepararArquivo(FileOperations op) throws Exception {
		File file = new File("/Users/davicarvalho/Desktop/teste.txt"); 
        
        file.delete();
		op.createFile();
		op.addSomeData("1", "hannah");
		System.out.println(op.getEnderecoPrimeiroByteLivreDoBloco());
		op.addSomeData("2", "jbmmbj");
		System.out.println(op.getEnderecoPrimeiroByteLivreDoBloco());
		op.addSomeData("3", "wejkbt");
		System.out.println(op.getEnderecoPrimeiroByteLivreDoBloco());
	}

}
