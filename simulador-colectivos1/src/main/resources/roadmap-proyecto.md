# **Roadmap del Proyecto: Simulador de Colectivos Urbanos (Definitivo)**

Última Actualización: 2025-06-05 (Actualizado con detalles del Incremento 2\)  
Desarrollador: MiyoBran  
Documento de Contexto Principal: prompt-proyecto.md (ubicado en src/main/resources/)

## **0\. Etapa 0: Configuración Inicial del Proyecto y Entorno**

Esta etapa detalla los pasos para crear y configurar el proyecto Java con Maven en el IDE Eclipse, asegurando una estructura correcta desde el inicio.

### **0.1. Creación del Proyecto Maven en Eclipse**

1. **Abrir Eclipse IDE.**  
2. Ir a File \> New \> Maven Project.  
3. Asegurarse de que la casilla "Create a simple project (skip archetype selection)" **NO** esté marcada. Clic en Next.  
4. En "Filter", buscar y seleccionar el arquetipo maven-archetype-quickstart. Clic en Next.  
5. **Configurar las Coordenadas del Proyecto:**  
   * **Group Id:** ar.edu.unpsjb.ayp2.proyectointegrador  
   * **Artifact Id:** simulador-colectivos  
   * **Version:** 0.0.1-SNAPSHOT (o la que desees)  
   * **Package:** (Eclipse lo autocompletará basado en el Group Id y Artifact Id, ej: ar.edu.unpsjb.ayp2.proyectointegrador.simulador\_colectivos. El paquete base para tus clases será ar.edu.unpsjb.ayp2.proyectointegrador).  
6. Clic en Finish. Eclipse creará el proyecto Maven con una estructura básica.

### **0.2. Configuración del Archivo pom.xml**

Abre el archivo pom.xml que se encuentra en la raíz de tu proyecto recién creado. Asegúrate de que tenga la siguiente configuración (o modifícalo para que coincida con el pom.xml que ya tienes y hemos revisado, el cual incluye Java 21, JUnit 5 y net.datastructures):

\<?xml version="1.0" encoding="UTF-8"?\>  
\<project xmlns="http://maven.apache.org/POM/4.0.0"  
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd"\>  
    \<modelVersion\>4.0.0\</modelVersion\>

    \<groupId\>ar.edu.unpsjb.ayp2.proyectointegrador\</groupId\>  
    \<artifactId\>simulador-colectivos\</artifactId\>  
    \<version\>0.0.1-SNAPSHOT\</version\>  
    \<name\>Proyecto Integrador \- Simulador de Colectivos Urbanos\</name\>  
    \<description\>Sistema para simular el funcionamiento de líneas de colectivos urbanos para la materia Algoritmica y Programacion II.\</description\>

    \<properties\>  
        \<project.build.sourceEncoding\>UTF-8\</project.build.sourceEncoding\>  
        \<maven.compiler.source\>21\</maven.compiler.source\>  
        \<maven.compiler.target\>21\</maven.compiler.target\>  
        \<java.version\>21\</java.version\>  
    \</properties\>

    \<dependencies\>  
        \<dependency\>  
            \<groupId\>org.junit.jupiter\</groupId\>  
            \<artifactId\>junit-jupiter-api\</artifactId\>  
            \<version\>5.10.2\</version\> \<scope\>test\</scope\>  
        \</dependency\>  
        \<dependency\>  
            \<groupId\>org.junit.jupiter\</groupId\>  
            \<artifactId\>junit-jupiter-engine\</artifactId\>  
            \<version\>5.10.2\</version\> \<scope\>test\</scope\>  
        \</dependency\>

        \<dependency\>  
            \<groupId\>net.datastructures\</groupId\>  
            \<artifactId\>datastructures-library\</artifactId\>  
            \<version\>6.0.0-custom\</version\>  
            \</dependency\>  
    \</dependencies\>

    \<build\>  
        \<plugins\>  
            \<plugin\>  
                \<groupId\>org.apache.maven.plugins\</groupId\>  
                \<artifactId\>maven-compiler-plugin\</artifactId\>  
                \<version\>3.13.0\</version\> \<configuration\>  
                    \<release\>${java.version}\</release\>  
                \</configuration\>  
            \</plugin\>  
            \<plugin\>  
                \<groupId\>org.apache.maven.plugins\</groupId\>  
                \<artifactId\>maven-jar-plugin\</artifactId\>  
                \<version\>3.3.0\</version\> \<configuration\>  
                    \<archive\>  
                        \<manifest\>  
                            \<mainClass\>ar.edu.unpsjb.ayp2.proyectointegrador.interfaz.SimuladorColectivosApp\</mainClass\>  
                        \</manifest\>  
                    \</archive\>  
                \</configuration\>  
            \</plugin\>  
            \<plugin\>  
                \<groupId\>org.apache.maven.plugins\</groupId\>  
                \<artifactId\>maven-surefire-plugin\</artifactId\>  
                \<version\>3.2.5\</version\> \</plugin\>  
        \</plugins\>  
    \</build\>  
