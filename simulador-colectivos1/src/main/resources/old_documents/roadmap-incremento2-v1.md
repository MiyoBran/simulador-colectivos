# **Roadmap del Proyecto: Simulador de Colectivos Urbanos \- Incremento 2**

Última Actualización: 2025-06-05 (Detalle del Incremento 2\)  
Desarrollador: MiyoBran  
Documento de Contexto Principal: prompt-proyecto.md  
Documento Maestro del Proyecto: roadmap-proyecto-definitivo.md  
Este documento detalla la planificación específica y las decisiones de diseño para el **Incremento 2** del proyecto "Simulador de Colectivos Urbanos". Se basa en el roadmap definitivo y consolida las discusiones y mejoras propuestas.

## **1\. Objetivos Principales del Incremento 2**

**Fecha Límite Estimada:** 2025-06-24

🎯 El Incremento 2 expande significativamente la funcionalidad de la simulación, añadiendo realismo temporal, gestión de múltiples colectivos, recolección de estadísticas avanzadas y la capacidad de planificar rutas. Los objetivos clave son:

* Permitir que los colectivos realicen **múltiples recorridos** y gestionen su estado (en ruta, esperando en terminal).  
* Introducir una noción de **tiempo en la simulación** y gestión de horarios/frecuencias (leyendo horarios\_pm.txt).  
* Implementar **control de capacidad estricto** en los colectivos.  
* Recolectar y mostrar **estadísticas básicas** de la simulación:  
  * Pasajeros transportados.  
  * Tiempo promedio de espera de pasajeros (aproximado).  
  * Ocupación promedio de los colectivos (por tramos).  
  * (Opcional) Índice de satisfacción del pasajero.  
* Implementar la representación de la red de transporte como un **grafo** utilizando net.datastructures.AdjacencyMapGraph.  
* Implementar funcionalidad de **cálculo de rutas básicas** para pasajeros utilizando GraphAlgorithms.  
* Documentación completa del proyecto (JavaDoc, README actualizado).

## **2\. Tareas Detalladas para Incremento 2 por Paquete**

Esta sección detalla las modificaciones y adiciones de clases para alcanzar los objetivos del Incremento 2\.

### **2.1. Paquete modelo (Refinamientos)**

* **Pasajero.java**:  
  * **Atributos a Añadir:**  
    * double tiempoTotalEspera; // En minutos  
    * double tiempoInicioEspera; // Timestamp cuando empezó a esperar  
    * List\<Pasajero.ColectivoObservado\> colectivosObservados; // Para registrar colectivos vistos y llenos.  
  * **Clase Interna a Añadir:** public static class ColectivoObservado { ... }  
    * Atributos: String idColectivo, String lineaId, double tiempoObservacion, boolean estabaLleno.  
    * Constructor y getters.  
  * **Métodos a Implementar/Ajustar:**  
    * void iniciarEspera(double tiempoActual): Registra el tiempoActual cuando el pasajero comienza a esperar.  
    * void finalizarEspera(double tiempoActual): Calcula tiempoTotalEspera al terminar la espera.  
    * void registrarColectivoPasado(Colectivo colectivo, double tiempoActual, boolean estabaLleno): Registra un colectivo que pasó por la parada del pasajero, incrementando colectivosEsperados si estaba lleno.  
    * int calcularSatisfaccion(): Implementa la fórmula de satisfacción (penalización por colectivosEsperados y tiempoTotalEspera).  
    * Asegurar getters y setters para los nuevos campos.  
