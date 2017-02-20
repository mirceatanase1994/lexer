%%

%class Flexer
%unicode
/*%debug*/
%int
%line
%column

%{

    Gramatica g = new Gramatica();
    Regula r;

    Gramatica getResult() {
        return g;
    }
    /*
        Erorile semantice

        Numerotarea se face in ordinea in care au fost prezentate in enunt

    */
    void checkSemanticError2(){
        //toti terminalii din V sunt in sigma
        //System.out.println("Incep sa verific eroarea 2");
        for (Simbol s:g.V.getSimboluri()){
            //System.out.println("Verific simbolul "+s);
            if (s instanceof Terminal){
                if (!g.contineInSigma((Terminal)s)){
                    g.correctSemantic = false;
                }
            }
        }
    }

    void checkSemanticError5(char c){
        if (!g.contineInV(new Simbol(c))){
            g.correctSemantic = false;
        }
    }

    void checkSemanticError3(char c){
        if (!g.contineNeterminal(new Neterminal(c))){
            g.correctSemantic = false;
        }
    }

    void checkSemanticError1(char c){
        if (!g.contineInV(new Terminal(c))){
            g.correctSemantic = false;
        }
    }
    void checkSemanticError4(char c){
        if (!g.contineNeterminal(new Neterminal(c))){
            g.correctSemantic = false;
        }
    }
%}


LineTerminator = \r|\n|\r\n
WS = {LineTerminator} | [ \t\f]
other = "'" | "-" | "=" | "[" | "]" | ";" | "`" | "." | "/" | "~" | "!" | "@" | "#" | "$" | "%" | "^" | "&" | "*" | "=" | "+" | ":" | "|" | "<" | ">" | "_" | "\"" | "?"
lower = [a-d] | [f-z]
upper = [A-Z]
Nonterminal = {upper}
Terminal = {lower} | [:digit:] | {other}
Symbol = {Terminal} | {Nonterminal}


%state READVI READV VIRGULAV VIRGULAEXT1 READSIGMAI READSIGMA VIRGULASIGMA VIRGULAEXT2 REGULII REGULI REGULASTANGA VIRGULAREGULA REGULADREAPTA VIRGULAREGULI VIRGULAEXT3 READSTART READLAST CHECKSIGMAVID CHECKREGULIVID REGULAE SFARSITREGULA
/*
    READVI: citeste prima acolada din V
    READV: citeste cate un Simbol din V pana la "}"
    VIRGULAV: citeste virgulele din interiorul V
    VIRGULAEXT1: citeste virgula dintre V si Sigma
    READSIGMAI: citeste prima acolada din Sigma
    READSIGMA: citeste cate un Simbol din Sigma pana la "}"
    VIRGULASIGMA: citeste separatorii din Sigma
    VIRGULAEXT2: citeste virgula dintre Sigma si R
    REGULII:citeste prima acolada din R
    REGULI:incearca sa inceapa citirea unei Reguli (inghite "("), pana la "}"
    REGULASTANGA: citeste Neterminalul de derivat din regula
    VIRGULAREGULA: citeste separatorul din interiorul unei reguli
    REGULADREAPTA: citeste sirul din partea dreapta a regulii, pana la ")"
    VIRGULAREGULI: citeste separatorul din multimea de reguli
    VIRGULAEXT3: citeste separatorul dintre R si S
    READSTART: citeste Neterminalul de start
    READLAST: citeste ultima ")"
    CHECKSIGMAVID: dupa READSIGMAI, verifica daca Sigma e vida
    CHECKREGULIVID: dupa REGULII, verifica daca R e vida
    REGULAE: dupa VIRGULAREGULA, verifica daca avem o regula de tip Neterminal->e
    SFARSITREGULA: dupa REGULAE, necesara deoarece nu trebuie sa avem alte simboluri
        in partea dreapta a regulii, dupa e
*/
%%
{WS}    {/*skip whitespace*/} 

<YYINITIAL>"(" {
    yybegin(READVI);
}

<READVI> "{"    {
    yybegin(READV);
}

