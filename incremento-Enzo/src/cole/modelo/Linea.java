package cole.modelo;

import java.util.ArrayList;
import java.util.Objects;

public class Linea {
    private String codigo;
    private ArrayList<Parada> paradas;

    public Linea(String codigo) {
        this.codigo = codigo;
        this.paradas = new ArrayList<>();
    }

    public void agregarParada(Parada parada) {
        paradas.addLast(parada);
    }

    public String getCodigo() {
        return codigo;
    }

    public ArrayList<Parada> getParadas() {
        return paradas;
    }
    
    
	@Override
	public int hashCode() {
		return Objects.hash(codigo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Linea other = (Linea) obj;
		return Objects.equals(codigo, other.codigo);
	}

	@Override
	public String toString() {
		return "Linea [codigo=" + codigo + ", paradas=" + paradas + "]";
	}
    
    
    
}
