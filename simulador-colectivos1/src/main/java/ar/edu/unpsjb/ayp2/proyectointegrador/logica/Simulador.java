package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Simulador de recorrido de colectivos. * Esta clase gestiona la simulación de
 * colectivos recorriendo paradas, * subiendo y bajando pasajeros. * Requiere
 * líneas y paradas previamente cargadas.
 * 
 * @author Miyo
 * @version 1.0
 * 
 */
public class Simulador {

	private List<Colectivo> colectivosEnSimulacion;
	private Set<String> colectivosPendientesDeAvanzar; // IDs de colectivos que deben avanzar en el próximo paso
	private Map<String, Linea> lineasDisponibles;
    private GestorEstadisticas gestorEstadisticas;
    private PlanificadorRutas planificadorRutas;

    /**
     * Constructor del simulador. Permite inyectar dependencias para facilitar el testeo.
     * Si gestorEstadisticas o planificadorRutas son nulos, se crean instancias por defecto.
     *
     * @param lineas    Mapa de líneas disponibles para la simulación.
     * @param paradas   Mapa de paradas disponibles para la simulación.
     * @param pasajeros Lista de pasajeros que participarán en la simulación.
     * @param gestorEstadisticas (opcional) Gestor de estadísticas a utilizar.
     * @param planificadorRutas (opcional) Planificador de rutas a utilizar.
     */
    public Simulador(Map<String, Linea> lineas, Map<String, Parada> paradas, List<Pasajero> pasajeros,
                     GestorEstadisticas gestorEstadisticas, PlanificadorRutas planificadorRutas) {
        if (lineas == null || lineas.isEmpty()) {
            throw new IllegalArgumentException("El simulador requiere líneas cargadas.");
        }
        if (paradas == null || paradas.isEmpty()) {
            throw new IllegalArgumentException("El simulador requiere paradas cargadas.");
        }
        if (pasajeros == null) {
            throw new IllegalArgumentException("La lista de pasajeros no puede ser nula.");
        }
        this.lineasDisponibles = lineas;
        this.colectivosEnSimulacion = new ArrayList<>();
        this.colectivosPendientesDeAvanzar = new HashSet<>();
        this.gestorEstadisticas = (gestorEstadisticas != null) ? gestorEstadisticas : new GestorEstadisticas();
        this.planificadorRutas = (planificadorRutas != null) ? planificadorRutas : new PlanificadorRutas();
    }

    /**
     * Constructor original para compatibilidad: instancia dependencias por defecto.
     */
    public Simulador(Map<String, Linea> lineas, Map<String, Parada> paradas, List<Pasajero> pasajeros) {
        this(lineas, paradas, pasajeros, null, null);
    }

	/**
	 * Inicializa los colectivos en la simulación con las capacidades y recorridos dados.
	 * Cada colectivo se asigna a una línea disponible y se le asigna un ID único.
	 *
	 * @param capacidadColectivo Capacidad máxima de pasajeros por colectivo.
	 * @param capacidadSentados Capacidad máxima de pasajeros sentados.
	 * @param capacidadParados Capacidad máxima de pasajeros parados.
	 * @param recorridosRestantes Cantidad de recorridos que debe realizar el colectivo.
	 */
	public void inicializarColectivos(int capacidadColectivo, int capacidadSentados, int capacidadParados, int recorridosRestantes) {
        if (capacidadColectivo <= 0 || capacidadSentados < 0 || capacidadParados < 0 || recorridosRestantes <= 0) {
            throw new IllegalArgumentException("Las capacidades y recorridos deben ser positivos.");
        }
        this.colectivosEnSimulacion.clear();
        int colectivoCounter = 1;
        for (Linea linea : lineasDisponibles.values()) {
            String idColectivo = "C" + colectivoCounter + "-" + linea.getId();
            Colectivo nuevoColectivo = new Colectivo(idColectivo, linea, capacidadColectivo, capacidadSentados, capacidadParados, recorridosRestantes);
            this.colectivosEnSimulacion.add(nuevoColectivo);
            colectivoCounter++;
        }
        this.colectivosPendientesDeAvanzar.clear();
    }

