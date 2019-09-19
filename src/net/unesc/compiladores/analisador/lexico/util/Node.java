package net.unesc.compiladores.analisador.lexico.util;

public class Node {
	private boolean numerico;
	private boolean alfanumerico;
	private boolean literal;
	private boolean simbolo;
	private boolean espaco;
	private boolean final_linha;
	private int linha;
	private char source;
	
	public Node(Character source, int linha, boolean final_linha) {
		setTipoLido(source);
		this.source = source;
		this.linha = linha;
		this.final_linha = final_linha;
	}

	private void setTipoLido(Character source) {
		espaco = Character.isWhitespace(source);
		alfanumerico = Character.isAlphabetic(source) || source.toString().matches("_");
		numerico = Character.isDigit(source);
		simbolo = source.toString().matches("(\\+|\\-|\\*|\\/|\\[|\\]|\\(|\\)|\\:|\\=|\\>|\\<|\\,|\\;|\\.|\\')");
		literal = (source == '\"');
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
	
	public boolean isEndLine() {
		return final_linha;
	}
		
	/**
	 * @param b
	 */
	public void setEndLine(boolean final_linha) {
		this.final_linha = final_linha;
	}
	
	public int getLine() {
		return linha;
	}

	public String getChar()
	{
		return String.valueOf(source);
	}
	
	@Override
	public String toString()
	{
		return "{source: " + source +
				", numerico: " + numerico +
				", alfanumerico: " + alfanumerico +
				", literal: " + literal +
				", simbolo: " + simbolo +
				", espaco: " + espaco +
				", final_linha: " + final_linha +
				", linha: " + linha +
			"}";
	}
}