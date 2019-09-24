package net.unesc.compiladores.analisador.sintatico;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.unesc.compiladores.analisador.BaseAnalisador;
import net.unesc.compiladores.analisador.Erro;
import net.unesc.compiladores.analisador.lexico.util.Token;
import net.unesc.compiladores.analisador.sintatico.parsing.TabelaParsing;

public class Sintatico extends BaseAnalisador {

	private LinkedList<Token> tokens;
	private TabelaParsing tabelaParsing;
	private LinkedList<String> derivado;

	public Sintatico(LinkedList<Token> tokens) {
		this.tokens = tokens;
		this.tabelaParsing = new TabelaParsing();
	}

	@Override
	public LinkedList<Token> getAnalise() {

		setDerivacao(52, 1);
		Integer codigo_token;
		Integer codigo_pilha;
		Integer linha;

		while (!derivado.isEmpty()) {
			Token token = null;
			if (tokens != null && !tokens.isEmpty()) {
				token = tokens.peek();
				codigo_token = token.getCodigo();
				codigo_pilha = Integer.parseInt(derivado.peek());
				linha = token.getLinha();

				/*
				Se é um terminal
				*/
				if	(codigo_pilha < 52)
				{
					
					System.out.println("Terminal");
					System.out.println(codigo_pilha);
					System.out.println(codigo_token);
					
					/*
					Se códigos foram iguais
					*/
					
					if	(codigo_pilha.equals(codigo_token))
					{
						System.out.println("Removendo os dados da pilha");

						derivado.pop();
						tokens.pop();
					}
					
					/*
					Se não forem iguais
					*/
					else
					{
						/*
						Sinaliza o erro sintático e a linha
						*/
						addErro(new Erro("Erro sintático na linha ", linha));
						
						break;
					}
				}
				
				/*
				Se é um não terminal
				*/
				else
				{
					
					System.out.println("Não terminal");
					
					/*
					Remove os dados da pilha
					*/
					derivado.pop();
					
					/*
					Guarda os novos códigos de derivação no começo da pilha
					*/
					setDerivacao(codigo_pilha, codigo_token);
					
					/*
					Continua a leitura dos registros
					*/
					continue;
				
				}
				
			System.out.println(getErro());
				
			}
		}

		return null;
	}

	private LinkedList<String> setDerivacao(int codigo, int derivacao) {
		String codigo_derivacao = tabelaParsing.getParsing(codigo + "|" + derivacao);
System.out.println(codigo_derivacao);

		if (codigo_derivacao != null && !codigo_derivacao.trim().isEmpty()) {
			derivado = new LinkedList<String>(Arrays.asList(codigo_derivacao.split("\\|")));
			System.out.println(derivado);
		}

		return derivado != null && !derivado.isEmpty() ? null : derivado;
	}

	private LinkedList<String> ParcingRecuperar(int codigo, int linha) {

		System.out.println(
				"Iniciando recuperação dos dados da tabela de parcing. Codigo -> " + codigo + " Linha -> " + linha);

		/*
		 * Recupera os dados da tabela de parsing
		 */
		String parcing = tabelaParsing.getParsing(codigo + "|" + linha);
		System.out.println("Tabela parcing -> " + parcing);

		LinkedList<String> derivacao = null;

		/*
		 * Se entrado os dados da tabela de parsing
		 */
		if (parcing != null && !parcing.isEmpty()) {

			System.out.println("Encontrei algo");

			/*
			 * Adiciona na lista de derivação retirando quebrando pelo "|"
			 */
			derivacao = new LinkedList<String>(Arrays.asList(parcing.split("\\|")));
		}

		/*
		 * Retorna a lista de derivação
		 */
		return (derivacao != null && !derivacao.isEmpty() ? derivacao : null);
	}
}
