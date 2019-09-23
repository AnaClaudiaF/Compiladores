package net.unesc.compiladores.analisador.lexico.util;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

public class Automato {
	private String source;

	public Automato(String source) {
		this.source = source;
	}
	
	public LinkedList<Node> getArvoreDerivacao() {
		
		String[] linhas = source.split("\n");
		LinkedList<Node> node = new LinkedList<Node>();
		char conteudo;
		int linha = 1;
		
		for (String line : linhas) {
			String[] colunas = line.split("");
			
			for (int i = 0; i < colunas.length; i++) {
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