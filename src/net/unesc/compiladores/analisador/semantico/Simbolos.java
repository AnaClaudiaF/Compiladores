package net.unesc.compiladores.analisador.semantico;

import java.util.List;

public class Simbolos {
	private String nome;
	private String categoria;
	private String tipo;
	private int nivel;
	private List<Simbolos> simbolo;
	
	public Simbolos(String categoria, String nome, String tipo) {
		this.categoria = categoria;
		this.nome = nome;
		this.tipo = tipo;
	}

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
	
	public void setSimbolo(List<Simbolos> simbolo) {
		this.simbolo = simbolo;
	}
	
	public List<Simbolos> getSimbolo(){
		return simbolo;
	}
}
