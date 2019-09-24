package net.unesc.compiladores.grafico;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.unesc.compiladores.analisador.BaseAnalisador;
import net.unesc.compiladores.analisador.lexico.AnalisadorLexico;
import net.unesc.compiladores.analisador.lexico.util.Token;
import net.unesc.compiladores.analisador.sintatico.Sintatico;
import net.unesc.compiladores.grafico.util.TableModel;
import net.unesc.compiladores.grafico.util.TextLineNumber;
import net.unesc.compiladores.grafico.util.TextPane;

public class TelaCompilador extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTable table;
	private TextPane txa_entrada_codigo;
	private JTextArea console;
	private TableModel model;

	/**
	Construtor
	*/
	public TelaCompilador() {
		setTitle("Compilador");
		setSize(650, 675);

		model = new TableModel();

		telaGraficaMontar();

		setVisible(true);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	Create the frame.
	*/
	private void telaGraficaMontar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnAbrir = new JButton("Abrir");
		menuBar.add(btnAbrir);
		
		JButton btnNovo = new JButton("Novo");
		menuBar.add(btnNovo);
		
		JButton btnExecutar = new JButton("Executar");
		btnExecutar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BaseAnalisador analisador = new AnalisadorLexico(txa_entrada_codigo.getText());
				LinkedList<Token> analise = analisador.getAnalise();
				
				if (!analisador.getErro().isEmpty()) {
					console.setText(analisador.getErro().toString());
				}
				
				analisador = new Sintatico(analise);
				analisador.getAnalise();
				
				model = new TableModel(analise);
				table.setModel(model);
			}
		});
		menuBar.add(btnExecutar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.13);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane);
		
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		
		txa_entrada_codigo = new TextPane();
		scrollPane.setViewportView(txa_entrada_codigo);
		
		TextLineNumber contadorLinhas = new TextLineNumber(txa_entrada_codigo);
		scrollPane.setRowHeaderView(contadorLinhas);
		
		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.15);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panel_1.add(splitPane_1);
		
		JPanel panel_2 = new JPanel();
		splitPane_1.setLeftComponent(panel_2);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel_2.add(scrollPane_2);
		
		table = new JTable();
		table.setModel(model);
		scrollPane_2.setViewportView(table);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Console", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		splitPane_1.setRightComponent(panel_3);
		panel_3.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_3.add(scrollPane_1);
		
		console = new JTextArea();
		console.setEditable(false);
		scrollPane_1.setViewportView(console);
	}
}