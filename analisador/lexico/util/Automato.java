package net.unesc.compiladores.analisador.lexico.util;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

public class Automato {
	private String source;

	private Node node;

	public Automato(String source) {
		this.source = source;
	}

	public LinkedList<Node> getSource() {
		LinkedList<Node> lst_node = new LinkedList<Node>();

		String[] linhas = source.split("\n");
		char conteudo;
		int cursor;
		int linha = 1;

		for (String line : linhas) {
			cursor = 0;
			while (cursor < line.length()) {
				conteudo = line.charAt(cursor);

				node = new Node(conteudo, linha, false);

				lst_node.add(node);

				cursor++;
			}
			linha++;
		}

		System.out.println(lst_node);

		return lst_node;
	}
}