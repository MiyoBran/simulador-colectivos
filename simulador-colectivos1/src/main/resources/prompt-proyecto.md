# Prompt del Proyecto: Simulador de Colectivos Urbanos

**Proyecto Integrador: Algorítmica y Programación II**
Desarrollador: MiyoBran
Desarrollador: Enzo
Fecha de última actualización: 2025-06-22

---

## **Estado actualizado (junio 2025)**

* **Proyecto Finalizado:** Se implementaron y probaron todas las funcionalidades planificadas para los Incrementos 1 y 2, incluyendo gestión de estadísticas, cálculo de rutas óptimas mediante grafos y una interfaz de usuario interactiva.
* **Código Estable y Probado:** La suite completa de tests unitarios (113 pruebas) se ejecuta con éxito.
* **Documentación Completa:** Se ha revisado y actualizado toda la documentación del proyecto para reflejar el estado final de la entrega.
* **Próximos pasos:** Preparación del paquete de entrega final.

---

## **1. Contexto y Objetivo**

Este proyecto simula el funcionamiento de líneas de colectivos urbanos en Java para la ciudad de Puerto Madryn. Se desarrolla en dos incrementos principales y sigue una arquitectura por capas estricta:

* **modelo**: entidades del dominio (Parada, Pasajero, Linea, Colectivo).
* **datos**: carga y gestión de datos (LectorArchivos, manejo de config.properties).
* **logica**: simulación y procesamiento central (Simulador, GeneradorPasajeros, y para Inc.2: GestorEstadisticas, PlanificadorRutas).
* **interfaz**: interacción con el usuario, refactorizada en `SimuladorController`, `SimuladorUI` y `SimuladorConfig`.
* **test**: pruebas unitarias automatizadas con JUnit 5 y Mockito.

Los objetivos principales:
* Simular el movimiento y la interacción de colectivos y pasajeros.
* Analizar la eficiencia, calcular rutas óptimas y proporcionar estadísticas relevantes.

---

## **2. Principios de Diseño y Referencias**

* **Separación de Lógica e Interfaz:** La lógica (`logica`) es "silenciosa" y no tiene conocimiento de la presentación. La capa de interfaz (`interfaz`), refactorizada en un patrón Controlador-UI, se encarga de toda la interacción con el usuario y la presentación de datos.
* **Inspiración en el proyecto "subte" de la cátedra**, especialmente para el manejo de grafos y estructura de datos.
* **Uso de TADs híbridos**:
    * Colecciones internas: Java Collections (`ArrayList`, `LinkedList`, etc.).
    * Colecciones globales de datos: Java `TreeMap`.
    * Grafos y rutas: `net.datastructures.AdjacencyMapGraph`.

---

## **3. Estado Actual**

* **Incremento 1**: **COMPLETADO**
    * Refactoring finalizado. Arquitectura por capas respetada.
    * Pruebas unitarias implementadas.
    * Documentación y estructura de carpetas limpia.
* **Incremento 2**: **COMPLETADO**
    * Se implementó la lógica para el cálculo de rutas óptimas (`PlanificadorRutas`) y la recolección de métricas (`GestorEstadisticas`).
    * Se extendió el modelo de dominio para soportar las nuevas funcionalidades (capacidad, tiempo, estado).
    * Se integraron las nuevas capacidades en la simulación principal y se expusieron a través de un menú de usuario interactivo.
    * Se crearon y actualizaron tests unitarios para todos los componentes, asegurando la compatibilidad con Java 21.

---

## **5. Resumen de Funcionalidades – Incremento 2**

### **5.1. Objetivos Alcanzados**

* **Gestión de Estado y Capacidad:** Los colectivos ahora manejan múltiples recorridos, un estado (ej: EN_RUTA) y capacidad diferenciada (sentados/parados).
* **Simulación con Noción de Tiempo:** El sistema incorpora la gestión de tiempos de espera y viaje.
* **Módulo de Estadísticas:** Se implementó `GestorEstadisticas` para reportar sobre pasajeros transportados, tiempos, ocupación (Anexo II) y satisfacción (Anexo I).
* **Red de Transporte como Grafo:** Se utiliza `PlanificadorRutas` para modelar la red como un grafo dirigido y calcular la ruta más corta para los pasajeros.
* **Integración Completa:** Las nuevas funcionalidades son accesibles a través de un menú de texto en la aplicación principal.

### **5.2. Cambios Técnicos Realizados**

* **Nuevos parámetros en `config.properties`** para gestionar las nuevas capacidades.
* **Nuevos módulos/clases implementados y testeados**:
    * `GestorEstadisticas`
    * `PlanificadorRutas`
* **Refactorización de la Interfaz:** Se reestructuró la capa de interfaz en `SimuladorController`, `SimuladorUI` y `SimuladorConfig` para mejorar la separación de responsabilidades y la mantenibilidad.
* **Extensión de clases del modelo** (`Colectivo`, `Pasajero`, `Parada`).
* **Ampliación de la suite de tests** con Mockito para un testing más robusto.

---

## **6. Checklist previo a cada entrega**

* [x] Código limpio, comentado y sin impresiones indebidas.
* [x] Tests automáticos completos y exitosos.
* [x] Documentación y archivos de instrucciones claros.
* [x] README actualizado y útil para nuevos colaboradores.
* [x] Estructura de carpetas y dependencias respetadas.
* [x] Estado actual reflejado en roadmap y prompt.

---

## **7. Mantenimiento y Mejora Continua**

* Toda instrucción central para el desarrollo debe quedar documentada en los archivos de `resources`.
* Utilizar issues y PRs para gestionar tareas y discutir cambios futuros.

---

## **8. Mejoras Futuras (Post-Entrega)**

* **Logger Centralizado:** Implementar un sistema de logging (como Log4j) para reemplazar los `System.out.println` y tener un control más granular sobre los mensajes.
* **Gestión avanzada de horarios y demoras.**
* **Generación dinámica de pasajeros** basada en el tiempo de simulación.
* **Interfaz gráfica de usuario (GUI).**
* **Asignación Automática de Recorridos de Ida y Vuelta:** Desarrollar la funcionalidad para que, cuando un colectivo finalice un recorrido y llegue a su parada terminal, el sistema verifique la variable `recorridosRestantes`. Si esta es mayor a cero, se le debe asignar automáticamente la línea correspondiente al trayecto inverso (ej., cambiar de "Línea 1 - Ida" a "Línea 1 - Regreso") para que inicie el siguiente viaje sin intervención manual. Esto permitiría simular ciclos de trabajo completos y más realistas.

---

> **Nota:** Este archivo debe ser el punto de referencia principal para la planificación y organización del proyecto.