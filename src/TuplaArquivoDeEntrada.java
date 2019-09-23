import java.util.ArrayList;
import java.util.List;

public class TuplaArquivoDeEntrada {
	
	private Integer qtdColunas;
	
	private List<String> colunas = new ArrayList<>();
	private List<String> valores = new ArrayList<>();
	
	public TuplaArquivoDeEntrada(String def, String dados) {
		List<String> fields = new ArrayList<>();
		
		while (def.contains("[")) {
			int i = def.indexOf("[");
			int f = def.indexOf("]");
			String s = def.substring(i+1, f);
			def = def.substring(f+1);
			fields.add(s.substring(0,1));
		}
		
		qtdColunas = fields.size();
		
		setColunas(dados);
	}
	

	private void setColunas(String dados) {
		while (dados.contains("|")) {
			int f = dados.indexOf("|");
			String s = dados.substring(0, f);
			dados = dados.substring(f+1);
			valores.add(s);
		}
	}
	
	public String getTupla() {
		String col = "";
		Integer size = 0;
		
		for (String str : valores) {
			col += lpad2(str.length()) + str;
			size += str.length() + 2;
		}
		String tupla = lpad4(size) + col;

		return tupla;
	}
	
	
	public Integer getQtdColunas() {
		return qtdColunas;
	}


	public void setQtdColunas(Integer qtdColunas) {
		this.qtdColunas = qtdColunas;
	}


	public Integer getTamanhoTupla() {
		return getTupla().getBytes().length;
	}

	public List<String> getValores() {
		return valores;
	}

	public List<String> getColunas() {
		return colunas;
	}
	
	public String lpad2(Integer s) {
		if (s.toString().length() == 1) {
			return "0" + s;
		}
		return s.toString();
	}

	public String lpad3(Integer s) {
		String str = s.toString();
		while (str.length() < 3) {
			str = "0" + str;
		}
		return str;
	}

	public String lpad4(Integer s) {
		String str = s.toString();
		while (str.length() < 4) {
			str = "0" + str;
		}
		return str;
	}

}