	/**
	 * Ejecuta un paso de simulación. Este método avanza los colectivos pendientes
	 * de avanzar, procesa los colectivos en su parada actual (puede ser la
	 * terminal), y maneja la lógica de subida y bajada de pasajeros.
	 * 
	 * @return Lista de eventos generados en este paso de simulación. *
	 *
	 */
public List<String> ejecutarPasoDeSimulacion() {
		
		//Map<String, List<String>> eventosPorColectivo = new LinkedHashMap<>();
		
		List<String> eventosDelPaso = new ArrayList<>();

		// 1. Avanzar colectivos pendientes (deben avanzar al inicio del paso)
		if (!colectivosPendientesDeAvanzar.isEmpty()) {
			for (String id : colectivosPendientesDeAvanzar) {
				Colectivo colectivo = buscaColectivoPorId(id);
				if (colectivo != null) {
					colectivo.avanzarAProximaParada();
					if (colectivo.estaEnTerminal()) {
						
						eventosDelPaso.add("  Colectivo " + colectivo.getIdColectivo() + " ha llegado a la terminal.");
						procesarLogicaTerminal(colectivo, eventosDelPaso);
					} else {
						
						eventosDelPaso
						
								.add("  Colectivo " + colectivo.getIdColectivo() + " avanza a la próxima parada.");
						
					}
				}
			}
			colectivosPendientesDeAvanzar.clear();
		}

		// 2. Procesar colectivos en su parada actual (puede ser la terminal)
		for (Colectivo colectivo : colectivosEnSimulacion) {
			if (colectivo.estaEnTerminal()) {
				// Procesar colectivos que YA están en terminal desde el paso anterior
				// Nota: La lógica de procesamiento de terminal se excluye aquí intencionalmente
				// para evitar mensajes duplicados, ya que los colectivos que llegan a la terminal
				// en este paso ya se procesan en el primer bucle.
				//procesarLogicaTerminal(colectivo, eventosDelPaso);
			} else {
				// Procesar parada actual
				procesarPasoParaColectivo(colectivo, eventosDelPaso);
			}
		}

		return eventosDelPaso;
	}


	/**
	 * Verifica si la simulación ha terminado. La simulación se considera terminada
	 * cuando todos los colectivos han llegado a su terminal y no hay colectivos
	 * pendientes de avanzar.
	 * 
	 * @return true si la simulación ha terminado, false en caso contrario.
	 */
	public boolean isSimulacionTerminada() {
		for (Colectivo colectivo : colectivosEnSimulacion) {
			if (!colectivo.estaEnTerminal()) {
				return false;
			}
		}
		return colectivosPendientesDeAvanzar.isEmpty();
	}

	/**
	 * Genera un reporte final de la simulación. Este reporte incluye información
	 * sobre los colectivos que han completado su recorrido y cualquier advertencia
	 * sobre pasajeros que no llegaron a su destino.
	 * 
	 * @return Lista de strings con el reporte final.
	 */
	public List<String> getReporteFinal() {
		List<String> reporte = new ArrayList<>();
		reporte.add("Todos los colectivos han completado su primer recorrido.");
		// Verificación final de pasajeros que quedaron a bordo
		for (Colectivo colectivo : colectivosEnSimulacion) {
			if (colectivo.getCantidadPasajerosABordo() > 0) {
				reporte.add(
						"ADVERTENCIA: El colectivo " + colectivo.getIdColectivo() + " terminó con pasajeros a bordo.");
			}
		}
		return reporte;
	}

	/**
	 * Procesa la lógica de un paso para un colectivo en su parada actual. Incluye
	 * bajada y subida de pasajeros, y marca el colectivo para avanzar si no está en
	 * terminal.
	 */
	private void procesarPasoParaColectivo(Colectivo colectivo, List<String> eventos) {
		Parada paradaActual = colectivo.getParadaActual();
		eventos.add("\nColectivo " + colectivo.getIdColectivo() + " (Línea " + colectivo.getLineaAsignada().getNombre()
				+ ") en la Parada: " + paradaActual.getDireccion() + " (ID: " + paradaActual.getId() + ")");
		eventos.add("Pasajeros a bordo: " + colectivo.getCantidadPasajerosABordo() + " / "
				+ colectivo.getCapacidadMaxima());

		procesarBajadaPasajeros(colectivo, paradaActual, eventos);
		procesarSubidaPasajeros(colectivo, paradaActual, eventos);

		// MARCAR para avanzar en el próximo paso (NO avanzar ahora)
		if (!colectivo.estaEnTerminal()) {
			colectivosPendientesDeAvanzar.add(colectivo.getIdColectivo());
		}
	}

