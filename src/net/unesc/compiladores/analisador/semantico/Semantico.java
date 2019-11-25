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
	private static final String PROGRAM = "PROGRAM";
	private static final String CONST = "CONST";
	private static final String VAR = "VAR";
	private static final String PROCEDURE = "PROCEDURE";
	private static final String BEGIN = "BEGIN";
	private static final String PARAMETER = "PARAMETER";
	private static final String LABEL = "LABEL";

	private List<Simbolos> tabelaSimbolo;
	private Simbolos simbolo;
	private Simbolos aux;
	private String categoria;
	private int nivel;

	public Semantico() {
		this.tabelaSimbolo = new ArrayList<Simbolos>();
		this.simbolo = new Simbolos();
		this.aux = simbolo;
	}

	public LinkedList<?> getAnalise(Token token) {
		System.out.println(token.getCodigo() + " " + token.getNome());

		switch (token.getCodigo()) {
		case 1: { // Programa
			categoria = PROGRAM;

			break;
		}

		case 2: { // Label
			categoria = LABEL;

			break;
		}

		case 3: { // Constante
			categoria = CONST;

			break;
		}

		case 4: {

			categoria = VAR;

			break;
		}

		case 5: {
			categoria = PROCEDURE;

			break;
		}

		case 6: {
			categoria = BEGIN;

			break;
		}

		case 7: { // End

			if (aux.getParent() != null) {
				aux = aux.getParent();
			}
			
			break;
		}

		case 25: { // Identificador

			isDeclarado(token);

			break;
		}
		}

		return null;
	}

	private void isDeclarado(Token token) {
		System.out.println("Verificando token " + token.getNome() + " da linha " + token.getLinha());
		switch (categoria) {
		case PROGRAM: {// Finaliza porque o nome do programa é obrigado a ser declarado
			break;
		}

		case PROCEDURE: { // Busca para ver se a procedure foi ou não declarada

			// Se procedure declarada
			if (!buscaProcedure(token)) {
				Simbolos procedure = new Simbolos(token.getNome(), categoria, "Procedure", nivel);
				aux.setFilho(simbolo);
				procedure.setParent(aux);
				aux = procedure;
				
			} else {
				System.out.println("Erro semantico na procedure: " + token.getNome());
			}

			categoria = PARAMETER;

			break;
		}

		case PARAMETER: {
			// Se não tiver nada declarado, adiciona na lista a variável lida
			if (!buscaVariavel(token)) {
				aux.setVariavel(new Simbolos(categoria, token.getNome(), "Parâmetro"));
			} else {
				// Adiciona o erro semântico
				System.out.println("Erro semantico na linha " + token.getLinha());
			}

			break;
		}

		case CONST: {
			// Se não tiver nada declarado, adiciona na lista a variável lida
			if (!buscaVariavel(token)) {
				aux.setVariavel(new Simbolos(categoria, token.getNome(), "Constante"));
			} else {
				// Adiciona o erro semântico
				System.out.println("Erro semantico na linha " + token.getLinha());
			}

			break;
		}

		case LABEL: {
			// Se não tiver nada declarado, adiciona na lista a variável lida
			if (!buscaVariavel(token)) {
				aux.setVariavel(new Simbolos(categoria, token.getNome(), "Label"));
			} else {
				// Adiciona o erro semântico
				System.out.println("Erro semantico na linha " + token.getLinha());
			}

			break;
		}

		case VAR: {

			// Se não tiver nada declarado, adiciona na lista a variável lida
			if (!buscaVariavel(token)) {
				aux.setVariavel(new Simbolos(categoria, token.getNome(), "Variável"));
			} else {
				// Adiciona o erro semântico
				System.out.println("Erro semantico na linha " + token.getLinha());
			}

			break;
		}

		default: { // Erro semântico de variável não declarada
			break;
		}
		}
	}

	private boolean buscaVariavel(Token token) {

		if (aux.getVariavel().isEmpty()) {
			return false;
		}

		for (Simbolos s : aux.getVariavel()) {
			if (s.getNome().equalsIgnoreCase(token.getNome())) {
				return true;
			}
		}

		return false;
	}

	private boolean buscaProcedure(Token token) {
		if (simbolo.getFilhos().isEmpty()) {
			return false;
		}

		for (Simbolos procedure : aux.getFilhos()) {
			if (procedure.getNome().equalsIgnoreCase(token.getNome()) && procedure.getTipo().equals("Procedure")) {
				return true;
			}
		}

		return false;
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