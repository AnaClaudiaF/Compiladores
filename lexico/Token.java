package compiladores.analisador.lexico;

public class Token {	
	private String nome;
	private int codigo;
	
	public Token(int codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public int getCodigo() {
		return codigo;
	}
}