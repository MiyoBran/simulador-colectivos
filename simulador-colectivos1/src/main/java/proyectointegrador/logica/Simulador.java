package proyectointegrador.logica;

import proyectointegrador.interfaz.SimuladorConfig;
import proyectointegrador.modelo.Colectivo;
import proyectointegrador.modelo.Linea;
import proyectointegrador.modelo.Parada;
import proyectointegrador.modelo.Pasajero;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Motor principal de la simulación. Gestiona el ciclo de vida de los
 * colectivos, el movimiento de pasajeros y la progresión del tiempo.
 *
 * @author Miyo
 * @author Enzo
 * @version 1.2
 */
public class Simulador {

	// =================================================================================
	// ATRIBUTOS
	// =================================================================================

	private final List<Colectivo> colectivosEnSimulacion;
	private final Set<String> colectivosPendientesDeAvanzar;
	private final Map<String, Linea> lineasDisponibles;
	private final List<Pasajero> pasajerosSimulados;
	private final GestorEstadisticas gestorEstadisticas;
	private final PlanificadorRutas planificadorRutas;
	private final Properties configProperties;

	private int pasoActual;
	private boolean simulacionFinalizada;

	// =================================================================================
	// CONSTRUCTOR
	// =================================================================================

	/**
	 * Constructor principal del simulador.
	 * @param lineas Mapa de líneas disponibles para la simulación.
	 * @param paradas Mapa de paradas disponibles.
	 * @param pasajeros Lista de todos los pasajeros que participarán.
	 * @param gestorEstadisticas Gestor de estadísticas a utilizar.
	 * @param planificadorRutas Planificador de rutas a utilizar.
	 * @param configProperties Propiedades de configuración.
	 */
	public Simulador(Map<String, Linea> lineas, Map<String, Parada> paradas, List<Pasajero> pasajeros,
			GestorEstadisticas gestorEstadisticas, PlanificadorRutas planificadorRutas, Properties configProperties) {
		if (lineas == null || lineas.isEmpty() || paradas == null || paradas.isEmpty() || pasajeros == null) {
			throw new IllegalArgumentException("Líneas, paradas y pasajeros no pueden ser nulos o vacíos.");
		}
		this.lineasDisponibles = lineas;
		this.pasajerosSimulados = pasajeros;
		this.configProperties = configProperties;
		this.colectivosEnSimulacion = new ArrayList<>();
		this.colectivosPendientesDeAvanzar = new HashSet<>();
		this.gestorEstadisticas = (gestorEstadisticas != null) ? gestorEstadisticas : new GestorEstadisticas();
		this.planificadorRutas = (planificadorRutas != null) ? planificadorRutas : new PlanificadorRutas();
		this.pasoActual = 0;
		this.simulacionFinalizada = false;
	}

	// =================================================================================
	// MÉTODOS PÚBLICOS DE CONTROL DE LA SIMULACIÓN
	// =================================================================================

