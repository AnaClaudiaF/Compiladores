package net.unesc.compiladores.analisador.lexico;
import java.util.ArrayList;
import net.unesc.hal.controllers.FiniteAutomaton;
import net.unesc.hal.analysis.lexicon.Char;
import net.unesc.hal.analysis.lexicon.Source;
import net.unesc.hal.analysis.lexicon.Token;
import net.unesc.hal.languages.HAL;

public class Lexicon {

    private final Source src;
    private HAL lang;
    private FiniteAutomaton fa;
    private ArrayList<String[]> tokens;
    private ArrayList<String[]> errors;

    public Lexicon(Source src, FiniteAutomaton fa) {
        this.src = src;
        this.fa = fa;
        this.lang = fa.getLang();
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
        run();
    }

    public ArrayList<String[]> getTokens() {
        return tokens;
    }

    public ArrayList<String[]> getErrors() {
        return errors;
    }

    private void addError(Integer line, String msg) {
        String[] adition = new String[3];
        adition[0] = line.toString();
        adition[1] = msg;
        errors.add(adition);
    }

    private void addToken(Integer line, Token token, String name) {
        String[] adition = new String[4];
        adition[0] = line.toString();
        adition[1] = token.getCode().toString();
        adition[2] = token.getName();
        adition[3] = name;
        tokens.add(adition);
    }

    private String parseBuffer(ArrayList<Char> buffer) {
        String out = "";

        for (int i = 0; i < buffer.size(); i++) {
            out += buffer.get(i).getChar();
        }
        return out;
    }

