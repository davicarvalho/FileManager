import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App5 {
	
	public static void main(String[] args) throws Exception {
		
		File f = new File("/Users/davicarvalho/Desktop/forn-tpch.txt");
		String def = extrairDefinicao(f);
		FileOperations op = new FileOperations("1", def);
		BufferManager bm = new BufferManager(op, 3);
		
		//4739
		//6975
		//4094
		//2801
		//7915
		
		String ids[] = new String[10];
		ids[0] = "4739";
		ids[1] = "6975";
		ids[2] = "4094";
		ids[3] = "2801";
		ids[4] = "7915";
		//ate aqui 5 misses
		
		ids[5] = "4094";
		ids[6] = "7915";
		ids[7] = "2801";
		//ate aqui 3 hits
		
		ids[8] = "4739";
		// ate aqui 6 misses
		ids[9] = "2801";
		//ate aqui 4 hits
		
		bm.procurarVariasTuplas(ids);
		bm.printHitsAndMisses();
		
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
}
