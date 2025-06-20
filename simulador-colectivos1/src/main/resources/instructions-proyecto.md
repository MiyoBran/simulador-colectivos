# Instrucciones para el Desarrollo y Colaboración

## Checklist de la Fase 2: Refactor y Mejoras Iniciales

- [x] Limpiar el código: eliminar comentarios innecesarios y código muerto (modelo, datos, logica, interfaz).
- [x] Revisar que solo haya System.out.println en la interfaz o tests.
- [x] Asegurar que todas las clases y métodos públicos tengan JavaDoc.
- [ ] Actualizar README.md, roadmap-proyecto.md y prompt-proyecto.md con el estado real del proyecto.
- [x] Verificar que todos los archivos estén en su paquete/carpeta correspondiente.
- [x] Borrar archivos obsoletos o de pruebas no relevantes.
- [x] Revisar dependencias en pom.xml y parámetros en config.properties.
- [x] Correr todos los tests y confirmar que pasan.
- [x] Marcar o corregir los tests desactualizados.
- [ ] Agregar una sección en README.md para primeros pasos y colaboración.
- [x] Documentar cada avance relevante en este archivo y en los issues/PRs.

## Avances Fase 2 (Refactor y Mejoras Iniciales)

- Se revisaron y limpiaron los paquetes `modelo`, `datos`, `logica` e `interfaz`.
- Se confirmó la ausencia de código muerto, comentarios innecesarios y la presencia de JavaDoc en clases y métodos públicos.
- Se verificó que los únicos `System.out.println`/`System.err.println` están en la interfaz o justificados para interacción con el usuario.
- No se detectaron problemas de organización de archivos ni código obsoleto en los paquetes revisados.
- Se revisaron los archivos de tests y recursos asociados, confirmando su correcta organización y vigencia.
- Se ejecutaron todos los tests del proyecto (101 en total) y pasaron correctamente, sin fallos ni errores.
- Próximo paso: actualizar la documentación general (README.md, roadmap-proyecto.md, prompt-proyecto.md) y agregar una sección de onboarding en el README.

## Estado actual del workspace y objetivo de la rama `miyo-workspace`

Este workspace y la rama `miyo-workspace` fueron creados para abordar la **Fase 2: Refactor y Mejoras Iniciales** del proyecto, según el plan general detallado en `roadmap-proyecto.md` (ver sección 6). Aquí se documentarán los avances, decisiones y tareas específicas de esta fase, así como las instrucciones para los colaboradores que se sumen a este proceso.

- En este archivo (`instructions-proyecto.md`) se irá reflejando el avance concreto, los acuerdos de trabajo y las instrucciones prácticas para la colaboración en la Fase 2.
- Para el desarrollo completo, la planificación a largo plazo y la arquitectura general, consulta siempre `roadmap-proyecto.md`.

## Tareas Previas a Compartir el Repositorio

1. **Limpieza de Código**
   - Elimina comentarios innecesarios y código muerto.
   - Revisa que solo haya System.out.println en la interfaz o tests.

2. **Documentación**
   - Asegúrate de que todas las clases y métodos públicos tengan JavaDoc.
   - Actualiza README.md, roadmap-proyecto.md y prompt-proyecto.md con el estado real del proyecto.

3. **Organización**
   - Verifica que todos los archivos estén en su paquete/carpeta correspondiente.
   - Borra archivos obsoletos o de pruebas no relevantes.

4. **Tests**
   - Corre todos los tests y confirma que pasan.
   - Marca o corrige los que estén desactualizados.

5. **Configuración**
   - Revisa dependencias en pom.xml y parámetros en config.properties.

6. **Guía para Colaboradores**
   - Agrega una sección en README.md para primeros pasos y colaboración.

---

## Proceso de Onboarding y Colaboración

### Fase 1: Comprensión del Proyecto

- El nuevo colaborador debe leer roadmap-proyecto.md, prompt-proyecto.md y conventions-proyecto.md.
- Ejecutar el proyecto y los tests localmente.
- Revisar el código fuente y generar dudas o sugerencias en issues/discusiones.

### Fase 2: Refactor y Mejoras Iniciales

- Miyen: Limpieza, documentación y preparación de la estructura para colaboración.
- Colaborador: Probar los tests, proponer mejoras y ayudar a documentar desde la mirada de alguien nuevo.

### Fase 3: Refactorización y Nuevas Funcionalidades

- Dividir tareas de refactorización (separación de lógica/exportación, modularización).
- Comenzar implementación de nuevas clases (GestorEstadisticas, PlanificadorRutas, exportadores).

### Fase 4: Testing y Documentación

- Ambos: Crear y ampliar coverage de tests, asegurar que la documentación refleje el estado real del proyecto.

---

## Flujo de Trabajo Recomendado

- Usar ramas para trabajar en nuevas funcionalidades o refactors.
- Hacer pull requests claros y con descripción del cambio.
- Revisar y aprobar PRs antes de hacer merge.
- Mantener comunicación y seguimiento de tareas en issues o proyectos de GitHub.
