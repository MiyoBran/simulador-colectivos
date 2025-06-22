# Registro de Tareas y Colaboración – Incremento 2

## Resumen de Tareas Completadas

Durante el desarrollo del Incremento 2, se abordó una refactorización completa y la implementación de nuevas funcionalidades. Las tareas se dividieron de la siguiente manera y se completaron en su totalidad:

### **Desarrollo de Funcionalidades (Miyen)**
-   `[x]` **Extensión del Modelo:** Se ampliaron las clases `Colectivo`, `Pasajero` y `Parada` con nuevos atributos para soportar la lógica de capacidad, tiempo y estadísticas.
-   `[x]` **Implementación del Gestor de Estadísticas:** Se creó y probó la clase `GestorEstadisticas` para calcular el Índice de Satisfacción y el Promedio de Ocupación.
-   `[x]` **Implementación del Planificador de Rutas:** Se desarrolló y probó la clase `PlanificadorRutas` utilizando un grafo dirigido para el cálculo de rutas óptimas.
-   `[x]` **Integración en la Interfaz:** Se integraron todas las nuevas funcionalidades en la aplicación principal, exponiéndolas a través de un menú interactivo.
-   `[x]` **Ampliación de Tests:** Se actualizaron y crearon tests unitarios para toda la nueva lógica implementada.

### **Refactorización de Arquitectura (Enzo y Miyen)**
-   `[x]` **Separación de Lógica y Presentación:** Se refactorizó la clase `Simulador` para que devolviera datos estructurados en lugar de imprimir en consola.
-   `[x]` **Modularización de la Interfaz:** Se reestructuró la capa de `interfaz` en un patrón más robusto con `SimuladorController`, `SimuladorUI` y `SimuladorConfig`, mejorando la separación de responsabilidades.
-   `[x]` **Actualización de Tests:** Se adaptaron todas las clases de prueba a las nuevas arquitecturas y dependencias, incluyendo la configuración de Mockito para Java 21.

### **Colaboración General (Ambos)**
-   `[x]` **Integración y Pruebas Coordinadas:** Se aseguró la correcta integración de todos los componentes.
-   `[x]` **Mantenimiento de Documentación:** Se mantuvieron actualizados todos los documentos del proyecto (`prompt`, `roadmap`, etc.).
-   `[x]` **Respeto por las Convenciones:** Se siguió el estándar de código y estilo definido en `conventions-proyecto.md`.
-   `[x]` **Uso de Git Flow:** Se utilizaron ramas y Pull Requests para la gestión de cambios.

---

## Flujo de Trabajo Recomendado (Para Futuro Mantenimiento)

-   Usar ramas para nuevas funcionalidades o correcciones.
-   Hacer pull requests claros y con descripción del cambio.
-   Revisar y aprobar PRs antes de hacer merge.
-   Mantener comunicación y seguimiento de tareas en los issues del repositorio.

---

## Notas Finales

-   El `roadmap-proyecto.md` contiene el plan general y la arquitectura del proyecto.
-   Este archivo sirve como registro de los acuerdos y avances de la colaboración durante el Incremento 2.