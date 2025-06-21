# Prompt del Proyecto: Simulador de Colectivos Urbanos

**Proyecto Integrador: Algorítmica y Programación II** Desarrollador : MiyoBran  
Desarrollador: Enzo  
Fecha de última actualización: 2025-06-21

---

## Estado actualizado (junio 2025)

- **Incremento 2 Completado:** Se implementaron todas las funcionalidades planificadas, incluyendo la gestión de estadísticas, el cálculo de rutas óptimas mediante grafos y un menú interactivo.
- **Código Estable y Probado:** Todos los tests unitarios para las nuevas funcionalidades pasan correctamente.
- **Documentación Actualizada:** Se ha revisado y actualizado la documentación del proyecto para reflejar el estado final.
- **Próximos pasos:** Revisión final y preparación para la entrega.

---

## 1. Contexto y Objetivo

Este proyecto simula el funcionamiento de líneas de colectivos urbanos en Java para la ciudad de Puerto Madryn. Se desarrolla en dos incrementos principales y sigue una arquitectura por capas estricta:

- **modelo**: entidades del dominio (Parada, Pasajero, Linea, Colectivo)
- **datos**: carga y gestión de datos (LectorArchivos, manejo de config.properties, etc)
- **logica**: simulación y procesamiento central (Simulador, GeneradorPasajeros, y para Inc.2: GestorEstadisticas, PlanificadorRutas, exportadores)
- **interfaz**: interacción con el usuario (SimuladorColectivosApp)
- **test**: pruebas automatizadas

Los objetivos principales:
- Simular el movimiento y la interacción de colectivos y pasajeros.
- Analizar la eficiencia, calcular rutas óptimas y proporcionar estadísticas relevantes.

---

## 2. Principios de Diseño y Referencias

- **Separación de lógica e interfaz**: la lógica (Simulador, etc.) es "silenciosa" y nunca imprime en consola, sólo devuelve datos/eventos. La interfaz (SimuladorColectivosApp) maneja toda la presentación.
- **Inspiración en el proyecto "subte" de la cátedra**, especialmente para el manejo de grafos y estructura de datos.
- **Uso de TADs híbridos**:  
  - Colecciones internas: Java Collections (`ArrayList`, `LinkedList`, etc.)
  - Colecciones globales de datos: Java `TreeMap` (Inc. 2)
  - Grafos y rutas: `net.datastructures.AdjacencyMapGraph`, `GraphAlgorithms` (Inc. 2)

---

## 3. Estado Actual

- **Incremento 1**: COMPLETADO
    - Refactoring finalizado. Arquitectura por capas respetada.
    - Pruebas unitarias implementadas.
    - Documentación y estructura de carpetas limpia.
    - Prompt y roadmap actualizados.
- **Incremento 2**: COMPLETADO
    - Se implementó la lógica para el cálculo de rutas óptimas (`PlanificadorRutas`) y la recolección de métricas (`GestorEstadisticas`).
    - Se extendió el modelo de dominio para soportar las nuevas funcionalidades (capacidad, tiempo, estado).
    - Se integraron las nuevas capacidades en la simulación principal y se expusieron a través de un menú de usuario interactivo.
    - Se crearon tests unitarios para todos los nuevos componentes.

---

---

## 5. Resumen de Funcionalidades – Incremento 2

### 5.1. Objetivos Alcanzados

- **Gestión de Estado y Capacidad:** Los colectivos ahora manejan múltiples recorridos, un estado (ej: EN_RUTA) y capacidad diferenciada (sentados/parados).
- **Simulación con Noción de Tiempo:** El sistema incorpora la gestión de tiempos de espera, viaje y frecuencias.
- **Módulo de Estadísticas:** Se implementó `GestorEstadisticas` para reportar sobre pasajeros transportados, tiempos, ocupación y satisfacción.
- **Red de Transporte como Grafo:** Se utiliza `PlanificadorRutas` para modelar la red y calcular la ruta más corta para los pasajeros mediante el algoritmo de Dijkstra.
- **Integración Completa:** Las nuevas funcionalidades son accesibles a través de un menú de texto en la aplicación principal.

### 5.2. Cambios Técnicos Realizados

- **Nuevos parámetros en `config.properties`** para gestionar las nuevas capacidades.
- **Nuevos módulos/clases implementados y testeados**:
  - `GestorEstadisticas`
  - `PlanificadorRutas`
- **Extensión de clases del modelo** (`Colectivo`, `Pasajero`, `Parada`) con nuevos atributos y métodos.
- **Ampliación de la interfaz de usuario** (`SimuladorColectivosApp`).

---

## 6. Checklist previo a cada entrega

- [ ] Código limpio, comentado y sin impresiones indebidas
- [ ] Tests automáticos completos y exitosos
- [ ] Documentación y archivos de instrucciones claros
- [ ] README actualizado y útil para nuevos colaboradores
- [ ] Estructura de carpetas y dependencias respetadas
- [ ] Estado actual reflejado en roadmap y prompt

---

## 7. Mantenimiento y Mejora Continua

- Si se completa una etapa importante o se cambia el plan, actualizar este roadmap y el prompt.
- Usar issues y PRs para gestionar tareas y discutir cambios.
- Toda instrucción central para el desarrollo debe quedar documentada en los archivos de resources.

---

## 8. Mejoras Futuras (Post-Incremento 2)

- Gestión avanzada de horarios y demoras
- Estadísticas por chofer y colectivo
- Generación dinámica de pasajeros
- Cálculo de rutas avanzadas (distancia, tiempo real)
- Interfaz gráfica de usuario

---

> **Nota:** > Este archivo debe ser el punto de referencia principal para la planificación y organización del proyecto, tanto para el desarrollador original como para nuevos colaboradores.