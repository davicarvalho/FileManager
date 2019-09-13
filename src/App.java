import java.io.File;

public class App {
	
	public static void main(String[] args) throws Exception {
		deleteFile();
		FileOperations op = new FileOperations();
		op.createFile();
		op.addSomeData("1", "hannah");
		op.addSomeData("2", "jbmmbj");

		op.printBlocos();
	}
	
	public static void deleteFile() throws Exception {
		File file = new File("/Users/davicarvalho/Desktop/teste.txt"); 
        
        file.delete();
	}

}
