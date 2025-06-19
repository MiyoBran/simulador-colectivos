# **Roadmap del Proyecto: Simulador de Colectivos Urbanos (Definitivo)**

Última Actualización: 2025-06-04 (Actualizado tras completar Incremento 1\)  
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
   * Dentro de src/main/resources/, crea la subcarpeta:  
     * datos\_pm  
   * Coloca los archivos proporcionados por la cátedra y tus documentos de planificación:  
     * src/main/resources/datos\_pm/parada.txt  
     * src/main/resources/datos\_pm/linea.txt  
     * src/main/resources/config.properties  
     * src/main/resources/prompt-proyecto.md  
     * src/main/resources/roadmap-proyecto-definitivo.md (este mismo archivo)  
     * (Opcional: otros archivos de la cátedra, como el ejemplo "subte" si lo tienes localmente)  
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
          * ├── parada.txt  
          * └── linea.txt  
        * ├── config.properties  
        * ├── prompt-proyecto.md  
        * └── roadmap-proyecto-definitivo.md  
    * └── test/  
      * ├── java/  
        * └── ar/edu/unpsjb/ayp2/proyectointegrador/ \<-- PAQUETE RAÍZ PARA TESTS  
          * ├── modelo/ (LineaTest.java, etc.)  
          * ├── datos/ (LectorArchivosTest.java, etc.)  
          * └── logica/ (SimuladorTest.java, etc.)  
      * └── resources/  
        * └── datos\_test/  
          * ├── paradas\_test.txt  
          * └── lineas\_test.txt

Con esto, el proyecto está correctamente configurado y listo para empezar a desarrollar las clases según el plan.

## **1\. Introducción y Principios Guía (del Proyecto)**

Este documento detalla el plan de desarrollo para el proyecto "Simulador de Colectivos Urbanos". El objetivo es crear un sistema en Java que simule el funcionamiento de líneas de colectivos, el movimiento de pasajeros y, eventualmente, permita analizar la eficiencia del sistema y calcular rutas óptimas, utilizando datos del mundo real de Puerto Madryn.

El proyecto se desarrollará en dos incrementos principales y seguirá una arquitectura de programación por capas (modelo, datos, lógica, interfaz, test).

### **1.1. Política de Uso de TADs (Enfoque Híbrido)**

El proyecto adopta un **enfoque híbrido** para el uso de Tipos Abstractos de Datos (TADs) y sus implementaciones:

* **Java Collections Framework (java.util)**:  
  * **Uso Principal:** Para las colecciones internas de las entidades del modelo de simulación (Parada, Linea, Colectivo, Pasajero) y para el almacenamiento de datos cargados en el Incremento 1\.  
    * Ejemplo: Parada.pasajerosEsperando será una java.util.Queue (implementada con java.util.LinkedList).  
    * Ejemplo: Linea.recorrido (lista de paradas) será una java.util.List\<Parada\> (implementada con java.util.ArrayList).  
    * Ejemplo: Colectivo.pasajerosABordo será una java.util.List\<Pasajero\> (implementada con java.util.ArrayList).  
    * Ejemplo: LectorArchivos utilizará java.util.HashMap para paradasCargadas y lineasCargadas en el Incremento 1\.  
  * **Justificación:** Robustez, API completa, estándar Java, simplicidad para la lógica de simulación directa y familiaridad. El rendimiento de HashMap es óptimo para la recuperación rápida de datos por ID.  
* **Librería** net.datastructures **(versión** 6.0.0-custom\*\*)\*\*:  
  * **Uso Específico:**  
    * **Modelado de Grafos y Pathfinding (Principalmente Incremento 2):** Para la funcionalidad avanzada de cálculo de rutas que involucre múltiples líneas y transbordos, se utilizará net.datastructures.AdjacencyMapGraph y GraphAlgorithms. La red de transporte se modelará con Paradas como vértices y los tramos directos entre paradas (definidos por los recorridos de las Lineas) como aristas dirigidas y ponderadas (e.g., por distancia o tiempo).  
    * **Almacenamiento Global de Datos Cargados (Evaluación para consistencia con "subte"):** Si bien el Incremento 1 usará java.util.HashMap en LectorArchivos, se podría evaluar para el Incremento 2 el uso de net.datastructures.TreeMap si la necesidad de ordenamiento intrínseco o la alineación con el ejemplo "subte" para el almacenamiento de datos maestros lo justifica.  
  * **Justificación:** Aprovechar las implementaciones especializadas de la librería de la cátedra para tareas complejas de grafos y alinearse con el ejemplo "subte" en estas áreas específicas. Sirve como material de referencia para comprender la implementación de TADs.

