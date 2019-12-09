package net.unesc.compiladores.analisador.semantico;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.unesc.compiladores.analisador.BaseAnalisador;
import net.unesc.compiladores.analisador.Erro;
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
			this.nivel = 1;
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
			if (this.nivel == 2) {
				this.nivel--;
			}
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
		// System.out.println("Verificando token " + token.getNome() + " da linha " +
		// token.getLinha());
		switch (categoria) {
		case PROGRAM: {// Finaliza porque o nome do programa √© obrigado a ser declarado
			break;
		}

		case PROCEDURE: { // Busca para ver se a procedure foi ou n√£o declarada

			// Se procedure declarada
			if (!buscaProcedure(token)) {
				Simbolos procedure = new Simbolos(token.getNome(), categoria, "Procedure", nivel);
				tabelaSimbolo.add(procedure);
				aux.setFilho(procedure);
				procedure.setParent(aux);
				aux = procedure;
				this.nivel = 2;
			} else {
				System.out.println("Erro semantico na procedure: " + token.getNome());
				addErro(new Erro("Erro sem‚ntico na linha " + token.getLinha() + " a procedure " + token.getNome()
						+ " j· foi declarada", token.getLinha()));
			}

			categoria = PARAMETER;

			break;
		}

		case PARAMETER:
		case CONST:
		case LABEL:
		case VAR: { // caso for um par‚metro, constante, label ou vari·vel verifica se n„o foi
					// declarada
			if (!buscaVariavel(token)) {
				
				Simbolos variavel = new Simbolos(token.getNome(), categoria, (categoria.equals(VAR) ? "vari·vel"
						: categoria.equals(CONST) ? "constante" : categoria.equals(LABEL) ? "label" : "parametro"),
						nivel);
				
				aux.setVariavel(variavel);
				tabelaSimbolo.add(variavel);
			} else {
				// Adiciona o erro sem√¢ntico
				System.out.println("Erro semantico na linha " + token.getLinha());
				addErro(new Erro("Erro sem‚ntico na linha " + token.getLinha() + " "
						+ (categoria.equals(VAR) ? "vari·vel"
								: categoria.equals(CONST) ? "constante"
										: categoria.equals(LABEL) ? "label" : "parametro")
						+ " " + token.getNome() + " j· foi declarada ", token.getLinha()));
			}

			break;
		}

		default: { // Erro sem√¢ntico de vari√°vel n√£o declarada
			if (!existeVariavel(token, aux)) {
				System.err.println("caiu");
				addErro(new Erro("Erro semantico na linha " + token.getLinha() + " a vari·vel " + token.getNome() + " n„o foi declarada", token.getLinha()));
			}
			break;

		}
		}
	}
	
	private boolean existeVariavel(Token token, Simbolos aux) {
		
		for (Simbolos procedure : aux.getFilhos()) {
			if (procedure.getTipo().equalsIgnoreCase("procedure") && procedure.getNome().equalsIgnoreCase(token.getNome())) {
				return true;
			}
		}
		
		for (Simbolos variavel : aux.getVariavel()) {
			if (variavel.getNome().equalsIgnoreCase(token.getNome())) {
				return true;
			}
		}
		
		if (aux.getParent() != null) {
			return existeVariavel(token, aux.getParent());
		}
				
		return false;
	}

	private boolean buscaVariavel(Token token) {

		if (aux.getVariavel().isEmpty()) {
			return false;
		}

		for (Simbolos s : aux.getVariavel()) {
			if (s.getNome().equalsIgnoreCase(token.getNome()) && this.nivel == s.getNivel()
					&& !s.getTipo().equalsIgnoreCase("Procedure")) {
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
			if (procedure.getNome() != null && procedure.getNome().equalsIgnoreCase(token.getNome())
					&& procedure.getTipo().equals("Procedure")) {
				return true;
			}
		}

		return false;
	}

	@Override
	public LinkedList<?> getAnalise() {
		return null;
	}

	public List<Simbolos> getTabelaSimbolo() {
		return tabelaSimbolo;
	}
}
