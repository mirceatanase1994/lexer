
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
	Se bazeaza pe scheletul din Tema0
	Citeste o gramatica cu scanner-ul generat de lex,
	printeaza erorile ,daca apar, iar daca nu
	aplica algoritmii potriviti pentru operatiile cerute
*/
public class Main {
	public static void main(String[] args) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("grammar"));
			Flexer scanner = new Flexer(br);
			scanner.yylex();
			Gramatica g = scanner.getResult();
			
			if ((!args[0].equals("--useless-nonterminals")) &&
				(!args[0].equals("--is-void")) &&
				(!args[0].equals("--has-e"))){
				System.err.println("Argument error");
			} else {
				if (!g.correctSyntax){
					System.err.println("Syntax error");
				} else {
					if (!g.correctSemantic){
						System.err.println("Semantic error");			
					} else {
						if (args[0].equals("--useless-nonterminals")){
							g.markUseless();
							g.printUseless();
						} else {
							if (args[0].equals("--is-void")){
								g.markUseless();
								if (g.useless.contains(g.S)){
									System.out.println("Yes");
								} else {
									System.out.println("No");
								}
							} else {
								if (args[0].equals("--has-e")){
									g.markVoidReach();
									if (g.canReachVoid.contains(g.S)){
										System.out.println("Yes");
									} else {
										System.out.println("No");
									}
								} 
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
