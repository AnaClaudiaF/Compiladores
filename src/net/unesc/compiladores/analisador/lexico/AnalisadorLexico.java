package net.unesc.compiladores.analisador.lexico;

import static net.unesc.compiladores.analisador.Tokens.FIM_COMENTARIO;
import static net.unesc.compiladores.analisador.Tokens.INICIO_COMENTARIO;
import static net.unesc.compiladores.analisador.Tokens.INICIO_LITERAL;
import static net.unesc.compiladores.analisador.Tokens.TAMANHO_LITERAL;

import java.util.LinkedList;
import java.util.regex.Pattern;

import net.unesc.compiladores.analisador.BaseAnalisador;
import net.unesc.compiladores.analisador.Erro;
import net.unesc.compiladores.analisador.Tokens;
import net.unesc.compiladores.analisador.lexico.util.Automato;
import net.unesc.compiladores.analisador.lexico.util.Node;
import net.unesc.compiladores.analisador.lexico.util.Token;

public class AnalisadorLexico extends BaseAnalisador {
	private Automato source;
	private Tokens tokens;
	private LinkedList<Token> saida;

	public AnalisadorLexico(String source) {
		this.source = new Automato(source);
		this.tokens = new Tokens();
		this.saida = new LinkedList<Token>();
	}

	public LinkedList<Token> getAnalise() {

		LinkedList<Node> source = this.source.getArvoreDerivacao();
		StringBuilder buffer = new StringBuilder();
		Token token = null;
		Node node;

		c: while (!source.isEmpty()) {
			node = source.pop();

			if (node.isNumerico() || node.isAlfanumerico()) {
				buffer.append(node.getCharacter());
				continue;
			}

			if (!buffer.toString().isEmpty()) {
				token = tokens.getCodigoToken(buffer.toString().toLowerCase());

				if (token != null) {
					saida.add(new Token(token.getCodigo(), token.getNome(), node.getLinha()));
				} else {
					if (isIdentificador(buffer.toString())) {
						saida.add(new Token(tokens.Identificador.getCodigo(), buffer.toString(), node.getLinha()));
					} else {
						if (isFloat(buffer.toString())) {
							addErro(new Erro("Valor numérico (" + buffer.toString() + ") informado no formato errado",
									node.getLinha()));
						} else {
							Integer inteiro = new Integer(buffer.toString());
							if (inteiro >= -32767 && inteiro <= 32767) {
								saida.add(new Token(tokens.Inteiro.getCodigo(), buffer.toString(), node.getLinha()));
								buffer.delete(0, buffer.length());
							} else {
								addErro(new Erro(
										"Valor numérico (" + buffer.toString() + ") forma os limites permitidos",
										node.getLinha()));
							}
						}
					}
				}

				buffer.delete(0, buffer.length());
			}

			if (node.isSimbolo()) {
				buffer.append(node.getCharacter());

				node = source.peek();
				System.out.println(node);
				if (tokens.getCodigoToken(buffer.toString()) != null) {
					if (tokens.getCodigoToken(buffer.toString()).getCodigo() == 31 && node.isNumerico()) {
						node = source.pop();
						while (node.isNumerico()) {
							buffer.append(node.getCharacter());
							node = source.pop();
						}

						if (node.isAlfanumerico()) {
							addErro(new Erro("Valor alfanumérico inválido", node.getLinha()));
							break;
						} else {
							if (isFloat(buffer.toString())) {
								addErro(new Erro(
										"Valor numérico (" + buffer.toString() + ") informado no formato errado",
										node.getLinha()));
							} else {
								Integer inteiro = new Integer(buffer.toString());
								if (inteiro >= -32767 && inteiro <= 32767) {
									saida.add(
											new Token(tokens.Inteiro.getCodigo(), buffer.toString(), node.getLinha()));
									buffer.delete(0, buffer.length());
								} else {
									addErro(new Erro(
											"Valor numérico (" + buffer.toString() + ") forma os limites permitidos",
											node.getLinha()));
									break;
								}
							}
						}
					}
					System.out.println(buffer);
					if (tokens.getCodigoToken(buffer.toString() + (source.peek().getCharacter())) != null) {
						node = source.pop();
						buffer.append(node.getCharacter());
					}

					if ((buffer.toString() + source.peek().getCharacter()).equals(INICIO_COMENTARIO)) {
						do {
							buffer.delete(0, buffer.length());

							if (!source.isEmpty()) {
								node = source.pop();
								buffer.append(node.getCharacter());
							} else {
								// cometário sem fechamento
								addErro(new Erro("Comentário sem fechar", node.getLinha()));
								break c;
							}
						} while (!(buffer.toString() + (!source.isEmpty() ? source.peek().getCharacter() : ""))
								.equals(FIM_COMENTARIO));
						node = source.pop();
						buffer.delete(0, buffer.length());
					}
				}

				if (buffer.toString().equals(INICIO_LITERAL)) {
					node = source.pop();
					do {

					} while (source.peek().getCharacter().equals(INICIO_LITERAL));
				}
			}

			if (!buffer.toString().isEmpty()) {
				token = tokens.getCodigoToken(buffer.toString().toLowerCase());

				if (token != null) {
					saida.add(new Token(token.getCodigo(), token.getNome(), node.getLinha()));
				} else {
					if (buffer.toString().trim().length() <= 30) {
						saida.add(new Token(111, buffer.toString(), node.getLinha()));
					}
				}

				buffer.delete(0, buffer.length());
			}
		}

		return saida;
	}

	private final boolean isIdentificador(final String buffer) {
		return (buffer.trim().length() > 0 && Character.isAlphabetic(buffer.trim().charAt(0))
				&& buffer.trim().length() <= 30);
	}

	private boolean isFloat(String number) {
		String decimalPattern = "([0-9]*)\\.([0-9]*)";

		return Pattern.matches(decimalPattern, number);
	}

	public static void main(String[] args) {
		new AnalisadorLexico("program teste_proc1; \n" + "var \n" + "x, y, z :integer; \n" + "procedure p; \n"
				+ "var \n" + "a :integer; \n" + "begin \n" + "a:=-20000;\n" + "readln(a); \n" + "if a=x then\n"
				+ "z:=z+x\n" + "else begin \n" + "z:=z-x;   \n" + "call p; \n" + "end; \n"
				+ "(* comentario integer .*)\n" + "end; \n" + "begin \n" + "z:=0; \n" + "readln(x,y); \n"
				+ "if x>y then \n" + "call p \n" + "else  \n" + "z:=z+x+y; \n" + "writeln(z); \n" + "end.")
						.getAnalise();
	}
}