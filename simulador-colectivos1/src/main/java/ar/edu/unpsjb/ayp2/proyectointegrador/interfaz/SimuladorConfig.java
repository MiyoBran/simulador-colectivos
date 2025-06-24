package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import java.util.Properties;

/**
 * Clase de utilidad para leer y validar la configuración del simulador desde un
 * archivo de propiedades.
 * <p>
 * Proporciona métodos estáticos para obtener de forma segura cada parámetro de
 * configuración, aplicando valores por defecto si no se encuentran o son
 * inválidos.
 *
 * @author Miyo
 * @author Enzo
 * @version 1.1 // Versión refactorizada
 */
public final class SimuladorConfig {

	// =================================================================================
	// CONSTANTES DE CONFIGURACIÓN
	// (Centraliza todas las claves y valores por defecto para mantenimiento)
	// =================================================================================

	private static final String KEY_LINEAS = "linea";
	private static final String DEFAULT_LINEAS = "lineas_pm_mapeadas.txt";

	private static final String KEY_PARADAS = "parada";
	private static final String DEFAULT_PARADAS = "paradas_pm_mapeadas.txt";

	private static final String KEY_CANTIDAD_PASAJEROS = "cantidadPasajeros";
	private static final int DEFAULT_CANTIDAD_PASAJEROS = 150;

	private static final String KEY_CAPACIDAD_COLECTIVO = "capacidadColectivo";
	private static final int DEFAULT_CAPACIDAD_COLECTIVO = 30;

	private static final String KEY_CAPACIDAD_SENTADOS = "capacidadSentadosColectivo";
	private static final int DEFAULT_CAPACIDAD_SENTADOS = 20;

	private static final String KEY_COLECTIVOS_SIMULTANEOS = "cantidad_de_colectivos_simultaneos_por_linea";
	private static final int DEFAULT_COLECTIVOS_SIMULTANEOS = 1;

	private static final String KEY_RECORRIDOS_POR_COLECTIVO = "recorridos_por_colectivo";
	private static final int DEFAULT_RECORRIDOS_POR_COLECTIVO = 1;

	private static final String KEY_FRECUENCIA_SALIDA = "frecuencia_salida_colectivos_minutos";
	private static final int DEFAULT_FRECUENCIA_SALIDA = 10;

	/**
	 * Constructor privado para evitar que la clase de utilidad sea instanciada.
	 */
	private SimuladorConfig() {
	}

	// =================================================================================
	// MÉTODOS PÚBLICOS DE ACCESO A LA CONFIGURACIÓN
	// =================================================================================

	// Método para obtener el nombre del archivo de líneas
	public static String obtenerNombreArchivoLineas(Properties configProperties) {
		return configProperties.getProperty(KEY_LINEAS, DEFAULT_LINEAS);
	}

	// Método para obtener el nombre del archivo de paradas
	public static String obtenerNombreArchivoParadas(Properties configProperties) {
		return configProperties.getProperty(KEY_PARADAS, DEFAULT_PARADAS);
	}

	// Método para obtener la cantidad total de pasajeros a generar
	public static int obtenerCantidadPasajeros(Properties configProperties) {
		return obtenerEnteroDeConfig(configProperties, KEY_CANTIDAD_PASAJEROS, DEFAULT_CANTIDAD_PASAJEROS);
	}

	// Método para obtener la capacidad total de un colectivo
	public static int obtenerCapacidadColectivo(Properties configProperties) {
		return obtenerEnteroDeConfig(configProperties, KEY_CAPACIDAD_COLECTIVO, DEFAULT_CAPACIDAD_COLECTIVO);
	}

	// Método para obtener la cantidad de asientos de un colectivo
	public static int obtenerCapacidadSentadosColectivo(Properties configProperties) {
		// Nótese que para este valor, el mínimo permitido es 0
		return obtenerEnteroDeConfig(configProperties, KEY_CAPACIDAD_SENTADOS, DEFAULT_CAPACIDAD_SENTADOS, 0);
	}

	// Método para obtener la cantidad de colectivos por línea que operan a la vez
	public static int obtenerCantidadColectivosSimultaneosPorLinea(Properties configProperties) {
		return obtenerEnteroDeConfig(configProperties, KEY_COLECTIVOS_SIMULTANEOS, DEFAULT_COLECTIVOS_SIMULTANEOS);
	}

	// Método para obtener la cantidad de recorridos que hace cada colectivo
	public static int obtenerRecorridosPorColectivo(Properties configProperties) {
		return obtenerEnteroDeConfig(configProperties, KEY_RECORRIDOS_POR_COLECTIVO, DEFAULT_RECORRIDOS_POR_COLECTIVO);
	}

	// Método para obtener la frecuencia de salida de los colectivos en minutos
	public static int obtenerFrecuenciaSalidaColectivosMinutos(Properties configProperties) {
		return obtenerEnteroDeConfig(configProperties, KEY_FRECUENCIA_SALIDA, DEFAULT_FRECUENCIA_SALIDA);
	}

	// =================================================================================
	// MÉTODO PRIVADO DE AYUDA (HELPER)
	// (Contiene la lógica repetida para leer y validar un entero)
	// =================================================================================

	/**
	 * Método de ayuda para obtener un valor entero de la configuración. Asume que
	 * el valor mínimo válido es 1.
	 */
	private static int obtenerEnteroDeConfig(Properties props, String clave, int valorPorDefecto) {
		// Llama a la versión más específica del método con un valor mínimo de 1.
		return obtenerEnteroDeConfig(props, clave, valorPorDefecto, 1);
	}

	/**
	 * Método de ayuda principal para obtener un valor entero de la configuración,
	 * validando que sea mayor o igual a un mínimo especificado.
	 *
	 * @param props           El objeto de propiedades.
	 * @param clave           La clave a buscar en las propiedades.
	 * @param valorPorDefecto El valor a devolver si la clave no existe o es
	 *                        inválida.
	 * @param valorMinimo     El valor mínimo aceptable para el parámetro.
	 * @return El valor entero leído y validado, o el valor por defecto.
	 */
	private static int obtenerEnteroDeConfig(Properties props, String clave, int valorPorDefecto, int valorMinimo) {
		// DECLARACIÓN DE VARIABLES
		String valor = props.getProperty(clave); // Valor leído del archivo como texto

		// LÓGICA DEL MÉTODO
		if (valor != null) {
			try {
				int cantidad = Integer.parseInt(valor);
				if (cantidad >= valorMinimo) {
					return cantidad; // El valor es válido, se devuelve
				}
			} catch (NumberFormatException e) {
				// El valor no es un número, se ignora y se usará el valor por defecto.
			}
		}

		// Si el valor es nulo, inválido, o no cumple el mínimo, se imprime un error y
		// se devuelve el valor por defecto.
		System.err.println("Valor de '" + clave + "' inválido o no encontrado en configuración. Se usará '"
				+ valorPorDefecto + "' por defecto.");
		return valorPorDefecto;
	}
}