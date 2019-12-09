package net.unesc.compiladores.analisador.sintatico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.unesc.compiladores.analisador.BaseAnalisador;
import net.unesc.compiladores.analisador.Erro;
import net.unesc.compiladores.analisador.lexico.util.Token;
import net.unesc.compiladores.analisador.semantico.Semantico;
import net.unesc.compiladores.analisador.sintatico.parsing.Parsing;
import net.unesc.compiladores.analisador.sintatico.parsing.TabelaParsing;

/*
 * Sintatico: 
 * • Verificar se a estrutura geral do texto ou programa-fonte está correta 
 *   (de acordo, com as regras definidas pelagramática).
 * • Detecção de erros 
 */

/*
 * 1.Inicia derivando pelo primeiro item da tabela parsing, posição inicial;
 * 	1.2 Chama o metodo ParcingRecuperar repassando o codigo e linha, 
 * 		1.2.1 Nesse metodo percorre a tabela e caso encontre então retorna a uma lista de derivação com a sequencia dos codigos(lexicos)
 * 	1.3 Chama o metodo Derivacao passando essa lista de codigos(lexicos)
 * 		1.3.1 Nesse metodo realiza a reversão dessa lista(garantir recursão a esquerda) e adiciona os codigos em um pilha(listParsing).
 * 		1.3.2 Aciona o metodo addSaida que monta a saida dos tokens que foram derivados para imprimir na tabela da tela.
 * 2. Após termino desse fluxo, verifica a sequencia dos tokens
 * 	2.1 Percorre a lista de parsing com os codigos, verifica se é um terminal
 * 		2.1.1 Se for terminal
 * 			  	Verifica se a ordem da entrada informada pelo usuario, é a mesma da tabela de parsing(que está sendo derivada)
 *  		  	Se o codigoPilha(derivado da tabela parcing) é o mesmo que o codigoLexico(informado pelo usuario e criado na lista de token pelo lexico)
			  		Então a ordem de derivação está correta e não apresenta erro apenas remove o token e o elemento da pilha para não analisar novamente, e salva a saida
			  	Senão 
			  		Apresenta erro no codigo informado pelo usuario, pois a ordem não confere com a da tabela parsing(que está sendo derivada)
		2.2.1 Senão for terminal
				Então Deriva novamente(processo 1.2 para frente) 
		 		
 */
public class Sintatico extends BaseAnalisador {

	private TabelaParsing tabelaParsing;
	private LinkedList<Token> listToken;
	private LinkedList<String> listParsing;
	private LinkedList<Parsing> tokenSaida;
	private Token token = null;
	private String saidaParsing = "";
	private Semantico semantico;

	public Sintatico(LinkedList<Token> listToken) {
		this.listToken = listToken;
		this.tabelaParsing = new TabelaParsing();
		this.listParsing = new LinkedList<String>();
		this.tokenSaida = new LinkedList<Parsing>();
		this.semantico = new Semantico();
	}

	@Override
	public LinkedList<Parsing> getAnalise() {
		Integer codigoLexico;
		Integer codigoPilha;
		Integer linha;
		
		/* 
		 * Recuperando os dados inicias da tabela de parcing
		 * Retorna uma lista de token e repassa ao metodo devivacao;
		 */
		Derivacao(ParcingRecuperar(52, 1));
		
		//Percorre a lista de parsing
		while (!listParsing.isEmpty()) {
			
			//verifica se a lista de token está fazia
			if (listToken.isEmpty()) {
				break;
			}
			
			//pega o primeiro token
			token = listToken.peek();
			
			//pega o codigo do token(lexico)
			codigoLexico = token.getCodigo();
			
			//pega o codigo da pila(listParsing) convertendo para int
			codigoPilha = new Integer(listParsing.peek());
			
			//pega a linha do token(lexico)
			linha = token.getLinha();
			
			//verifica se é um terminal
			if (codigoPilha < 52) {
				/*
				 * Verifica se a ordem da entrada informada pelo usuario, é a mesma da tabela de parsing
				 * 
				 * Se o codigoPilha(derivado da tabela parcing) e o mesmo que o codigoLexico(informado pelo usuario e criado lista de token pelo lexico)
				 * 	Então a ordem de derivação está correta e não apresenta erro apenas remove o token para não analisar novamente
				 * Senão 
				 * 	Apresenta erro no codigo informado pelo usuario, pois a ordem não confere com a da tabela parsing
				 */
				if (codigoPilha.equals(codigoLexico)) {
					
					semantico.getAnalise(token);
					
					if (semantico.getErro() != null && !semantico.getErro().isEmpty()) {
						break;
					}
					
					listToken.pop();
					listParsing.pop();
					addSaida();
				} else {
					addErro(new Erro("Erro sintático na linha " + linha + " o token esperado era (" + codigoPilha + ") " + getTokens().getSintatico(codigoPilha).getNome()  + " e foi recebido (" + codigoLexico + ") " + getTokens().getSintatico(codigoLexico).getNome() , linha));
					break;
				}
			} else {
//				System.out.println("Não terminal");

				listParsing.pop();
				addSaida();

				Derivacao(ParcingRecuperar(codigoPilha, codigoLexico));

				continue;
			}
		}

		return tokenSaida;
	}

	private List<String> ParcingRecuperar(int codigo, int linha) {

//		System.out.println(
//				"Iniciando recuperação dos dados da tabela de parcing. Codigo -> " + codigo + " Linha -> " + linha);

		/*
		 * Recupera os dados da tabela de parsing
		 */
		String parcing = tabelaParsing.getParsing(codigo + "|" + linha);
//		System.out.println("Tabela parcing -> " + parcing);

		List<String> derivacao = null;

		/*
		 * Se encontrado os dados da tabela de parsing
		 */
		if (parcing != null && !parcing.isEmpty()) {

//			System.out.println("Encontrei algo");

			/*
			 * Adiciona na lista de derivação quebrando pelo "|"
			 */
			derivacao = new ArrayList<>(Arrays.asList(parcing.split("\\|")));
		}

		/*
		 * Retorna a lista de derivação ex:
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
			
//			System.out.println("Derivando os registros do parsing -> " + derivar);

			/*
			 * Adiciona na primeira posição da lista os dados da tabela de parsing
			 */
			listParsing.add(0, derivar);
		}
		
		addSaida();

		return listParsing;
	}

	private void addSaida() {
		//Monta a saida dos tokens que foram derivados para apresentar na tabela da tela.
		Token t;
		for (String s : listParsing) {
			t = getTokens().getSintatico(Integer.parseInt(s));
			
			saidaParsing += t.getCodigo() + " | ";
		}
		
		tokenSaida.add(new Parsing(saidaParsing));
		
		saidaParsing = "";
	}
	
	@Override
	public Semantico getSemantico() {
		return semantico;
	}
}