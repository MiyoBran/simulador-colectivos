# **Roadmap del Proyecto: Simulador de Colectivos Urbanos (Definitivo)**

Última Actualización: 2025-06-21 (Versión final del Incremento 2)
Desarrollador: MiyoBran
Documento de Contexto Principal: prompt-proyecto.md (ubicado en src/main/resources/)

## Estado actualizado (junio 2025)

- **Desarrollo del Incremento 2 Finalizado:** Se implementaron y probaron exitosamente las funcionalidades de cálculo de rutas con grafos y gestión de estadísticas.
- **Integración Completa:** Los nuevos módulos de lógica fueron integrados en el simulador principal y son accesibles a través de un menú de usuario interactivo.
- **Proyecto Estable:** Todos los tests unitarios (nuevos y existentes) pasan correctamente.
- **Próximos pasos:** Revisión final de la documentación y preparación para la entrega del proyecto.

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

## 6. Log de Desarrollo – Incremento 2 (COMPLETADO)

**Fecha de Finalización:** 2025-06-21

### 6.1. Objetivos Principales Alcanzados

* Se logró soportar múltiples recorridos por colectivo y una gestión de estado básica.
* Se introdujo la noción de tiempo en la simulación (tiempos de espera, viaje, etc.).
* Se implementó un control estricto de capacidad, diferenciando pasajeros sentados y de pie.
* Se recolectan y muestran estadísticas detalladas sobre la simulación (pasajeros, tiempos, ocupación, satisfacción) a través del `GestorEstadisticas`.
* Se modeló la red de transporte como un grafo dirigido (`AdjacencyMapGraph`) y se implementó el cálculo de rutas óptimas para pasajeros con `PlanificadorRutas`.
* Se mantuvo una estructura limpia por capas y se amplió la cobertura de tests.

---

### 6.2. Parámetros y Configuración para Incremento 2

Se agregaron y utilizaron exitosamente los siguientes parámetros en `config.properties`:

-   `capacidadSentadosColectivo`
-   `cantidad_de_colectivos_simultaneos_por_linea`
-   `recorridos_por_colectivo`
-   `frecuencia_salida_colectivos_minutos`

---

### 6.3. Estructura Final y Clases Implementadas

La arquitectura final del proyecto consolidó la separación por capas, resultando en la siguiente estructura para las nuevas funcionalidades:

**Modelo (`modelo`):**
-   Se extendieron las clases `Colectivo`, `Pasajero` y `Parada` con nuevos atributos y métodos para soportar las funcionalidades de estado, tiempo y estadísticas.

**Lógica (`logica`):**
-   **`GestorEstadisticas`**: Se implementó y testeó esta clase para centralizar la recolección de métricas.
-   **`PlanificadorRutas`**: Se implementó y testeó esta clase para encapsular la construcción del grafo dirigido y el cálculo de rutas óptimas con Dijkstra.
-   **`Simulador`**: Se modificó para integrar y utilizar instancias de los nuevos componentes de lógica.

**Interfaz (`interfaz`):**
-   `SimuladorColectivosApp` se actualizó con un menú de usuario interactivo para ejecutar la simulación y acceder a las nuevas funcionalidades de planificación y estadísticas.

**Test (`test`):**
-   Se crearon las clases `GestorEstadisticasTest` y `PlanificadorRutasTest` para garantizar el correcto funcionamiento de los nuevos componentes.

---

### 6.4. Resumen del Proceso de Desarrollo

El desarrollo del incremento siguió un flujo de trabajo iterativo y basado en componentes:
1.  **Extensión del Modelo:** Se modificaron las clases de dominio para soportar los nuevos datos.
2.  **Implementación de Lógica:** Se desarrollaron y testearon de forma aislada los componentes `GestorEstadisticas` y `PlanificadorRutas`.
3.  **Fase de Integración:** Se unificaron los nuevos componentes en la clase `Simulador` y se expusieron las funcionalidades en la `SimuladorColectivosApp`.
4.  **Consolidación y Documentación:** Se unificó el trabajo en una única rama de proyecto y se actualizó toda la documentación para reflejar el estado final.

---

## 7. Checklist Previo a Entrega (Actualizado)

* Modelo de clases implementado correctamente.
* Carga de archivos y configuración robusta.
* Simulación y lógica funcional según objetivos del incremento.
* Interfaz (consola) funcional, modular y desacoplada de la lógica.
* Resultados/exportación desacoplados y flexibles (consola, archivo, etc.).
* Sin `System.out.println` indebidos (solo en exportadores autorizados).
* Tests unitarios exhaustivos para componentes nuevos y refactorizados.
* Estructura de carpetas y paquetes respeta arquitectura.
* Código bien comentado, con JavaDoc para métodos públicos.
* `README.md` y este roadmap actualizados.

---

## 8. Actualización Continua del Proyecto y Documentación

* Este `roadmap-proyecto.md` es un documento vivo y debe actualizarse para reflejar el progreso, cambios de arquitectura y nuevas decisiones de diseño.
* Mantener el “Estado Actual” en `prompt-proyecto.md` sincronizado con el roadmap.

---
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