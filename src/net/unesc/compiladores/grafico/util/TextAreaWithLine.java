package net.unesc.compiladores.grafico.util;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class TextAreaWithLine {

    private void setTextInLineNo(JTextArea textArea, int lineNo, String newText) {
        Document doc = textArea.getDocument();
        Element root = doc.getDefaultRootElement();
        Element contentEl = root.getElement(lineNo - 1);

        int start = contentEl.getStartOffset();
        int end = contentEl.getEndOffset();

        try {
            // remove words in the line (-1 to prevent removing newline character)
            doc.remove(start, end - start - 1);
            doc.insertString(start, newText, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public JComponent makeUI() {
        String str = "name : andy\n"
                + "birth : jakarta, 1 jan 1990\n"
                + "number id : 01011990 01\n"
                + "age : 26\n"
                + "study : Informatics engineering\n";

        JTextArea textArea = new JTextArea(str);
        textArea.setEditable(false);
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(textArea));
        p.add(new JButton(new AbstractAction("change line") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String lineNoStr = (String)JOptionPane.showInputDialog(
                        textArea.getRootPane(),
                        "Which line # to modify?",
                        "Line #:",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "2");
                String value = (String)JOptionPane.showInputDialog(
                        textArea.getRootPane(),
                        "What value do you want to set it to?",
                        "Value:",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "value");

                setTextInLineNo(textArea, Integer.parseInt(lineNoStr), value);
            }
        }), BorderLayout.SOUTH);
        return p;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.getContentPane().add(new TextAreaWithLine().makeUI());
            f.setSize(320, 240);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}