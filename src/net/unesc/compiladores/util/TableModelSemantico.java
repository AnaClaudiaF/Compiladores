package net.unesc.compiladores.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.unesc.compiladores.analisador.semantico.Simbolos;

public class TableModelSemantico extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<Simbolos> simbolos;

	private String[] colunas = new String[] {
			"Nome", "Categoria", "Tipo", "Nivel"
	};
	
//	private static final int CODIGO = 0;
	private static final int NOME = 0;
	private static final int CATEGORIA = 1;
	private static final int TIPO = 2;
	private static final int NIVEL = 3;
	
	public TableModelSemantico() {
		simbolos = new ArrayList<>();
	}
	
	public TableModelSemantico(List<Simbolos> token) {
		this.simbolos = new ArrayList<>(token);
	}

	@Override
	public int getRowCount() {
		return simbolos.size();
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

		case NOME:
			return String.class;		
		case CATEGORIA:
			return String.class;		
		case TIPO:
			return String.class;		
		case NIVEL:
			return String.class;		
			
		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Simbolos token = this.simbolos.get(rowIndex);
		
		switch (columnIndex) {
//		case CODIGO:
//			return token.getCodigo();
			
		case NOME:
			return token.getNome();
		case CATEGORIA:
			return token.getCategoria();
		case TIPO:
			return token.getTipo();
		case NIVEL:
			return token.getNivel();

		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Simbolos token = this.simbolos.get(rowIndex);
		
		switch (columnIndex) {
//		case CODIGO:
//			token.setCodigo((Integer) aValue);
//			break;
			
		case NOME:
			token.setNome((String) aValue);
			break;
		case CATEGORIA:
			token.setCategoria((String) aValue);
			break;
		case TIPO:
			token.setTipo((String) aValue);
			break;
		case NIVEL:
			token.setNivel((Integer) aValue);
			break;

		default:
			throw new IndexOutOfBoundsException("columnIndex out of bounds");
		}
		
		fireTableCellUpdated(rowIndex, columnIndex);
	}
}