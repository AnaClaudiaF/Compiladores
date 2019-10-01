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

			if (node.isNumerico() || node.isAlfanumerico() || (node.getCharacter().equals(".") && source.peek().isNumerico())) {
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
						do {
							if (source.peek().getCharacter().equals(".")){
								buffer.append(node.getCharacter());
								node = source.pop();
							}
							buffer.append(node.getCharacter());
							node = source.pop();
						} while (node.isNumerico());

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
						String comentario = "";

						do {
							buffer.delete(0, buffer.length());

							// comentário sem fechamento
							if (comentario.equals(INICIO_COMENTARIO) || source.isEmpty()) {
								addErro(new Erro("Comentário aberto", node.getLinha()));
								break c;
							}

							node = source.pop();
							buffer.append(node.getCharacter());
							
							comentario = buffer.toString() + (!source.isEmpty() ? source.peek().getCharacter() : "");
System.out.println(comentario);
						} while (!comentario.equals(FIM_COMENTARIO));

						node = source.pop();
						buffer.delete(0, buffer.length());
					}
				}

				if (buffer.toString().equals(INICIO_LITERAL)) {
					node = source.pop();
					String literal = "";
					buffer.delete(0, buffer.length());
					do {
//						buffer.delete(0, buffer.length());

						// comentário sem fechamento
						if (literal.equals(INICIO_LITERAL) || source.isEmpty()) {
							addErro(new Erro("Literal sem fechamento", node.getLinha()));
							break c;
						}

						node = source.pop();
						buffer.append(node.getCharacter());
						
						literal = (!source.isEmpty() ? source.peek().getCharacter() : "");
System.out.println(literal);
					} while (!literal.equals(INICIO_LITERAL));
					
					if (buffer.toString().length() <= TAMANHO_LITERAL) {
						saida.add(
								new Token(tokens.Inteiro.getCodigo(), buffer.toString(), node.getLinha()));
						
						buffer.delete(0, buffer.length());
					} else {
						addErro(new Erro("Literal com o tamanho inválido", node.getLinha()));
						break c;
					}
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
}