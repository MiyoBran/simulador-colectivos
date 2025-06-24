package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import ar.edu.unpsjb.ayp2.proyectointegrador.interfaz.SimuladorConfig;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;

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
 * @version 1.1
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

	private int pasoActual = 0;
	private boolean simulacionTerminada = false;

	// =================================================================================
	// CONSTRUCTORES
	// =================================================================================

	/**
	 * Constructor principal del simulador.
	 *
	 * @param lineas           Mapa de líneas disponibles para la simulación.
	 * @param paradas          Mapa de paradas disponibles (usado para validación).
	 * @param pasajeros        Lista de todos los pasajeros que participarán.
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
	}

	// =================================================================================
	// MÉTODOS DE INICIALIZACIÓN
	// =================================================================================

	/**
	 * Inicializa todos los colectivos de la simulación basándose en los parámetros
	 * del archivo de configuración. Este es el único método para crear los colectivos.
	 *
	 * @param capacidadTotal    Capacidad máxima total de un colectivo.
	 * @param capacidadSentados Capacidad de asientos de un colectivo.
	 */
	public void inicializarColectivos(int capacidadTotal, int capacidadSentados) {
		// 1. Leer parámetros desde la configuración
		int recorridosPorColectivo = SimuladorConfig.obtenerRecorridosPorColectivo(this.configProperties);
		int cantidadPorLinea = SimuladorConfig.obtenerCantidadColectivosSimultaneosPorLinea(this.configProperties);
		int frecuenciaMin = SimuladorConfig.obtenerFrecuenciaSalidaColectivosMinutos(this.configProperties);
		int capacidadParados = capacidadTotal - capacidadSentados;
		
		// 2. Validar parámetros leídos
		if (capacidadTotal <= 0 || capacidadSentados < 0 || capacidadParados < 0 || recorridosPorColectivo <= 0 || cantidadPorLinea <= 0 || frecuenciaMin <= 0) {
			throw new IllegalArgumentException("Las capacidades, recorridos y cantidad de colectivos deben ser positivos.");
		}

		// 3. Limpiar estado anterior y calcular pasos de frecuencia
		this.colectivosEnSimulacion.clear();
		this.colectivosPendientesDeAvanzar.clear();
		this.pasoActual = 0;
		int pasosPorFrecuencia = (int) Math.ceil(frecuenciaMin / 2.0); // Asumiendo 2 min por paso
		int colectivoCounter = 1;

		// 4. Crear los colectivos para cada línea
		for (Linea linea : lineasDisponibles.values()) {
			for (int i = 0; i < cantidadPorLinea; i++) {
				String idColectivo = "C" + colectivoCounter + "-" + linea.getId();
				int pasoDeSalida = i * pasosPorFrecuencia; // Salidas escalonadas

				Colectivo nuevoColectivo = new Colectivo(idColectivo, linea, capacidadTotal, capacidadSentados,
						capacidadParados, recorridosPorColectivo, pasoDeSalida);
				
				this.colectivosEnSimulacion.add(nuevoColectivo);
				this.gestorEstadisticas.registrarCapacidadColectivo(idColectivo, capacidadTotal);
				colectivoCounter++;
			}
		}
	}

	/**
	 * Ejecuta un paso de simulación. Este método avanza los colectivos pendientes
	 * de avanzar, procesa los colectivos en su parada actual (puede ser la
	 * terminal), y maneja la lógica de subida y bajada de pasajeros.
	 * 
	 * @return Lista de eventos generados en este paso de simulación. *
	 *
	 */
    /**
     * Minutos que representa cada paso/ciclo de simulación.
     */
    private static final int MINUTOS_POR_PASO_POR_CICLO = 2;

