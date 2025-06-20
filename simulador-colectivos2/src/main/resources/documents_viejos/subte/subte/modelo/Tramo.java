package subte.modelo;

public class Tramo {

	private Estacion estacion1;
	private Estacion estacion2;
	private int tiempo;
	private int tipo; 
	
	public Tramo(Estacion estacion1, Estacion estacion2, int tiempo, int tipo) {
		super();
		this.estacion1 = estacion1;
		this.estacion2 = estacion2;
		this.tiempo = tiempo;
		this.tipo = tipo;
	}

	public Estacion getEstacion1() {
		return estacion1;
	}

	public void setEstacion1(Estacion estacion1) {
		this.estacion1 = estacion1;
	}

	public Estacion getEstacion2() {
		return estacion2;
	}

	public void setEstacion2(Estacion estacion2) {
		this.estacion2 = estacion2;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Tramo [estacion1=" + estacion1 + ", estacion2=" + estacion2
				+ ", tiempo=" + tiempo + ", tipo=" + tipo + "]";
	}
	
	
}
