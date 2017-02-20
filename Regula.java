
import java.util.ArrayList;

public class Regula {
	Neterminal stanga;	//neterminalul de derivat
	ArrayList<Simbol> dreapta;	//sirul de simboluri din dreapta
	public Regula() {
		super();
		this.dreapta = new ArrayList<Simbol>();
	}
	
	public void setStanga(Neterminal n){
		this.stanga = n;
	}
	
	//concateneaza un simbol la "sirul" din dreapta al regulii
	public void concatDreapta(Simbol s){
		this.dreapta.add(s);
	}

	@Override
	public String toString() {
		return "Regula [stanga=" + stanga + ", dreapta=" + dreapta + "]";
	}

	//necesara deoarece in flexer lucrez cu acelasi obiect Regula la fiecare citire
	public Regula copieRegula() throws CloneNotSupportedException {
		Regula r = new Regula();
		r.setStanga(this.stanga);
		for (Simbol s: this.dreapta){
			r.concatDreapta(s);
		}
		return r;
	}
	
}