### **1.2. Referencias del Ejemplo "subte" de la Cátedra**

El proyecto "subte" sirve como referencia conceptual, especialmente en:

* **Gestión de Datos a Nivel de Aplicación:** Uso de net.datastructures.TreeMap.  
* **Implementación de Grafos:** Uso de AdjacencyMapGraph y GraphAlgorithms.  
* **Diferencia Clave:** "Subte" se enfoca en pathfinding estático; nuestro proyecto se centra en simulación dinámica, añadiendo el pathfinding como una funcionalidad avanzada.

## **2\. Estructura del Proyecto (Reafirmación)**

* **Lenguaje:** Java (JDK 21\)  
* **Gestor de Proyecto:** Maven  
* **Pruebas:** JUnit 5  
* **IDE:** Eclipse  
* **Paquete Raíz Java:** ar.edu.unpsjb.ayp2.proyectointegrador (ubicado en src/main/java/ar/edu/unpsjb/ayp2/proyectointegrador/)

(La estructura de carpetas detallada se encuentra en la Etapa 0.3)

## **3\. Modelo Conceptual Detallado (Clases y Relaciones UML)**

El sistema se basa en las siguientes clases y sus relaciones, interpretadas del diagrama UML:

* Linea: Representa una línea de colectivo.  
  * Atributos: codigo: String, nombre: String (sugerido), recorrido: List\<Parada\> (rol \-paradas).  
* Parada: Ubicación física.  
  * Atributos: id: String (o int), nombre: String (sugerido), direccion: String, latitud: double, longitud: double, pasajerosEsperando: Queue\<Pasajero\> (rol \-pasajeros).  
* Colectivo: Vehículo.  
  * Atributos: id: String (o int), patente: String (sugerido), capacidadMaxima: int, lineaAsignada: Linea (rol \-linea), pasajerosABordo: List\<Pasajero\> (rol \-pasajeros).  
* Pasajero: Usuario.  
  * Atributos: id: String (o int), paradaOrigen: Parada (sugerido), paradaDestino: Parada (rol \-destino), estado: EnumPasajeroEstado (sugerido para Inc. 2: ESPERANDO, VIAJANDO, LLEGO\_DESTINO).

### **3.1. Relaciones Detalladas:**

1. **Entre** Linea **y** Parada **(Linea \<\>--(-paradas)--\>\> Parada)**  
   * **Tipo:** Agregación (rombo vacío en Linea).  
   * **Navegabilidad:** Desde Linea hacia Parada.  
   * **Rol/Atributo:** Linea tiene una colección recorrido (o paradas) de tipo List\<Parada\>.  
   * **Multiplicidad:** 1 (Linea) \--- 2..\* (Parada). (Una Línea tiene al menos 2 Paradas).  
   * **Significado:** Una Línea agrupa una secuencia ordenada de Paradas que componen su recorrido.  
2. **Entre** Colectivo **y** Linea **(Colectivo \--(-linea)--\> Linea)**  
   * **Tipo:** Asociación Dirigida.  
   * **Navegabilidad:** Desde Colectivo hacia Linea.  
   * **Rol/Atributo:** Colectivo tiene un atributo lineaAsignada (o linea) de tipo Linea.  
   * **Multiplicidad:** 1 (Colectivo) \--- 1 (Linea).  
   * **Significado:** Un Colectivo pertenece y conoce exactamente a una Línea.  
3. **Entre** Colectivo **y** Pasajero **(Colectivo \<\>--(-pasajeros)--\>\> Pasajero)**  
   * **Tipo:** Agregación (rombo vacío en Colectivo).  
   * **Navegabilidad:** Desde Colectivo hacia Pasajero.  
   * **Rol/Atributo:** Colectivo tiene una colección pasajerosABordo (o pasajeros) de tipo List\<Pasajero\>.  
   * **Multiplicidad:** 1 (Colectivo) \--- 0..\* (Pasajero).  
   * **Significado:** Un Colectivo agrupa a los Pasajeros que están actualmente a bordo.  
