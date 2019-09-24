package net.unesc.compiladores.analisador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.unesc.compiladores.analisador.lexico.util.Token;

public abstract class BaseAnalisador {
	private List<Erro> erro;

	public BaseAnalisador() {
		erro = new ArrayList<Erro>();
	}

	public List<Erro> getErro() {
		return erro;
	}

	public abstract LinkedList<Token> getAnalise();

	protected void addErro(Erro e) {
		erro.add(e);
	}
}