* **Colectivo.java**:  
  * **Atributos a Añadir:**  
    * double tiempoActualEnSimulacion; // El tiempo actual interno del colectivo en la simulación (para saber cuándo está listo para la siguiente acción).  
    * double tiempoInicioServicio; // Minutos desde inicio de simulación para que este colectivo comience su primer recorrido.  
    * Map\<String, Double\> tiemposPorTramo; // Minutos para viajar entre paradas, clave: "IDParadaOrigen-IDParadaDestino".  
    * EstadoColectivo estado; // EN\_RUTA, EN\_PARADA, EN\_TERMINAL\_ESPERANDO, etc. (requiere un enum).  
    * boolean activo; // Si está en servicio (puede que un colectivo tenga un tiempo de inicio tardío).  
    * Linea.Direccion direccionActual; // Para líneas de ida y vuelta que comparten el mismo ID de línea.  
  * **Métodos a Implementar/Ajustar:**  
    * void setTiempoInicioServicio(double tiempoInicioServicio): Para asignar el momento de su primera salida.  
    * void configurarTiemposEntreParadas(Map\<String, Double\> tiemposTramos): Para establecer los tiempos de viaje de sus tramos.  
    * boolean estaActivo(double tiempoActualSimulacion): Verifica si el colectivo ya debería estar en servicio según tiempoActualSimulacion y tiempoInicioServicio.  
    * Parada avanzarAProximaParada(double tiempoActualSimulacion):  
      * Modificar para que, además de actualizar la paradaActual e indiceParadaActualEnRecorrido, actualice tiempoActualEnSimulacion del colectivo sumando el tiempo de viaje del tramo completado (usando getTiempoAProximaParada()).  
      * Debe cambiar el estado del colectivo.  
    * double getTiempoAProximaParada(): Calcula el tiempo estimado para el próximo tramo usando tiemposPorTramo.  
    * Métodos para actualizar y obtener el estado del colectivo.  
    * Lógica para gestionar múltiples recorridos (ej., si llega a terminal, puede "reiniciar" su recorrido o cambiar de dirección).  
* **Parada.java**:  
  * **Atributos a Añadir (para estadísticas locales):**  
    * double maxTiempoEsperaPasajero;  
    * int maxPersonasEnCola;  
    * double tiempoTotalEsperaAcumulado;  
    * int pasajerosAtendidos;  
  * **Métodos a Implementar/Ajustar:**  
    * void registrarLlegadaPasajero(Pasajero p, double tiempoActual): Marca el tiempoInicioEspera del pasajero y actualiza maxPersonasEnCola.  
    * Pasajero removerSiguientePasajero(double tiempoActual): Al remover, se actualiza tiempoTotalEsperaAcumulado.

### **2.2. Paquete datos**

* **LectorArchivos.java**:  
  * **Extender para leer horarios\_pm.txt:** Este archivo probablemente contiene las frecuencias de las líneas y/o los tiempos de viaje entre paradas. Deberá parsear este archivo y cargar la información en estructuras adecuadas (ej., Map\<String, Double\> para frecuencias o Map\<String, Map\<String, Double\>\> para tiempos por tramo).  
  * **Decisión clave de Estructuras de Datos Globales (Map\<String, Parada\>, Map\<String, Linea\>):**  
    * **Implementación:** Se decide utilizar java.util.TreeMap (o net.datastructures.TreeMap si se quiere mayor familiaridad con la librería de la cátedra para el desarrollo de I2) para las colecciones globales de paradasCargadas y lineasCargadas.  
    * **Justificación:**  
      * **Alineación con el Ejemplo "subte" de la Cátedra:** El ejemplo "subte" utiliza TreeMap para la gestión de sus datos maestros, lo que sugiere que esta es la preferencia de la cátedra para este tipo de estructuras globales.  
      * **Ordenamiento Intrínseco:** TreeMap mantiene sus entradas ordenadas por las claves (IDs de paradas o líneas). Esto es un factor de peso significativo, ya que facilita la depuración, la generación de reportes ordenados por ID, y la presentación de datos en la interfaz sin necesidad de ordenar la colección explícitamente cada vez. Esto es particularmente útil para las estadísticas "por línea" o "por parada".  
      * **Rendimiento:** Si bien HashMap ofrece un rendimiento promedio de O(1) para operaciones básicas de mapa y TreeMap es O(logn), para el tamaño de los conjuntos de datos (104 paradas, 12 líneas), la diferencia de rendimiento es **totalmente insignificante** en la práctica. El beneficio del ordenamiento y la alineación con la cátedra superan con creces cualquier micro-optimización de rendimiento en este contexto.  
      * **Nota sobre HashMap (contexto):** Tenemos presente que HashMap fue utilizado en el Incremento 1 por su simplicidad y eficiencia O(1) promedio para el acceso por clave, y que sigue siendo una opción muy eficiente. Sin embargo, la ventaja del ordenamiento intrínseco de TreeMap y la directriz de la cátedra justifican este refactoring para el Incremento 2\.

