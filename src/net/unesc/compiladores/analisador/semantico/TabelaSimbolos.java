package net.unesc.compiladores.analisador.semantico;

public class TabelaSimbolos {
	private String nome;
	private String categoria;
	private String tipo;
	private int nivel;
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	
	public int getNivel() {
		return nivel;
	}
}