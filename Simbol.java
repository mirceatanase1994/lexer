
public class Simbol {
	char prZis;

	public Simbol(char prZis) {
		super();
		this.prZis = prZis;
	}

	//necesara deoarece folosim intens contains pe listele de simboluri
	@Override
	public boolean equals(Object s){
		return (this.prZis == ((Simbol)s).prZis);
	}
}