	/**
	 * Inicializa todos los colectivos basándose en los parámetros de configuración.
	 * @param capacidadTotal Capacidad máxima total de un colectivo.
	 * @param capacidadSentados Capacidad de asientos de un colectivo.
	 */
	public void inicializarColectivos(int capacidadTotal, int capacidadSentados) {
		int recorridosPorColectivo = SimuladorConfig.obtenerRecorridosPorColectivo(this.configProperties);
		int cantidadPorLinea = SimuladorConfig.obtenerCantidadColectivosSimultaneosPorLinea(this.configProperties);
		int frecuenciaMin = SimuladorConfig.obtenerFrecuenciaSalidaColectivosMinutos(this.configProperties);
		int capacidadParados = capacidadTotal - capacidadSentados;

		if (capacidadTotal <= 0 || capacidadSentados < 0 || capacidadParados < 0 || recorridosPorColectivo <= 0 || cantidadPorLinea <= 0 || frecuenciaMin <= 0) {
			throw new IllegalArgumentException("Capacidades, recorridos y cantidad de colectivos deben ser positivos.");
		}

		this.colectivosEnSimulacion.clear();
		this.colectivosPendientesDeAvanzar.clear();
		int pasosPorFrecuencia = (int) Math.ceil(frecuenciaMin / 2.0);
		int colectivoCounter = 1;

		for (Linea linea : lineasDisponibles.values()) {
			for (int i = 0; i < cantidadPorLinea; i++) {
				String idColectivo = "C" + colectivoCounter + "-" + linea.getId();
				int pasoDeSalida = i * pasosPorFrecuencia;

				Colectivo nuevoColectivo = new Colectivo(idColectivo, linea, capacidadTotal, capacidadSentados,
						capacidadParados, recorridosPorColectivo, pasoDeSalida);
				
				this.colectivosEnSimulacion.add(nuevoColectivo);
				this.gestorEstadisticas.registrarCapacidadColectivo(idColectivo, capacidadTotal);
				colectivoCounter++;
			}
		}
	}

	/**
	 * Ejecuta un único paso o ciclo de la simulación.
	 * @return Una lista de eventos (en formato String) que ocurrieron durante este paso.
	 */
	public List<String> ejecutarPasoDeSimulacion() {
		List<String> eventosDelPaso = new ArrayList<>();
		
		// 1. Avanzar colectivos que fueron marcados para moverse en el paso anterior.
		avanzarColectivosPendientes(eventosDelPaso);

		// 2. Procesar cada colectivo en su parada actual.
		for (Colectivo colectivo : colectivosEnSimulacion) {
			// Solo procesar si no está en una terminal (ya se procesó al llegar).
			// Y si ya cumplió su paso de salida programado.
			if (!colectivo.estaEnTerminal() && colectivo.getPasoDeSalida() <= pasoActual) {
				gestorEstadisticas.registrarOcupacionTramo(colectivo.getIdColectivo(), colectivo.getCantidadPasajerosABordo());
				procesarPasoParaColectivo(colectivo, eventosDelPaso);
			}
		}
		
		pasoActual++;
		return eventosDelPaso;
	}

	/**
	 * Verifica si la simulación ha terminado. Se considera terminada cuando todos los
	 * colectivos han completado todos sus recorridos.
	 * @return true si la simulación ha terminado, false en caso contrario.
	 */
	public boolean isSimulacionTerminada() {
		for (Colectivo colectivo : colectivosEnSimulacion) {
			if (colectivo.getRecorridosRestantes() > 0) {
				return false; // Si al menos un colectivo tiene recorridos pendientes, no ha terminado.
			}
		}
		
		// Si llegamos aquí, todos los colectivos terminaron.
		// Nos aseguramos de finalizar el estado de la simulación una sola vez.
		if (!this.simulacionFinalizada) {
			finalizarSimulacion();
		}

		return true;
	}
	
	/**
	 * Genera un reporte final con advertencias sobre el estado de la simulación.
	 * @return Lista de strings con las advertencias del reporte.
	 */
	public List<String> getReporteFinal() {
		List<String> reporte = new ArrayList<>();
		reporte.add("Verificación final de la simulación completada.");
		
		for (Colectivo colectivo : colectivosEnSimulacion) {
			if (colectivo.getCantidadPasajerosABordo() > 0) {
				reporte.add("ADVERTENCIA: El colectivo " + colectivo.getIdColectivo() + " terminó con pasajeros a bordo.");
			}
		}
		return reporte;
	}

	// =================================================================================
	// MÉTODOS PRIVADOS DE LÓGICA
	// =================================================================================

