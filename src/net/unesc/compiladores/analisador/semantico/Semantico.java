package net.unesc.compiladores.analisador.semantico;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.unesc.compiladores.analisador.BaseAnalisador;
import net.unesc.compiladores.analisador.lexico.util.Token;

public class Semantico extends BaseAnalisador {

	private static final String INT = "INT";
	private static final String LITERAL = "LITERAL";
	private static final String IDENTIFICADOR = "IDENTIFICADOR";
	private static final String PROGRAM = null;
	private List<Simbolos> tabelaSimbolo;
	private String LABEL = "LABEL";

	private String categoria;

	private int nivel;

	public Semantico() {
		this.tabelaSimbolo = new ArrayList<Simbolos>();
	}

	public LinkedList<?> getAnalise(Token token) {
		System.out.println(token.getCodigo() + " " + token.getNome());

		switch (token.getCodigo()) {

		case 1: {
			// program
			categoria = PROGRAM;
			break;
		}

		case 2: { // label
			categoria = LABEL;
			break;
		}

		case 3: { // const
			break;
		}

		case 4: { // var
			break;
		}

		case 5: { // procedure
			break;
		}

		case 6: { // begin
			break;
		}

		case 25: { // identificador

			insercao(new Simbolos(categoria, token.getNome(), retornaTipo(token.getCodigo())));
			break;
		}

		case 7: { // end

			break;
		}
		}

		return null;
	}

	private boolean busca(Simbolos simbolo) {
		for (Simbolos s : this.tabelaSimbolo) {
			if (s.getNome().equalsIgnoreCase(simbolo.getNome())) {
				return true;
			}
		}
		return false;
	}

	private boolean insercao(Simbolos simbolo) {
		if (busca(simbolo)) {
			return false;
		}

		return tabelaSimbolo.add(simbolo);
	}

	private String retornaTipo(int aCodigo) {
		switch (aCodigo) {
		case 37:
			return INT;
		case 39:
			return LITERAL;
		case 40:
			return IDENTIFICADOR;
		}
		return null;
	}
	
	private void incrementaNivel() {
		nivel++;
	}
	
	private void decrementaNivel() {
		nivel--;
	}

	@Override
	public LinkedList<?> getAnalise() {
		// TODO Auto-generated method stub
		return null;
	}
}