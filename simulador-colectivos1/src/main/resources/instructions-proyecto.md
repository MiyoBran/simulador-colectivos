# Registro de Tareas y Colaboración – Incremento 2

## Resumen de Tareas Completadas

Durante el desarrollo del Incremento 2, se abordó una refactorización completa y la implementación de nuevas funcionalidades. Las tareas se dividieron de la siguiente manera y se completaron en su totalidad:

### **Fase 1: Desarrollo de Funcionalidades (Miyen)**
-   `[x]` **Extensión del Modelo:** Se ampliaron las clases `Colectivo`, `Pasajero` y `Parada` con nuevos atributos para soportar la lógica de capacidad, tiempo y estadísticas.
-   `[x]` **Implementación del Gestor de Estadísticas:** Se creó y probó la clase `GestorEstadisticas` para calcular el Índice de Satisfacción y el Promedio de Ocupación.
-   `[x]` **Implementación del Planificador de Rutas:** Se desarrolló y probó la clase `PlanificadorRutas` utilizando un grafo dirigido para el cálculo de rutas óptimas.
-   `[x]` **Integración en la Interfaz:** Se integraron todas las nuevas funcionalidades en la aplicación principal, exponiéndolas a través de un menú interactivo.

### **Fase 2: Refactorización de Arquitectura (Enzo y Miyen)**
-   `[x]` **Separación de Lógica y Presentación:** Se refactorizó la clase `Simulador` para que devolviera datos estructurados en lugar de imprimir en consola.
-   `[x]` **Modularización de la Interfaz:** Se reestructuró la capa de `interfaz` en un patrón más robusto con `SimuladorController`, `SimuladorUI` y `SimuladorConfig`, mejorando la separación de responsabilidades.

---

## Fase 3: Refactorización y Calidad de Pruebas (Post-Incremento 2)

Tras completar las funcionalidades principales, se llevó a cabo una fase intensiva dedicada a asegurar la calidad y mantenibilidad del código a través de una revisión completa de la suite de pruebas unitarias.

-   `[x]` **Revisión Integral de la Suite de Pruebas:** Se auditó y refactorizó cada clase de test del proyecto (`modelo`, `datos`, `logica` e `interfaz`).
-   `[x]` **Corrección de Pruebas Obsoletas:** Se alinearon todos los tests con la API actual del código de producción, eliminando llamadas a métodos y constructores que ya no existían.
-   `[x]` **Mejora de la Estructura de Tests:** Se implementó un estándar de organización usando clases anidadas (`@Nested`) de JUnit 5 para mejorar drásticamente la legibilidad y el mantenimiento de los tests.
-   `[x]` **Aumento de la Cobertura Lógica:** Se añadieron nuevos tests para cubrir casos límite, escenarios de error y lógica de negocio que no estaban previamente validados.
-   `[x]` **Descubrimiento y Corrección de Bugs:** El proceso de mejorar los tests permitió descubrir y corregir errores sutiles y condiciones lógicas contradictorias en el código de producción (ej. en `LectorArchivos` y `Simulador`).
-   `[x]` **Actualización de Documentación Técnica:** Se revisó y actualizó el archivo `README.md` y otros documentos para reflejar con precisión el estado final del proyecto, sus características y su proceso de instalación.

---

### Colaboración General y Flujo de Trabajo

Durante todo el proceso se mantuvieron las buenas prácticas de desarrollo:
-   `[x]` **Integración y Pruebas Coordinadas:** Se aseguró la correcta integración de todos los componentes.
-   `[x]` **Mantenimiento de Documentación:** Se mantuvieron actualizados todos los documentos del proyecto.
-   `[x]` **Respeto por las Convenciones:** Se siguió el estándar de código y estilo definido en `conventions-proyecto.md`.
-   `[x]` **Uso de Git Flow:** Se utilizaron ramas y Pull Requests para la gestión de cambios.

---
## Notas Finales

-   El `roadmap-proyecto.md` contiene el plan general y la arquitectura del proyecto.
-   Este archivo sirve como registro de los acuerdos y avances de la colaboración.