\</project\>

**Nota sobre** net.datastructures\*\*: Asegúrate de que la librería datastructures-library-6.0.0-custom.jar esté accesible para Maven, ya sea instalándola localmente o mediante la configuración que provea la cátedra.

Después de guardar el pom.xml, haz clic derecho en el proyecto en Eclipse y selecciona Maven \> Update Project... (marcando "Force Update of Snapshots/Releases").

### **0.3. Creación de la Estructura de Carpetas y Paquetes**

Maven crea una estructura estándar. Deberás crear los subpaquetes y carpetas de recursos manualmente si no existen, siguiendo esta guía:

1. **Paquetes Java Principales (dentro de** src/main/java\*\*):\*\*  
   * La ruta base para tus paquetes será: src/main/java/ar/edu/unpsjb/ayp2/proyectointegrador/  
   * Dentro de esta ruta, crea los subpaquetes:  
     * modelo  
     * datos  
     * logica  
     * interfaz  
2. **Carpetas de Recursos Principales (dentro de** src/main/**):**  
   * Asegúrate de que exista la carpeta src/main/resources/.  
   * Coloca los archivos proporcionados por la cátedra y tus documentos de planificación:  
   * (src/main/resources/):
   * paradas_pm_mapeadas.txt (Datos de paradas, vía config.properties)
   * lineas_pm_mapeadas.txt (Datos de líneas, vía config.properties)
   * config.properties
   * prompt-proyecto.md
   * roadmap-proyecto.md (este mismo archivo)
   * datos_pm/ (Para otros archivos de datos)
		** horarios_pm.txt (Formato experimental: ID_Linea;Sentido;Rango_Dias;HH:MM_Inicio;HH:MM_Fin;Frecuencia_Minutos. Uso detallado para Inc. futuros, para Inc. 2 podría informar frecuencias básicas o tiempos de tramo si se decide).
     
3. **Paquetes** Java de Pruebas (dentro **de** src/test/java\*\*):\*\*  
   * La ruta base será: src/test/java/ar/edu/unpsjb/ayp2/proyectointegrador/  
   * Dentro de esta ruta, crea los subpaquetes:  
     * modelo  
     * datos  
     * logica  
4. **Carpetas** de Recursos de Pruebas (dentro de src/test/**):**  
   * Asegúrate de que exista la carpeta src/test/resources/.  
   * Dentro de src/test/resources/, crea la subcarpeta:  
     * datos\_test (para archivos paradas\_test.txt, lineas\_test.txt que usarás en tus JUnit tests)

**Estructura Visual Final (resumida):**

* simulador-colectivos/  
  * ├── pom.xml  
  * └── src/  
    * ├── main/  
      * ├── java/  
        * └── ar/edu/unpsjb/ayp2/proyectointegrador/ \<-- PAQUETE RAÍZ  
          * ├── modelo/ (Linea.java, Parada.java, etc.)  
          * ├── datos/ (LectorArchivos.java, etc.)  
          * ├── logica/ (Simulador.java, etc.)  
          * └── interfaz/ (SimuladorColectivosApp.java)  
      * └── resources/  
        * ├── datos\_pm/  
          * ├── parada.txt  \<-- Y otras versiones anteriores  
          * └── horarios_pm.txt  
        * ├── config.properties  
        * ├── prompt-proyecto.md  
        * ├── roadmap-proyecto.md
        * ├── Laboratorio2025.txt
        * ├── paradas_pm_mapeadas.txt  
        * └── lineas_pm_mapeadas.txt    
    * └── test/  
      * ├── java/  
        * └── ar/edu/unpsjb/ayp2/proyectointegrador/ \<-- PAQUETE RAÍZ PARA TESTS  
          * ├── modelo/ (LineaTest.java, etc.)  
          * ├── datos/ (LectorArchivosTest.java, etc.)  
          * └── logica/ (SimuladorTest.java, etc.)  
      * └── resources/  
        * └── datos\_test/  
          * ├── paradas\_test.txt  
          * └── lineas\_test.test

Con esto, el proyecto está correctamente configurado y listo para empezar a desarrollar las clases según el plan.

## **1. Introducción y Principios Guía (del Proyecto)**

Este proyecto busca simular líneas de colectivos urbanos en Java, con datos de Puerto Madryn, dividido en dos incrementos y usando una arquitectura por capas. El contexto completo, motivaciones y detalles de la filosofía del proyecto se encuentran en `prompt-proyecto.md`.

### **1.1. Política de Uso de TADs (Enfoque Híbrido)**

El proyecto adopta un enfoque híbrido para el uso de TADs, detallado extensamente en `prompt-proyecto.md`.
**En resumen:**
* **Java Collections Framework (`java.util.*`)**: Uso principal para colecciones internas de entidades del modelo (e.g., `Parada.pasajerosEsperando` como `Queue`, `Linea.recorrido` como `List`) y para el almacenamiento de datos cargados en `LectorArchivos` (e.g., `HashMap` en Inc. 1, `java.util.TreeMap` en Inc. 2). Justificación: Robustez, API estándar, familiaridad, buen rendimiento general y ordenamiento intrínseco (con `TreeMap`).
* **Librería `net.datastructures` (v6.0.0-custom)**: Uso específico para modelado de grafos y pathfinding en Incremento 2 (e.g., `AdjacencyMapGraph` y `GraphAlgorithms`). Justificación: Implementaciones especializadas de la cátedra para estas tareas.

*(Referirse a `prompt-proyecto.md` para la justificación completa y la comparación con el ejemplo "subte").*

### **1.2. Referencias del Ejemplo "subte" de la Cátedra**

El proyecto "subte" sirve como referencia conceptual, especialmente en la gestión de datos a nivel de aplicación con `net.datastructures.TreeMap` y la implementación de grafos. Nuestro proyecto se diferencia por su enfoque en simulación dinámica. Más detalles en `prompt-proyecto.md`.

## **2. Estructura del Proyecto (Reafirmación)**

* **Lenguaje:** Java (JDK 21)
* **Gestor de Proyecto:** Maven
* **Pruebas:** JUnit 5
* **IDE:** Eclipse
* **Paquete Raíz Java:** `ar.edu.unpsjb.ayp2.proyectointegrador`

(La estructura de carpetas detallada se encuentra en la Etapa 0.3 del inicio de este documento)


## **3. Modelo Conceptual Detallado (Clases y Relaciones UML)**

* **`Linea`**: `codigo: String`, `nombre: String`, `recorrido: List<Parada>`.
* **`Parada`**: `id: String`, `nombre: String`, `direccion: String`, `latitud: double`, `longitud: double`, `pasajerosEsperando: Queue<Pasajero>`.
* **`Colectivo`**: `idColectivo: String`, `patente: String`, `capacidadMaxima: int`, `lineaAsignada: Linea`, `pasajerosABordo: List<Pasajero>`, `paradaActual: Parada`. (Atributos adicionales para Inc. 2).
* **`Pasajero`**: `id: String`, `paradaOrigen: Parada`, `paradaDestino: Parada`. (Atributos adicionales para Inc. 2).

(Las relaciones detalladas se describen en `diagramaUML-descripcion.txt` y se resumen en `prompt-proyecto.md`).

## **4. Plan de Desarrollo – Incremento 1 (COMPLETADO)**

**Fecha Límite Estimada:** 2025-06-11 (CUMPLIDO)
🎯 **Objetivo General del Incremento 1:** Implementar una primera versión funcional (carga de datos, generación de pasajeros, un colectivo por línea, un solo recorrido, salida por consola).

*(Las tareas detalladas por paquete para el Incremento 1, marcadas como completadas `[x]`, se asumen realizadas según el plan original. La funcionalidad de `ConfiguracionSimulacion.java` fue integrada en `LectorArchivos.java` para leer `config.properties`, y `SimuladorColectivosApp.java` pasa los parámetros al `Simulador`.)*

🎯 Objetivo General del Incremento 1:

Implementar una primera versión funcional del sistema de simulación de colectivos urbanos que permita:

* \[x\] Cargar desde archivos la información de Linea y Parada (datos de Puerto Madryn, utilizando config.properties para las rutas).  
* \[x\] Generar hasta N Pasajeros (cantidad definida en config.properties) distribuidos aleatoriamente en las paradas de origen.  
* \[x\] Asignar un Colectivo por cada Linea cargada.  
* \[x\] Simular un recorrido único para cada Colectivo siguiendo la secuencia de paradas definida en su Linea.recorrido.  
* \[x\] Mostrar en la consola (salida controlada por la capa de interfaz):  
  * Las paradas por las que pasa cada colectivo.  
  * Los pasajeros que suben y bajan en cada parada.

### **4.1. Tareas del Paquete modelo (Paquete: ar.edu.unpsjb.ayp2.proyectointegrador.modelo)**

* \[x\] Tarea: Implementar Pasajero.java.  
  * Atributos: id: String, paradaDestino: Parada. (Considerar paradaOrigen: Parada para facilitar la generación).  
  * Constructor, getters, toString(), equals(), hashCode().  
* \[x\] Tarea: Implementar Parada.java.  
  * Atributos: id: String, nombre: String, direccion: String, latitud: double, longitud: double, pasajerosEsperando: Queue\<Pasajero\>.  
  * Constructor, getters, métodos para agregar/quitar pasajeros de la cola, toString(), equals(), hashCode().  
* \[x\] Tarea: Implementar Linea.java.  
  * Atributos: codigo: String, nombre: String, recorrido: List\<Parada\>.  
  * Constructor, getters, método para agregar paradas al recorrido, toString(), equals(), hashCode().  
* \[x\] Tarea: Implementar Colectivo.java.  
  * Atributos: id: String, patente: String, capacidadMaxima: int (inicializar pero no forzar límite en Inc. 1), lineaAsignada: Linea, pasajerosABordo: List\<Pasajero\>, paradaActual: Parada.  
  * Constructor, getters, métodos para subir/bajar pasajeros, avanzar a la siguiente parada, toString(), equals(), hashCode().

### **4.2. Tareas del Paquete datos (Paquete: ar.edu.unpsjb.ayp2.proyectointegrador.datos)**

* \[x\] Tarea: Asegurar que los archivos de datos (parada.txt, linea.txt) y config.properties estén en sus ubicaciones correctas (src/main/resources/datos\_pm/ y src/main/resources/ respectivamente).  
* \[x\] Tarea: Implementar LectorArchivos.java.  
  * Deberá poder obtener los nombres de los archivos de datos (linea.txt, parada.txt) desde config.properties.  
  * Método cargarParadas(String rutaArchivoParadas): Devuelve Map\<String, Parada\>.  
  * Método cargarLineas(String rutaArchivoLineas, Map\<String, Parada\> paradasExistentes): Devuelve Map\<String, Linea\>.  
  * Usará java.util.HashMap internamente para las colecciones de paradas y líneas cargadas.  
  * **Nota:** Estos Maps servirán como fuente de datos para la construcción del grafo de la red de transporte en el Incremento 2 por la clase PlanificadorRutas.  
  * Manejo de errores de formato en los archivos y datos faltantes (e.g., ID de parada en una línea que no existe).  
* \[x\] Tarea (Recomendado): Implementar ConfiguracionSimulacion.java (o una funcionalidad similar dentro de LectorArchivos o SimuladorColectivosApp).  
  * Responsable de leer config.properties para todos los parámetros de simulación (rutas de archivos, cantidad de pasajeros, etc.).

### **4.3. Tareas del Paquete logica (Paquete: ar.edu.unpsjb.ayp2.proyectointegrador.logica)**

* \[x\] Tarea: Implementar GeneradorPasajeros.java.  
  * Método generarPasajerosAleatorios(int cantidad, Collection\<Parada\> todasLasParadas): Devuelve List\<Pasajero\>.  
  * IDs de pasajeros únicos.  
  * Asegurar que paradaOrigen y paradaDestino sean distintas y válidas del conjunto de todasLasParadas.  
* \[x\] Tarea: Implementar Simulador.java (Versión Inicial).  
  * Atributos: mapaParadas: Map\<String, Parada\>, mapaLineas: Map\<String, Linea\>, listaColectivosActivos: List\<Colectivo\>, listaPasajerosGlobales: List\<Pasajero\>.  
  * Constructor: Simulador(Map\<String, Parada\> paradas, Map\<String, Linea\> lineas).  
  * Método inicializarSimulacion(int numPasajerosAGenerar, int capacidadColectivos):  
    * Crea un Colectivo para cada Linea en mapaLineas (con capacidadColectivos).  
    * Genera numPasajerosAGenerar usando GeneradorPasajeros (la cantidad se leerá de config.properties).  
    * Distribuye los pasajeros generados en las colas pasajerosEsperando de sus paradaOrigen.  
  * \[x\] Método ejecutarCicloSimulacionUnicoRecorrido():  
    * Para cada colectivo en listaColectivosActivos:  
      * Si el colectivo no ha completado su recorrido:  
        * Obtener paradaActual del colectivo.  
        * Procesar bajada de pasajeros cuyo paradaDestino sea la paradaActual.  
        * Procesar subida de pasajeros desde paradaActual.getPasajerosEsperando() (sin exceder capacidad, aunque el chequeo estricto es para Inc. 2).  
        * Avanzar colectivo a la próxima parada en su recorrido.  
        * Registrar/Imprimir eventos (esta lógica de impresión se delegará a la interfaz).

### **4.4. Tareas del Paquete interfaz (Paquete: ar.edu.unpsjb.ayp2.proyectointegrador.interfaz)**

* \[x\] Tarea: Implementar SimuladorColectivosApp.java.  
  * Método main():  
    * (Opcional) Cargar configuración desde config.properties usando ConfiguracionSimulacion.java o una lógica similar.  
    * Crea instancia de LectorArchivos.  
    * Llama a lector.cargarParadas() y lector.cargarLineas() (las rutas a los archivos se obtendrán de config.properties).  
    * Crea instancia de Simulador con los datos cargados.  
    * Llama a simulador.inicializarSimulacion() (cantidad de pasajeros y capacidad se obtendrán de config.properties o se pasarán como parámetros fijos si ConfiguracionSimulacion no se implementa en Inc. 1).  
    * Implementa un menú de texto simple para:  
      * Opción 1: Ejecutar un ciclo de simulación (simulador.ejecutarCicloSimulacionUnicoRecorrido()).  
      * Opción 2: Mostrar estado actual (ej: colectivos y su parada actual, pasajeros en parada).  
      * Opción 3: Salir.  
    * La lógica de mostrar información en consola debe residir aquí, obteniendo datos del simulador.

### **4.5. Tareas del Paquete test (Paquetes en src/test/java/ar/edu/unpsjb/ayp2/proyectointegrador/...)**

* \[x\] Tarea: Crear PasajeroTest.java.  
* \[x\] Tarea: Crear ParadaTest.java.  
* \[x\] Tarea: Crear LineaTest.java.  
* \[x\] Tarea: Crear ColectivoTest.java.  
* \[x\] Tarea: Crear LectorArchivosTest.java (probar carga exitosa y manejo de errores, incluyendo la lectura de nombres de archivo desde config.properties si se implementa).  
* \[x\] Tarea: Crear GeneradorPasajerosTest.java.  
* \[x\] Tarea: Crear SimuladorTest.java (pruebas para inicialización y un ciclo básico de simulación).

## **5. Reglas y Buenas Prácticas (Generales)**

* **E/S y Consola:** `System.out.println` solo en `SimuladorColectivosApp.java` o clases de prueba controladas.
* **Encapsulamiento:** Atributos `private`.
* **Invariantes:** Validar parámetros.
* **Estructura de Carpetas:** Respetar arquitectura por capas.
* **Manejo de Errores:** Usar excepciones.
* **JavaDoc:** Para clases y métodos públicos (especialmente en Incremento 2).

## **6. Plan de Desarrollo – Incremento 2**

**Fecha Límite Estimada:** 2025-06-24

### **6.1. Objetivos Principales del Incremento 2**

* Permitir que los colectivos realicen **múltiples recorridos** (controlado por parámetro) y gestionen su estado.
* Introducir una noción de **tiempo en la simulación**.
* Implementar **control de capacidad estricto** en los colectivos (total y **sentados**).
* Recolectar y mostrar **estadísticas básicas**: pasajeros transportados, tiempo promedio de espera, ocupación promedio, índice de satisfacción del pasajero.
* Implementar la red de transporte como un **grafo** (`net.datastructures.AdjacencyMapGraph`).
* Implementar funcionalidad de **cálculo de rutas básicas** (`GraphAlgorithms`).
* Documentación completa (JavaDoc).


### **6.2. Nuevos Parámetros en `config.properties` para Incremento 2**

Se añadirán los siguientes parámetros a `config.properties`, que `LectorArchivos.java` deberá cargar:
* `capacidadSentadosColectivo=15` (Ej: capacidad de pasajeros sentados)
* `cantidad_de_colectivos_simultaneos_por_linea=3` (Ej: cuántos buses operan por cada línea-sentido)
* `recorridos_por_colectivo=4` (Ej: cuántos segmentos (ida o vuelta) hace cada bus antes de finalizar)
* `frecuencia_salida_colectivos_minutos=20` (Ej: para escalonar la salida inicial de los múltiples buses de una misma línea-sentido)


### **6.3. Tareas Detalladas por Paquete para Incremento 2**

#### **6.3.1. Paquete `modelo` (Refinamientos y Adiciones)**

* **`Pasajero.java`**:
    * **Atributos a Añadir:** `double tiempoTotalEspera`, `double tiempoInicioEspera`, `List<Pasajero.ColectivoObservado> colectivosObservados`, `boolean tieneAsiento`.
    * **Clase Interna:** `public static class ColectivoObservado { String idColectivoObservado; double tiempoObservacion; boolean estabaLleno; /* Constructor y getters */ }`
    * **Métodos:**
        * `void iniciarEspera(double tiempoActual)`
        * `void finalizarEspera(double tiempoActual)`
        * `void registrarColectivoPasado(Colectivo colectivo, double tiempoActual, boolean estabaLleno)`
        * `int calcularSatisfaccion()`: Implementar según Anexo I de la consigna, considerando `colectivosObservados` (para # de buses esperados) y `tieneAsiento`.
        * Getters/Setters para nuevos campos (ej. `setTieneAsiento(boolean)`).
    * **Tests Unitarios:** Actualizar y verificar `PasajeroTest.java`.
* **`Colectivo.java`**:
    * **Atributos a Añadir:** `int capacidadSentados` (del config), `int pasajerosSentadosActuales`, `double tiempoActualEnSimulacionInterno`, `double tiempoInicioServicioProgramado`, `EstadoColectivo estado` (Enum: `EN_RUTA`, `EN_PARADA`, `EN_TERMINAL_ESPERANDO`, `FINALIZADO`), `int recorridosRestantes`.
    * **Métodos a Implementar/Ajustar:**
        * Constructor modificado para recibir `capacidadSentados`.
        * `void setTiempoInicioServicioProgramado(double tiempo)`
        * `boolean estaActivo(double tiempoSimulador)`: Verifica si `tiempoSimulador >= tiempoInicioServicioProgramado` y `recorridosRestantes > 0`.
        * `Parada avanzarAProximaParada(double tiempoViajeTramo)`: Actualiza `paradaActual`, `tiempoActualEnSimulacionInterno += tiempoViajeTramo`. Si llega a la última parada del recorrido, cambia `estado` a `EN_TERMINAL_ESPERANDO`.
        * Lógica de `subirPasajero(Pasajero p)`: Verificar `capacidadMaxima` y `capacidadSentados`. Asignar `p.setTieneAsiento(true/false)`. Actualizar `pasajerosSentadosActuales`.
        * `void procesarLlegadaATerminal()`: Vaciar todos los pasajeros a bordo. Decrementar `recorridosRestantes`.
        * Getters/Setters para nuevos campos y estado.
    * **Tests Unitarios:** Actualizar y verificar `ColectivoTest.java`.
* **`Parada.java`**:
    * **Atributos (Opcional, para estadísticas locales):** `double maxTiempoEsperaPasajero`, `int maxPersonasEnCola`.
    * **Tests Unitarios:** Revisar `ParadaTest.java`.

#### **6.3.2. Paquete `datos`**

* **`LectorArchivos.java`**:
    * Refactorizar el almacenamiento de `paradasCargadas` y `lineasCargadas` para usar `java.util.TreeMap<String, Parada>` y `java.util.TreeMap<String, Linea>` respectivamente. Justificación: ordenamiento intrínseco útil para reportes y depuración.
    * Extender para leer los nuevos parámetros de `config.properties`: `capacidadSentadosColectivo`, `cantidad_de_colectivos_simultaneos_por_linea`, `recorridos_por_colectivo`, `frecuencia_salida_colectivos_minutos`.
    * Método para leer `horarios_pm.txt` (ubicado en `src/main/resources/datos_pm/`). Para Incremento 2, este archivo podría usarse de forma simplificada para obtener tiempos de viaje entre paradas (si no se calculan de otra forma) o confirmar frecuencias básicas. Su uso detallado para horarios complejos se considera futuro (ver Sección 9).
    * **Tests Unitarios:** Actualizar y verificar `LectorArchivosTest.java` para `TreeMap` y nuevos parámetros.

#### **6.3.3. Paquete `logica`**

* **`Simulador.java` (Extensión Mayor):**
    * **Atributos:** `double tiempoActualSimulacionGlobal`.
    * **Métodos a Implementar/Ajustar:**
        * `inicializarMultiplesColectivos()`:
            * Lee parámetros de configuración relevantes de `LectorArchivos`.
            * Para cada `Linea` (sentido):
                * Crear `N` (`cantidad_de_colectivos_simultaneos_por_linea`) instancias de `Colectivo`.
                * Asignarles un ID único (ej. "L1-Ida-Bus1").
                * Asignarles `recorridos_por_colectivo`.
                * Escalonar sus `tiempoInicioServicioProgramado` usando `frecuencia_salida_colectivos_minutos` (ej. 0, 20, 40 min).
        * `ejecutarSimulacionAvanzada(double tiempoMaximoSimulacion)`:
            * Bucle principal que avanza `tiempoActualSimulacionGlobal`.
            * En cada paso, iterar sobre todos los `Colectivo`s.
            * Procesar colectivos `activos` cuyo `tiempoActualEnSimulacionInterno <= tiempoActualSimulacionGlobal`.
            * Para un colectivo en una parada:
                * Llamar a `procesarColectivoEnParada(Colectivo colectivo, double tiempoActualSimulacionGlobal)`.
            * Para un colectivo listo para avanzar entre paradas:
                * Estimar/obtener `tiempoViajeTramo` (podría ser fijo, o de `horarios_pm.txt` de forma simplificada, o calculado por distancia si se implementa).
                * Llamar a `colectivo.avanzarAProximaParada(tiempoViajeTramo)`.
            * Para un colectivo en `EN_TERMINAL_ESPERANDO`:
                * Llamar a `colectivo.procesarLlegadaATerminal()` (vacía pasajeros, decrementa recorridos).
                * Si `colectivo.recorridosRestantes > 0`: Asignarle la `Linea` del sentido opuesto. El colectivo estará listo para su nuevo `tiempoInicioServicioProgramado` (que será su `tiempoActualEnSimulacionInterno` actual).
                * Si `colectivo.recorridosRestantes == 0`: Cambiar su estado a `FINALIZADO`.
        * `private void procesarColectivoEnParada(Colectivo colectivo, double tiempoActual)`: Lógica de bajada/subida (considerando capacidad total y sentados), actualizando tiempos y estados de `Pasajero` y `Colectivo`.
    * **Tests Unitarios:** Extender `SimuladorTest.java` masivamente.
* **`GestorEstadisticas.java` (Nueva Clase):**
    * **Propósito:** Centralizar recolección y cálculo de métricas.
    * **Métodos:** `registrarEventoSubida(Pasajero p, Colectivo c, double tiempo, boolean conAsiento)`, `registrarEventoBajada(...)`, `calcularTiempoPromedioEspera(List<Pasajero> pasajerosCompletados)`, `calcularOcupacionPromedioColectivos(List<Colectivo> colectivos, double tiempoTotalSimulacion)`, `calcularSatisfaccionPromedio(List<Pasajero> pasajerosCompletados)`, `generarReporteConsola()`.
    * **Tests Unitarios:** Crear `GestorEstadisticasTest.java`.
* **`PlanificadorRutas.java` (Nueva Clase Central de Grafos):**
    * **Propósito:** Modelar red como grafo (`net.datastructures.AdjacencyMapGraph<Parada, Double>`) y encontrar rutas. `Double` para el peso (ej. tiempo estimado de viaje o número de paradas).
    * **Constructor:** `PlanificadorRutas(Map<String, Parada> todasLasParadas, Map<String, Linea> todasLasLineas)`: Construye el grafo. Las aristas se crean a partir de los `recorrido`s de las `Linea`s. El peso podría ser 1 (nro. paradas) o un tiempo estimado (a definir).
    * **Método:** `public List<Parada> encontrarRuta(Parada origen, Parada destino)`: Usa `GraphAlgorithms.shortestPath()`.
    * **Tests Unitarios:** Crear `PlanificadorRutasTest.java`.

#### **6.3.4. Paquete `interfaz`**

* **`SimuladorColectivosApp.java` (Mejoras):**
    * Usar `ejecutarSimulacionAvanzada`.
    * Obtener todos los parámetros de simulación de `LectorArchivos`.
    * Mostrar estadísticas de `GestorEstadisticas`.
    * Opción de menú para usar `PlanificadorRutas`.

#### **6.3.5. Paquete `test`**

* Actualizar todos los tests existentes para reflejar cambios en clases del Inc. 1.
* Crear nuevas clases de test para `GestorEstadisticas` y `PlanificadorRutas`.
* Asegurar cobertura de la nueva lógica en `SimuladorTest.java`.

## **7. Checklist Previo a Entrega (General para cada Incremento)**

* Modelo de clases implementado correctamente.
* Carga de archivos y configuración robusta.
* Lógica de simulación funcional según objetivos del incremento.
* Interfaz de usuario (consola) funcional.
* Sin `System.out.println` indebidos.
* Tests unitarios (JUnit) exhaustivos.
* Estructura de carpetas respetada.
* Código bien comentado y legible (JavaDoc para métodos públicos, especialmente en Inc. 2).
* `README.md` actualizado.
## **8. Actualización Continua del Proyecto y Documentación**

* Este `roadmap-proyecto.md` es un documento vivo y debe ser actualizado por MiyoBran para reflejar el progreso y cambios.
* El "Estado Actual" en `prompt-proyecto.md` debe mantenerse sincronizado.

## **9. Posibles Mejoras y Extensiones Futuras (Post-Incremento 2)**

Las siguientes funcionalidades se consideran fuera del alcance del Incremento 2 y se dejan para futuras extensiones:
* Gestión detallada de horarios específicos de salida de colectivos (variaciones por día de la semana, horarios exactos leídos de `horarios_pm.txt`).
* Modelado avanzado de demoras:
    * Impacto del tiempo de subida/bajada de pasajeros en el tiempo de detención en parada.
    * Propagación de demoras de un recorrido "Ida" al subsecuente recorrido "Vuelta" del mismo colectivo.
    * Tiempos de descanso obligatorios para choferes entre recorridos o al final de un ciclo.
* Asignación de entidades `Chofer` a los `Colectivo`s, con gestión de turnos de trabajo.
* Recolección de estadísticas detalladas por instancia de `Colectivo` o por `Chofer`.
* Generación de `Pasajero`s de forma dinámica durante la simulación, basada en franjas horarias con diferente afluencia (ej. horas pico).
* Cálculo de pesos de arista en el grafo basados en distancia geográfica (usando lat/lon) o tiempos de viaje variables.
* Interfaz gráfica de usuario (GUI).