	/**
	 * Avanza todos los colectivos que estaban esperando para moverse.
	 */
	private void avanzarColectivosPendientes(List<String> eventosDelPaso) {
		if (colectivosPendientesDeAvanzar.isEmpty()) return;

		for (String id : colectivosPendientesDeAvanzar) {
			Colectivo colectivo = buscaColectivoPorId(id);
			if (colectivo != null && colectivo.getPasoDeSalida() <= pasoActual) {
				colectivo.avanzarAProximaParada();
				if (colectivo.estaEnTerminal()) {
					eventosDelPaso.add("  Colectivo " + colectivo.getIdColectivo() + " ha llegado a la terminal.");
					procesarLogicaTerminal(colectivo, eventosDelPaso);
				} else {
					eventosDelPaso.add("  Colectivo " + colectivo.getIdColectivo() + " avanza a la próxima parada.");
				}
			}
		}
		colectivosPendientesDeAvanzar.clear();
	}
	
	/**
	 * Procesa un paso para un colectivo: bajada y subida de pasajeros.
	 */
	private void procesarPasoParaColectivo(Colectivo colectivo, List<String> eventos) {
		Parada paradaActual = colectivo.getParadaActual();
		eventos.add("\nColectivo " + colectivo.getEtiqueta() + " en Parada: " + paradaActual.getDireccion() + " (ID: " + paradaActual.getId() + ")");
		eventos.add("  Pasajeros a bordo: " + colectivo.getCantidadPasajerosABordo() + "/" + colectivo.getCapacidadMaxima());

		procesarBajadaPasajeros(colectivo, paradaActual, eventos);
		procesarSubidaPasajeros(colectivo, paradaActual, eventos);
	    
		if (!colectivo.estaEnTerminal()) {
			colectivosPendientesDeAvanzar.add(colectivo.getIdColectivo());
		}
	}

	/**
	 * Procesa la bajada de pasajeros en la parada actual del colectivo.
	 */
	private void procesarBajadaPasajeros(Colectivo colectivo, Parada paradaActual, List<String> eventos) {
		// CORRECCIÓN DE BUG: Se crea una copia para iterar de forma segura mientras se modifica la lista original.
		List<Pasajero> copiaPasajeros = new ArrayList<>(colectivo.getPasajerosABordo());
		for (Pasajero p : copiaPasajeros) {
			if (p.getParadaDestino().equals(paradaActual)) {
				colectivo.bajarPasajero(p);
				eventos.add("  - Bajó " + p + " en su destino.");
				gestorEstadisticas.registrarTransporte(p);
			}
		}
	}
	
	/**
	 * Procesa la subida de pasajeros en la parada actual del colectivo.
	 */
	private void procesarSubidaPasajeros(Colectivo colectivo, Parada paradaActual, List<String> eventos) {
        eventos.add("  Pasajeros esperando en parada: " + paradaActual.cantidadPasajerosEsperando());
        int pasajerosSubidos = 0;
        List<Pasajero> pasajerosQueSubieron = new ArrayList<>();
        List<Pasajero> pasajerosEnEspera = new ArrayList<>(paradaActual.getPasajerosEsperando()); // Copia para iterar

        for (Pasajero pasajero : pasajerosEnEspera) {
            int idxActual = colectivo.getLineaAsignada().getIndiceParada(paradaActual);
            int idxDestino = colectivo.getLineaAsignada().getIndiceParada(pasajero.getParadaDestino());

            if (idxDestino > idxActual) { // Solo subir si el destino está más adelante.
                if (colectivo.subirPasajero(pasajero)) {
                    pasajerosQueSubieron.add(pasajero);
                    pasajerosSubidos++;
                    pasajero.setPudoSubir(true);
                    eventos.add("  + Subió " + pasajero);
                } else {
                    pasajero.incrementarColectivosEsperados();
                    eventos.add("  - No pudo subir " + pasajero.getId() + " (colectivo lleno).");
                }
            }
        }
        
        if (!pasajerosQueSubieron.isEmpty()) {
        	// La lógica para remover de la cola de espera debe ser robusta.
        	// Se asume que Parada usa una Queue y se remueven los que subieron.
        	paradaActual.getPasajerosEsperando().removeAll(pasajerosQueSubieron);
        }

        eventos.add("  Pasajeros que subieron: " + pasajerosSubidos);
        eventos.add("  Quedan esperando: " + paradaActual.cantidadPasajerosEsperando());
    }

