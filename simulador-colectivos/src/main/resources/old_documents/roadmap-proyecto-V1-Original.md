# Roadmap del Proyecto: Simulador de Colectivos Urbanos (Puerto Madryn)
*Última Actualización: 2025-05-28 (MiyoBran & Copilot)*
*Referencia de Hora para Actualización: 2025-05-28 03:22:34 UTC*

## Principios Guía y Uso de TADs (Enfoque Híbrido)

El proyecto adopta un **enfoque híbrido** para el uso de TADs:

1.  **Java Collections Framework (`java.util`):**
    *   **Uso Principal:** Para las colecciones internas de las entidades del modelo de simulación (`Parada`, `LineaColectivo`, `Colectivo`).
        *   `Parada.pasajerosEsperando`: `java.util.Queue` (implementado con `java.util.LinkedList`).
        *   `LineaColectivo.recorrido`: `java.util.List<Parada>` (implementado con `java.util.ArrayList`).
        *   `Colectivo.pasajerosABordo`: `java.util.List<Pasajero>` (implementado con `java.util.ArrayList`).
    *   **Justificación:** Robustez, API completa, estándar Java, simplicidad para la lógica de simulación directa.

2.  **Librería `net.datastructures` (versión `6.0.0-custom`):**
    *   **Uso Específico:**
        *   **Modelado de Grafos y Pathfinding:** Si se implementa funcionalidad avanzada de cálculo de rutas que involucre múltiples líneas y transbordos (potencialmente en el Incremento 2 o posterior), se utilizará `net.datastructures.AdjacencyMapGraph` y `GraphAlgorithms`.
        *   **Almacenamiento Global de Datos Cargados:** Para la capa de datos (`LectorArchivos`), si se requiere un almacenamiento centralizado y ordenado de todas las paradas y líneas (similar a los `TreeMap` del ejemplo "subte"), se podría optar por `net.datastructures.TreeMap`. Actualmente, `LectorArchivos` usa `java.util.HashMap` por simplicidad.
    *   **Justificación:** Aprovechar las implementaciones especializadas de la librería de la cátedra para tareas complejas de grafos y alinearse con el ejemplo "subte" en estas áreas específicas.

## Lecciones y Referencias del Ejemplo "subte" de la Cátedra

El proyecto "subte" sirve como referencia conceptual, especialmente en:
*   **Gestión de Datos a Nivel de Aplicación:** Utiliza `net.datastructures.TreeMap` para mantener colecciones globales de estaciones y líneas, cargadas desde archivos.
*   **Implementación de Grafos:** Su clase `Calculo` demuestra cómo usar `AdjacencyMapGraph` y `GraphAlgorithms` para encontrar caminos óptimos.
*   **POJOs para el Modelo Base:** Las entidades `Estacion`, `Linea`, `Tramo` en "subte" son POJOs simples.
*   **Diferencia de Enfoque:** "Subte" se centra en pathfinding estático. Nuestro proyecto se centra en simulación dinámica.

---

## Fase Actual (Post-Implementación y Testeo de `LectorArchivos.java`)

1.  **Configuración del Entorno:** Completa.
2.  **Definición de Arquitectura y TADs:** Enfoque híbrido definido.
3.  **Análisis de Referencias ("subte"):** Completado.
4.  **Clases del Modelo Iniciales (`java.util` para colecciones internas):**
    *   `modelo.Pasajero`: Completa.
    *   `modelo.Parada`: Completa (incluye ahora latitud y longitud).
    *   `modelo.LineaColectivo`: Completa.
    *   `modelo.Colectivo`: Completa.
5.  **Pruebas JUnit Iniciales:**
    *   `PasajeroTest.java`: Completa y funcionando.
    *   `ParadaTest.java`: Completa y funcionando (adaptada a nuevos constructores con lat/lon).
    *   `LineaColectivoTest.java`: Completa y funcionando.
    *   `ColectivoTest.java`: Completa y funcionando.
6.  **Capa de Datos Inicial:**
    *   `datos.LectorArchivos`: Implementado y funcionando. Carga paradas (con lat/lon) y líneas desde archivos de texto. Usa `java.util.HashMap` internamente.
    *   `datos.LectorArchivosTest`: Completo y funcionando. Cubre carga exitosa y manejo de errores de formato y datos.
7.  **Archivos de Datos (Puerto Madryn):**
    *   Ubicación: `src/main/resources/datos_pm/`
    *   `paradas_pm.txt`: Formato `id_parada;nombre_parada;latitud;longitud`. **Sin encabezado.**
    *   `lineas_pm.txt`: Formato `id_linea;nombre_linea;id_parada1,id_parada2,...`. **Sin encabezado.**
    *   `horarios_pm.txt`: Formato `id_linea;sentido;dias;hora_inicio;hora_fin;frecuencia_minutos`. **Sin encabezado.** Reservado para Incremento 2+.
