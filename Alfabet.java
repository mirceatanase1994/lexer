
import java.util.ArrayList;
/*
	Clasa implementeaza functionalitatile unei multimi de simboluri.
	In implementarea mea, este folosita pentru V, nu pentru Sigma.
*/
public class Alfabet {
	ArrayList<Simbol> simboluri;
	public Alfabet(){
		this.simboluri = new ArrayList<Simbol>();
	}
	
	public void adaugaSimbol(Simbol t){
		this.simboluri.add(t);
	}
	
	public boolean contineSimbol(Simbol t){
		return this.simboluri.contains(t);
	}

	@Override
	public String toString() {
		return "Alfabet [simboluri=" + simboluri + "]";
	}
	public ArrayList<Simbol> getSimboluri(){
		return this.simboluri;
	}
}
