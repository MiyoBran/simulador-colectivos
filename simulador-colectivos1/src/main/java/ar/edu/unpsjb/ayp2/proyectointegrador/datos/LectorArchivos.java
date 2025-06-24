package ar.edu.unpsjb.ayp2.proyectointegrador.datos;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;
import java.util.Map;
import java.util.TreeMap;

/**
 * Gestiona la carga de datos (paradas, líneas, configuración) desde archivos.
 * <p>
 * Proporciona una interfaz unificada para leer los datos necesarios para la
 * simulación, manejando errores de archivo y de formato de datos.
 *
 * @author Miyen
 * @author Enzo
 * @version 1.2
 */
public class LectorArchivos {

	// =================================================================================
	// CONSTANTES
	// =================================================================================

	private static final String DEFAULT_CONFIG_FILE = "config.properties";
	private static final String DELIMITADOR_CAMPOS = ";";
	private static final String DELIMITADOR_RECORRIDO = ",";

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	private final Map<String, Parada> paradasCargadas;
	private final Map<String, Linea> lineasCargadas;
	private final Properties propiedades;

	// =================================================================================
	// CONSTRUCTORES
	// =================================================================================

	/**
	 * Constructor por defecto. Carga las propiedades desde "config.properties".
	 */
	public LectorArchivos() {
		this.propiedades = new Properties();
		cargarPropiedadesDesdeClasspath(DEFAULT_CONFIG_FILE);
		this.paradasCargadas = new TreeMap<>();
		this.lineasCargadas = new TreeMap<>();
	}

	/**
	 * Constructor para inyección de dependencias, ideal para pruebas.
	 * 
	 * @param props las propiedades a utilizar.
	 */
	public LectorArchivos(Properties props) {
		if (props == null) {
			throw new IllegalArgumentException("El objeto Properties no puede ser nulo.");
		}
		this.propiedades = props;
		this.paradasCargadas = new TreeMap<>();
		this.lineasCargadas = new TreeMap<>();
	}

	// =================================================================================
	// MÉTODOS PÚBLICOS PRINCIPALES
	// =================================================================================

	/**
	 * Carga todos los datos necesarios en orden: paradas y luego líneas.
	 * 
	 * @throws IOException si ocurre un error al leer los archivos de datos.
	 */
	public void cargarDatosCompletos() throws IOException {
		this.paradasCargadas.clear();
		this.lineasCargadas.clear();
		cargarParadas();
		cargarLineas();
	}

	/**
	 * Carga las paradas desde el archivo especificado en la configuración.
	 * 
	 * @throws IOException si la propiedad no está definida o el archivo no se
	 *                     encuentra.
	 */
	public void cargarParadas() throws IOException {
		try (Scanner scanner = obtenerScannerParaArchivo("parada")) {
			while (scanner.hasNextLine()) {
				procesarLineaDeParada(scanner.nextLine());
			}
		}
	}

	/**
	 * Carga las líneas desde el archivo especificado en la configuración.
	 * 
	 * @throws IOException si la propiedad no está definida o el archivo no se
	 *                     encuentra.
	 */
	public void cargarLineas() throws IOException {
		if (this.paradasCargadas.isEmpty()) {
			System.err.println("Advertencia: No se cargaron líneas porque no hay paradas cargadas previamente.");
			return;
		}
		try (Scanner scanner = obtenerScannerParaArchivo("linea")) {
			while (scanner.hasNextLine()) {
				procesarLineaDeLinea(scanner.nextLine());
			}
		}
	}

	// =================================================================================
	// GETTERS (Defensivos, devuelven copias)
	// =================================================================================

	public Map<String, Parada> getParadasCargadas() {
		return new TreeMap<>(this.paradasCargadas);
	}

	public Map<String, Linea> getLineasCargadas() {
		return new TreeMap<>(this.lineasCargadas);
	}

	public Properties getPropiedades() {
		Properties copia = new Properties();
		copia.putAll(propiedades);
		return copia;
	}

	// =================================================================================
	// MÉTODOS PRIVADOS DE AYUDA (HELPERS)
	// =================================================================================