8.  **Archivos de Datos de Prueba:**
    *   Ubicación: `src/test/resources/datos_test/`
    *   `paradas_test.txt` y `lineas_test.txt` creados y utilizados por `LectorArchivosTest`. **Sin encabezados.**

---

## Incremento 1: Carga de Datos (Pto. Madryn), Simulación Básica de Un Recorrido

**Fecha Límite Estimada:** (MiyoBran, puedes añadir una fecha si lo deseas)

**Objetivos:**
*   Definir formato y cargar paradas y líneas reales de Puerto Madryn desde archivos (**COMPLETADO**).
*   Generar pasajeros aleatoriamente y distribuirlos en las paradas (**EN PROGRESO**).
*   Simular un único recorrido para un colectivo por cada línea.
*   Mostrar en consola el paso de los colectivos por las paradas, y los pasajeros que suben y bajan.

### 1. Paquete `modelo`
*   **`Parada.java`**: Actualizada para incluir `latitud` y `longitud`. (**COMPLETADO**)
*   **`Colectivo.java`**: Finalizar implementación y asegurar que pase todos los tests. (**COMPLETADO**)

### 2. Paquete `datos`
*   **Tarea**: "Investigar fuentes y formatos de datos para líneas de colectivo y paradas de Puerto Madryn." (**COMPLETADO**)
*   **Tarea**: "Crear archivos de datos de ejemplo para Puerto Madryn" (`paradas_pm.txt`, `lineas_pm.txt`). Ubicados en `src/main/resources/datos_pm/`. **Sin encabezados.** (**COMPLETADO**)
*   **`ConfiguracionSimulacion.java`** (Opcional, similar a `CargarParametros` de "subte"):
    *   Podría cargar rutas de archivos, parámetros de simulación (e.g., cantidad de pasajeros a generar) desde un `config.properties`. (Pendiente, considerar más adelante si es necesario)
*   **`LectorArchivos.java`**:
    *   Implementado para leer `paradas_pm.txt` (con lat/lon) y `lineas_pm.txt`.
    *   Usa `java.util.HashMap` para `paradasCargadas` y `lineasCargadas`.
    *   Maneja errores de formato y datos faltantes.
    *   (**COMPLETADO**)
*   **`LectorArchivosTest.java`**: Pruebas unitarias para `LectorArchivos`. (**COMPLETADO**)

### 3. Paquete `logica`
*   **`GeneradorPasajeros.java`** (Nueva Clase):
    *   **Objetivo:** Crear y devolver una lista de pasajeros con origen y destino aleatorios válidos.
    *   **Método principal**: `public List<Pasajero> generarPasajerosAleatorios(int cantidad, Map<String, Parada> todasLasParadas)`
        *   IDs de pasajeros únicos (e.g., "PAX_1", "PAX_2", ... o UUID).
        *   Asegurar que parada origen y destino sean distintas.
    *   **(PRÓXIMA TAREA)**
*   **`Simulador.java`**:
    *   **Atributos**:
        *   `Map<String, Parada> mapaParadas;`
        *   `Map<String, LineaColectivo> mapaLineas;`
        *   `List<Colectivo> listaColectivosActivos;`
        *   `List<Pasajero> listaPasajerosGlobales;` (todos los generados)
    *   **Constructor**: `public Simulador(Map<String, Parada> paradas, Map<String, LineaColectivo> lineas)`
    *   **Método**: `public void inicializarSimulacion(int numColectivosPorLinea, int capacidadColectivos, int numPasajerosAGenerar)`
        *   Crea `numColectivosPorLinea` para cada línea en `mapaLineas`.
        *   Genera `numPasajerosAGenerar` usando `GeneradorPasajeros`.
        *   Distribuye los pasajeros generados en las colas de sus paradas de origen.
    *   **Método**: `public void ejecutarCicloSimulacionUnicoRecorrido()`
        *   Para cada colectivo en `listaColectivosActivos`:
            *   Procesar bajada de pasajeros en `paradaActual`.
            *   Procesar subida de pasajeros desde `paradaActual.getPasajerosEsperando()`.
            *   Avanzar colectivo a la próxima parada.
            *   Registrar eventos (para la interfaz).
        *   Este método simulará un solo "paso" o un recorrido completo.
    *   **(Pendiente)**

