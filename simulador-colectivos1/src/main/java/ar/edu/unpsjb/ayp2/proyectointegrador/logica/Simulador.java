package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

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
    private List<Pasajero> pasajerosSimulados;
    private int pasoActual = 0;
    private int pasosPorFrecuencia = 1;
    private Properties configProperties;

    /**
     * Constructor del simulador. Permite inyectar dependencias para facilitar el testeo.
     * Si gestorEstadisticas o planificadorRutas son nulos, se crean instancias por defecto.
     *
     * @param lineas    Mapa de líneas disponibles para la simulación.
     * @param paradas   Mapa de paradas disponibles para la simulación.
     * @param pasajeros Lista de pasajeros que participarán en la simulación.
     * @param gestorEstadisticas (opcional) Gestor de estadísticas a utilizar.
     * @param planificadorRutas (opcional) Planificador de rutas a utilizar.
     * @param configProperties Propiedades de configuración para la simulación.
     */
    public Simulador(Map<String, Linea> lineas, Map<String, Parada> paradas, List<Pasajero> pasajeros,
                     GestorEstadisticas gestorEstadisticas, PlanificadorRutas planificadorRutas, Properties configProperties) {
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
        this.pasajerosSimulados = pasajeros; // Guardar referencia a todos los pasajeros simulados
        this.configProperties = configProperties;
    }

    /**
     * Constructor original para compatibilidad: instancia dependencias por defecto.
     */
    public Simulador(Map<String, Linea> lineas, Map<String, Parada> paradas, List<Pasajero> pasajeros, Properties configProperties) {
        this(lineas, paradas, pasajeros, null, null, configProperties);
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
            // Registrar capacidad máxima para estadísticas de ocupación promedio (Anexo II)
            if (gestorEstadisticas != null) {
                gestorEstadisticas.registrarCapacidadColectivo(idColectivo, capacidadColectivo);
            }
            colectivoCounter++;
        }
        this.colectivosPendientesDeAvanzar.clear();
    }

    /**
     * Inicializa los colectivos en la simulación según la cantidad de colectivos simultáneos por línea.
     *
     * @param capacidadColectivo Capacidad máxima de pasajeros por colectivo.
     * @param capacidadSentados Capacidad máxima de pasajeros sentados.
     * @param capacidadParados Capacidad máxima de pasajeros parados.
     * @param recorridosRestantes Cantidad de recorridos que debe realizar el colectivo.
     * @param cantidadColectivosPorLinea Cantidad de colectivos simultáneos por línea.
     */
    public void inicializarColectivos(int capacidadColectivo, int capacidadSentados, int capacidadParados, int recorridosRestantes, int cantidadColectivosPorLinea) {
        if (capacidadColectivo <= 0 || capacidadSentados < 0 || capacidadParados < 0 || recorridosRestantes <= 0 || cantidadColectivosPorLinea <= 0) {
            throw new IllegalArgumentException("Las capacidades, recorridos y cantidad de colectivos deben ser positivos.");
        }
        int frecuenciaMin = ar.edu.unpsjb.ayp2.proyectointegrador.interfaz.SimuladorConfig.obtenerFrecuenciaSalidaColectivosMinutos(this.configProperties);
        if (frecuenciaMin <= 0) {
            throw new IllegalArgumentException("La frecuencia mínima de salida de colectivos debe ser mayor a cero.");
        }
        this.pasosPorFrecuencia = (int) Math.ceil(frecuenciaMin / 2.0);
        this.colectivosEnSimulacion.clear();
        int colectivoCounter = 1;
        for (Linea linea : lineasDisponibles.values()) {
            for (int i = 0; i < cantidadColectivosPorLinea; i++) {
                int pasoDeSalida = i * pasosPorFrecuencia;
                String idColectivo = "C" + colectivoCounter + "-" + linea.getId();
                Colectivo nuevoColectivo = new Colectivo(idColectivo, linea, capacidadColectivo, capacidadSentados, capacidadParados, recorridosRestantes, pasoDeSalida);
                this.colectivosEnSimulacion.add(nuevoColectivo);
                // Registrar capacidad máxima para estadísticas de ocupación promedio (Anexo II)
                if (gestorEstadisticas != null) {
                    gestorEstadisticas.registrarCapacidadColectivo(idColectivo, capacidadColectivo);
                }
                colectivoCounter++;
            }
        }
        this.colectivosPendientesDeAvanzar.clear();
        this.pasoActual = 0;
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
		for(Pasajero p : pasajerosSimulados) {
			if (!p.isPudoSubir()) {
				gestorEstadisticas.registrarTransporte(p); 
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
					gestorEstadisticas.registrarTransporte(p); // Registrar como transportado
				} else {
					p.setBajadaForzosa(true);
					// Satisfacción mínima para bajada forzosa
					p.setSatisfaccion(1); // 1 el valor mínimo que corresponda
					eventos.add("  - BAJADA FORZOSA: " + p + " no llegó a su destino ("
							+ p.getParadaDestino().getDireccion() + ").");
					gestorEstadisticas.registrarTransporte(p); // Registrar como transportado aunque bajó forzosamente
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