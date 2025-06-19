package ar.edu.unpsjb.ayp2.proyectointegrador.datos;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;
import java.util.Map;
import java.util.TreeMap;

/**
 * Gestiona la carga de datos de paradas y líneas de colectivos desde archivos de texto.
 * También maneja la configuración desde un archivo de propiedades.
 * @author MiyoBran
 * @version 1.1
 */
public class LectorArchivos {

	// --- CONSTANTES Y ATRIBUTOS ---
    private static final String DEFAULT_CONFIG_FILE = "config.properties";

    // REFACTORING: Cambiamos a TreeMap para un orden natural de las claves.
    private Map<String, Parada> paradasCargadas;
    private Map<String, Linea> lineasCargadas;
    // REFACTORING: Usamos un atributo Properties para manejar la configuración.
    private final Properties propiedades;

    /**
     * Constructor por defecto. Carga las propiedades desde "config.properties".
     * Lanza una RuntimeException si el archivo de configuración no se puede cargar.
     */
    public LectorArchivos() {
        this.propiedades = new Properties();
        cargarPropiedadesDesdeClasspath(DEFAULT_CONFIG_FILE);
        // REFACTORING: Unificamos el tipo de Map a TreeMap.
        this.paradasCargadas = new TreeMap<>();
        this.lineasCargadas = new TreeMap<>();
    }

    /**
     * Constructor para inyección de dependencias, ideal para pruebas.
     * @param props las propiedades a utilizar.
     */
    public LectorArchivos(Properties props) {
        if (props == null) {
            throw new IllegalArgumentException("Las propiedades no pueden ser nulas.");
        }
        this.propiedades = props;
        // REFACTORING: Unificamos el tipo de Map a TreeMap.
        this.paradasCargadas = new TreeMap<>();
        this.lineasCargadas = new TreeMap<>();
    }
    
    /**
     * Carga todos los datos necesarios: primero las paradas y luego las líneas.
     * @throws IOException si ocurre un error al leer los archivos de datos.
     */
    public void cargarDatosCompletos() throws IOException {
        this.paradasCargadas.clear();
        this.lineasCargadas.clear();
        cargarParadas();
        cargarLineas();
    }

    /**
     * Devuelve una copia del mapa de paradas cargadas.
     * @return un nuevo mapa (TreeMap) con las paradas.
     */
    public Map<String, Parada> getParadasCargadas() {
        return new TreeMap<>(this.paradasCargadas);
    }
    
    /**
     * Devuelve una copia del mapa de líneas cargadas.
     * @return un nuevo mapa (TreeMap) con las líneas.
     */
    public Map<String, Linea> getLineasCargadas() {
        return new TreeMap<>(this.lineasCargadas);
    }
    
    /**
     * Devuelve una copia de las propiedades de configuración.
     * @return un nuevo objeto Properties con la configuración.
     */
    public Properties getPropiedades() {
        Properties copia = new Properties();
        copia.putAll(propiedades);
        return copia;
    }

    // --- MÉTODO PRIVADO AUXILIAR ---

    /**
	 * Carga las propiedades desde un archivo en el classpath.
	 * Si el archivo no se encuentra, lanza una RuntimeException.
	 * @param nombreArchivo el nombre del archivo de propiedades a cargar.
	 */
    private void cargarPropiedadesDesdeClasspath(String nombreArchivo) {
        try (InputStream input = LectorArchivos.class.getClassLoader().getResourceAsStream(nombreArchivo)) {
            if (input == null) {
                throw new IOException("No se pudo encontrar el archivo de configuración '" + nombreArchivo + "' en el classpath.");
            }
            propiedades.load(input);
        } catch (IOException ex) {
            // Envolvemos la IOException en una RuntimeException porque si la configuración
            // no se puede cargar, la aplicación no puede continuar.
            throw new RuntimeException("Error crítico al cargar el archivo de configuración: " + nombreArchivo, ex);
        }
    }

    // --- MÉTODOS PÚBLICOS DE CARGA ---


