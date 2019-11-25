package net.unesc.compiladores.analisador.semantico;

import java.util.ArrayList;
import java.util.List;

public class Simbolos {
	private String nome;
	private String categoria;
	private String tipo;
	private int nivel;
	private List<Simbolos> simbolo;
	private List<Simbolos> variavel;
	private List<Simbolos> filhos;
	private Simbolos parent;
	
	public Simbolos() {
		simbolo = new ArrayList<>();
		filhos = new ArrayList<>();
		variavel = new ArrayList<>();
		parent = null;
	}
	
	public Simbolos(String categoria, String nome, String tipo) {
		this.categoria = categoria;
		this.nome = nome;
		this.tipo = tipo;
		
		simbolo = new ArrayList<>();
		filhos = new ArrayList<>();
		parent = new Simbolos();
	}

	public Simbolos(String nome, String categoria, String tipo, int nivel) {
		this.nome = nome;
		this.categoria = categoria;
		this.tipo = tipo;
		this.nivel = nivel;
		
		simbolo = new ArrayList<>();
		filhos = new ArrayList<>();
		variavel = new ArrayList<>();
		parent = null;
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
	
	public List<Simbolos> getFilhos(){
		return filhos;
	}
	
	public Simbolos getParent() {
		return parent;
	}
	
	public List<Simbolos> getVariavel() {
		return variavel;
	}

	public void setVariavel(Simbolos variavel) {
		this.variavel.add(variavel);
	}

	public void setFilho(Simbolos simbolo) {
		filhos.add(simbolo);
	}
	
	public void setParent(Simbolos simbolo) {
		this.parent = simbolo;
	}
}
