import java.util.ArrayList;
import java.util.List;

public class TesteTupla {
	
	
	public static void main(String[] args) {
		List<String> fields = new ArrayList<>();
				
		String def = "COD_FORN[I(6)]|NOME_FORN[A(25)]|END_FORN[A(40)]|NASC_FORN[I(4)]|FON_FORN[A(15)]|COM_CLI[A(101)]|";
		
		
		while (def.contains("[")) {
			int i = def.indexOf("[");
			int f = def.indexOf("]");
			String s = def.substring(i+1, f);
			def = def.substring(f+1);
			fields.add(s.substring(0,1));
		}
		
//		for (String string : fields) {
//			System.out.println(string);
//		}
		
		String dados = "4195|Supplier#000004195|NHCu,qYwb21TrXtL8iXEI4QZ|11|21-437-493-6911|s. furiously special requests are. ironically regular packages doubt alongside o|";

		while (dados.contains("|")) {
			int f = dados.indexOf("|");
			String s = dados.substring(0, f);
			dados =dados.substring(f+1);
			System.out.println(s);
		}
		
	}

}