    /**
     * Carga las paradas desde el archivo especificado en la propiedad 'parada'.
     * @throws IOException si la propiedad no está definida o el archivo no se encuentra.
     */
    public void cargarParadas() throws IOException {
        String nombreArchivo = propiedades.getProperty("parada");
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            throw new IOException("La propiedad 'parada' no está definida en config.properties.");
        }

        // Primero obtenemos el InputStream
        InputStream input = LectorArchivos.class.getClassLoader().getResourceAsStream(nombreArchivo);

        // --- ¡CORRECCIÓN AQUÍ! ---
        // Verificamos si el input es null ANTES del try-with-resources.
        if (input == null) {
            throw new FileNotFoundException("Archivo de paradas no encontrado en classpath: " + nombreArchivo);
        }

        // Ahora que sabemos que input no es null, podemos usarlo en el try-with-resources.
        try (Scanner scanner = new Scanner(input, StandardCharsets.UTF_8.name())) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                if (linea.trim().isEmpty()) continue;
                
                String[] partes = linea.split(";", -1);
                if (partes.length != 4) continue;

                try {
                    String id = partes[0].trim();
                    if (id.isEmpty() || paradasCargadas.containsKey(id)) continue;

                    String direccion = partes[1].trim();
                    double latitud = Double.parseDouble(partes[2].trim());
                    double longitud = Double.parseDouble(partes[3].trim());
                    
                    paradasCargadas.put(id, new Parada(id, direccion, latitud, longitud));
                } catch (IllegalArgumentException e) {
                    // Ignora silenciosamente la línea si los números no son válidos o si el constructor de Parada falla.
                }
            }
        } // El InputStream 'input' se cierra automáticamente aquí gracias al try-with-resources.
    }

    /**
     * Carga las líneas desde el archivo especificado en la propiedad 'linea'.
     * Requiere que las paradas hayan sido cargadas previamente.
     * @throws IOException si la propiedad no está definida o el archivo no se encuentra.
     */
    public void cargarLineas() throws IOException {
        String nombreArchivo = propiedades.getProperty("linea");
        if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
            throw new IOException("La propiedad 'linea' no está definida en config.properties.");
        }

        if (this.paradasCargadas.isEmpty()) {
            // No tiene sentido cargar líneas si no hay paradas a las cuales referenciar.
            return;
        }

        InputStream input = LectorArchivos.class.getClassLoader().getResourceAsStream(nombreArchivo);

        // --- ¡CORRECCIÓN AQUÍ (ANÁLOGA)! ---
        if (input == null) {
            throw new FileNotFoundException("Archivo de líneas no encontrado en classpath: " + nombreArchivo);
        }
        
        try (Scanner scanner = new Scanner(input, StandardCharsets.UTF_8.name())) {
            while (scanner.hasNextLine()) {
                String lineaStr = scanner.nextLine();
                if (lineaStr.trim().isEmpty()) continue;
                
                String[] partes = lineaStr.split(";", -1);
                if (partes.length != 3) continue;

                try {
                    String id = partes[0].trim();
                    String nombre = partes[1].trim();
                    if (id.isEmpty()) continue;

                    String claveMapa = id + " - " + nombre;
                    if (lineasCargadas.containsKey(claveMapa)) continue;

                    Linea lineaObj = new Linea(id, nombre);
                    String[] idsParadas = partes[2].trim().split(",");

                    boolean recorridoValido = true;
                    if (!(idsParadas.length == 1 && idsParadas[0].isEmpty())) {
                        for (String idParada : idsParadas) {
                            Parada parada = this.paradasCargadas.get(idParada.trim());
                            if (parada == null) {
                                recorridoValido = false;
                                break;
                            }
                            lineaObj.agregarParadaAlRecorrido(parada);
                        }
                    }

                    if (recorridoValido) {
                        lineasCargadas.put(claveMapa, lineaObj);
                    }
                } catch (IllegalArgumentException e) {
					// Ignora silenciosamente la línea si el constructor de Linea falla.
					// Esto puede ocurrir si el ID o el nombre son inválidos.
                }
            }
        }
    }
}