4. **Entre** Parada **y** Pasajero **(Parada \<\>--(-pasajeros)--\>\> Pasajero)**  
   * **Tipo:** Agregación (rombo vacío en Parada).  
   * **Navegabilidad:** Desde Parada hacia Pasajero.  
   * **Rol/Atributo:** Parada tiene una colección pasajerosEsperando (o pasajeros) de tipo Queue\<Pasajero\>.  
   * **Multiplicidad:** 1 (Parada) \--- 0..\* (Pasajero).  
   * **Significado:** Una Parada agrupa a los Pasajeros que están actualmente esperando en ella.  
5. **Entre** Pasajero **y** Parada **(Pasajero \--(-destino)--\> Parada)**  
   * **Tipo:** Asociación Dirigida.  
   * **Navegabilidad:** Desde Pasajero hacia Parada.  
   * **Rol/Atributo:** Pasajero tiene un atributo paradaDestino (o destino) de tipo Parada.  
   * **Multiplicidad:** 1 (Pasajero) \--- 1 (Parada).  
   * **Significado:** Un Pasajero conoce y tiene exactamente una Parada como su destino final.

## **4\. Plan de Desarrollo – Incremento 1 (COMPLETADO)**

**Fecha Límite Estimada:** 2025-06-11 (CUMPLIDO)

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

## **5\. Reglas y Buenas Prácticas (Incremento 1\)**

* **E/S y Consola:**  
  * System.out.println o similares **solo permitidos** en:  
    * SimuladorColectivosApp.java (para el menú textual y la visualización de la simulación).  
    * Clases de prueba manuales (si se crean fuera de JUnit) en el paquete test.  
    * Bloques catch para mostrar mensajes de error de excepciones críticas (controlado).  
  * **Prohibido** imprimir en clases de modelo, logica (excepto para logging muy específico si fuera necesario y bien justificado) o datos.  
* **Encapsulamiento:** Todos los atributos de las clases deben ser private. Usar getters y setters según sea necesario.  
* **Invariantes:** Validar parámetros en constructores y métodos set para mantener la consistencia de los objetos.  
* **Estructura de Carpetas:** Respetar la estructura de capas definida.  
* **Manejo de Errores:** Usar excepciones para errores de carga de archivos o datos inválidos.

## **6\. Plan de Desarrollo – Incremento 2**

**Fecha Límite Estimada:** 2025-06-24

**🎯 Objetivos Principales del Incremento 2:**

* Permitir que los colectivos realicen **múltiples recorridos** y gestionen su estado (en ruta, esperando en terminal).  
* Introducir una noción de **tiempo en la simulación** y gestión de horarios/frecuencias (leyendo horarios\_pm.txt desde src/main/resources/datos\_pm/).  
* Implementar **control de capacidad** estricto en los colectivos.  
* Recolectar y mostrar **estadísticas básicas** de la simulación:  
  * Pasajeros transportados.  
  * Tiempo promedio de espera de pasajeros (aproximado).  
  * Ocupación promedio de los colectivos.  
  * (Opcional) Índice de satisfacción del pasajero.  
* **Implementar la representación de la red de transporte como un grafo** utilizando net.datastructures.AdjacencyMapGraph.  
* **Implementar funcionalidad de cálculo de rutas básicas** para pasajeros (e.g., ruta más corta en términos de número de tramos o considerando pesos si se definen) utilizando GraphAlgorithms.  
* **Documentación completa** del proyecto (JavaDoc, README actualizado).

### **6.1. Tareas Detalladas para Incremento 2 (Borrador Inicial)**

* **Paquete** modelo **(Refinamientos):**  
  * Pasajero.java: Añadir atributos para registrar horaLlegadaAParada, horaSubidaAColectivo, horaBajadaDeColectivo. Considerar un EnumPasajeroEstado { ESPERANDO, VIAJANDO, LLEGO\_DESTINO }.  
  * Colectivo.java: Añadir estado (e.g., EN\_RUTA, ESPERANDO\_EN\_TERMINAL), lógica para manejar frecuencias/horarios, y respetar capacidadMaxima.  
  * Parada.java: Podría acumular estadísticas locales (e.g., máximo de personas en espera, tiempo total de espera).  
* **Paquete** datos\*\*:  
  * LectorArchivos.java: Extender para leer horarios\_pm.txt.  
  * Evaluar si la necesidad de ordenamiento intrínseco o la integración con el módulo de grafos justifica cambiar HashMap a TreeMap (de java.util o net.datastructures) para las colecciones globales.  