### **2.3. Paquete logica**

* **Simulador.java (Extensión del Motor de Simulación):**  
  * **Atributos a Añadir/Modificar:** double tiempoActualSimulacion;, Map\<String, Double\> frecuenciasColectivos;.  
  * **Métodos a Implementar/Ajustar:**  
    * void configurarFrecuencias(Map\<String, Double\> frecuenciasPorLinea): Para que el Simulador sepa cada cuánto salen los colectivos de cada línea.  
    * void inicializarMultiplesColectivos(int capacidadColectivo, double tiempoTotalRecorrido): Reemplaza a la versión anterior. Crea múltiples instancias de Colectivo por línea, distribuyendo sus tiempoInicioServicio según la frecuencia de la línea.  
    * void ejecutarSimulacionAvanzada(double tiempoMaximoSimulacion):  
      * Este será el nuevo bucle principal de la simulación.  
      * Avanzará tiempoActualSimulacion en pasos discretos (ej. 1, 5, 10 minutos).  
      * En cada paso de tiempo, iterará sobre colectivosEnSimulacion.  
      * Solo procesará aquellos colectivos que estén estaActivo() y cuyo tiempoActualEnSimulacion sea igual o menor al tiempoActualSimulacion del simulador (es decir, estén "listos" para su próxima acción).  
      * Llamará a procesarColectivoEnParada(Colectivo colectivo, double tiempoActualSimulacion).  
      * Gestionará el cambio de recorrido o dirección de los colectivos al llegar a las terminales para permitir múltiples viajes.  
      * Condiciones de finalización: tiempoActualSimulacion alcanza tiempoMaximoSimulacion o todosPasajerosAtendidos().  
    * private void procesarColectivoEnParada(Colectivo colectivo, double tiempoActualSimulacion): Lógica de bajada/subida de pasajeros en una parada específica, actualizando los tiempos y estados de Pasajero y Colectivo.  
    * private boolean todosPasajerosAtendidos(): Verifica si todos los pasajeros generados llegaron a su destino.  
    * private void generarEstadisticasSimulacion(): Recopila y muestra las estadísticas finales.  
* **GestorEstadisticas.java (Nueva Clase):**  
  * **Propósito:** Centralizar la recolección, cálculo y reporte de todas las métricas de la simulación.  
  * **Atributos:** Listas para registrar eventos (ej., List\<EventoPasajero\>, List\<EventoOcupacion\>), o referencias a los Pasajeros completados.  
  * **Métodos:** registrarSubida(Pasajero p, Colectivo c, double tiempo), registrarBajada(...), calcularTiempoPromedioEspera(), calcularOcupacionPromedioPorLinea(), calcularSatisfaccionPromedio(), generarReporteConsola().  
