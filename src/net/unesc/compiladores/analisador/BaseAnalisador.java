package net.unesc.compiladores.analisador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import net.unesc.compiladores.analisador.lexico.util.Token;

public abstract class BaseAnalisador {
	private List<Erro> erro;

	public BaseAnalisador() {
		erro = new ArrayList<Erro>();
	}

	public abstract LinkedList<Token> getAnalise();
	
	public List<Erro> getErro() {
		return erro;
	}

	protected void addErro(Erro e) {
		erro.add(e);
	}

	protected final boolean isIdentificador(final String buffer) {
		return (buffer.trim().length() > 0 && Character.isAlphabetic(buffer.trim().charAt(0))
				&& buffer.trim().length() <= 30);
	}

	protected boolean isFloat(String number) {
		String decimalPattern = "\\-([0-9]*)\\.([0-9]*)||([0-9]*)\\.([0-9]*)";

		return Pattern.matches(decimalPattern, number);
	}
}