public List<String> ejecutarPasoDeSimulacion() {
        List<String> eventosDelPaso = new ArrayList<>();
        // --- DEBUG: Separador de pasos ---
        System.out.println("\n==================== PASO DE SIMULACIÓN ====================\n");
        // --- NUEVO: Actualizar tiempos de espera y viaje ---
       // final int MINUTOS_POR_PASO = MINUTOS_POR_PASO_POR_CICLO;
        /*/ 1. Incrementar tiempo de espera de pasajeros en paradas
        for (Linea linea : lineasDisponibles.values()) {
            for (Parada parada : linea.getRecorrido()) {
                for (Pasajero pasajero : parada.getPasajerosEsperando()) {
                    pasajero.setTiempoEspera(pasajero.getTiempoEspera() + MINUTOS_POR_PASO);
                }
            }
        }*/
        /*/ 2. Incrementar tiempo de viaje de pasajeros a bordo de colectivos
        for (Colectivo colectivo : colectivosEnSimulacion) {
            for (Pasajero pasajero : colectivo.getPasajerosABordo()) {
                pasajero.setTiempoViaje(pasajero.getTiempoViaje() + MINUTOS_POR_PASO);
            }
        }*/
        // 1. Avanzar colectivos pendientes (deben avanzar al inicio del paso)
        if (!colectivosPendientesDeAvanzar.isEmpty()) {
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
        // 2. Procesar colectivos en su parada actual (puede ser la terminal)
        for (Colectivo colectivo : colectivosEnSimulacion) {
            if (!colectivo.estaEnTerminal()) {
                // Procesar colectivos que YA están en terminal desde el paso anterior
                // Nota: La lógica de procesamiento de terminal se excluye aquí intencionalmente
                // para evitar mensajes duplicados, ya que los colectivos que llegan a la terminal
                // en este paso ya se procesan en el primer bucle.
                // procesarLogicaTerminal(colectivo, eventosDelPaso);
          
            	
                // Registrar ocupación por tramo antes de procesar el paso
                gestorEstadisticas.registrarOcupacionTramo(colectivo.getIdColectivo(), colectivo.getCantidadPasajerosABordo());
                // Procesar parada actual
                procesarPasoParaColectivo(colectivo, eventosDelPaso);
            }
        }
        pasoActual++;
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
		if(!this.simulacionTerminada) {
			for(Pasajero p : pasajerosSimulados) {
				if (!p.isPudoSubir()) {
					gestorEstadisticas.registrarTransporte(p); 
				}
			}
			this.simulacionTerminada = true; // Marcar como terminada una vez que se procesan todos los pasajeros
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
        for (Colectivo colectivo : colectivosEnSimulacion) {
            if (colectivo.getCantidadPasajerosABordo() > 0) {
                reporte.add(
                        "ADVERTENCIA: El colectivo " + colectivo.getIdColectivo() + " terminó con pasajeros a bordo.");
            }
        }
        // Registrar capacidad de cada colectivo para ocupación promedio
        for (Colectivo colectivo : colectivosEnSimulacion) {
            gestorEstadisticas.registrarCapacidadColectivo(colectivo.getIdColectivo(), colectivo.getCapacidadMaxima());
        }
        // Registrar calificación de satisfacción de TODOS los pasajeros simulados
        if (pasajerosSimulados != null) {
            for (Pasajero pasajero : pasajerosSimulados) {
                int calificacion = calcularCalificacionSatisfaccion(pasajero);
                gestorEstadisticas.registrarCalificacionSatisfaccion(calificacion);
            }
        }
        return reporte;
    }

    /**
     * Calcula la calificación de satisfacción de un pasajero según Anexo I.
     */
    private int calcularCalificacionSatisfaccion(Pasajero pasajero) {
        return pasajero.calcularSatisfaccion(); // Caso por defecto
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
		
		//optimizacion: evitar crear una copia de la lista de pasajeros a bordo
		for (Pasajero p : colectivo.getPasajerosABordo()) {
			if (p.getParadaDestino().equals(paradaActual)) {
				colectivo.bajarPasajero(p);
				eventos.add("  - Bajó " + p + " en su destino.");
				gestorEstadisticas.registrarTransporte(p); // Registrar transporte del pasajero
			
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
        List<Pasajero> pasajerosQueSubieron = new ArrayList<>();
        List<Pasajero> pasajerosEnEspera = new ArrayList<>(paradaActual.getPasajerosEsperando());

        int pasajerosEnParadaAlInicio = paradaActual.cantidadPasajerosEsperando();
        int intentos = 0;
        for (Pasajero pasajero : pasajerosEnEspera) {
            
            intentos++;
            // Solo permitir subir si el destino está más adelante en el recorrido
            int idxActual = colectivo.getLineaAsignada().getRecorrido().indexOf(paradaActual);
            int idxDestino = colectivo.getLineaAsignada().getRecorrido().indexOf(pasajero.getParadaDestino());
            if (colectivo.getLineaAsignada().tieneParadaEnRecorrido(pasajero.getParadaDestino()) && idxDestino > idxActual) {
                boolean pudoSubir = colectivo.subirPasajero(pasajero);
                
                if (pudoSubir) {
                	pasajerosQueSubieron.add(pasajero);
                    pasajerosSubidos++;
                    pasajero.setPudoSubir(true);
                    
                    eventos.add("  + Subió pasajero " + pasajero.getId() + " (destino: "
                            + pasajero.getParadaDestino().getDireccion() + ")");
                } else {
                    pasajero.incrementarColectivosEsperados();
                    //pasajero.setSubioAlPrimerColectivoQuePaso(false); // No subió al primer colectivo
                    eventos.add("  - Pasajero " + pasajero.getId()
                            + " no pudo subir (colectivo lleno). Esperando al siguiente.");
                    
                }
            } 
            //ya no se tiene que insertar otra vez en la lista ya que lo que se hizo es mirar el siguiente pasajero
        }
        paradaActual.getPasajerosEsperando().removeAll(pasajerosQueSubieron); // Actualizar la lista de pasajeros esperando

        eventos.add("  Cantidad de pasajeros que subieron en esta parada: " + pasajerosSubidos);
        eventos.add("  Pasajeros restantes esperando en parada: " + paradaActual.cantidadPasajerosEsperando());
    }

	/**
	 * Procesa la lógica cuando un colectivo ha finalizado su recorrido.
	 */
	private void procesarLogicaTerminal(Colectivo colectivo, List<String> eventos) {
		Parada paradaFinal = colectivo.getParadaActual();
		String paradaInfo = (paradaFinal != null) ? paradaFinal.getDireccion() + " (ID: " + paradaFinal.getId() + ")" : "N/A";
		eventos.add("Colectivo " + colectivo.getIdColectivo() + " de la línea "
				+ colectivo.getLineaAsignada().getNombre() + " ha finalizado su recorrido " + colectivo.getRecorridoActual() + " en: " + paradaInfo);
		
		colectivo.actualizarRecorridosRestantes();

		// Bajar a todos los pasajeros restantes
		if (colectivo.getCantidadPasajerosABordo() > 0) {
			eventos.add("  Procesando pasajeros en la parada terminal...");
			List<Pasajero> pasajerosCopia = new ArrayList<>(colectivo.getPasajerosABordo());
			for (Pasajero p : pasajerosCopia) {
				colectivo.bajarPasajero(p);
				if (p.getParadaDestino().equals(paradaFinal)) {
					eventos.add("  - Bajó " + p + " en su destino (terminal).");
				} else {
					p.setBajadaForzosa(true);
					eventos.add("  - BAJADA FORZOSA: " + p + " no llegó a su destino (" + p.getParadaDestino().getDireccion() + ").");
				}
				gestorEstadisticas.registrarTransporte(p);
			}
		}

		// Preparar para el siguiente recorrido o finalizar
		if (colectivo.getRecorridosRestantes() > 0) {
			colectivo.reiniciarParaNuevoRecorrido();
			
			// MEJORA DE LEGIBILIDAD PARA LA CONSOLA
			String separador = "----->";
			String eventoReinicio = String.format("\n%s EVENTO: Colectivo %s reiniciado para un nuevo recorrido %s\n",
					separador, colectivo.getIdColectivo(), separador);
			eventos.add(eventoReinicio);
			
			colectivosPendientesDeAvanzar.add(colectivo.getIdColectivo()); // Marcar para avanzar
		} else {
			eventos.add("  Colectivo " + colectivo.getIdColectivo() + " ha finalizado todos sus recorridos.");
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

    /**
     * Reporte extendido para depuración: muestra el total de pasajeros generados,
     * los que quedaron esperando en paradas y los que bajaron forzosamente en la terminal.
     */
    public void imprimirReportePasajeros() {
        // Usar el desglose de GestorEstadisticas para asegurar consistencia
        if (gestorEstadisticas != null) {
            var desglose = gestorEstadisticas.getDesglosePasajeros();
            int totalGenerados = gestorEstadisticas.getPasajerosTotales();
            int transportados = desglose.getOrDefault("transportados", 0);
            int bajadosForzosamente = desglose.getOrDefault("bajadosForzosamente", 0);
            int nuncaSubieron = desglose.getOrDefault("nuncaSubieron", 0);
            int suma = transportados + bajadosForzosamente + nuncaSubieron;
            System.out.println(String.format("\n--- Reporte de Pasajeros ---"));
            System.out.println(String.format("Total de pasajeros generados: %d", totalGenerados));
            System.out.println(String.format("Pasajeros transportados: %d", transportados));
            System.out.println(String.format("Pasajeros bajados forzosamente en terminal: %d", bajadosForzosamente));
            System.out.println(String.format("Pasajeros que nunca subieron a un colectivo: %d", nuncaSubieron));
            if (suma != totalGenerados) {
                System.err.println(String.format("[ADVERTENCIA] La suma de pasajeros reportados no coincide con el total generado. Suma: %d, Total generados: %d", suma, totalGenerados));
            }
        } else {
            System.out.println(String.format("[ERROR] No hay gestor de estadísticas disponible para el reporte de pasajeros."));
        }
    }

    /**
     * DEBUG: Imprime los IDs de los pasajeros esperando en cada parada y verifica duplicados.
     * Quitar o comentar este método cuando no se necesite más debug.
     */
    public void imprimirDebugPasajerosEsperandoPorParada() {
        System.out.println(String.format("\n--- DEBUG: Pasajeros esperando por parada ---"));
        Set<String> idsVistos = new HashSet<>();
        boolean hayDuplicados = false;
        Set<Parada> paradasUnicas = new HashSet<>();
        for (Linea linea : lineasDisponibles.values()) {
            paradasUnicas.addAll(linea.getRecorrido());
        }
        for (Parada parada : paradasUnicas) {
            List<Pasajero> esperando = new ArrayList<>(parada.getPasajerosEsperando());
            if (!esperando.isEmpty()) {
                System.out.println(String.format("Parada %s (%s):", parada.getId(), parada.getDireccion()));
                for (Pasajero p : esperando) {
                    System.out.println(String.format("  - %s", p.getId()));
                    if (!idsVistos.add(p.getId())) {
                        System.out.println(String.format("    [DUPLICADO] El pasajero %s ya fue listado en otra parada!", p.getId()));
                        hayDuplicados = true;
                    }
                }
            }
        }
        if (!hayDuplicados) {
            System.out.println(String.format("No se detectaron pasajeros duplicados en las paradas."));
        }
    }
}
