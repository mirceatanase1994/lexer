
import java.util.ArrayList;

public class Gramatica {
	Alfabet V; //reprezinta toata multimea de simboluri, nu doar alfabetul sigma
	ArrayList<Neterminal> VfaraSigma; //multimea de neterminali	
	ArrayList<Terminal> Sigma;
	ArrayList<Regula> R; //multimea de reguli
	Neterminal S;
	ArrayList<Neterminal> useful;
	ArrayList<Neterminal> useless;
	ArrayList<Neterminal> canReachVoid;
	boolean correctSyntax;
	boolean correctSemantic;
	public Gramatica() {
		super();
		this.VfaraSigma = new ArrayList<Neterminal>();
		this.R = new ArrayList<Regula>();
		this.V = new Alfabet();
		this.Sigma = new ArrayList<Terminal>();
		this.useful = new ArrayList<Neterminal>();
		this.useless = new ArrayList<Neterminal>();
		this.canReachVoid = new ArrayList<Neterminal>();
		this.correctSyntax = true;
		this.correctSemantic = true;
	}
	
	//adauga in V
	public void adaugaSimbol(Terminal t){
		this.V.adaugaSimbol(t);
	}

	public void adaugaInSigma(Terminal t){
		this.Sigma.add(t);
	}
	
	//adauga atat in V, cat si in VfaraSigma
	public void adaugaSimbol(Neterminal n){
		this.V.adaugaSimbol(n);
		this.VfaraSigma.add(n);
	}
	
	public void setStart(Neterminal start){
		this.S = start;
	}
	
	public void adaugaRegula(Regula r){
		this.R.add(r);
	}

	public boolean contineInV(Simbol s){
		return this.V.contineSimbol(s);
	}

	public boolean contineInSigma(Terminal t){
		return this.Sigma.contains(t);
	}

	//contine in VfaraSigma
	public boolean contineNeterminal(Neterminal t){
		return this.VfaraSigma.contains(t);
	}

	@Override
	public String toString() {
		return "Gramatica [V=" + V + ",\n VfaraSigma=" + VfaraSigma + ",\n R=" + R + ",\n S=" + S + "]";
	}
	
	//marcheaza Neterminali care nu sunt useless, dupa algoritmul sugerat
	public void markUseful() {
		boolean done = false;
		while (!done) {
			done = true;
			for (Regula r : this.R){
				boolean onlyTerminals = true;
				for (Simbol s: r.dreapta){
					if ((s instanceof Neterminal)  && (!useful.contains(s))){
						onlyTerminals = false;
					}
				}
				if (onlyTerminals){
					//daca sirul din dreapta contine numai Terminali sau
					//neterminali uitili
					if (!useful.contains(r.stanga)){
						useful.add(r.stanga);
						done = false;
					}
				}
			}
		}
	}

	//toti neterminalii care nu sunt utili sunt adaugati in lista useless
	public void markUseless() {
		markUseful();
		for (Neterminal n:VfaraSigma){
			if (!useful.contains(n)){
				useless.add(n);
			}
		}
	}

	//debug
	public void printUseless() {
		for (Neterminal n: this.useless){
			System.out.println(n.prZis);
		}
	}

	//sterge regulile al caror neterminal de derivat este inutil
	public void removeUselessRules(){
		markUseless();
		for (int i = 0; i<this.R.size(); i++){
			if (useless.contains(this.R.get(i).stanga)){
				this.R.remove(i);
				i--;
			}
		}
	}

	//creaza lista canReachVoid, in care se afla neterminalii din 
	//care se poate deriva sirul vid
	public void markVoidReach() {
		//pentru reducerea timpului, eliminam neterminalii inutili intai
		removeUselessRules();
		boolean done = false;
		while (!done) {
			done = true;
			for (Regula r : this.R){
				boolean canReachE = true;
				for (Simbol s: r.dreapta){
					if (((s instanceof Neterminal)  && (!canReachVoid.contains(s))) ||
						((s instanceof Terminal) && (s.prZis!= 'e'))){
						canReachE = false;
					}
				}
				if (canReachE){
					//daca regula este de tipul (Neterminal->e) sau partea ei dreapta
					//e formata doar din Neterminali adaugati deja in canReachVoid
					if (!canReachVoid.contains(r.stanga)){
						canReachVoid.add(r.stanga);
						done = false;
					}
				}
			}
		}
	}
	
}
