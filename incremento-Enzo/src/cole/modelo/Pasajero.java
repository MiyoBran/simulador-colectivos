package cole.modelo;

import java.util.Objects;

public class Pasajero {
    private int id;
    private Parada destino;
    private String linea;

    public Pasajero(int id, Parada destino, String linea) {
        this.id = id;
        this.destino = destino;
        this.linea = linea; 
    }
    
    public String getLinea() {
        return linea;
    }
    
    public int getId() {
        return id;
    }

    public Parada getDestino() {
        return destino;
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
		Pasajero other = (Pasajero) obj;
		return id == other.id;
	}
    
}

