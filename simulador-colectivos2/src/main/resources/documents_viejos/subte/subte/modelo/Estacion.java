package subte.modelo;

public class Estacion {

	private String codigo;
	private String nombre;
	private Linea linea;
	
	public Estacion(String codigo, String nombre, Linea linea) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.linea = linea;
	}
	
	

	public String getCodigo() {
		return codigo;
	}



	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public Linea getLinea() {
		return linea;
	}



	public void setLinea(Linea linea) {
		this.linea = linea;
	}



	@Override
	public String toString() {
		return "Estacion [codigo=" + codigo + ", nombre=" + nombre + ", linea="
				+ linea + "]";
	}
	
	
	
}
