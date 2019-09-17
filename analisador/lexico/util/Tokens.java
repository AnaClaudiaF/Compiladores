package net.unesc.compiladores.analisador.lexico.util;

import java.util.HashMap;

public class Tokens {
	public static final char CHAR_DEFAULT = 'Z';
	public static final char CHAR_ALFA = 'A';
	public static final char CHAR_NUMERICO = 'N';
	public static final char CHAR_RELACIONAL = 'R';
	public static final char CHAR_ESPECIAL = 'E';
	public static final char CHAR_LITERAL = 'L';
	
	private HashMap<String, Integer> token = new HashMap<>();
	{
		token.put("program", 1);
		token.put("label", 2);
		token.put("const", 3);
		token.put("var", 4);
		token.put("procedure", 5);
		token.put("begin", 6);
		token.put("end", 7);
		token.put("integer", 8);
		token.put("array", 9);
		token.put("of", 10);
		token.put("call", 11);
		token.put("goto", 12);
		token.put("if", 13);
		token.put("then", 14);
		token.put("else", 15);
		token.put("while", 16);
		token.put("do", 17);
		token.put("repeat", 18);
		token.put("until", 19);
		token.put("readln", 20);
		token.put("writeln", 21);
		token.put("or", 22);
		token.put("and", 23);
		token.put("not", 24);
		token.put("identificador", 25);
		token.put("inteiro", 26);
		token.put("for", 27);
		token.put("to", 28);
		token.put("case", 29);
		token.put("+", 30);
		token.put("-", 31);
		token.put("*", 32);
		token.put("/", 33);
		token.put("[", 34);
		token.put("]", 35);
		token.put("(", 36);
		token.put(")", 37);		
		token.put(":=", 38);
		token.put(":", 39);
		token.put("=", 40);
		token.put(">", 41);
		token.put(">=", 42);
		token.put("<", 43);
		token.put("<=", 44);
		token.put("<>", 45);		
		token.put(",", 46);
		token.put(";", 47);
		token.put("literal", 48);
		token.put(".", 49);
		token.put("..", 50);
	}

	public Integer getCodigoToken(String nome) {
System.out.println(token.get(nome));
		return token.get(nome);
	}
	
	public boolean isSimbolo(String nome) {
		return token.containsKey(nome);
	}
}