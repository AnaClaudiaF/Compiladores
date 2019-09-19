package compiladores.analisador.lexico;

import java.util.ArrayList;

import java.util.List;

public class AnalisadorLexico {

	private Tokens token = new Tokens();
	private String gramatica;
	private List<Token> tokens = new ArrayList<>();

	public AnalisadorLexico(String gramatica) {
		this.gramatica = gramatica;
	}
	
	public List<Token> AnaliseLexica() {
		String caracter_lido = "";
		int ln_cursor = 0;

		Character caracter = ' ';
		Character negativo = ' ';

		try {
			/*
			 * Guarda o tipo do caracter lido
			 */
			char tipo_lido = Tokens.CHAR_DEFAULT;

			/*
			 * Percorre o programa
			 */
			while (ln_cursor <= gramatica.length()) {
				caracter = gramatica.charAt(ln_cursor);

				tipo_lido = TipoCharVerificar(caracter);

				int ln_codigo = -1;

				ln_cursor++;

				/*
				 * Recebe o valor negativo pra ele constar na tabela do léxico
				 */
				if (negativo == '-' && caracter == ' ') {
					tipo_lido = Tokens.CHAR_ESPECIAL;
					caracter = '-';
				}

				/*
				 * Verifica se são alfanumerico
				 */
				if (tipo_lido == Tokens.CHAR_ALFA || tipo_lido == Tokens.CHAR_NUMERICO) {
					/*
					 * Vai alimentado até encontrar seu tipo
					 */
					caracter_lido += caracter;

					/*
					 * Retira os espaços vazios
					 */
					caracter_lido = caracter_lido.trim();

					/*
					 * Recupera o último valor processado
					 */
					negativo = caracter;

					/*
					 * Volta para ler o proximo caractere
					 */
					continue;
				}

				/*
				 * Verifica se são literal
				 */
				if (AlfaNumericoVerificar(caracter)) {
					/*
					 * Guarda o caracter que tem que encontrar
					 */
					final char ls_procurar = caracter;

					/*
					 * Se tipo literal
					 */
					if (tipo_lido == Tokens.CHAR_LITERAL) {
						/*
						 * Analisa o cursor na horizontal
						 */
						final Object[] lo_cursor_analise = CursorAnalisar(gramatica, caracter_lido, caracter, ln_cursor,
								ls_procurar);

						/*
						 * Alimenta os objetos
						 */
						ln_cursor = (int) lo_cursor_analise[0];
						caracter_lido = (String) lo_cursor_analise[1];

						/*
						 * Atribui o seu devido código
						 */
						ln_codigo = 48;

						tokens.add(new Token(ln_codigo, caracter_lido));
					}
				}

				else {
					/*
					 * Verifica se é uma palavra reservada
					 */
					if (PalavraReservadaVerificar(caracter_lido)) {
						/*
						 * Guarda o código da palavra reservada
						 */
						ln_codigo = token.getCodigoToken(caracter_lido);

						/*
						 * Monta a informação do usuário
						 */
						tokens.add(new Token(ln_codigo, caracter_lido));
					}

					/*
					 * Verifica se não é um identificador
					 */
					else if (IdentificadorVerificar(caracter_lido)) {
						ln_codigo = 25;

						tokens.add(new Token(ln_codigo, caracter_lido));
					}

					/*
					 * Verifica se a leitura é um numerico
					 */
					else if (NumericoVerificar(caracter_lido)) {
						/*
						 * Define o codigo para um tipo numerico
						 */
						ln_codigo = 26;

						tokens.add(new Token(ln_codigo, caracter_lido));
					}

					/*
					 * Verifica se é um numerico negativo
					 */
					if (tipo_lido == Tokens.CHAR_NUMERICO) {
						/*
						 * Se é um numerico negativo
						 */
						if (Integer.valueOf(caracter_lido) >= -32768 && Integer.valueOf(caracter_lido) <= 32767) {
							/*
							 * Define o codigo para um tipo numerico
							 */
							ln_codigo = 26;

							tokens.add(new Token(ln_codigo, caracter_lido));
						}
					}

					/*
					 * Verifica se é do tipo caractere
					 */
					if (tipo_lido == Tokens.CHAR_RELACIONAL || tipo_lido == Tokens.CHAR_ESPECIAL) {
						/*
						 * Se dois caracteres
						 */
						final String ls_char = Character.toString(caracter)
								+ (ln_cursor < gramatica.length() ? Character.toString(gramatica.charAt(ln_cursor))
										: "");

						/*
						 * Se caracter encontrado for um comentario
						 */
						if (VerificaComentario(ls_char)) {
							/*
							 * Procura até fechar o comentário
							 */
							final char lc_procurar = ')';

							/*
							 * Analisa o cursor na horizontal.
							 */
							final Object[] lo_cursor_analise = CursorAnalisar(gramatica, caracter_lido, caracter,
									ln_cursor, lc_procurar);

							/*
							 * Alimenta os objetos.
							 */
							ln_cursor = (int) lo_cursor_analise[0];
							caracter_lido = (String) lo_cursor_analise[1];

							/*
							 * Atribui o seu devido codigo
							 */
							ln_codigo = 0;

//							tokens.add(new Token(ln_codigo, caracter_lido));
						}

						else {
							/*
							 * Verifica se são 2 caracteres
							 */
							if (token.isSimbolo(ls_char)) {
								/*
								 * Guarda o codigo
								 */
								ln_codigo = token.getCodigoToken(ls_char);

								/*
								 * Carrega o caracter lido
								 */
								caracter_lido = ls_char;

								/*
								 * Move o cursor
								 */
								ln_cursor++;
							}

							/*
							 * Se somente um caractere
							 */
							else {
								/*
								 * Guarda o codigo do caractere
								 */
								ln_codigo = token.getCodigoToken(Character.toString(caracter));

								/*
								 * Carrega o caracter lido
								 */
								caracter_lido = Character.toString(caracter);
							}

							tokens.add(new Token(ln_codigo, caracter_lido));

						}
					}
				}

				/*
				 * Se achou o código para analisar
				 */
				if (ln_codigo >= 0) {
					/*
					 * Reseta a leitura
					 */
					caracter_lido = "";
				}

				/*
				 * Identifica uma quebra de linha
				 */
				if (caracter == '\n') {
					/*
					 * Volta para ler o proximo caracter
					 */
					continue;
				}
			}
		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

		/*
		 * Mostra a analise para o usuário
		 */
		return tokens;
	}

	/**
	 * Verifica se argumento passado é um comentario.
	 */
	private final boolean VerificaComentario(final String as_char) {
		return (as_char.equals("(*"));
	}

	private final Object[] CursorAnalisar(final String as_programa, String as_leitura, char ac_char, int an_cursor,
			final char ac_procurar) {
		/*
		 * Guarda o literal até encontrar o final
		 */
		do {
			/*
			 * Vai alimentado até encontrar seu tipo
			 */
			as_leitura += ac_char;

			/*
			 * Busca o caractere
			 */
			ac_char = as_programa.charAt(an_cursor);

			/*
			 * Retira os espaços vazios
			 */
			as_leitura = as_leitura.toLowerCase();

			/*
			 * Incrementa o cursor
			 */
			an_cursor++;
		}

		while (ac_char != ac_procurar);

		/*
		 * Adiciona o ultimo caracter lido
		 */
		as_leitura += ac_char;

		return (new Object[] { an_cursor, as_leitura });
	}

	/**
	 * Verifica se o caracter passado é um AlfaNumerico.
	 */
	private final boolean AlfaNumericoVerificar(final char ac_char) {
		return (NumericoVerificar(Character.toString(ac_char)) || TipoCharVerificar(ac_char) == Tokens.CHAR_ALFA
				|| TipoCharVerificar(ac_char) == Tokens.CHAR_LITERAL);
	}

	/**
	 * Verifica se o argumento passado é um numerico.
	 */
	private final boolean NumericoVerificar(final String as_leitura) {
		boolean lb_numerico = false;

		/*
		 * Verifica se o numero é somente inteiro.
		 */
		for (char lc_char : as_leitura.toCharArray()) {
			lb_numerico = TipoCharVerificar(lc_char) == Tokens.CHAR_NUMERICO && lc_char != '-';
		}

		return (lb_numerico);
	}

	/**
	 * Verifica se o argumento passado é uma palavra reservada.
	 */
	private final boolean PalavraReservadaVerificar(final String leitura) {
		return (token.isSimbolo(leitura));
	}

	/**
	 * Identifica que esta palavra é um Identificador.
	 */
	private final boolean IdentificadorVerificar(final String as_leitura) {
		return (as_leitura.trim().length() > 0 && TipoCharVerificar(as_leitura.trim().charAt(0)) == Tokens.CHAR_ALFA
				&& as_leitura.trim().length() <= 30);
	}

	/**
	 * Verifica se o caracter passado é um alfa
	 */
	private final char TipoCharVerificar(final char ac_caracter) {
		char lc_char = ' ';

		/*
		 * Se for de a..z ou A..Z
		 */
		if ((ac_caracter >= 65 && ac_caracter <= 90) || (ac_caracter >= 97 && ac_caracter <= 122)
				|| (ac_caracter == '_')) {
			lc_char = Tokens.CHAR_ALFA;
		}

		/*
		 * Se 0..
		 */
		else if (

		ac_caracter >= 48 && ac_caracter <= 57 || (ac_caracter == '-')

		) {
			lc_char = Tokens.CHAR_NUMERICO;
		}

		/*
		 * Se operadores quaisquer
		 */
		else if (token.isSimbolo(Character.toString(ac_caracter))) {
			lc_char = Tokens.CHAR_RELACIONAL;
		}

		/*
		 * Se outros caracteres
		 */
		else if (ac_caracter == ':' || ac_caracter == ';' || ac_caracter == ')' || ac_caracter == '('
				|| ac_caracter == '[' || ac_caracter == ']' || ac_caracter == '.' || ac_caracter == '/'
				|| ac_caracter == '*' || ac_caracter == '\\' || ac_caracter == '$' || ac_caracter == ','
				|| ac_caracter == '+' || ac_caracter == '-') {
			lc_char = Tokens.CHAR_ESPECIAL;
		}

		/*
		 * Se tipo literal..
		 */
		else if (ac_caracter == '\"') {
			lc_char = Tokens.CHAR_LITERAL;
		}

		/*
		 * Retorna o tipo do caracter..
		 */
		return (lc_char);
	}
}