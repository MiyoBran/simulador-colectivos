# Descripción del Diagrama UML del Paquete `proyectointegrador.modelo`

Este documento detalla la estructura y las relaciones de las clases principales del modelo: `Colectivo`, `Linea`, `Parada` y `Pasajero`, según su implementación real en el código fuente.

## 1. Clases y sus Relaciones

**Colectivo**
- Modela un vehículo de transporte colectivo.
- Tiene una **Agregación** con `Linea` (cada colectivo pertenece a una línea concreta, pero la línea existe independientemente).
- Tiene una **Agregación** con `Pasajero` (a través de su lista `pasajerosABordo`).
- Tiene una **Asociación** con `Parada` (referencia a `paradaActual`).

**Linea**
- Representa una ruta de transporte determinada.
- Tiene una **Agregación** con `Parada` (lista ordenada `recorrido` que define el trayecto).

**Parada**
- Representa una ubicación física donde los pasajeros esperan.
- Tiene una **Agregación** con `Pasajero` (cola `pasajerosEsperando` de los que esperan en la parada).

**Pasajero**
- Modela a una persona usuaria del sistema de transporte.
- Tiene **Asociaciones** con dos instancias de `Parada`: `paradaOrigen` y `paradaDestino`.

## 2. Atributos principales y visibilidad

Todos los atributos son `private` y la mayoría son `final` (inmutables tras la construcción), salvo los de estado (que pueden cambiar durante la simulación). El acceso es por métodos públicos (getters/setters) según buenas prácticas de Java.

### Colectivo

- `- final idColectivo: String`
- `- final lineaAsignada: Linea`
- `- final capacidadMaxima: int`
- `- final capacidadSentados: int`
- `- final capacidadParados: int`
- `- final pasajerosABordo: List<Pasajero>`
- `- paradaActual: Parada`
- `- indiceParadaActualEnRecorrido: int`
- `- cantidadPasajerosSentados: int`
- `- estado: String`
- `- recorridoActual: int`
- `- recorridosRestantes: int`
- `- pasoDeSalida: int`

### Linea

- `- final id: String`
- `- final nombre: String`
- `- final recorrido: List<Parada>`

### Parada

- `- final id: String`
- `- final direccion: String`
- `- final latitud: double`
- `- final longitud: double`
- `- final pasajerosEsperando: Queue<Pasajero>`
- `- tiempoEsperaPromedio: double`
- `- pasajerosAbordados: int`
- `- colectivosPasados: int`

### Pasajero

- `- final id: String`
- `- final paradaOrigen: Parada`
- `- final paradaDestino: Parada`
- `- colectivosEsperados: int`
- `- viajoSentado: boolean`
- `- pudoSubir: boolean`
- `- bajadaForzosa: boolean`
- `- satisfaccion: int`

## 3. Detalle de relaciones y cardinalidad

- `Colectivo "1" o-- "1" Linea`
- `Colectivo "1" o-- "0..*" Pasajero`
- `Colectivo "1" -- "1" Parada`
- `Linea "1" o-- "2..*" Parada`
- `Parada "1" o-- "0..*" Pasajero`
- `Pasajero "1" --> "1" Parada` (origen)
- `Pasajero "1" --> "1" Parada` (destino)

---