	/**
	 * Procesa la lógica cuando un colectivo llega a su parada terminal.
	 */
	private void procesarLogicaTerminal(Colectivo colectivo, List<String> eventos) {
		Parada paradaFinal = colectivo.getParadaActual();
		String paradaInfo = (paradaFinal != null) ? paradaFinal.getDireccion() + " (ID: " + paradaFinal.getId() + ")" : "N/A";
		eventos.add("Colectivo " + colectivo.getEtiqueta() + " ha finalizado su recorrido " + colectivo.getRecorridoActual() + " en: " + paradaInfo);
		
		colectivo.actualizarRecorridosRestantes();

		if (colectivo.getCantidadPasajerosABordo() > 0) {
			eventos.add("  Procesando pasajeros en la parada terminal...");
			List<Pasajero> pasajerosCopia = new ArrayList<>(colectivo.getPasajerosABordo()); // Copia segura
			for (Pasajero p : pasajerosCopia) {
				colectivo.bajarPasajero(p);
				if (p.getParadaDestino().equals(paradaFinal)) {
					eventos.add("  - Bajó " + p + " en su destino (terminal).");
				} else {
					p.setBajadaForzosa(true);
					eventos.add("  - BAJADA FORZOSA: " + p + " no llegó a su destino (" + p.getParadaDestino().getId() + ").");
				}
				gestorEstadisticas.registrarTransporte(p);
			}
		}
		
		// Reiniciar el colectivo para un nuevo recorrido si tiene recorridos restantes.
		// Separador para claridad en los eventos/debug.
		if (colectivo.getRecorridosRestantes() > 0) {
			colectivo.reiniciarParaNuevoRecorrido();
			String separador = "\n\n--##--->\n\n";
			String eventoReinicio = String.format("%sEVENTO: Colectivo %s reiniciado para un nuevo recorrido %s",
					separador, colectivo.getIdColectivo(), separador);
			eventos.add(eventoReinicio);
			colectivosPendientesDeAvanzar.add(colectivo.getIdColectivo());
		} else {
			eventos.add("  Colectivo " + colectivo.getIdColectivo() + " ha finalizado todos sus recorridos.");
		}
	}
	
	/**
	 * Realiza los cómputos finales una única vez cuando la simulación termina.
	 */
	private void finalizarSimulacion() {
		// Registrar a todos los pasajeros que nunca pudieron subir para las estadísticas.
		for (Pasajero p : this.pasajerosSimulados) {
			if (!p.isPudoSubir()) {
				gestorEstadisticas.registrarTransporte(p);
			}
		}
		this.simulacionFinalizada = true;
	}
	
	/**
	 * Busca un colectivo por su identificador.
	 */
	private Colectivo buscaColectivoPorId(String id) {
		for (Colectivo c : colectivosEnSimulacion) {
			if (c.getIdColectivo().equals(id)) {
				return c;
			}
		}
		return null; // Devuelve null si no se encuentra
	}
	
	
	// =================================================================================
	// GETTERS
	// =================================================================================
	
	public List<Colectivo> getColectivosEnSimulacion() { return new ArrayList<>(this.colectivosEnSimulacion); }
	public Map<String, Linea> getLineasDisponibles() { return this.lineasDisponibles; }
    public GestorEstadisticas getGestorEstadisticas() { return this.gestorEstadisticas; }
    public PlanificadorRutas getPlanificadorRutas() { return this.planificadorRutas; }

 	/**
 	 * Devuelve una copia de la lista de todos los pasajeros que participan en la simulación.
 	 * @return Una lista con todos los pasajeros.
 	 */
 	public List<Pasajero> getPasajerosSimulados() {
 		return new ArrayList<>(this.pasajerosSimulados);
 	}

}