## Contexto del Proyecto: Simulador de Colectivos Urbanos (Algoritmica y Programacion II)

**Desarrollador:** MiyoBran
**Fecha de última actualización de este prompt:** 2025-05-28
**Objetivo de este prompt:** Establecer rápidamente el contexto para @copilot sobre el proyecto "Simulador de Colectivos Urbanos", su planificación completa para dos incrementos, y su relación con el material de referencia de la cátedra.

---

**Descripción General del Proyecto:**
Estamos desarrollando un sistema en Java para simular el funcionamiento de líneas de colectivos urbanos. El proyecto se divide en dos incrementos principales y sigue una arquitectura de programación por capas (`modelo`, `datos`, `logica`, `interfaz`, `test`). El objetivo es simular el movimiento de colectivos, la subida y bajada de pasajeros, y, potencialmente, analizar la eficiencia del sistema.

**Datos del Mundo Real (Puerto Madryn):**
*   **Líneas de Colectivo:** El sistema modelará los recorridos, paradas y (eventualmente) horarios de las líneas de colectivo de Puerto Madryn.
*   **Mapa de la Ciudad:** Se busca integrar o referenciar datos del mapa, especialmente para la ubicación de paradas.
*   **Impacto:** Esta orientación influirá en el diseño de las clases de la capa `datos` y en la información que manejan las entidades del `modelo`.

**Tecnologías y Herramientas Clave:**
*   **Lenguaje:** Java (JDK 21)
*   **Gestor de Proyecto:** Maven
*   **Pruebas:** JUnit 5
*   **IDE:** Eclipse
*   **Librería de TADs Principal (para entidades de simulación):** Java Collections Framework (`java.util`).
*   **Librería de TADs Secundaria (para funcionalidad de grafos y almacenamiento global de datos):** `net.datastructures-library` (versión `6.0.0-custom`).

**Política de Uso de Colecciones/TADs (Enfoque Híbrido):**
El proyecto adopta un enfoque híbrido para el uso de Tipos Abstractos de Datos (TADs) y sus implementaciones:
1.  **Para las colecciones internas de las entidades del modelo de simulación (`Parada`, `LineaColectivo`, `Colectivo`):**
    *   Se utilizarán primordialmente las implementaciones del **Java Collections Framework (`java.util`)** (e.g., `ArrayList`, `LinkedList` como `Queue`).
    *   **Justificación:** Para la gestión de listas de pasajeros, recorridos secuenciales de paradas y colas de espera, `java.util` ofrece robustez, una API completa, y un rendimiento estándar y bien conocido. Esta elección simplifica el código de estas entidades y se alinea con las prácticas comunes de Java.
2.  **Para la funcionalidad de grafos y el almacenamiento principal de datos a nivel de aplicación:**
    *   Se utilizará la librería **`net.datastructures`** (la versión `6.0.0-custom` incluida en el proyecto).
    *   **Justificación:** Si el proyecto requiere modelar la red de transporte completa como un grafo (e.g., para calcular rutas óptimas entre dos puntos cualesquiera de la ciudad considerando transbordos), se emplearán las implementaciones de grafos de `net.datastructures` (`AdjacencyMapGraph`) y sus algoritmos asociados (`GraphAlgorithms`). Además, para el almacenamiento y acceso global a todas las paradas y líneas cargadas desde archivos (similar a los `TreeMap` en el ejemplo "subte"), se podría optar por `net.datastructures.TreeMap` si se requiere ordenación o para mantener consistencia con el ejemplo de referencia en esta capa.
    *   Esta decisión permite aprovechar las implementaciones especializadas de `net.datastructures` para tareas complejas de grafos, alineándose con el material de la cátedra donde estas estructuras son fundamentales.

**Referencia del Ejemplo "subte" de la Cátedra:**
El proyecto "subte" proporcionado por la cátedra sirve como un importante material de referencia. Sus características principales y cómo informan nuestro proyecto son:
*   **Uso de `net.datastructures`:** "Subte" utiliza intensivamente `net.datastructures` para:
    *   `TreeMap`: Almacenar las colecciones principales de estaciones y líneas cargadas desde archivos.
    *   `AdjacencyMapGraph` y `GraphAlgorithms`: Modelar la red de subtes como un grafo y calcular la ruta más rápida entre estaciones.
