# Documento de Decisiones sobre Tipos Abstractos de Datos (TADs)

**Proyecto:** Simulador de Colectivos Urbanos  
**Fecha:** 2025-06-22

Este documento fundamenta la elección de las implementaciones concretas para los Tipos Abstractos de Datos (TADs) utilizados en los componentes clave del proyecto. Las decisiones se basan en la eficiencia (Complejidad Algorítmica / Notación Big O) y la adecuación al caso de uso específico.

---

## 1. Cola de Pasajeros en una Parada

-   **Componente/Clase:** `modelo.Parada`
-   **Atributo:** `pasajerosEsperando`
-   **TAD Requerido:** **Cola (Queue)**, para modelar el comportamiento "Primero en Entrar, Primero en Salir" (FIFO) de las personas que esperan el colectivo.
-   **Implementación Elegida:** `java.util.LinkedList`

#### Justificación:

La clase `java.util.LinkedList` en Java implementa la interfaz `Queue`. Fue elegida por sobre otras estructuras por su rendimiento óptimo para operaciones de cola:

-   **`addLast()` (equivalente a `offer(e)`):** Añadir un pasajero al final de la cola es una operación de tiempo constante, $O(1)$.
-   **`removeFirst()` (equivalente a `poll()`):** Quitar un pasajero del inicio de la cola también es una operación de tiempo constante, $O(1)$.

Una alternativa como un `ArrayList` sería ineficiente, ya que quitar un elemento del principio (`remove(0)`) implica desplazar todos los demás elementos, resultando en una complejidad de $O(n)$, lo cual es inaceptable para una operación tan frecuente en la simulación.

---

## 2. Recorrido de una Línea de Colectivo

-   **Componente/Clase:** `modelo.Linea`
-   **Atributo:** `recorrido`
-   **TAD Requerido:** **Lista (List)**, para almacenar la secuencia ordenada de paradas que componen una ruta.
-   **Implementación Elegida:** `java.util.ArrayList`

#### Justificación:

El recorrido de una línea se construye una vez durante la carga de datos y luego se consulta frecuentemente, pero rara vez se modifica. Las operaciones más comunes son:

-   **Acceso por índice (`get(i)`):** Para saber qué parada viene después de otra.
-   **Iteración:** Para recorrer la ruta completa.

`ArrayList` es ideal para este escenario:

-   **`get(i)`:** El acceso por índice es en tiempo constante, $O(1)$, lo cual es perfecto para la lógica de la simulación.
-   **`add(e)` (al final):** La adición de paradas durante la construcción inicial es en tiempo constante amortizado, $O(1)$.
-   **Memoria:** `ArrayList` es más eficiente en el uso de memoria que `LinkedList` porque no necesita almacenar punteros adicionales para cada elemento.

Dado que no se realizan inserciones o eliminaciones en medio del recorrido, la pobre performance de `ArrayList` para esas operaciones ($O(n)$) es irrelevante aquí.

---

## 3. Almacenamiento de Datos Cargados

-   **Componente/Clase:** `datos.LectorArchivos`
-   **Atributos:** `paradasCargadas`, `lineasCargadas`
-   **TAD Requerido:** **Mapa (Map)**, para asociar un ID único (un `String`) con su objeto correspondiente (`Parada` o `Linea`).
-   **Implementación Elegida:** `java.util.TreeMap`

#### Justificación:

La elección clave aquí fue entre `HashMap` y `TreeMap`.

-   **`HashMap`:** Ofrece un rendimiento promedio de $O(1)$ para las operaciones `put` y `get`. Es la opción más rápida.
-   **`TreeMap`:** Ofrece un rendimiento de $O(\log n)$ para `put` y `get`, pero con una ventaja crucial: **mantiene las claves ordenadas**.

Se eligió `TreeMap` deliberadamente por la **predictibilidad y facilidad de depuración**. Al iterar sobre el mapa de paradas o líneas, siempre se obtienen en el mismo orden (alfabético por ID), lo que hace que los reportes, los logs y el comportamiento de la simulación sean consistentes y reproducibles. En la fase de carga de datos, esta consistencia fue más valiosa que la pequeña ganancia de rendimiento de un `HashMap`.

---
### 4. Almacenamiento de Datos en Memoria para Impresión Ordenada

-   **Componente/Clase:** `interfaz.SimuladorUi`
-   **TAD Requerido:**
    1.  **Mapa (`Map`)** para asociar un ID único (`String`) con la lista de eventos (`List<String>`) generados por cada colectivo.
-   **Implementación Elegida:**
    1.  `java.util.LinkedHashMap`

#### Justificación:

1.  **Mapa (`LinkedHashMap`):**
    -   Aunque `HashMap` ofrece un rendimiento promedio de $O(1)$ para operaciones de acceso e inserción, se eligió `LinkedHashMap` debido a su capacidad de **preservar el orden de inserción de las claves**.
    -   Esta característica es clave para la presentación ordenada de los eventos de la simulación al usuario. Al mantener el orden en que se agregan los eventos por colectivo, se mejora la **claridad temporal** y se garantiza una salida más **intuitiva y reproducible** entre ejecuciones.
    -   En este contexto, cada entrada del mapa representa un colectivo, y la lista asociada contiene los eventos generados paso a paso durante la simulación.


---