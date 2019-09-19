package net.unesc.compiladores.grafico.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.unesc.compiladores.analisador.lexico.util.Token;

public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<Token> token;

	private String[] colunas = new String[] {
			"Código", "Palavra", "Linha"
	};
	
	private static final int CODIGO = 0;
	private static final int PALAVRA = 1;
	private static final int LINHA = 2;
	
	public TableModel() {
		token = new ArrayList<>();
	}
	
	public TableModel(List<Token> token) {
		this.token = new ArrayList<>(token);
	}

	@Override
	public int getRowCount() {
		return token.size();
	}

	@Override
	public int getColumnCount() {
		return colunas.length;
	}
	
	@Override
	public String getColumnName(int column) {
		return colunas[column];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case CODIGO:
			return String.class;

		case PALAVRA:
			return String.class;
			
		case LINHA:
			return String.class;
			
		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Token token = this.token.get(rowIndex);
		
		switch (columnIndex) {
		case CODIGO:
			return token.getCodigo();
			
		case PALAVRA:
			return token.getNome();
		
		case LINHA:
			return token.getLinha();

		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Token token = this.token.get(rowIndex);
		
		switch (columnIndex) {
		case CODIGO:
			token.setCodigo((Integer) aValue);
			break;
			
		case PALAVRA:
			token.setNome((String) aValue);
			break;
		
		case LINHA:
			token.setLinha((Integer) aValue);

		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
		
		fireTableCellUpdated(rowIndex, columnIndex);
	}
}