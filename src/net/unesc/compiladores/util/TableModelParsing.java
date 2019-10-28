package net.unesc.compiladores.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.unesc.compiladores.analisador.lexico.util.Token;
import net.unesc.compiladores.analisador.sintatico.parsing.Parsing;

public class TableModelParsing extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<Parsing> token;

	private String[] colunas = new String[] {
			"Tokens"
	};
	
//	private static final int CODIGO = 0;
	private static final int PALAVRA = 0;
	
	public TableModelParsing() {
		token = new ArrayList<>();
	}
	
	public TableModelParsing(List<Parsing> token) {
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
//		case CODIGO:
//			return String.class;

		case PALAVRA:
			return String.class;		
			
		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Parsing token = this.token.get(rowIndex);
		
		switch (columnIndex) {
//		case CODIGO:
//			return token.getCodigo();
			
		case PALAVRA:
			return token.getDescricao();

		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Parsing token = this.token.get(rowIndex);
		
		switch (columnIndex) {
//		case CODIGO:
//			token.setCodigo((Integer) aValue);
//			break;
			
		case PALAVRA:
			token.setDescricao((String) aValue);
			break;

		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
		
		fireTableCellUpdated(rowIndex, columnIndex);
	}
}