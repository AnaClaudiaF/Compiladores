package net.unesc.compiladores.grafico;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.unesc.compiladores.analisador.BaseAnalisador;
import net.unesc.compiladores.analisador.Erro;
import net.unesc.compiladores.analisador.lexico.AnalisadorLexico;
import net.unesc.compiladores.analisador.lexico.util.Token;
import net.unesc.compiladores.analisador.sintatico.Sintatico;
import net.unesc.compiladores.analisador.sintatico.parsing.Parsing;
import net.unesc.compiladores.analisador.sintatico.parsing.TabelaParsing;
import net.unesc.compiladores.grafico.util.TableModel;
import net.unesc.compiladores.grafico.util.TextLineNumber;
import net.unesc.compiladores.grafico.util.TextPane;
import net.unesc.compiladores.util.File;
import net.unesc.compiladores.util.TableModelParsing;
import net.unesc.compiladores.util.TableModelSemantico;

public class TelaCompilador extends JFrame {
	private static final long serialVersionUID = 1L;

	private TextPane txa_entrada_codigo;
	private JTextArea txa_console;
	private JTable tabela_lexico;
	private JTable tabela_sintatico;
	private JTable tabela_semantico;
	private TableModel model;
	private JSplitPane splitPane;
	private TableModelParsing modelParsing;
	private TableModelSemantico modelSemantico;
	/**
	 * Construtor
	 */
	public TelaCompilador() {
		setTitle("Compilador");
		setSize(650, 675);

		model = new TableModel();
		modelParsing = new TableModelParsing();
		modelSemantico = new TableModelSemantico();

		telaGraficaMontar();

		setVisible(true);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void telaGraficaMontar() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JButton btn_novo = new JButton("Novo");
		btn_novo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txa_entrada_codigo.setText("");
				model = new TableModel();
				tabela_lexico.setModel(model);
				
				modelParsing = new TableModelParsing();
				tabela_sintatico.setModel(modelParsing);
				
				modelSemantico = new TableModelSemantico();
				tabela_semantico.setModel(modelSemantico);
				
			}
		});
		menuBar.add(btn_novo);

		JButton btn_abrir = new JButton("Abrir");
		btn_abrir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txa_entrada_codigo.setText(File.SelecionarArquivo(0, -1));
			}
		});
		menuBar.add(btn_abrir);

		JButton btn_salvar = new JButton("Salvar");
		btn_salvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File.Save(txa_entrada_codigo.getText(), txa_entrada_codigo);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		menuBar.add(btn_salvar);

		JButton btn_executar = new JButton("Executar");
		btn_executar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				txa_console.setText("");
				
				BaseAnalisador analisador = new AnalisadorLexico(txa_entrada_codigo.getText());
				LinkedList<Token> token = (LinkedList<Token>) analisador.getAnalise();
				
				if (!analisador.getErro().isEmpty()) {
					for (Erro err : analisador.getErro()) {
						txa_console.append(err.getErro());
//						txa_console.paintLine(3);
					}
//					txa_console.setText(analisador.getErro().toString());
				}
					model = new TableModel(token);
					tabela_lexico.setModel(model);
					
					analisador = new Sintatico(token);
					LinkedList<Parsing> sintatico = (LinkedList<Parsing>) analisador.getAnalise();
					
					if (!analisador.getErro().isEmpty()) {
						for (Erro err : analisador.getErro()) {
							txa_console.append(err.getErro());
//							txa_console.paintLine(3);
						}
//						txa_console.setText(analisador.getErro().toString());
					}
					
					modelParsing = new TableModelParsing(sintatico);
					tabela_sintatico.setModel(modelParsing);
					
					if (analisador.getSemantico() != null && analisador.getSemantico().getErro() != null && !analisador.getSemantico().getErro().isEmpty()) {
						for (Erro err : analisador.getSemantico().getErro()) {
							txa_console.append(err.getErro());
//							txa_console.paintLine(3);
						}
					}
					
					modelSemantico = new TableModelSemantico(analisador.getSemantico().getTabelaSimbolo());
					tabela_semantico.setModel(modelSemantico);
			}
		});
		menuBar.add(btn_executar);

		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		splitPane = new JSplitPane();
		splitPane.setDividerLocation(900);
		splitPane.setDividerSize(10);
		splitPane.setOneTouchExpandable(true);
		getContentPane().add(splitPane);

		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel_2.add(scrollPane);

		txa_entrada_codigo = new TextPane();
		txa_entrada_codigo.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollPane.setViewportView(txa_entrada_codigo);
		
		TextLineNumber contadorLinhas = new TextLineNumber(txa_entrada_codigo);
		scrollPane.setRowHeaderView(contadorLinhas);

		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));

		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.7);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panel_3.add(splitPane_1);

		JPanel panel_4 = new JPanel();
		splitPane_1.setLeftComponent(panel_4);
		panel_4.setLayout(new GridLayout(1, 0, 0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_4.add(tabbedPane);

		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("L�xico", null, panel_6, null);
		panel_6.setLayout(new GridLayout(1, 0, 0, 0));

		JScrollPane scrollPane_2 = new JScrollPane();
		panel_6.add(scrollPane_2);

		tabela_lexico = new JTable();
		tabela_lexico.setModel(model);
		scrollPane_2.setViewportView(tabela_lexico);

		JPanel panel_7 = new JPanel();
		tabbedPane.addTab("Sint�tico", null, panel_7, null);
		panel_7.setLayout(new GridLayout(1, 0, 0, 0));

		JScrollPane scrollPane_3 = new JScrollPane();
		panel_7.add(scrollPane_3);

		tabela_sintatico = new JTable();
//		tabela_sintatico.setModel(null);
		scrollPane_3.setViewportView(tabela_sintatico);
		
		JPanel panel_8 = new JPanel();
		tabbedPane.addTab("Semantico", null, panel_8, null);
		panel_8.setLayout(new GridLayout(1, 0, 0, 0));

		JScrollPane scrollPane_4 = new JScrollPane();
		panel_8.add(scrollPane_4);

		tabela_semantico = new JTable();
//		tabela_sintatico.setModel(null);
		scrollPane_4.setViewportView(tabela_semantico);

		JPanel panel_5 = new JPanel();
		splitPane_1.setRightComponent(panel_5);
		panel_5.setLayout(new GridLayout(0, 1, 0, 0));

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_5.add(scrollPane_1);

		txa_console = new JTextArea();
		scrollPane_1.setViewportView(txa_console);
		txa_console.setEditable(false);
	}
}