    private void run() {
        int cur_line = 1;
        int car = 0;
        boolean is_comment_loop = false;
        boolean is_literal_loop = false;
        int literal_count = 0;
        int literal_line = 0;

        ArrayList<Char> chars = src.getChars();

        Char cur_char;

        ArrayList<Char> buffer = new ArrayList<>();

        while (car < chars.size()) {
            cur_char = chars.get(car);

            // Integer
            while (cur_char.isNum() && !is_comment_loop && !is_literal_loop) {
                buffer.add(cur_char);
                if (car < chars.size()) {
                    cur_char = chars.get(++car);
                }
            }

            if (!buffer.isEmpty() && !is_comment_loop && !is_literal_loop) {
                Token token = lang.getTerminal(parseBuffer(buffer));
                if (token != null) {
                    addToken(cur_line, token, parseBuffer(buffer));
                } else {
                    Integer inteiro = new Integer(parseBuffer(buffer));
                    if (inteiro >= -32767 && inteiro <= 32767) {
                        addToken(cur_line, lang.INTEGER, parseBuffer(buffer));
                    } else {
                        this.addError(cur_line, "Limite excedido");
                        break;
                    }
                }
                buffer.clear();
            }

            if (!is_comment_loop && !is_literal_loop && !cur_char.isSymbol() && !cur_char.isEndFile() && !cur_char.isEndLine() && !cur_char.isLetter() && !cur_char.isNum() && !cur_char.isSpace()) {
                addError(cur_line, "Caracter " + cur_char + " desconhecido");
                break;
            }

            // Keywords
            while (cur_char.isLetter() && !is_comment_loop && !is_literal_loop) {
                buffer.add(cur_char);
                if (car < chars.size()) {
                    cur_char = chars.get(++car);
                }
            }

            // Identificador
            if (cur_char.isNum() && !is_comment_loop && !is_literal_loop) {
                while (cur_char.isNum()) {
                    buffer.add(cur_char);

                    if (car < chars.size()) {
                        cur_char = chars.get(++car);
                    }

                }
                if (!buffer.isEmpty()) {
                    //System.out.println(cur_line + " : " + parseBuffer(buffer));

                    addToken(cur_line, lang.IDENTIFIER, parseBuffer(buffer));
                    buffer.clear();
                }
            }

            if (!buffer.isEmpty() && !is_comment_loop && !is_literal_loop) {
                //System.out.println(cur_line + " : " + parseBuffer(buffer));
                Token token = lang.getTerminal(parseBuffer(buffer));
                if (token != null) {
                    addToken(cur_line, token, parseBuffer(buffer));
                } else {
                    if (parseBuffer(buffer).length() <= 30) {
                        addToken(cur_line, lang.IDENTIFIER, parseBuffer(buffer));
                    } else {
                        this.addError(cur_line, "Limite excedido");
                        break;
                    }
                }
                buffer.clear();
            }
            // Operadores Aritiméticos, Sinais Relacionais, Simbolos Especiais, Dígitos negativos, Literais

            if (cur_char.isSymbol()) {
                buffer.add(cur_char);

                if (car < chars.size()) {
                    cur_char = chars.get(++car);

                    if (lang.getTerminal(parseBuffer(buffer)) != null && !is_literal_loop) {

                        // Dígitos negativos
                        if (lang.getTerminal(parseBuffer(buffer)).getCode() == 31 && cur_char.isNum() && !is_comment_loop) {
                            buffer.add(cur_char);
                            cur_char = chars.get(++car);

                            while (cur_char.isNum()) {
                                buffer.add(cur_char);
                                if (car < chars.size()) {
                                    cur_char = chars.get(++car);
                                } else {
                                    break;
                                }
                            }

                            if (cur_char.isLetter()) {
                                addError(cur_line, "Erro ao processar sequência de caracteres");
                                break;
                            } else {
                                Integer inteiro = new Integer(parseBuffer(buffer));
                                if (inteiro >= -32767 && inteiro <= 32767) {
                                    addToken(cur_line, lang.INTEGER, parseBuffer(buffer));
                                    buffer.clear();
                                } else {
                                    this.addError(cur_line, "Limite excedido");
                                    break;
                                }
                            }

                        }

                        if (lang.getTerminal(parseBuffer(buffer) + cur_char.getChar()) != null && !is_comment_loop && !is_literal_loop) {
                            buffer.add(cur_char);
                            cur_char = chars.get(++car);
                        }

                        // Comentários
                        if ((parseBuffer(buffer) + cur_char.getChar()).equals(lang.COMMENT_BLOCK_BEGIN) && !is_comment_loop && !is_literal_loop) {
                            is_comment_loop = true;
                            cur_char = chars.get(++car);
                            buffer.clear();
                        }

                        if ((parseBuffer(buffer) + cur_char.getChar()).equals(lang.COMMENT_BLOCK_END) && !is_literal_loop) {
                            is_comment_loop = false;
                            cur_char = chars.get(++car);
                            buffer.clear();
                        }
                    }

                    // Literal
                    if (parseBuffer(buffer).equals(lang.LITERAL_END) && !is_comment_loop && is_literal_loop) {
                        is_literal_loop = false;
                        if (literal_count <= lang.LITERAL_LIMIT) {
                            addToken(literal_line, lang.LITERAL, parseBuffer(buffer));
                        } else {
                            addError(cur_line, "Limite de literal excedido");
                            break;
                        }

                        literal_count = 0;
                        buffer.clear();
                    }

                    if (parseBuffer(buffer).equals(lang.LITERAL_BEGIN) && !is_comment_loop && !is_literal_loop) {
                        literal_line = cur_line;
                        is_literal_loop = true;
                        buffer.clear();
                    }

                }
                if (is_comment_loop || is_literal_loop) {
                    literal_count++;
                    //cur_char = chars.get(++car);
                    buffer.clear();
                }
            }
            if (is_comment_loop || is_literal_loop) {
                while (!cur_char.isSymbol() && !cur_char.isEndFile() && !cur_char.isEndLine() && !cur_char.isSpace()) {
                    //System.out.println(cur_line + " : " + cur_char.getChar());
                    literal_count++;
                    if (car < chars.size()) {
                        cur_char = chars.get(++car);
                    }
                }
            }

            if (!buffer.isEmpty() && !is_comment_loop && !is_literal_loop) {
                //System.out.println(cur_line + " : " + parseBuffer(buffer));
                Token token = lang.getTerminal(parseBuffer(buffer));
                if (token != null) {
                    addToken(cur_line, token, parseBuffer(buffer));
                } else {
                    //System.out.println("Simbolo inexistente: " + parseBuffer(buffer));
                }
                buffer.clear();
            }

            // Controle do pointeiro
            if (cur_char.isEndLine()) {
                if (car < chars.size()) {
                    cur_char = chars.get(++car);
                }
                cur_line++;
            }

            // Identifica o final do arquivo
            if (cur_char.isEndFile()) {
                if (is_comment_loop) {
                    this.addError(cur_line, "Comentário sem fechamento");
                    break;
                }
                if (is_literal_loop) {
                    this.addError(cur_line, "Literal sem fechamento");
                    break;
                }
                addToken(cur_line, lang.EOF, "EOF");
                break;
            }
            while (cur_char.isSpace()) {
                if (car < chars.size()) {
                    cur_char = chars.get(++car);
                    if (is_literal_loop) {
                        literal_count++;
                    }
                }
            }
        }

    }

}