package net.unesc.compiladores.analisador.lexico;

import static net.unesc.compiladores.analisador.Tokens.FIM_COMENTARIO;
import static net.unesc.compiladores.analisador.Tokens.FIM_LITERAL;
import static net.unesc.compiladores.analisador.Tokens.FIM_LITERAL2;
import static net.unesc.compiladores.analisador.Tokens.INICIO_COMENTARIO;
import static net.unesc.compiladores.analisador.Tokens.INICIO_LITERAL;
import static net.unesc.compiladores.analisador.Tokens.TAMANHO_LITERAL;

import java.util.LinkedList;

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

	@Override
	public LinkedList<Token> getAnalise() {

		LinkedList<Node> source = this.source.getArvoreDerivacao();
		StringBuilder buffer = new StringBuilder();
		Token token = null;
		Node node;
		int linha = 0;

		// Percorrendo a lista de nodes
		c: while (!source.isEmpty()) {
			// Armazena o primeiro node e ser lida na variavel node e o remove do topo da
			// lista, para não ler sempre o mesmo node.
			node = source.pop();

			// Verifica se o node percorrido é numero, ou se é alfanumero ou se é igual ao
			// "."(ultimo caracter do codigo) ou e o primeiro caracter
			if (node.isNumerico() || node.isAlfanumerico()
					|| (node.getCharacter().equals(".") && source.peek().isNumerico())) {
				buffer.append(node.getCharacter());
				continue;
			}

			// Verifica se o buffer está vazio
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
							// Testar com a ANA, coloquei um try catch
							try {
								Integer inteiro = new Integer(buffer.toString());
								if (inteiro >= -32767 && inteiro <= 32767) {
									saida.add(
											new Token(tokens.Inteiro.getCodigo(), buffer.toString(), node.getLinha()));
									buffer.delete(0, buffer.length());
								} else {
									addErro(new Erro(
											"Valor numérico (" + buffer.toString() + ") forma os limites permitidos",
											node.getLinha()));
								}
							} catch (Exception e) {
								addErro(new Erro(
										"Valor numérico (" + buffer.toString() + ") forma os limites permitidos",
										node.getLinha()));
							}
						}
					}
				}

				buffer.delete(0, buffer.length());
			}

			// Verifica se é um simbolo
			if (node.isSimbolo()) {
				// Adiciona caracter no buffer
				buffer.append(node.getCharacter());

				// Atualiza o node com o primeiro node da lista (Ver com a ANA a LOGICA)
				node = source.peek();
				System.out.println(node);

				// Pega o codigo do token e verifica se é diferente de nulo
				if (tokens.getCodigoToken(buffer.toString()) != null) {

					// Verifica se o codigo do token é igual ao 31 E verifica se é numero (Ver com a
					// ANA a LOGICA)
					if (tokens.getCodigoToken(buffer.toString()).getCodigo() == 31 && node.isNumerico()) {
						node = source.pop();
						do {
							if (source.peek().getCharacter().equals(".")) {
								buffer.append(node.getCharacter());
								node = source.pop();
							}
							buffer.append(node.getCharacter());
							
							if (source.peek().getCharacter().equals(";")) {
								break;
							}
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
								try {
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
								} catch (Exception e) {
									addErro(new Erro(
											"Valor numérico (" + buffer.toString() + ") forma os limites permitidos",
											node.getLinha()));
								}
							}
						}
					}
					System.out.println(buffer);

					if (tokens.getCodigoToken(buffer.toString() + (source.peek().getCharacter())) != null) {
						node = source.pop();
						buffer.append(node.getCharacter());
					}

					/*
					 * Verifica comentarios, verifica o inicio do comentario. Caso ele esteja em a
					 * aberto, gera um erro.
					 */
					if ((buffer.toString() + source.peek().getCharacter()).equals(INICIO_COMENTARIO)) {
						String comentario = "";

						/*
						 * Percorre toda a lista source novamente lendo node a node e os removendo Até
						 * encontrar o fim do comentario ou gerar o erro de comentario sem fechamento
						 */
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

				// Verificação dos literais, foi ajustado o fim do literal. Mesma logica do
				// comentario.
				if (buffer.toString().equals(INICIO_LITERAL)) {
					node = source.pop();
					String literal = "";
					buffer.delete(0, buffer.length());
					boolean fechamentoLiteral = false;
					do {
						buffer.delete(0, buffer.length());

						// literal sem fechamento
						if (source.isEmpty()) {	
							addErro(new Erro("Literal sem fechamento", node.getLinha()));
							break c;
						}

						node = source.pop();
						buffer.append(node.getCharacter());

						literal = buffer.toString() + (!source.isEmpty() ? source.peek().getCharacter() : "");
						System.out.println(literal);
					} while (!literal.matches("'\\)" +"|"+ FIM_LITERAL));
					
					if(fechamentoLiteral) {
						addErro(new Erro("Literal sem fechamento", node.getLinha()));
						break c;
					}

					if (buffer.toString().length() <= TAMANHO_LITERAL) {
						saida.add(new Token(tokens.Inteiro.getCodigo(), buffer.toString(), node.getLinha()));

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
			
			linha = node.getLinha();
		}
		
		saida.add(new Token(51, "$", linha));

		return saida;
	}
}