*   **Enfoque Principal:** El proyecto "subte" se centra en el cálculo de rutas óptimas (pathfinding) sobre un grafo estático.
*   **Modelo de Datos Simplificado:** Sus clases de modelo (`Estacion`, `Linea`, `Tramo`) son principalmente POJOs que sirven para alimentar el grafo y las colecciones principales. No usan `net.datastructures` para colecciones internas.
*   **Nuestro Enfoque en Relación a "subte":**
    *   **Simulación vs. Pathfinding:** Nuestro proyecto se enfoca primordialmente en la *simulación dinámica* del movimiento de colectivos y la interacción con pasajeros, lo cual difiere del enfoque principal de "subte".
    *   **TADs:** Adoptamos el enfoque híbrido descrito arriba. Usamos `java.util` donde "subte" no usa colecciones internas en sus POJOs (nuestras entidades de simulación), y reservamos `net.datastructures` para funcionalidades análogas a donde "subte" las usa (gestión global de datos cargados y, especialmente, grafos).
    *   **Clase `Tramo`:** La clase `Tramo` en "subte" es crucial para definir las aristas del grafo. Si implementamos una funcionalidad de grafo similar, consideraremos una entidad análoga.

**Estructura del Proyecto:**
*   `pom.xml`: Define dependencias y build.
*   Paquetes principales en `src/main/java/ar/edu/unpsjb/ayp2/proyectointegrador/`:
    *   `modelo`: Clases de entidad (Parada, Pasajero, LineaColectivo, Colectivo).
    *   `datos`: Carga de datos desde archivos (e.g., `LectorArchivos`, y potencialmente una clase de configuración).
    *   `logica`: Lógica de negocio y simulación (e.g., `Simulador`, y potencialmente `PlanificadorRutas` si se implementa grafo).
    *   `interfaz`: Interacción con el usuario (e.g., `SimuladorColectivosApp` para consola).
*   Pruebas en `src/test/java/ar/edu/unpsjb/ayp2/proyectointegrador/`.
*   Archivos de recursos (datos, este prompt, roadmap) en `src/main/resources/`.

**REFERENCIA PRINCIPAL Y ESTADO ACTUAL:**
*   **Roadmap Detallado:** El plan de trabajo completo para los dos incrementos, las clases a desarrollar, los TADs por usar y los objetivos de cada uno se encuentran en:
    `src/main/resources/roadmap-proyecto.md`
*   **Estado Actual:** Nos encontramos trabajando en el **Incremento 1**.
    *   Configuración base del proyecto (`pom.xml`) completada.
    *   Política de TADs (enfoque híbrido) definida.
    *   Análisis del ejemplo "subte" completado e incorporado a la planificación.
    *   Clase `Pasajero.java` y sus pruebas, completadas.
    *   Clase `Parada.java` (usa `java.util.LinkedList` como `Queue`) y sus pruebas, completadas.
    *   Clase `LineaColectivo.java` (usa `java.util.ArrayList` como `List`) y sus pruebas, completadas.
    *   Clase `Colectivo.java` (usa `java.util.ArrayList`) implementada.
    *   **Siguiente paso inmediato:** Completar y verificar `ColectivoTest.java`.
    *   **Pendiente (Incremento 1):** Desarrollo de `LectorArchivos.java` (para datos de Pto. Madryn), `Simulador.java` (lógica de simulación básica), e `SimuladorColectivosApp.java` (interfaz de consola simple).

**MANTENIMIENTO DE ESTE PROMPT Y EL ROADMAP:**
*   **Instrución para @copilot:** Si nuestra conversación indica que hemos completado una tarea significativa del roadmap, hemos decidido cambiar una parte del plan, o si el "Estado Actual" descrito arriba ya no es preciso, por favor, **sugiéreme explícitamente que actualice el archivo `src/main/resources/roadmap-proyecto.md` y/o esta sección de "Estado Actual" en `src/main/resources/prompt-proyecto.md`**.

**Instrucción para @copilot (General):**
Por favor, considera toda esta información para nuestras interacciones. Cuando tengas dudas sobre los próximos pasos, la estructura de una clase, los objetivos de un incremento, o los TADs a utilizar, **consulta (conceptualmente) el contenido del `roadmap-proyecto.md` como guía principal.** Si te pido ayuda para una nueva clase o funcionalidad, asume que debe alinearse con este contexto y el roadmap.

Mi nombre de usuario es MiyoBran.
La fecha actual es: 2025-05-28 02:00:57

---
**Pregunta/Tarea Actual:**
(Aquí es donde MiyoBran añadirá su pregunta específica después de pegar este prompt)
---
***Ejemplo***
 Pregunta/Tarea Actual: Necesito ayuda para diseñar la clase Colectivo.java según lo especificado para el Incremento 1 en el roadmap. ¿Qué atributos y métodos principales debería tener, considerando los TADs de net.datastructures?