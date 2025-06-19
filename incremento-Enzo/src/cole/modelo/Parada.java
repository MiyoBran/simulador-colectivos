package cole.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Parada {
    private int id;
    private String direccion;
    private ArrayList<Pasajero> pasajeros;

    public Parada(int id, String direccion) {
        this.id = id;
        this.direccion = direccion;
        this.pasajeros = new ArrayList<>();
    }

    public void agregarPasajero(Pasajero p) {
        pasajeros.addLast(p);
    }

    public int getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<Pasajero> getPasajeros() {
        return pasajeros;
    }

	@Override
	public String toString() {
		return "Parada [id=" + id + ", direccion=" + direccion + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Parada other = (Parada) obj;
		return id == other.id;
	}
	

	
    
}
