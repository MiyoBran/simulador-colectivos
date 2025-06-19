package ar.edu.unpsjb.ayp2.proyectointegrador.modelo;

import java.util.ArrayList; // Para usar como List
import java.util.List;      // Interfaz List de java.util
import java.util.Objects;

public class LineaColectivo {
    private String id;
    private String nombre;
    private List<Parada> recorrido; // Secuencia ordenada de paradas, usando ArrayList

    /**
     * Constructor principal.
     * @param id El identificador único de la línea.
     * @param nombre El nombre descriptivo de la línea.
     * @throws IllegalArgumentException si id o nombre son nulos o vacíos.
     */
    public LineaColectivo(String id, String nombre) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la línea no puede ser nulo o vacío.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la línea no puede ser nulo o vacío.");
        }
        this.id = id;
        this.nombre = nombre;
        this.recorrido = new ArrayList<>(); // Usamos ArrayList
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve una copia de la lista de paradas del recorrido.
     * @return Una nueva lista (ArrayList) con las paradas del recorrido.
     */
    public List<Parada> getRecorrido() {
        return new ArrayList<>(this.recorrido); // Constructor de copia de ArrayList
    }

    /**
     * Añade una parada al final del recorrido de la línea.
     * @param parada La parada a añadir.
     * @throws IllegalArgumentException si la parada es nula.
     */
    public void agregarParadaAlRecorrido(Parada parada) {
        if (parada == null) {
            throw new IllegalArgumentException("No se puede agregar una parada nula al recorrido.");
        }
        // Evitar paradas duplicadas consecutivas (opcional)
        if (!this.recorrido.isEmpty() && this.recorrido.get(this.recorrido.size() - 1).equals(parada)) {
            System.err.println("Advertencia: Se intentó agregar la misma parada consecutiva: " + parada.getId());
            return;
        }
        this.recorrido.add(parada); // add para ArrayList
    }
    
    /**
     * Obtiene una parada del recorrido por su índice.
     * @param indice El índice de la parada (0-based).
     * @return La parada en el índice especificado.
     * @throws IndexOutOfBoundsException si el índice está fuera de rango.
     */
    public Parada getParadaPorIndice(int indice) {
        if (indice < 0 || indice >= this.recorrido.size()) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice + ". Tamaño del recorrido: " + this.recorrido.size());
        }
        return this.recorrido.get(indice); // Acceso directo con ArrayList
    }

    /**
     * Devuelve la primera parada del recorrido.
     * @return La primera parada, o null si el recorrido está vacío.
     */
    public Parada getPrimeraParada() {
        if (this.recorrido.isEmpty()) {
            return null;
        }
        return this.recorrido.get(0);
    }

    /**
     * Devuelve la última parada del recorrido.
     * @return La última parada, o null si el recorrido está vacío.
     */
    public Parada getUltimaParada() {
        if (this.recorrido.isEmpty()) {
            return null;
        }
        return this.recorrido.get(this.recorrido.size() - 1);
    }
    
    /**
     * Devuelve la cantidad de paradas en el recorrido.
     * @return El número de paradas.
     */
    public int longitudRecorrido() {
        return this.recorrido.size();
    }

    /**
     * Verifica si una parada específica forma parte del recorrido de la línea.
     * @param parada La parada a verificar.
     * @return true si la parada está en el recorrido, false en caso contrario.
     */
    public boolean contieneParada(Parada parada) {
        if (parada == null) {
            return false;
        }
        return this.recorrido.contains(parada); // ArrayList tiene un contains eficiente
    }
    
    /**
     * Encuentra el índice de una parada en el recorrido.
     * @param parada La parada cuyo índice se busca.
     * @return El índice de la parada (0-based) o -1 si no está en el recorrido.
     */
    public int getIndiceParada(Parada parada) {
        if (parada == null) {
            return -1;
        }
        return this.recorrido.indexOf(parada); // ArrayList tiene indexOf
    }
    
    /**
     * Encuentra la parada siguiente a la parada dada en el recorrido.
     * @param parada La parada actual.
     * @return La siguiente parada en el recorrido o null si la parada dada no está
     *         en el recorrido o es la última.
     */
    public Parada getSiguienteParada(Parada parada) {
        int indice = getIndiceParada(parada);
        if (indice == -1 || indice >= this.recorrido.size() - 1) {
            return null;
        }
        return getParadaPorIndice(indice + 1);
    }
    
    /**
     * Determina si la parada dada es la terminal (última del recorrido).
     * @param parada La parada a verificar.
     * @return true si es la última parada, false en caso contrario.
     */
    public boolean esTerminal(Parada parada) {
        if (!this.recorrido.isEmpty() && parada != null) {
            return this.recorrido.get(this.recorrido.size() - 1).equals(parada);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LineaColectivo{id='").append(id).append('\'');
        sb.append(", nombre='").append(nombre).append('\'');
        sb.append(", recorrido=[");
        if (!this.recorrido.isEmpty()) {
            for (int i = 0; i < this.recorrido.size(); i++) {
                sb.append(this.recorrido.get(i).getId());
                if (i < this.recorrido.size() - 1) {
                    sb.append(" -> ");
                }
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineaColectivo that = (LineaColectivo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}