### 4. Paquete `interfaz`
*   **`SimuladorColectivosApp.java`**:
    *   `main()`:
        *   Crea instancias de `LectorArchivos`, `Simulador`.
        *   Llama a `lector.cargarDatosCompletos(...)` con las rutas a los archivos en `src/main/resources/datos_pm/`.
        *   Llama a `simulador.inicializarSimulacion(...)`.
        *   Tiene un bucle que llama a `simulador.ejecutarCicloSimulacionUnicoRecorrido()` y muestra el estado en consola.
    *   **(Pendiente)**

### 5. Pruebas JUnit
*   `ColectivoTest.java`: (**COMPLETADO**)
*   `LectorArchivosTest.java`: (**COMPLETADO**)
*   `GeneradorPasajerosTest.java`. (**Pendiente**)
*   `SimuladorTest.java` (pruebas para inicialización y un ciclo básico). (**Pendiente**)

---

## Incremento 2: Múltiples Recorridos, Estadísticas, ¿Pathfinding Básico?

**Fecha Límite Estimada:** (MiyoBran, puedes añadir una fecha si lo deseas)

**Objetivos:**
*   Permitir que los colectivos realicen múltiples recorridos.
*   Introducir una noción de tiempo en la simulación (posiblemente usando `horarios_pm.txt`).
*   Recolectar estadísticas básicas (e.g., pasajeros transportados, tiempo promedio de espera).
*   **Opcional/Exploratorio:** Si el tiempo lo permite y se considera relevante, implementar un pathfinding muy básico para pasajeros usando `net.datastructures.Graph` (e.g., encontrar una ruta con menos transbordos entre dos paradas cualesquiera).

### 1. Paquete `modelo` (Refinamientos)
*   **`Pasajero.java`**: Podría añadir `horaLlegadaAParada`, `horaSubidaAColectivo`, `horaBajadaDeColectivo` para estadísticas.
*   **`Colectivo.java`**: Podría necesitar un estado (e.g., `EN_RUTA`, `ESPERANDO_EN_TERMINAL`), y lógica para manejar frecuencias/horarios.
*   **`Parada.java`**: Podría acumular estadísticas (e.g., máximo de personas en espera).

### 2. Paquete `logica`
*   **`Simulador.java` (Extensión)**:
    *   **Atributo**: `private int tiempoSimulacionActual;` (o `LocalDateTime`)
    *   Gestión de horarios de inicio/fin de servicio, frecuencias (leyendo `horarios_pm.txt`).
    *   **Método `ejecutarCicloSimulacion` (modificado)**:
        *   Incrementar `tiempoSimulacionActual`.
        *   Gestionar múltiples recorridos: cuando un colectivo llega a la terminal, puede reiniciar su recorrido según horario/frecuencia o esperar.
        *   Actualizar estadísticas.
    *   **Método**: `public void ejecutarSimulacionCompleta(int tiempoTotalSimulacion)`: Bucle principal de simulación.
*   **`GestorEstadisticas.java`** (Nueva Clase):
    *   **Métodos**: `registrarSubida(Pasajero p, int tiempo)`, `registrarBajada(Pasajero p, int tiempo)`, etc.
    *   **Métodos**: `calcularTiempoPromedioEspera()`, `calcularTotalPasajerosTransportados()`, etc.
*   **`PlanificadorRutas.java`** (Opcional - Usaría `net.datastructures.Graph`):
    *   **Constructor**: Toma todas las paradas y líneas, construye un `AdjacencyMapGraph`.
    *   **Método**: `public List<InstruccionViaje> encontrarRutaOptima(Parada origen, Parada destino, Criterio criterio)`

### 3. Paquete `interfaz`
*   **`SimuladorColectivosApp.java` (Mejoras)**:
    *   Permitir al usuario configurar parámetros de simulación.
    *   Mostrar estadísticas al final de la simulación.
    *   Si se implementa `PlanificadorRutas`, permitir al usuario consultarlo.

### 4. Pruebas JUnit
*   Tests para todas las nuevas funcionalidades y clases (`GestorEstadisticasTest`, `PlanificadorRutasTest` si se implementa).
*   Tests más exhaustivos para `Simulador.java` cubriendo múltiples ciclos, horarios y estadísticas.

---

## Consideraciones del Grafo (Red de Transporte de Puerto Madryn)
*   La modelización completa de la red de transporte como un grafo (usando `net.datastructures.AdjacencyMapGraph`) es una extensión natural y poderosa.
*   El ejemplo "subte" sirve como una excelente guía para esta implementación.
*   Esta funcionalidad se considera para el **Incremento 2 (opcional/exploratorio)** o como una posible extensión futura.

---