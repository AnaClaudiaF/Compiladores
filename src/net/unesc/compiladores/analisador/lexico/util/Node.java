package net.unesc.compiladores.analisador.lexico.util;

public class Node {
	private boolean numerico;
	private boolean alfanumerico;
	private boolean literal;
	private boolean simbolo;
	private boolean espaco;
	private char source;
	private int linha;
	
	public Node(Character source, int linha) {
		this.linha = linha;
		this.source = source;
		
		setTipoLido(source);
	}
	
	private void setTipoLido(Character source) {
		espaco = Character.isWhitespace(source);
		alfanumerico = Character.isAlphabetic(source) || source.toString().matches("_");
		numerico = Character.isDigit(source);
		simbolo = source.toString().matches("(\\+|\\-|\\*|\\/|\\[|\\]|\\(|\\)|\\:|\\=|\\>|\\<|\\,|\\;|\\.|\\')");
	}

	public boolean isNumerico() {
		return numerico;
	}
	
	public boolean isAlfanumerico() {
		return alfanumerico;
	}
	
	public boolean isLiteral() {
		return literal;
	}
	
	public boolean isSimbolo() {
		return simbolo;
	}
	
	public boolean isEspaco() {
		return espaco;
	}
	
	public int getLinha() {
		return linha;
	}
	
	public String getCharacter() {
		return String.valueOf(source);
	}

	@Override
	public String toString()
	{
		return "{Source: " + source + ", Linha: " + linha + ", Numerico: " + numerico + ", Alfanumerico: " + alfanumerico + ", Literal: " + literal + ", Simbolo: " + simbolo + "}";
	}
}