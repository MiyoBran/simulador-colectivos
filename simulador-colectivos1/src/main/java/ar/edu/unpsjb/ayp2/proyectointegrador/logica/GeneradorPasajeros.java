package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.interfaz.SimuladorConfig;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Properties;

/**
 * Responsable de crear las instancias de Pasajero para la simulación.
 * <p>
 * Genera una cantidad específica de pasajeros, asignándoles aleatoriamente
 * una línea, origen y destino, y los añade a la cola de espera de su parada.
 *
 * @author Miyen
 * @author Enzo
 * 
 * @version 1.1
 */
public class GeneradorPasajeros {

	// =================================================================================
	// CONSTANTES Y ATRIBUTOS
	// =================================================================================

	private static final int MIN_PARADAS_RECORRIDO = 2;

	private final Map<String, Linea> lineasDisponibles;
	private final Random random;
	private final int cantidadPasajerosAGenerar;
	private final GestorEstadisticas gestorEstadisticas;

	// =================================================================================
	// CONSTRUCTOR
	// =================================================================================

	/**
	 * Constructor principal para el generador de pasajeros.
	 *
	 * @param lineas           Un mapa de líneas disponibles cargadas.
	 * @param configProperties Las propiedades de configuración.
	 * @param gestorEstadisticas El gestor donde se registrarán los pasajeros.
	 * @throws IllegalArgumentException si los argumentos son nulos o inválidos.
	 */
	public GeneradorPasajeros(Map<String, Linea> lineas, Properties configProperties, GestorEstadisticas gestorEstadisticas) {
		if (lineas == null || lineas.isEmpty())
			throw new IllegalArgumentException("Las líneas disponibles no pueden ser nulas o vacías.");
		if (configProperties == null)
			throw new IllegalArgumentException("Las propiedades de configuración no pueden ser nulas.");
		if (gestorEstadisticas == null)
			throw new IllegalArgumentException("El gestor de estadísticas no puede ser nulo.");
		
		this.lineasDisponibles = lineas;
		this.random = new Random();
		this.gestorEstadisticas = gestorEstadisticas;
		
		// REFACTORIZACIÓN: Se delega la lectura de la config a la clase especializada.
		this.cantidadPasajerosAGenerar = SimuladorConfig.obtenerCantidadPasajeros(configProperties);
	}

	// =================================================================================
	// MÉTODOS PÚBLICOS
	// =================================================================================

	/**
	 * Genera la cantidad de pasajeros especificada en la configuración.
	 *
	 * @return Una lista con todas las instancias de Pasajero generadas.
	 * @throws IllegalStateException si no hay líneas válidas para generar pasajeros.
	 */
	public List<Pasajero> generarPasajeros() {
		List<Pasajero> pasajerosGenerados = new ArrayList<>();
		List<Linea> lineasValidas = obtenerLineasValidasParaGeneracion();

		if (lineasValidas.isEmpty()) {
			throw new IllegalStateException("No hay líneas con recorridos válidos (mínimo " + MIN_PARADAS_RECORRIDO + " paradas) para generar pasajeros.");
		}

		for (int i = 0; i < this.cantidadPasajerosAGenerar; i++) {
			// 1. Seleccionar línea, origen y destino
			Linea lineaSeleccionada = lineasValidas.get(random.nextInt(lineasValidas.size()));
			Parada[] origenYDestino = seleccionarParadasOrigenDestino(lineaSeleccionada);
			Parada paradaOrigen = origenYDestino[0];
			Parada paradaDestino = origenYDestino[1];

			// 2. Crear al pasajero
			String idPasajero = "Pasajero-" + (i + 1);
			Pasajero nuevoPasajero = new Pasajero(idPasajero, paradaOrigen, paradaDestino);
			pasajerosGenerados.add(nuevoPasajero);

			// 3. Registrar al pasajero en el sistema
			paradaOrigen.agregarPasajero(nuevoPasajero);
			this.gestorEstadisticas.registrarPasajero(nuevoPasajero);
		}
		
		return pasajerosGenerados;
	}

	// =================================================================================
	// MÉTODOS PRIVADOS DE AYUDA (HELPERS)
	// =================================================================================

	/**
	 * Filtra las líneas disponibles y devuelve solo aquellas que tienen un
	 * recorrido suficientemente largo para generar un origen y un destino.
	 *
	 * @return Lista de líneas válidas.
	 */
	private List<Linea> obtenerLineasValidasParaGeneracion() {
		List<Linea> lineasValidas = new ArrayList<>();
		for (Linea linea : this.lineasDisponibles.values()) {
			// Comprobar si la línea tiene al menos 2 paradas para poder elegir origen y destino.
			if (linea.getRecorrido().size() >= MIN_PARADAS_RECORRIDO) {
				lineasValidas.add(linea);
			}
		}
		return lineasValidas;
	}

	/**
	 * Selecciona aleatoriamente una parada de origen y una de destino de una línea.
	 * Se asegura de que el destino esté siempre después del origen en el recorrido.
	 *
	 * @param linea la línea de la cual seleccionar las paradas.
	 * @return un array de Parada de tamaño 2 (índice 0: origen, índice 1: destino).
	 */
	private Parada[] seleccionarParadasOrigenDestino(Linea linea) {
		List<Parada> recorrido = linea.getRecorrido();
		int totalParadas = recorrido.size();

		// La parada de origen puede ser cualquiera excepto la última.
		int indiceOrigen = random.nextInt(totalParadas - 1);

		// La parada de destino debe estar después del origen.
		int rangoDestino = totalParadas - (indiceOrigen + 1);
		int indiceDestino = indiceOrigen + 1 + random.nextInt(rangoDestino);

		Parada[] resultado = new Parada[2];
		resultado[0] = recorrido.get(indiceOrigen);
		resultado[1] = recorrido.get(indiceDestino);

		return resultado;
	}
}