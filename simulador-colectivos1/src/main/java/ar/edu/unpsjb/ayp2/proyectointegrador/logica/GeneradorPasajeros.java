package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Properties;

/**
 * Responsable de crear instancias de Pasajero para la simulación. Genera una
 * cantidad específica de pasajeros, asignándoles aleatoriamente una línea, una
 * parada de origen y una de destino, y los añade a la cola de espera de su
 * parada de origen correspondiente.
 * 
 * @author Miyen
 * @version 1.0
 */
public class GeneradorPasajeros {

	// --- CONSTANTES ---
	private static final int MIN_PARADAS_RECORRIDO = 2;

	// --- ATRIBUTOS ---
	private final Map<String, Linea> lineasDisponibles;
	// private final Map<String, Parada> paradasDisponibles; // Eliminada porque no
	// se usa
	private final Random random;
	private final int cantidadPasajerosAGenerar;
	private final GestorEstadisticas gestorEstadisticas;

	/**
	 * Constructor principal para el generador de pasajeros.
	 *
	 * @param lineas           Un mapa de líneas disponibles cargadas.
	 * @param paradas          Un mapa de paradas disponibles cargadas.
	 * @param configProperties Las propiedades de configuración (debe incluir
	 *                         "cantidadPasajeros").
	 * @param gestorEstadisticas El gestor de estadísticas donde se registrarán los
	 *                          pasajeros generados.
	 * @throws IllegalArgumentException si los mapas o propiedades son nulos o están
	 *                                  vacíos, o si la propiedad
	 *                                  'cantidadPasajeros' falta, está vacía, o no
	 *                                  es un número positivo.
	 */
	public GeneradorPasajeros(Map<String, Linea> lineas, Map<String, Parada> paradas, Properties configProperties, GestorEstadisticas gestorEstadisticas) {
		if (lineas == null || lineas.isEmpty()) {
			throw new IllegalArgumentException("Las líneas disponibles no pueden ser nulas o vacías.");
		}
		if (paradas == null || paradas.isEmpty()) {
			throw new IllegalArgumentException("Las paradas disponibles no pueden ser nulas o vacías.");
		}
		if (configProperties == null) {
			throw new IllegalArgumentException("Las propiedades de configuración no pueden ser nulas.");
		}
		if (gestorEstadisticas == null) {
			throw new IllegalArgumentException("El gestor de estadísticas no puede ser nulo.");
		}
		this.lineasDisponibles = lineas;
		this.random = new Random();
		this.cantidadPasajerosAGenerar = leerCantidadPasajeros(configProperties);
		this.gestorEstadisticas = gestorEstadisticas;
	}

	/**
	 * Genera la cantidad de pasajeros especificada en la configuración. Cada
	 * pasajero es creado y asignado a la cola de espera de su parada de origen.
	 *
	 * @return Una lista con todas las instancias de Pasajero generadas.
	 * @throws IllegalStateException si no hay líneas disponibles para generar
	 *                               pasajeros.
	 */
	public List<Pasajero> generarPasajeros() {
		List<Pasajero> pasajerosGenerados = new ArrayList<>();
		List<Linea> listaDeLineas = new ArrayList<>(lineasDisponibles.values());

		if (listaDeLineas.isEmpty()) {
			throw new IllegalStateException("No hay líneas disponibles para generar pasajeros.");
		}

		for (int i = 0; i < cantidadPasajerosAGenerar; i++) {
			Linea lineaSeleccionada = listaDeLineas.get(random.nextInt(listaDeLineas.size()));

			if (lineaSeleccionada.longitudRecorrido() < MIN_PARADAS_RECORRIDO) {
				// Si una línea específica no es válida, simplemente la saltamos y continuamos
				// con la siguiente iteración para no detener toda la generación.
				continue;
			}

			Parada[] origenYDestino = seleccionarParadasOrigenDestino(lineaSeleccionada);
			Parada paradaOrigen = origenYDestino[0];
			Parada paradaDestino = origenYDestino[1];

			// Creamos un ID secuencial y legible en lugar de un UUID.
			String idPasajero = "Pasajero-" + (i + 1);
			Pasajero nuevoPasajero = new Pasajero(idPasajero, paradaOrigen, paradaDestino);

			// Asignar la línea al pasajero.
			pasajerosGenerados.add(nuevoPasajero);

			// Asignar el pasajero a la cola de espera de su parada de origen.
			paradaOrigen.agregarPasajero(nuevoPasajero);
			gestorEstadisticas.registrarPasajero(nuevoPasajero);
		}
		return pasajerosGenerados;
	}

	/**
	 * Devuelve la cantidad de pasajeros que se configuró para generar.
	 * 
	 * @return La cantidad de pasajeros.
	 */
	public int getCantidadPasajerosAGenerar() {
		return cantidadPasajerosAGenerar;
	}

	// --- MÉTODOS PRIVADOS AUXILIARES ---

	/**
	 * Lee y valida el parámetro 'cantidadPasajeros' desde el objeto Properties.
	 * 
	 * @param configProperties las propiedades de configuración.
	 * @return la cantidad de pasajeros a generar.
	 */
	private int leerCantidadPasajeros(Properties configProperties) {
		String cantidadStr = configProperties.getProperty("cantidadPasajeros");
		if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"La propiedad 'cantidadPasajeros' no está definida o está vacía en config.properties.");
		}
		try {
			int cantidad = Integer.parseInt(cantidadStr.trim());
			if (cantidad <= 0) {
				throw new IllegalArgumentException("La cantidad de pasajeros a generar debe ser un número positivo.");
			}
			return cantidad;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"La propiedad 'cantidadPasajeros' en config.properties no es un número válido.", e);
		}
	}

	/**
	 * Selecciona aleatoriamente una parada de origen y una de destino de una línea
	 * dada. Se asegura de que el destino esté siempre después del origen en el
	 * recorrido.
	 * 
	 * @param linea la línea de la cual seleccionar las paradas.
	 * @return un array de Parada de tamaño 2, donde el índice 0 es el origen y el 1
	 *         es el destino.
	 */
	private Parada[] seleccionarParadasOrigenDestino(Linea linea) {
		int indiceOrigen;
		int indiceDestino;

		// La parada de origen no puede ser la última.
		int limiteSuperiorOrigen = linea.longitudRecorrido() - 1;

		// Opcionalmente, se podría aplicar una lógica más compleja aquí, como
		// la de excluir las N últimas paradas que tenías antes, pero la mantenemos
		// simple.

		indiceOrigen = random.nextInt(limiteSuperiorOrigen);

		// La parada de destino debe ser una parada posterior al origen.
		int rangoDestino = linea.longitudRecorrido() - (indiceOrigen + 1);
		indiceDestino = indiceOrigen + 1 + random.nextInt(rangoDestino);

		Parada[] resultado = new Parada[2];
		resultado[0] = linea.getParadaPorIndice(indiceOrigen);
		resultado[1] = linea.getParadaPorIndice(indiceDestino);

		return resultado;
	}
}