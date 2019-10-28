package net.unesc.compiladores.analisador.sintatico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.unesc.compiladores.analisador.BaseAnalisador;
import net.unesc.compiladores.analisador.Erro;
import net.unesc.compiladores.analisador.lexico.util.Token;
import net.unesc.compiladores.analisador.sintatico.parsing.Parsing;
import net.unesc.compiladores.analisador.sintatico.parsing.TabelaParsing;

public class Sintatico extends BaseAnalisador {

	private TabelaParsing tabelaParsing;
	private LinkedList<Token> listToken;
	private LinkedList<String> listParsing;
	private LinkedList<Parsing> tokenSaida;
	private Token token = null;
	private String saidaParsing = "";

	public Sintatico(LinkedList<Token> listToken) {
		this.listToken = listToken;
		this.tabelaParsing = new TabelaParsing();
		this.listParsing = new LinkedList<String>();
		this.tokenSaida = new LinkedList<Parsing>();
	}

	@Override
	public LinkedList<Parsing> getAnalise() {
		Integer codigoLexico;
		Integer codigoPilha;
		Integer linha;

		Derivacao(ParcingRecuperar(52, 1));

		while (!listParsing.isEmpty()) {
			
			if (listToken.isEmpty()) {
				break;
			}

			token = listToken.peek();

			codigoLexico = token.getCodigo();
			codigoPilha = new Integer(listParsing.peek());
			linha = token.getLinha();

			if (codigoPilha < 52) {
				if (codigoPilha.equals(codigoLexico)) {
					listToken.pop();
					listParsing.pop();
					addSaida();
				} else {
					addErro(new Erro("Erro sintático na linha " + linha + " o token esperado era (" + codigoPilha + ") " + getTokens().getSintatico(codigoPilha).getNome()  + " e foi recebido (" + codigoLexico + ") " + getTokens().getSintatico(codigoLexico).getNome() , linha));
					break;
				}
			} else {
				System.out.println("Não terminal");

				listParsing.pop();
				addSaida();

				Derivacao(ParcingRecuperar(codigoPilha, codigoLexico));

				continue;
			}
		}

		return tokenSaida;
	}

	private List<String> ParcingRecuperar(int codigo, int linha) {

		System.out.println(
				"Iniciando recuperação dos dados da tabela de parcing. Codigo -> " + codigo + " Linha -> " + linha);

		/*
		 * Recupera os dados da tabela de parsing
		 */
		String parcing = tabelaParsing.getParsing(codigo + "|" + linha);
		System.out.println("Tabela parcing -> " + parcing);

		List<String> derivacao = null;

		/*
		 * Se entrado os dados da tabela de parsing
		 */
		if (parcing != null && !parcing.isEmpty()) {

			System.out.println("Encontrei algo");

			/*
			 * Adiciona na lista de derivação retirando quebrando pelo "|"
			 */
			derivacao = new ArrayList<>(Arrays.asList(parcing.split("\\|")));
		}

		/*
		 * Retorna a lista de derivação
		 */
		return (derivacao != null && !derivacao.isEmpty() ? derivacao : null);
	}

	private <E> LinkedList<String> Derivacao(List<String> derivacao) {

		/*
		 * Se não existir dados para derivação retorna nada
		 */
		if (derivacao == null)

			return null;

		/*
		 * Faz a reversão dos dados
		 */
		Collections.reverse(derivacao);

		/*
		 * Percorre todos os objetos de derivação e adiciona na lista
		 */
		for (String derivar : derivacao) {

System.out.println("Derivando os registros do parsing -> " + derivar);

			/*
			 * Adiciona na primeira posição da lista os dados da tabela de parsing
			 */
			listParsing.add(0, derivar);
		}
		
		addSaida();

		return listParsing;
	}

	private void addSaida() {
		Token t;
		for (String s : listParsing) {
			t = getTokens().getSintatico(Integer.parseInt(s));
			
			saidaParsing += t.getCodigo() + " | ";
		}
		
		tokenSaida.add(new Parsing(saidaParsing));
		
		saidaParsing = "";
	}
}