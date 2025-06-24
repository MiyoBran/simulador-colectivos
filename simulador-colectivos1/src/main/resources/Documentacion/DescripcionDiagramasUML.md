# Descripción de Diagramas UML del Paquete `proyectointegrador.modelo`

Este documento detalla la estructura y las relaciones de las clases principales del modelo: `Colectivo`, `Linea`, `Parada` y `Pasajero`.

#### 1. Clases y sus Relaciones

Se describe el rol de cada clase y cómo interactúan entre sí, utilizando la terminología estándar de UML (Agregación, Asociación).

* **`Colectivo`**
    * Modela un vehículo de transporte.
    * Tiene una relación de **Agregación** con `Linea` (un Colectivo pertenece a una Linea, pero la Linea existe independientemente del Colectivo).
    * Tiene una relación de **Agregación** con `Pasajero` a través de su lista `pasajerosABordo`.
    * Mantiene una **Asociación** simple con `Parada` para rastrear su `paradaActual`.

* **`Linea`**
    * Representa una ruta de transporte.
    * Tiene una relación de **Agregación** con `Parada`, conteniendo una lista ordenada de ellas que conforman su `recorrido`.

* **`Parada`**
    * Representa una ubicación física donde los pasajeros esperan.
    * Tiene una relación de **Agregación** con `Pasajero`, gestionando una cola (`pasajerosEsperando`) de los que aguardan.

* **`Pasajero`**
    * Modela a un usuario del sistema.
    * Mantiene dos **Asociaciones** con `Parada` para definir su `paradaOrigen` y `paradaDestino`.

#### 2. Atributos Principales y Visibilidad

A continuación se listan los atributos fundamentales de cada clase. Es importante destacar que, siguiendo las buenas prácticas de encapsulamiento, **todos los atributos son `private` (`-`)** y se accede a ellos a través de métodos públicos (getters/setters).

* **Colectivo**
    * `- idColectivo: String`
    * `- lineaAsignada: Linea`
    * `- capacidadMaxima: int`
    * `- pasajerosABordo: List<Pasajero>`
    * `- paradaActual: Parada`

* **Linea**
    * `- id: String`
    * `- nombre: String`
    * `- recorrido: List<Parada>`

* **Parada**
    * `- id: String`
    * `- direccion: String`
    * `- pasajerosEsperando: Queue<Pasajero>`

* **Pasajero**
    * `- id: String`
    * `- paradaOrigen: Parada`
    * `- paradaDestino: Parada`

#### 3. Detalle de Relaciones y Cardinalidad

Esta sección describe las relaciones en un formato similar a PlantUML, especificando la multiplicidad (cardinalidad) de cada una.

* `Colectivo "1" o-- "1" Linea`
    * Un `Colectivo` siempre pertenece a `1` y solo `1` `Linea`. La `Linea` es una parte agregada del `Colectivo`.

* `Colectivo "1" o-- "0..*" Pasajero`
    * Un `Colectivo` puede tener `0` o muchos (`*`) `Pasajero`s a bordo.

* `Colectivo "1" -- "1" Parada`
    * Un `Colectivo` siempre está en `1` `Parada` actual (o en tránsito hacia ella).

* `Linea "1" o-- "2..*" Parada`
    * Una `Linea` se compone de un recorrido de al menos `2` (origen y destino) o muchas (`*`) `Parada`s.

* `Parada "1" o-- "0..*" Pasajero`
    * Una `Parada` puede tener `0` o muchos (`*`) `Pasajero`s esperando.

* `Pasajero "1" --> "1" Parada` (origen)
* `Pasajero "1" --> "1" Parada` (destino)
    * Un `Pasajero` siempre tiene exactamente `1` `Parada` de origen y `1` `Parada` de destino.