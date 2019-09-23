package net.unesc.compiladores.analisador.lexico.util;

import java.util.HashMap;

public class Tokens {
	public static final String INICIO_COMENTARIO = "(*";
	public static final String FIM_COMENTARIO = "*)";
	public static final String INICIO_LITERAL = "'";
	public static final int TAMANHO_LITERAL = 255;
    public final Token Identificador = new Token(25, "identificador");
    public final Token Inteiro = new Token(26, "inteiro");
    public final Token Literal = new Token(48, "literal");
	
	private HashMap<String, Token> token = new HashMap<>();
	{
		token.put("program", new Token(1, "program"));
		token.put("label", new Token(2, "label"));
		token.put("const", new Token(3, "const"));
		token.put("var", new Token(4, "var"));
		token.put("procedure", new Token(5, "procedure"));
		token.put("begin", new Token(6, "begin"));
		token.put("end", new Token(7, "end"));
		token.put("integer", new Token(8, "integer"));
		token.put("array", new Token(9, "array"));
		token.put("of", new Token(10, "of"));
		token.put("call", new Token(11, "call"));
		token.put("goto", new Token(12, "goto"));
		token.put("if", new Token(13, "if"));
		token.put("then", new Token(14, "then"));
		token.put("else", new Token(15, "else"));
		token.put("while", new Token(16, "while"));
		token.put("do", new Token(17, "do"));
		token.put("repeat", new Token(18, "repeat"));
		token.put("until", new Token(19, "until"));
		token.put("readln", new Token(20, "readln"));
		token.put("writeln", new Token(21, "writeln"));
		token.put("or", new Token(22, "or"));
		token.put("and", new Token(23, "and"));
		token.put("not", new Token(24, "not"));
		token.put("identificador", new Token(25, "identificador"));
		token.put("inteiro", new Token(26, "inteiro"));
		token.put("for", new Token(27, "for"));
		token.put("to", new Token(28, "to"));
		token.put("case", new Token(29, "case"));
		token.put("+", new Token(30, "+"));
		token.put("-", new Token(31, "-"));
		token.put("*", new Token(32, "*"));
		token.put("/", new Token(33, "/"));
		token.put("[", new Token(34, "["));
		token.put("]", new Token(35, "]"));
		token.put("(", new Token(36, "("));
		token.put(")", new Token(37, ")"));		
		token.put(":=", new Token(38, ":="));
		token.put(":", new Token(39, ":"));
		token.put("=", new Token(40, "="));
		token.put(">", new Token(41, ">"));
		token.put(">=", new Token(42, ">="));
		token.put("<", new Token(43, "<"));
		token.put("<=", new Token(44, "<="));
		token.put("<>", new Token(45, "<>"));		
		token.put(",", new Token(46, ","));
		token.put(";", new Token(47, ";"));
		token.put("literal", new Token(48, "literal"));
		token.put(".", new Token(49, "."));
		token.put("..", new Token(50, ".."));
	}

	public Token getCodigoToken(String nome) {
		return token.get(nome);
	}
}