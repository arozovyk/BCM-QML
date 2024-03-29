package fr.sorbonne_u.components.qos.solver.booleval.lexer;

import java.io.*;

public class Lexer {
	private StreamTokenizer input;

	private int symbol = NONE;
	public static String lastWord;
	public static final int EOL = -3;
	public static final int EOF = -2;
	public static final int INVALID = -1;

	public static final int NONE = 0;

	public static final int OR = 1;
	public static final int AND = 2;
	public static final int NOT = 3;

	public static final int TRUE = 4;
	public static final int LEFT = 6;
	public static final int RIGHT = 7;


	public Lexer(InputStream in) {
		Reader r = new BufferedReader(new InputStreamReader(in));
		input = new StreamTokenizer(r);

		input.resetSyntax();
		input.wordChars('a', 'z');
		input.wordChars('0', '9');
		input.wordChars('A', 'Z');
		input.whitespaceChars('\u0000', ' ');
		input.whitespaceChars('\n', '\t');

		input.ordinaryChar('(');
		input.ordinaryChar(')');
		input.ordinaryChar('&');
		input.ordinaryChar('|');
		input.ordinaryChar('!');

	}


	public int nextSymbol() {
		try {
			int d =input.nextToken();
			switch (d) {
				case StreamTokenizer.TT_EOL:
					symbol = EOL;
					break;
				case StreamTokenizer.TT_EOF:
					symbol = EOF;
					break;
				case StreamTokenizer.TT_WORD: { //change here for literals;
					if (input.sval.substring(0,1).equalsIgnoreCase("c")) {
						lastWord=input.sval;
						symbol = TRUE;
					}
					break;
				}
				case '(':
					symbol = LEFT;
					break;
				case ')':
					symbol = RIGHT;
					break;
				case '&':
					symbol = AND;
					break;
				case '|':
					symbol = OR;
					break;
				case '!':
					symbol = NOT;
					break;
				default:
					symbol = INVALID;
			}
		} catch (IOException e) {
			symbol = EOF;
		}

		return symbol;
	}

	public String toString() {
		return input.toString();
	}

	public static void main(String[] args) {
		String expression = "true & ((true | false) & !(true & false))";
		Lexer l = new Lexer(new ByteArrayInputStream(expression.getBytes()));
		int s;
		while ( (s = l.nextSymbol()) != Lexer.EOF) if(s != EOL) System.out.printf("%s -> %s\n", l, s);
	}
}
