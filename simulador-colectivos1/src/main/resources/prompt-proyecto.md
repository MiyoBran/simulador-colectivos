Contexto del Proyecto: Simulador de Colectivos Urbanos (Algoritmica y Programacion II)

Desarrollador: MiyoBran
**Fecha de última actualización de este prompt:** 2025-06-06
Establecer rápidamente el contexto para asistentes de IA sobre el proyecto "Simulador de Colectivos Urbanos", su planificación, su arquitectura final tras el refactoring del Incremento 1, y su relación con el material de referencia de la cátedra.
Descripción General del Proyecto: Estamos desarrollando un sistema en Java para simular el funcionamiento de líneas de colectivos urbanos. El proyecto se divide en dos incrementos principales y sigue una estricta arquitectura por capas (`modelo`, `datos`, `logica`, `interfaz`, `test`). El objetivo es simular el movimiento de colectivos, la subida y bajada de pasajeros, y, para el Incremento 2, analizar la eficiencia del sistema y calcular rutas óptimas.

**Principio de Diseño Clave: Separación de Lógica e Interfaz**
Como resultado de un profundo proceso de refactoring al finalizar el Incremento 1, el proyecto adhiere estrictamente al principio de separación de capas:
* La capa de **`logica`** (ej. `Simulador.java`) es "silenciosa". Contiene toda la lógica de negocio pero **no realiza ninguna impresión en consola**. Sus métodos procesan el estado y devuelven datos o listas de eventos.
* La capa de **`interfaz`** (ej. `SimuladorColectivosApp.java`) es la única responsable de la interacción con el usuario. Orquesta la simulación, llama a los métodos de la capa de lógica y se encarga de presentar los resultados en la consola.



Datos del Mundo Real (Puerto Madryn):

* Líneas de Colectivo: El sistema modelará los recorridos, paradas y (para Incremento 2) horarios de las líneas de colectivo de Puerto Madryn.
* Mapa de la Ciudad: Se busca integrar o referenciar datos del mapa, especialmente para la ubicación de paradas y potencialmente para el cálculo de distancias en Incremento 2.
* Impacto: Esta orientación influirá en el diseño de las clases de la capa datos y en la información que manejan las entidades del modelo.

Tecnologías y Herramientas Clave:

* Lenguaje: Java (JDK 21)
* Gestor de Proyecto: Maven
* Pruebas: JUnit 5
* IDE: Eclipse
* Librería de TADs Principal (para entidades de simulación y colecciones generales en Inc. 1): Java Collections Framework (java.util).
* Librería de TADs Secundaria (para funcionalidad de grafos en Inc. 2 y potencialmente almacenamiento global de datos): net.datastructures-library (versión 6.0.0-custom).

**Política de Uso de Colecciones/TADs (Enfoque Híbrido)**
* **Java Collections Framework (`java.util`):** Se utiliza para todas las colecciones internas de las entidades del modelo (`ArrayList`, `LinkedList` como `Queue`) y para el almacenamiento de datos globales. Para el Incremento 2, se ha decidido que `LectorArchivos` utilizará **`java.util.TreeMap`** para las colecciones de paradas y líneas, con el fin de aprovechar el ordenamiento intrínseco de las claves, lo cual facilita la depuración y la generación de reportes.
* **`net.datastructures`:** Su uso está reservado para la funcionalidad de grafos en el Incremento 2 (`AdjacencyMapGraph` y `GraphAlgorithms`), alineándose así con el material de la cátedra para tareas algorítmicas complejas.

Referencia del Ejemplo "subte" de la Cátedra: El proyecto "subte" proporcionado por la cátedra sirve como un importante material de referencia. Sus características principales y cómo informan nuestro proyecto son:

* Uso de net.datastructures: "Subte" utiliza intensivamente net.datastructures para:
    * TreeMap: Almacenar las colecciones principales de estaciones y líneas cargadas desde archivos.
    * AdjacencyMapGraph y GraphAlgorithms: Modelar la red de subtes como un grafo y calcular la ruta más rápida entre estaciones.
* Enfoque Principal: El proyecto "subte" se centra en el cálculo de rutas óptimas (pathfinding) sobre un grafo estático.
* Modelo de Datos Simplificado: Sus clases de modelo (Estacion, Linea, Tramo) son principalmente POJOs que sirven para alimentar el grafo y las colecciones principales. No usan net.datastructures para colecciones internas.

Nuestro Enfoque en Relación a "subte":

* Simulación vs. Pathfinding: Nuestro proyecto se enfoca primordialmente en la simulación dinámica del movimiento de colectivos y la interacción con pasajeros. El pathfinding se considera una funcionalidad avanzada para el Incremento 2.
* TADs: Adoptamos el enfoque híbrido descrito arriba. Usamos java.util para las colecciones internas de nuestras entidades de simulación y para la gestión de datos en Incremento 1. Reservamos net.datastructures para funcionalidades análogas a donde "subte" las usa (gestión global de datos cargados si se opta por TreeMap en Inc. 2, y especialmente para grafos en Inc. 2).
* Clase Tramo: La clase Tramo en "subte" es crucial para definir las aristas del grafo. Si implementamos una funcionalidad de grafo similar en Incremento 2, consideraremos una entidad análoga para los pesos de las aristas si es necesario (e.g., un objeto que contenga distancia y tiempo).

Estructura del Proyecto:

* **Paquetes principales (`src/main/java/ar/edu/unpsjb/ayp2/proyectointegrador/`):**
    * `modelo`: Clases de entidad (`Parada`, `Pasajero`, `Linea`, `Colectivo`).
    * `datos`: Carga de datos desde archivos (`LectorArchivos`, que también maneja `config.properties`).
    * `logica`: Lógica de negocio y simulación (`Simulador`, `GeneradorPasajeros`). Para Inc. 2: `PlanificadorRutas`, `GestorEstadisticas`.
    * `interfaz`: Interacción con el usuario (`SimuladorColectivosApp`).

* pom.xml: Define dependencias (Java 21, JUnit 5, net.datastructures-library 6.0.0-custom) y configuración de build.
* Pruebas en src/test/java/ar/edu/unpsjb/ayp2/proyectointegrador/ (con subpaquetes análogos).
* Archivos de recursos (datos de Puerto Madryn, config.properties, este prompt, roadmap) en src/main/resources/.

Recordatorio de Identificadores Clave:
"Las entidades principales del modelo son Parada (id: String), Linea (codigo: String), Colectivo (idColectivo: String), y Pasajero (id: String), como se detalla en diagramaUML-descripcion.txt."

***REFERENCIA PRINCIPAL Y ESTADO ACTUAL:**
**Roadmap Detallado:** El plan de trabajo completo se encuentra en: `src/main/resources/roadmap-proyecto.md`

**Estado Actual:** Se ha completado exitosamente el **Incremento 1**, incluyendo un ciclo completo de refactoring y depuración. La base de código es robusta y respeta la arquitectura de capas. Estamos comenzando el desarrollo del **Incremento 2**.

* **Completado (Etapa 0 - Configuración y Planificación):**
    * Definición del `pom.xml` y configuración de dependencias (Java 21, JUnit 5, net.datastructures).
    * Creación de la estructura de carpetas y paquetes base (`ar.edu.unpsjb.ayp2.proyectointegrador` y subpaquetes).
    * Ubicación de archivos de datos iniciales (`paradas_pm_mapeadas.txt`, `lineas_pm_mapeadas.txt`, `config.properties`) en `src/main/resources/`.
    * Definición de la Política de TADs (enfoque híbrido).
    * Análisis del ejemplo "subte" de la cátedra completado e incorporado a la planificación conceptual.
    * Finalización y revisión del `roadmap-proyecto.md`.
    * Revisión y finalización inicial de este `prompt-proyecto.md`.
* **Completado (Incremento 1):**
    * Implementación y pruebas de las capas `modelo`, `datos`, `logica` e `interfaz`.
    * Implementación de las clases del paquete `modelo` (Pasajero, Parada, Linea, Colectivo) con su lógica básica.
    * Implementación del paquete `datos` (`LectorArchivos`) para cargar paradas, líneas y configuración desde archivos.
    * Implementación del paquete `logica` (`GeneradorPasajeros`, `Simulador`) para manejar la creación de pasajeros, asignación de colectivos y la simulación de un único recorrido por colectivo.
    * Implementación del paquete `interfaz` (`SimuladorColectivosApp`) para la interacción básica por consola.    
    * Separación exitosa de la lógica de simulación de la presentación en consola.
    * Desarrollo de pruebas unitarias (JUnit) para las clases implementadas.	
    * Resolución de bugs de lógica y temporización en el bucle de la simulación.
    * Creación de la documentación para la presentación del Incremento 1.
    
* **Siguiente paso inmediato:** Comenzar con las tareas del **Incremento 2** según el `roadmap-proyecto.md`. Esto incluye los refinamientos a las clases del paquete `modelo` para incorporar la gestión del tiempo y estados, extender `LectorArchivos` para nuevos formatos de datos (como `horarios_pm.txt`), y comenzar el desarrollo de `Simulador.java` para manejar múltiples recorridos, la lógica temporal, y la gestión de capacidad estricta. Posteriormente se abordará `GestorEstadisticas.java` y `PlanificadorRutas.java`.
* **Pendiente (Incremento 2):** Todas las tareas de desarrollo y refinamiento para el Incremento 2, como se detalla en el `roadmap-proyecto.md` (principalmente Sección 6), incluyendo múltiples recorridos, gestión del tiempo, control de capacidad, cálculo de estadísticas, modelado de grafos con `net.datastructures` y funcionalidad de cálculo de rutas.

MANTENIMIENTO DE ESTE PROMPT Y EL ROADMAP:

**Instrucción para el Asistente de IA:** Si nuestra conversación indica que hemos completado una tarea significativa del `roadmap-proyecto.md`, hemos decidido cambiar una parte del plan, o si el "Estado Actual" descrito arriba ya no es preciso, por favor, **sugiéreme explícitamente que actualice el archivo `src/main/resources/roadmap-proyecto.md` y/o esta sección de "Estado Actual" en `src/main/resources/prompt-proyecto.md`**.

**Instrucción para el Asistente de IA (General):** Por favor, considera toda esta información para nuestras interacciones. Cuando tengas dudas sobre los próximos pasos, la estructura de una clase, los objetivos de un incremento, o los TADs a utilizar, **consulta (conceptualmente) el contenido del `roadmap-proyecto.md` como guía principal.** Si te pido ayuda para una nueva clase o funcionalidad, asume que debe alinearse con este contexto y el roadmap.

Mi nombre de usuario es MiyoBran.
Pregunta/Tarea Actual: (Aquí es donde MiyoBran añadirá su pregunta específica después de pegar este prompt)