
public class Neterminal extends Simbol {
	public boolean useless;

	public Neterminal(char prZis){
		super(prZis);
	}


	@Override
	public String toString() {
		return "Neterminal ["+ prZis +"]";
	}
}