* **PlanificadorRutas.java (Nueva Clase Central de Grafos):**  
  * **Propósito:** Modelar la red de transporte como un grafo para encontrar rutas óptimas.  
  * **Implementación:** Utilizará net.datastructures.AdjacencyMapGraph.  
  * **Atributo:** private Graph\<Parada, Object\> grafoRedTransporte; (donde Object representará el peso de la arista, ej., Double para tiempo o distancia).  
  * **Constructor:** PlanificadorRutas(Map\<String, Parada\> todasLasParadas, Map\<String, Linea\> todasLasLineas):  
    * Inicializa grafoRedTransporte \= new AdjacencyMapGraph\<\>(true); (dirigido).  
    * Inserta todas las Paradas como vértices.  
    * Itera sobre las Lineas para añadir aristas dirigidas entre paradas consecutivas en el recorrido de cada línea.  
    * El peso de la arista se determinará (ej., 1 para un tramo, o la distancia geográfica/tiempo estimado usando datos de tiemposPorTramo o horarios\_pm.txt).  
  * **Método:** public List\<Parada\> encontrarRutaMasCorta(Parada origen, Parada destino):  
    * Utilizará GraphAlgorithms.shortestPath() de net.datastructures.  
    * Adaptará la salida para una secuencia de paradas o instrucciones.

### **2.4. Paquete interfaz**

* **SimuladorColectivosApp.java (Mejoras):**  
  * Modificar el main para utilizar el nuevo ejecutarSimulacionAvanzada.  
  * Ajustar la lectura de parámetros (tiempoMaximoSimulacion, frecuencias) desde config.properties.  
  * Mostrar las estadísticas detalladas al final de la simulación, obtenidas de GestorEstadisticas.  
  * Añadir una opción en el menú textual para consultar PlanificadorRutas (ej., "Encontrar ruta entre dos paradas").

### **2.5. Paquete test**

* **PasajeroTest.java (Extensión):**  
  * Pruebas para los nuevos atributos de tiempo y ColectivoObservado.  
  * Pruebas para iniciarEspera, finalizarEspera, registrarColectivoPasado.  
  * Pruebas para calcularSatisfaccion().  
* **ColectivoTest.java (Extensión):**  
  * Pruebas para tiempoInicioServicio, tiemposPorTramo, estado.  
  * Pruebas para estaActivo(), configurarTiemposEntreParadas(), getTiempoAProximaParada().  
  * Pruebas detalladas para avanzarAProximaParada(double tiempoActual) verificando el estado temporal.  
* **SimuladorTest.java (Extensión y Nueva Clase):**  
  * Pruebas para inicializarMultiplesColectivos (cantidad, distribución de tiempos de inicio).  
  * Pruebas exhaustivas para ejecutarSimulacionAvanzada: avance de tiempo, procesamiento de colectivos activos, múltiples recorridos, bajadas/subidas correctas, condiciones de finalización.  
* **GestorEstadisticasTest.java (Nueva Clase de Test):**  
  * Pruebas para el registro de eventos y el cálculo correcto de todas las estadísticas (tiempo de espera, ocupación, satisfacción).  
* **PlanificadorRutasTest.java (Nueva Clase de Test):**  
  * Pruebas para la correcta construcción del grafo (AdjacencyMapGraph) con vértices (paradas) y aristas (tramos de línea).  
  * Pruebas para la funcionalidad encontrarRutaMasCorta con diferentes escenarios (origen/destino, rutas directas, transbordos implícitos).

## **3\. Reglas y Buenas Prácticas para Incremento 2**

Se mantienen y refuerzan las reglas del Incremento 1:

* **E/S y Consola:** System.out.println o similares solo en SimuladorColectivosApp.java y clases de prueba. Prohibido en clases de modelo, lógica y datos (salvo para logging justificado).  
* **Encapsulamiento:** Todos los atributos private, acceso vía getters y setters según necesidad.  
* **Invariantes:** Validación estricta de parámetros en constructores y métodos.  
* **Estructura de Carpetas:** Estricto respeto de la arquitectura por capas.  
* **Manejo de Errores:** Uso de excepciones para errores de carga o lógica inválida.  
* **JavaDoc:** Documentación completa para todas las clases y métodos públicos.

Este documento detalla un plan muy completo para el Incremento 2\. Proporciona la claridad necesaria para avanzar paso a paso.