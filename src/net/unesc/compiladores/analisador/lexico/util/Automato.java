package net.unesc.compiladores.analisador.lexico.util;

import java.util.concurrent.LinkedBlockingDeque;

public class Automato {
	private String source;

	public Automato(String source) {
		this.source = source;
	}
	
	public LinkedBlockingDeque<Node> getArvoreDerivacao() {
		
		String[] linhas = source.split("\n");
		LinkedBlockingDeque<Node> node = new LinkedBlockingDeque<Node>();
		char conteudo;
		int linha = 1;
		
		for (String line : linhas) {
			String[] colunas = line.split("");
			
			for (int i = 0; i < colunas.length; i++) {
				conteudo = colunas[i].charAt(0);
				node.add(new Node(conteudo, linha));
			}
			
			linha++;
		}
		
		return node;
	}
}