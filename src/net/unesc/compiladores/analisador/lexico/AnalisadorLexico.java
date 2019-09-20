package net.unesc.compiladores.analisador.lexico;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import net.unesc.compiladores.analisador.lexico.util.Automato;
import net.unesc.compiladores.analisador.lexico.util.Node;
import net.unesc.compiladores.analisador.lexico.util.Token;
import net.unesc.compiladores.analisador.lexico.util.Tokens;

public class AnalisadorLexico {
	private Automato source;
	private Tokens tokens;
	private List<Token> saida;

	public AnalisadorLexico(String source) {
		this.source = new Automato(source);
		this.tokens = new Tokens();
		this.saida = new ArrayList<Token>();
	}

	public List<Token> getAnaliseLexica() {
		
		LinkedBlockingDeque<Node> s = source.getArvoreDerivacao();
		StringBuilder buffer = new StringBuilder();
		
		while (!s.isEmpty()) {
			Node n = s.pop();
			
			if (n.isNumerico() || n.isAlfanumerico()) {
				buffer.append(n.getCharacter());
				
				continue;
			}
			
			if (!buffer.toString().trim().isEmpty()) {
				Token token = tokens.getCodigoToken(buffer.toString().toLowerCase());
				
				if (token != null) {
					saida.add(new Token(token.getCodigo(), token.getNome(), n.getLinha()));
				}
				
				buffer.delete(0, buffer.length());
				
				System.out.println(saida);
			}
			
			if (n.isSimbolo()) {
				buffer.append(n.getCharacter().trim().toLowerCase());
				
				n = s.pop();
				
				if (tokens.getCodigoToken(buffer.toString().toLowerCase()).getCodigo() == 31) {
					if (n.isNumerico()) {
						while (n.isNumerico()){
							buffer.append(n.getCharacter().toLowerCase());
							
							n = s.pop();
						}
					}
				}
			}
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args){
		new AnalisadorLexico
		(
			"program teste_proc1; \n"+
			"var \n"+
			"x, y, z :integer; \n"+
			"procedure p; \n"+
			"var \n"+
			"a :integer; \n"+
			"begin \n"+
			"a:=-20000;\n"+
			"readln(a); \n"+
			"if a=x then\n"+
			"z:=z+x\n"+
			"else begin \n"+
			"z:=z-x;   \n"+
			"call p; \n"+
			"end; \n"+
			"(* comentario integer .*)\n"+
			"end; \n"+
			"begin \n"+
			"z:=0; \n"+
			"readln(x,y); \n"+
			"if x>y then \n"+
			"call p \n"+
			"else  \n"+
			"z:=z+x+y; \n"+
			"writeln(z); \n"+
			"end."
		).getAnaliseLexica();
	}
}