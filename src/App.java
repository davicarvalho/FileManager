import java.io.File;

public class App {
	
	public static void main(String[] args) throws Exception {
		deleteFile();
		FileOperations op = new FileOperations();
		op.createFile();
		op.addSomeData("1", "hannah");
		op.addSomeData("2", "jbmmbj");
//		op.addSomeData("3", "wejkbt");
//		op.addSomeData("4", "wertbd");

		op.printBlocos();
	}
	
	public static void deleteFile() throws Exception {
		File file = new File("/Users/davicarvalho/Desktop/teste.txt"); 
        
        file.delete();
	}

}