	/**
	 * Procesa una única línea del archivo de paradas y la añade al mapa si es
	 * válida.
	 * * @param linea La línea de texto a procesar.
	 */
	private void procesarLineaDeParada(String linea) {
		if (linea.trim().isEmpty())
			return;

		String[] partes = linea.split(DELIMITADOR_CAMPOS, -1);
		if (partes.length != 4) {
			System.err.println("Advertencia: Línea de parada con formato incorrecto omitida -> " + linea);
			return;
		}

		try {
			String id = partes[0].trim();
			String direccion = partes[1].trim();
			

			// Verificamos que los campos obligatorios no estén vacíos ANTES de crear el objeto.
			if (id.isEmpty() || direccion.isEmpty()) {
				System.err.println("Advertencia: ID o Dirección de parada vacíos en línea omitida -> " + linea);
				return;
			}

			
			if (paradasCargadas.containsKey(id)) {
				// Ya existe una parada con este ID, la ignoramos.
				return;
			}

			double latitud = Double.parseDouble(partes[2].trim());
			double longitud = Double.parseDouble(partes[3].trim());

			paradasCargadas.put(id, new Parada(id, direccion, latitud, longitud));
		} catch (NumberFormatException e) {
			System.err.println("Advertencia: Coordenadas inválidas en línea de parada omitida -> " + linea);
		}
	}

	/**
	 * Procesa una única línea del archivo de líneas y la añade al mapa si es
	 * válida.
	 * 
	 * @param lineaStr La línea de texto a procesar.
	 */
	private void procesarLineaDeLinea(String lineaStr) {
		if (lineaStr.trim().isEmpty())
			return;

		String[] partes = lineaStr.split(DELIMITADOR_CAMPOS, -1);
		if (partes.length != 3) {
			System.err.println("Advertencia: Línea de línea con formato incorrecto omitida -> " + lineaStr);
			return;
		}

		try {
			String id = partes[0].trim();
			String nombre = partes[1].trim();
			if (id.isEmpty())
				return;

			String claveMapa = id + " - " + nombre;
			if (lineasCargadas.containsKey(claveMapa))
				return;

			Linea lineaObj = new Linea(id, nombre);
			String[] idsParadas = partes[2].trim().split(DELIMITADOR_RECORRIDO);

			if (idsParadas.length == 1 && idsParadas[0].isEmpty()) {
				// Línea sin paradas, es válida.
			} else {
				for (String idParada : idsParadas) {
					Parada parada = this.paradasCargadas.get(idParada.trim());
					if (parada == null) {
						System.err.println("Error: La línea '" + claveMapa + "' referencia una parada desconocida ('"
								+ idParada.trim() + "'). La línea no será cargada.");
						return; // Omite toda la línea si una parada no existe
					}
					lineaObj.agregarParadaAlRecorrido(parada);
				}
			}
			lineasCargadas.put(claveMapa, lineaObj);
		} catch (IllegalArgumentException e) {
			System.err.println("Advertencia: Argumentos inválidos en línea de línea omitida -> " + lineaStr);
		}
	}

	/**
	 * Obtiene un Scanner para un archivo especificado en la configuración.
	 * 
	 * @param propiedadKey La clave en config.properties que contiene el nombre del
	 *                     archivo.
	 * @return Un Scanner listo para leer el archivo.
	 * @throws IOException si la propiedad no existe o el archivo no se encuentra.
	 */
	private Scanner obtenerScannerParaArchivo(String propiedadKey) throws IOException {
		String nombreArchivo = propiedades.getProperty(propiedadKey);
		if (nombreArchivo == null || nombreArchivo.trim().isEmpty()) {
			throw new IOException("La propiedad '" + propiedadKey + "' no está definida en config.properties.");
		}
		InputStream input = LectorArchivos.class.getClassLoader().getResourceAsStream(nombreArchivo);
		if (input == null) {
			throw new IOException("Archivo no encontrado en classpath: " + nombreArchivo);
		}
		return new Scanner(input, StandardCharsets.UTF_8.name());
	}

	/**
	 * Carga las propiedades desde un archivo en el classpath.
	 * 
	 * @param nombreArchivo el nombre del archivo de propiedades a cargar.
	 * @throws RuntimeException si el archivo de configuración no se puede cargar.
	 */
	private void cargarPropiedadesDesdeClasspath(String nombreArchivo) {
		try (InputStream input = LectorArchivos.class.getClassLoader().getResourceAsStream(nombreArchivo)) {
			if (input == null) {
				throw new IOException("No se pudo encontrar '" + nombreArchivo + "' en el classpath.");
			}

			propiedades.load(input);
		} catch (IOException ex) {
			throw new RuntimeException("Error crítico al cargar el archivo de configuración: " + nombreArchivo, ex);
		}
	}
}