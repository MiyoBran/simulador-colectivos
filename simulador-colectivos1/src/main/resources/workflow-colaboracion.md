# Flujo de Trabajo Colaborativo y Uso de Copilot

## Objetivo

Definir el flujo de trabajo recomendado para la colaboración en el proyecto, el manejo de ramas y el uso eficiente de GitHub Copilot y Pull Requests.

---

## 0. Requisitos de Entorno

- **Java 21 es obligatorio** para todo el desarrollo, tanto en Codespaces como en entornos personales.
- En el workspace colaborativo (Codespace), JAVA_HOME y PATH se configuran automáticamente para usar Java 21 en cada terminal.
- Cada colaborador debe asegurarse de tener Java 21 instalado y configurado en su entorno local para evitar incompatibilidades.

---

## 1. Ramas Principales

- **main**: Rama principal y estable del proyecto. Solo se fusionan (merge) Pull Requests revisados.
- **miyo-pc-escritorio**: Rama personal de trabajo local de MiyoBran.
- **miyo-workspace**: Rama de trabajo para experimentos, refactorizaciones y tareas compartidas entre MiyoBran y Enzo.
- **(colaborador)-workspace**: Cada colaborador puede crear su propia rama de workspace para tareas grandes o experimentales.

---

## 2. Flujo de Trabajo Sugerido

1. **Antes de empezar:**
    - Haz `git pull` en tu rama de trabajo para asegurar que está actualizada con main (o la base que corresponda).
    - Si vas a empezar una tarea nueva, crea una rama a partir de main o tu workspace:
      ```
      git checkout main
      git pull
      git checkout -b nombre-tarea
      ```

2. **Durante el trabajo:**
    - Realiza cambios y haz commits frecuentes y descriptivos.
    - Usa Copilot para sugerencias de código, refactors y generación de tests.
    - Si tienes dudas, documenta en comentarios o issues.

3. **Sincronización:**
    - Antes de hacer push, asegúrate de hacer `git pull origin main` (o la base de tu rama) para evitar conflictos.
    - Resuelve conflictos localmente si aparecen.

4. **Pull Requests (PR):**
    - Cuando termines una tarea, haz push de tu rama y crea un PR hacia main (o hacia la rama de workspace si es algo experimental).
    - Describe claramente el objetivo del PR y solicita revisión.
    - Otro colaborador revisará el código antes de hacer merge.

5. **Merge y limpieza:**
    - Una vez aprobado el PR, se hace merge a main o al workspace correspondiente.
    - Elimina ramas que ya no se usen para mantener el repo limpio.

---

## 3. Uso de Codespaces y Copilot

- Puedes crear un Codespace desde GitHub para trabajar en la nube.
- Copilot está disponible tanto en Codespaces como en VS Code local.
- Aprovecha Copilot Chat para pedir explicaciones, refactors, o generar documentación/tests.
- Sube tus cambios al repo al finalizar la sesión de Codespace.

---

## 4. Buenas Prácticas

- No trabajar directamente en main.
- Prefiere PRs pequeños y frecuentes.
- Documenta tus cambios y dudas en issues o comentarios de PR.
- Sigue las convenciones y guías del proyecto (`conventions-proyecto.md`, `instructions-proyecto.md`).

---

## 5. Primeros pasos para nuevos colaboradores

1. Lee los archivos:
   - `roadmap-proyecto.md`
   - `prompt-proyecto.md`
   - `conventions-proyecto.md`
   - `instructions-proyecto.md`
   - `workflow-copilot.txt` (este mismo)

2. Clona el repo y crea tu rama de trabajo.
3. Trabaja, haz commits y solicita PRs según el flujo arriba.
4. Si tienes dudas, consulta con el equipo o crea un issue.

---

**¡Bienvenido/a al equipo y a la colaboración profesional!**