	/**
	 * Procesa la lógica de bajada de pasajeros en la parada actual del colectivo.
	 * Baja a los pasajeros que tienen como destino la parada actual.
	 */
	private void procesarBajadaPasajeros(Colectivo colectivo, Parada paradaActual, List<String> eventos) {
		List<Pasajero> pasajerosABajar = new ArrayList<>();
		for (Pasajero p : colectivo.getPasajerosABordo()) {
			if (p.getParadaDestino().equals(paradaActual)) {
				pasajerosABajar.add(p);
			}
		}
		for (Pasajero p : pasajerosABajar) {
			if (colectivo.bajarPasajero(p)) {
				eventos.add("  - Bajó " + p + " en su destino.");
			}
		}
	}

	/**
	 * Procesa la lógica de subida de pasajeros en la parada actual del colectivo.
	 * Intenta subir a los pasajeros que están esperando en la parada y que tienen
	 * un destino válido.
	 */
	private void procesarSubidaPasajeros(Colectivo colectivo, Parada paradaActual, List<String> eventos) {
		eventos.add("  Pasajeros esperando en parada: " + paradaActual.cantidadPasajerosEsperando());
		int pasajerosSubidos = 0;
		List<Pasajero> pasajerosQueSeQuedan = new ArrayList<>();

		while (paradaActual.hayPasajerosEsperando()) {
			Pasajero pasajero = paradaActual.removerSiguientePasajero();
			if (colectivo.getLineaAsignada().tieneParadaEnRecorrido(pasajero.getParadaDestino())) {
				if (colectivo.subirPasajero(pasajero)) {
					pasajerosSubidos++;
					pasajero.setPudoSubir(true);
					eventos.add("  + Subió pasajero " + pasajero.getId() + " (destino: "
							+ pasajero.getParadaDestino().getDireccion() + ")");
				} else {
					pasajero.incrementarColectivosEsperados();
					eventos.add("  - Pasajero " + pasajero.getId()
							+ " no pudo subir (colectivo lleno). Esperando al siguiente.");
					pasajerosQueSeQuedan.add(pasajero);
				}
			} else {
				pasajerosQueSeQuedan.add(pasajero);
			}
		}

		for (Pasajero p : pasajerosQueSeQuedan) {
			paradaActual.agregarPasajero(p);
		}
		eventos.add("  Cantidad de pasajeros que subieron en esta parada: " + pasajerosSubidos);
		eventos.add("  Pasajeros restantes esperando en parada: " + paradaActual.cantidadPasajerosEsperando());
	}

	/**
	 * Procesa la lógica cuando un colectivo ha finalizado su recorrido. Este método
	 * se llama para colectivos que YA están en terminal.
	 */
	private void procesarLogicaTerminal(Colectivo colectivo, List<String> eventos) {
		Parada paradaFinal = colectivo.getParadaActual();
		String paradaInfo = (paradaFinal != null) ? paradaFinal.getDireccion() + " (ID: " + paradaFinal.getId() + ")"
				: "N/A (Recorrido Vacío)";
		eventos.add("Colectivo " + colectivo.getIdColectivo() + " de la línea "
				+ colectivo.getLineaAsignada().getNombre() + " ha finalizado su recorrido en: " + paradaInfo);

		if (colectivo.getCantidadPasajerosABordo() > 0) {
			eventos.add("  Procesando pasajeros en la parada terminal...");
			List<Pasajero> pasajerosCopia = new ArrayList<>(colectivo.getPasajerosABordo());
			for (Pasajero p : pasajerosCopia) {
				colectivo.bajarPasajero(p);
				// Distinguir entre pasajeros que llegaron a su destino vs bajada forzosa
				if (p.getParadaDestino().equals(paradaFinal)) {
					eventos.add("  - Bajó " + p + " en su destino (terminal).");
				} else {
					eventos.add("  - BAJADA FORZOSA: " + p + " no llegó a su destino ("
							+ p.getParadaDestino().getDireccion() + ").");
				}
			}
		}
	}

	public List<Colectivo> getColectivosEnSimulacion() {
		return new ArrayList<>(this.colectivosEnSimulacion);
	}

	public Map<String, Linea> getLineasDisponibles() {
		return this.lineasDisponibles;
	}

    public GestorEstadisticas getGestorEstadisticas() {
        return gestorEstadisticas;
    }
    public PlanificadorRutas getPlanificadorRutas() {
        return planificadorRutas;
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
		return null;
	}
}