* **Paquete** logica\*\*:  
  * Simulador.java (Extensión):  
    * Atributo: tiempoSimulacionActual: int (o LocalDateTime).  
    * Gestión de horarios de inicio/fin de servicio, frecuencias.  
    * Modificar ejecutarCicloSimulacion() para manejar múltiples recorridos, control de tiempo, y actualización de estadísticas.  
    * Método ejecutarSimulacionCompleta(int tiempoTotalSimulacion): Bucle principal de simulación.  
  * GestorEstadisticas.java (Nueva Clase):  
    * Métodos para registrar eventos relevantes (subidas, bajadas, tiempos de espera).  
    * Métodos para calcular y devolver las estadísticas requeridas.  
  * PlanificadorRutas.java **(Nueva Clase Central \- Usaría** net.datastructures.Graph\*\*):\*\*  
    * **Te recomiendo que, cuando llegues a la implementación de PlanificadorRutas en el Incremento 2, tengas este GraphExamples.java a mano como referencia directa para la construcción del grafo.**  
    * **Atributo:** private Graph\<Parada, Object\> grafoRedTransporte; (donde Object puede ser Integer para número de tramos, o Double para distancia/tiempo, o una clase Tramo si se decide modelar).  
    * **Constructor:** PlanificadorRutas(Map\<String, Parada\> todasLasParadas, Map\<String, Linea\> todasLasLineas)  
      * Inicializa grafoRedTransporte \= new AdjacencyMapGraph\<\>(true); (dirigido).  
      * Itera sobre todasLasParadas.values() e inserta cada Parada como un vértice en grafoRedTransporte.  
      * Itera sobre todasLasLineas.values():  
        * Para cada Linea, obtiene su List\<Parada\> recorrido.  
        * Para cada par de paradas consecutivas (p\_actual, p\_siguiente) en el recorrido:  
          * Inserta una arista dirigida en grafoRedTransporte desde p\_actual hasta p\_siguiente.  
          * El peso de la arista podría ser 1 (un tramo), o la distancia geográfica entre p\_actual y p\_siguiente (calculada a partir de lat/lon), o un tiempo estimado.  
    * **Método:** public List\<Parada\> encontrarRutaMasCorta(Parada origen, Parada destino) (o similar, podría devolver una lista de tramos o instrucciones).  
      * Utiliza GraphAlgorithms.shortestPath(grafoRedTransporte, verticeOrigen, verticeDestino) de net.datastructures (algoritmo de Dijkstra si los pesos son no negativos, o BFS si los pesos son todos 1).  
      * Adapta la salida para que sea comprensible (e.g., secuencia de paradas a seguir, líneas a tomar).  
* **Paquete** interfaz\*\*:  
  * SimuladorColectivosApp.java (Mejoras):  
    * Permitir al usuario configurar parámetros de simulación (tiempo total, etc.).  
    * Mostrar estadísticas detalladas al final de la simulación.  
    * Si se implementa PlanificadorRutas, permitir al usuario consultarlo para encontrar cómo ir de una parada a otra.  
* **Paquete** test\*\*:  
  * PlanificadorRutasTest.java (Nueva Clase de Test):  
    * Pruebas para la correcta construcción del grafo.  
    * Pruebas para la funcionalidad de encontrarRutaMasCorta con casos simples y conocidos.

## **7\. Checklist Previo a Entrega (General para cada Incremento)**

* Modelo de clases implementado correctamente según especificaciones.  
* Carga de archivos (si aplica) con validación robusta.  
* Lógica de simulación funcional según objetivos del incremento.  
* Interfaz de usuario (consola) funcional y clara.  
* Sin System.out.println indebidos (solo en capa de interfaz o tests controlados).  
* Tests unitarios (JUnit) para clases y lógicas clave.  
* Estructura de carpetas por capas respetada.  
* Código bien comentado y legible (JavaDoc para métodos públicos).  
* README.md actualizado (si hay cambios significativos en la estructura o uso).

## **8\. Actualización Continua del Proyecto y Documentación**

* Este roadmap (roadmap-proyecto-definitivo.md) es un documento vivo y debe ser consultado y actualizado regularmente por el desarrollador (MiyoBran) para reflejar el progreso real del proyecto, los cambios en la planificación o las decisiones de diseño.  
* La sección "Estado Actual" en el documento prompt-proyecto-definitivo.md (ubicado en src/main/resources/) debe mantenerse sincronizada con los avances descritos y completados en este roadmap.