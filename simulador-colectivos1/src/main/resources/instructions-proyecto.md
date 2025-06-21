# Instrucciones para el Desarrollo y Colaboración

## Introducción y División de Tareas – Incremento 2

En esta etapa del proyecto (Incremento 2), se propone el siguiente esquema de trabajo para optimizar la colaboración y el avance:

### División Sugerida de Tareas

- **Miyen**
  - Extender clases modelo (`Colectivo`, `Pasajero`, `Parada`) con los nuevos atributos y métodos requeridos.
  - Implementar y testear las clases `GestorEstadisticas` y `PlanificadorRutas`.
  - Integrar las nuevas funcionalidades en `SimuladorColectivosApp` (menú ampliado, opciones nuevas).
  - Actualizar y ampliar los tests unitarios correspondientes a las clases bajo su desarrollo.

- **Enzo**
  - Refactorizar la clase `Simulador` para separar claramente la generación de eventos de la exportación/visualización de resultados.
  - Implementar la interfaz y clases de exportadores (`ExportadorResultadosSimulacion` y sus implementaciones: consola, archivo, etc.).
  - Actualizar y ampliar los tests unitarios de las clases bajo su responsabilidad.

**Ambos** deben asegurarse de mantener la estructura, estilo y documentación según las convenciones, y comunicarse frecuentemente para coordinar la integración y pruebas.

---

## Checklist de la Fase 2: Refactor y Mejoras Iniciales

- [x] Limpiar el código: eliminar comentarios innecesarios y código muerto (modelo, datos, logica, interfaz).
- [x] Revisar que solo haya System.out.println en la interfaz o tests.
- [x] Asegurar que todas las clases y métodos públicos tengan JavaDoc.
- [x] Actualizar README.md, roadmap-proyecto.md y prompt-proyecto.md con el estado real del proyecto.
- [x] Verificar que todos los archivos estén en su paquete/carpeta correspondiente.
- [x] Borrar archivos obsoletos o de pruebas no relevantes.
- [x] Revisar dependencias en pom.xml y parámetros en config.properties.
- [x] Correr todos los tests y confirmar que pasan.
- [x] Marcar o corregir los tests desactualizados.
- [x] Agregar una sección en README.md para primeros pasos y colaboración.
- [x] Documentar cada avance relevante en este archivo y en los issues/PRs.

---

## Tareas Actuales (Fase 3 y 4: Desarrollo y Testing Incremento 2)

### Miyen
- [ ] Extender clases modelo (`Colectivo`, `Pasajero`, `Parada`) con atributos y métodos nuevos.
- [ ] Implementar y testear `GestorEstadisticas`.
- [ ] Implementar y testear `PlanificadorRutas`.
- [ ] Integrar nuevas funcionalidades en `SimuladorColectivosApp`.
- [ ] Actualizar y ampliar tests unitarios de sus clases.

### Enzo
- [ ] Refactorizar `Simulador` para separar generación de eventos y exportación/visualización.
- [ ] Implementar interfaz y clases de exportadores (`ExportadorResultadosSimulacion` y variantes).
- [ ] Actualizar y ampliar tests unitarios de sus clases.

### Ambos
- [ ] Coordinar integración y pruebas de las nuevas funcionalidades.
- [ ] Mantener la documentación y los archivos de instrucciones actualizados.
- [ ] Asegurarse de que la estructura y estilo del código respete las convenciones del proyecto.
- [ ] Usar ramas y pull requests para nuevas funcionalidades/refactors, y documentar avances en issues.

---

## Flujo de Trabajo Recomendado

- Usar ramas para nuevas funcionalidades o refactors.
- Hacer pull requests claros y con descripción del cambio.
- Revisar y aprobar PRs antes de hacer merge.
- Mantener comunicación y seguimiento de tareas en issues o proyectos de GitHub.

---

## Notas Finales

- Consulta siempre el roadmap-proyecto.md para el plan general y arquitectura.
- Este archivo debe reflejar acuerdos y avances concretos de la colaboración.
- Cualquier cambio importante debe registrarse aquí y en los issues/PRs.
