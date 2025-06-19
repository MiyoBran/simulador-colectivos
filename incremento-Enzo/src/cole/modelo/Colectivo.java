package cole.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Colectivo {
    private int id;
    private Linea linea;
    private List<Pasajero> pasajeros;

    public Colectivo(int id, Linea linea) {
        this.id = id;
        this.linea = linea;
        this.pasajeros = new ArrayList<>();
    }

    public void subirPasajero(Pasajero p) {
        pasajeros.addLast(p);
    }
    public void bajarPasajero(Pasajero p) {
		pasajeros.remove(p);
	}
    
    public int getId() {
        return id;
    }

    public Linea getLinea() {
        return linea;
    }

    public List<Pasajero> getPasajeros() {
        return pasajeros;
    }

	@Override
	public int hashCode() {
		return Objects.hash(pasajeros);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Colectivo other = (Colectivo) obj;
		return Objects.equals(pasajeros, other.pasajeros);
	}

	@Override
	public String toString() {
		return "Colectivo [id=" + id + ", linea=" + linea + ", pasajeros=" + pasajeros + "]";
	}
	
    
}

