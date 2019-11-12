package net.unesc.compiladores.grafico.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import net.unesc.compiladores.grafico.TelaCompilador;

public class Button extends JButton {
	private static final long serialVersionUID = 1L;

	public Button() {
		super();
	}

	public Button(String title, TelaCompilador t) {
		super(title);

		addActionListener(e -> {
			switch (e.getActionCommand()) {
				case "Novo": {
	
					break;
				}
				
				case "Abrir": {
					
					break;
				}
				
				case "Salvar": {
					
					break;
				}
				
				case "Executar": {
					
					break;
				}
	
				default: {
					break;
				}
			}

			System.out.println(e.getActionCommand());
		});
	}

	public Button(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}
}