<READV>{
    {Terminal} {
        String symbol = yytext();
        g.adaugaSimbol(new Terminal(symbol.charAt(0)));
        yybegin(VIRGULAV);
    }
    {Nonterminal} {
        String symbol = yytext();
        g.adaugaSimbol(new Neterminal(symbol.charAt(0)));
        yybegin(VIRGULAV);
    }
}

<VIRGULAV>{
    "," {
        yybegin(READV);
    }
    "}" {
        yybegin(VIRGULAEXT1);
    }
}

<VIRGULAEXT1>{
    "," {
        yybegin(READSIGMAI);
    }
}

<READSIGMAI>"{" {
    yybegin(CHECKSIGMAVID);
} 

<CHECKSIGMAVID> {
    "}" {
        checkSemanticError2();
        yybegin(VIRGULAEXT2);

    } 
    {Terminal} {
        
        String symbol = yytext();
        
        g.adaugaInSigma(new Terminal(symbol.charAt(0)));
        yybegin(VIRGULASIGMA);

    }
}

<READSIGMA> {
    {Terminal} {
        String symbol = yytext();
        checkSemanticError1(symbol.charAt(0));
        g.adaugaInSigma(new Terminal(symbol.charAt(0)));
        yybegin(VIRGULASIGMA);
    }
}

<VIRGULASIGMA>{
     "," {
        yybegin(READSIGMA);
    }
    "}" {
        checkSemanticError2();
        yybegin(VIRGULAEXT2);
    }    
}

<VIRGULAEXT2>"," {
    yybegin(REGULII);
}

<REGULII> "{" {
    yybegin(CHECKREGULIVID);
}

<CHECKREGULIVID> {
    "}" {
        yybegin(VIRGULAEXT3);
    }
    "(" {
        r = new Regula();
        yybegin(REGULASTANGA);
    }
}

<REGULI>{ 
    "(" {
        r = new Regula();
        yybegin(REGULASTANGA);
    }
    
}

<REGULASTANGA> {Nonterminal} {
    String symbol = yytext();
    checkSemanticError4(symbol.charAt(0));
    r.setStanga(new Neterminal(symbol.charAt(0)));
    yybegin(VIRGULAREGULA);
}

<VIRGULAREGULA> "," {
    yybegin(REGULAE);
}

<REGULAE> {
    "e" {
        yybegin(SFARSITREGULA);
    }
    {Terminal} {
        String symbol = yytext();


        r.concatDreapta(new Terminal(symbol.charAt(0)));
        yybegin(REGULADREAPTA);
    }
    {Nonterminal} {
        String symbol = yytext();
        r.concatDreapta(new Neterminal(symbol.charAt(0)));
        yybegin(REGULADREAPTA);
    }
}

<SFARSITREGULA> {
    ")" {
        try{
            Regula newR = r.copieRegula();
            g.adaugaRegula(newR);
        } catch (Exception e){} 
        yybegin(VIRGULAREGULI);
    }
}

<REGULADREAPTA>{
    {Terminal} {
        String symbol = yytext();
        checkSemanticError5(symbol.charAt(0));

        r.concatDreapta(new Terminal(symbol.charAt(0)));
    }
    {Nonterminal} {
        String symbol = yytext();
        checkSemanticError5(symbol.charAt(0));
        r.concatDreapta(new Neterminal(symbol.charAt(0)));
    }
    
    ")" {
        try{
            Regula newR = r.copieRegula();
            g.adaugaRegula(newR);
        } catch (Exception e){}    
        yybegin(VIRGULAREGULI);
    }
}

<VIRGULAREGULI>{
    "," {
        yybegin(REGULI);
    }
    "}" {
        yybegin(VIRGULAEXT3);
    }
}

<VIRGULAEXT3>{
    "," {
        yybegin(READSTART);
    }
}

<READSTART>{
    {Nonterminal} {
         String symbol = yytext();
         checkSemanticError3(symbol.charAt(0));
         g.setStart(new Neterminal(symbol.charAt(0)));
         yybegin(READLAST);
    }
}
<READLAST>{
    ")" {
        g.adaugaSimbol(new Terminal ('e'));
    }
}
//tratare erori sintactice
(.) {   
    g.correctSyntax = false;
}
