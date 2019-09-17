package net.unesc.compiladores.analisador;

public class Erro {
	private String erro;
	private int linha;

	public Erro(String erro, int linha) {
		this.erro = erro;
		this.linha = linha;
	}

	public String getErro() {
		return erro;
	}

	public int getLinha() {
		return linha;
	}

	@Override
	public String toString() {
		return "{" + "erro: " + erro + " linha: " + linha + "}";
	}
}