package net.unesc.compiladores.analisador.sintatico.parsing;

public class Parsing {
	private Integer codigo;
	private String descricao;
	
	public Parsing(String descricao) {
		this.descricao = descricao;
	}

	public Parsing(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
