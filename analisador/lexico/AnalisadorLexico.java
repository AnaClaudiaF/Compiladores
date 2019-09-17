package net.unesc.compiladores.analisador.lexico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import net.unesc.compiladores.analisador.Erro;
import net.unesc.compiladores.analisador.lexico.util.Automato;
import net.unesc.compiladores.analisador.lexico.util.Node;
import net.unesc.compiladores.analisador.lexico.util.Token;
import net.unesc.compiladores.analisador.lexico.util.Tokens;

public class AnalisadorLexico {
	private Automato source;
	private Tokens token;
	private List<Erro> erro;

	public AnalisadorLexico(String source) {
		this.source = new Automato(source);
		token = new Tokens();
	}

	public List<Token> getAnaliseLexica() {
		List<Token> tokens = new ArrayList<>();
		LinkedList<Node> src = source.getSource();
		StringBuilder buffer = new StringBuilder();
		int cursor = 0;
		Node node;
		Object codigo;

		while (cursor < src.size()) {
			node = src.get(cursor);

			while (node.isNumerico()) {
				buffer.append(node.getChar().trim().toLowerCase());
				node = src.get(++cursor);
			}

			if (!buffer.toString().trim().isEmpty()) {
				codigo = token.getCodigoToken(buffer.toString());

				if (codigo != null) {
					tokens.add(new Token(Integer.parseInt(codigo.toString()), buffer.toString(), node.getLine()));
				} else {
					Object numero = buffer;

					Integer inteiro = new Integer(buffer.toString());

					if (inteiro >= -32767 && inteiro <= 32767) {
						codigo = 26;
						tokens.add(new Token(Integer.parseInt(codigo.toString()), buffer.toString(), node.getLine()));
					} else {
						erro.add(new Erro("Valor inteiro com limite excedido", node.getLine()));
						break;
					}
				}

				buffer.delete(0, buffer.length());
			}
			
			while (node.isAlfanumerico()) {
				buffer.append(node.getChar().trim().toLowerCase());
				node = src.get(++cursor);
			}

			System.out.println(buffer);

			cursor++;
		}

		System.err.println(tokens);
		
		return tokens;
	}

	/* Verifica o tipo do numero, se é Integer ou float */
	private Number typeNumber(String simboloEncontrado, int posicaoAtual) {
		try {
			if (simboloEncontrado.matches(expressaoRegularNumero)) {
				if (simboloEncontrado.contains(".")) {
					return Double.parseDouble(simboloEncontrado) > Float.MAX_VALUE ? null
							: Float.parseFloat(simboloEncontrado);
				}
				return Long.parseLong(simboloEncontrado) > Integer.MAX_VALUE ? null
						: Integer.parseInt(simboloEncontrado);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Log.gravar(e.getMessage() + "\n" + simboloEncontrado, Log.LOG);
			geraTokenDesconhecido(recuperaLinha(posicaoAtual), posicaoAtual);
		}
		return null;
	}

	/* Verifica numero e gera Token */
	private void verificaNumero(int posicaoAtual) {
		Object objNumber = typeNumber(getSimboloEncontrado(posicaoAtual), posicaoAtual);
		numeroFloat = 0f;
		numeroInteiro = 0;
		if (objNumber != null) {
			if (objNumber instanceof Float) {
				numeroFloat = Float.parseFloat(objNumber.toString());
				geraToken("_numfloat", recuperaLinha(posicaoAtual), numeroFloat);
			} else if (objNumber instanceof Integer) {
				numeroInteiro = Integer.parseInt(objNumber.toString());
				if (numeroInteiro == 0) {
					geraToken("0", recuperaLinha(posicaoAtual), objNumber.toString());
				} else {
					geraToken("_numint", recuperaLinha(posicaoAtual), numeroInteiro);
				}
			}
		} else {
			geraTokenDesconhecido(recuperaLinha(posicaoAtual), posicaoAtual);
		}
		inicioAutomato(posicaoAtual);
	}

	public static void main(String[] args) {
		new AnalisadorLexico("PROGRAM TESTE_PROC;\n" + "	(*Proc A*)\n" + "	PROCEDURE p_a(idd : INTEGER);\n"
				+ "		VAR \n" + "		    X, Y, Z : INTEGER;\n" + "	PROCEDURE p_a(idd : INTEGER);\n"
				+ "		VAR \n" + "		    X, Y, Z : INTEGER;\n" + "	PROCEDURE p_a(idd : INTEGER);\n"
				+ "		VAR \n" + "		    X, Y, Z : INTEGER;\n" + "	BEGIN\n" + "		X := X * Y;\n" + "	END;\n"
				+ "	BEGIN\n" + "		X := X * Y;\n" + "	END;\n" + "	BEGIN\n" + "		X := X * Y;\n" + "	END;\n"
				+ "\n" + "	(*Proc B com os mesmos dados de A*)\n" + "	PROCEDURE p_b(idd : INTEGER);\n"
				+ "		VAR \n" + "		    X, Y, Z : INTEGER;\n" + "	PROCEDURE p_a(idd : INTEGER);\n"
				+ "		VAR \n" + "		    X, Y, Z : INTEGER;\n" + "	BEGIN\n" + "		X := X * Y;\n" + "	END;\n"
				+ "	BEGIN\n" + "		X := Z *Z;\n" + "	END;\n" + "\n" + "\n" + "BEGIN\n" + "	call p_a(10 + 2);\n"
				+ "	call p_b(-5);\n" + "END.\n").getAnaliseLexica();
	}

	public List<Erro> getErro() {
		return erro;
	}
}