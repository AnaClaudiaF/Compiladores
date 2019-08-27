package compiladores.grafico;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import compiladores.analisador.lexico.AnalisadorLexico;
import compiladores.util.TableModel;

public class TelaCompilador extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane;
	private JTable table;
	private JTextArea textArea;
	private TableModel model;

	public TelaCompilador() {
		
		setTitle("Compilador");
		setSize(650, 675);
		
		model = new TableModel();
		
		telaGraficaMontar();
		
		setVisible(true);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void telaGraficaMontar() {
		setBounds(100, 100, 478, 498);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JButton btnNovo = new JButton("Novo");
		menuBar.add(btnNovo);
		
		JButton btnAbrir = new JButton("Abrir");
		menuBar.add(btnAbrir);
		btnAbrir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model = new TableModel(new AnalisadorLexico(textArea.getText().trim()).AnaliseLexica());
				table.setModel(model);
			}
		});
		
		JButton btnExecutar = new JButton("Executar");
		menuBar.add(btnExecutar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane);
				
		textArea = new JTextArea();
		splitPane.setLeftComponent(textArea);
		
		JScrollPane msgScroller = new JScrollPane();
		msgScroller.setViewportView(textArea);
		splitPane.add(msgScroller);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		table = new JTable();
		table.setModel(model);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setViewportView(table);
	}
}
