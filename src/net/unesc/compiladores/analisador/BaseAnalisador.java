package net.unesc.compiladores.analisador;

import java.util.ArrayList;
import java.util.List;

import net.unesc.compiladores.analisador.lexico.util.Token;

public abstract class BaseAnalisador {
	private List<Erro> erro;
	
	public BaseAnalisador() {
		erro = new ArrayList<Erro>();
	}
	
	public abstract List<Erro> getErro();
	public abstract List<Token> getAnalise();
	
	protected void addErro(Erro e) {
		erro.add(e);
	}
}