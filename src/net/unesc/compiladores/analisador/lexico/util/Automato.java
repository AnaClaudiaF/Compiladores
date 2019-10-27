package net.unesc.compiladores.analisador.lexico.util;

import java.util.LinkedList;

/*
 * Essa classe precorre o codigo a ser compilado, separando caracter por caracter.
 * Essa separação é armaezena em uma lista de Node ja identificando a linha e a tipagem do caracter.
 * */
public class Automato {
	private String source;

	public Automato(String source) {
		this.source = source;
	}

	public LinkedList<Node> getArvoreDerivacao() {
		//Separa por quebra de linha o codigo a ser compilado. 
		String[] linhas = source.split("\n");
		LinkedList<Node> node = new LinkedList<Node>();
		char conteudo;
		int linha = 0;
		
		//Percorre cada linha separadamente
		for (String line : linhas) {
			String[] colunas = line.split("");
			
			//Percorre caracter por caracter
			for (int i = 0; i < colunas.length; i++) {
				
				/* Verifica se existe caracter por posição e insere na lista de node,
				 * Cada node armazena o tipo do caracter e sua respectiva linha.
				 * caso não tenha caracter então insere um node com conteudo em branco
				 */
				if (!colunas[i].trim().isEmpty()) {
					conteudo = colunas[i].charAt(0);
					node.add(new Node(conteudo, linha));
				} else {
					node.add(new Node(" ".charAt(0), linha));
				}
			}

			linha++;

			node.add(new Node(" ".charAt(0), linha));
		}

		return node;
	}
}