package net.unesc.compiladores.analisador.lexico.util;

public class Token {	
	private String nome;
	private int codigo;
	private int linha;
	
	public Token(int codigo, String nome) {
		this.codigo = codigo;
		this.nome = nome;
	}
	
	public Token(int codigo, String nome, int linha) {
		this.codigo = codigo;
		this.nome = nome;
		this.linha = linha;
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
	
	public void setLinha(int linha) {
		this.linha = linha;
	}
	
	public int getLinha() {
		return linha;
	}
	
	@Override
	public String toString() {
		return "{Token: " + nome + ", Codigo: " + codigo + "}";
	}
}