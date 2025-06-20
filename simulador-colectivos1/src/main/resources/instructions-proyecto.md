# Instrucciones para el Desarrollo y Colaboración

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
