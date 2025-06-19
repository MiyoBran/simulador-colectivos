# Flujo de trabajo con Git en Eclipse

## Al comenzar el día

1. **Cambiar a la rama principal (`main`):**  
   En la vista Git de Eclipse, haz clic derecho sobre el repositorio, luego:
   - Branches > Local > `main` > _Checkout_

2. **Actualizar la rama principal:**  
   - Haz clic derecho sobre el repositorio y selecciona _Pull_ para traer los últimos cambios de GitHub.

3. **Cambiar a tu rama de trabajo (`pc-escritorio`):**  
   - Branches > Local > `nombre-branch` > _Checkout_

4. **Actualizar tu rama de trabajo con los cambios de `main`:**  
   - Haz clic derecho sobre el repositorio, selecciona _Merge..._, elige `main` y confirma.

---

## Durante el día

- Trabaja normalmente en la rama `nombre-branch`.
- Realiza _commits_ frecuentemente para guardar tus avances.
- Haz _push_ regularmente para subir tus cambios a GitHub.

**Nota importante sobre el push:**  
Luego de seleccionar **Commit and Push** en la pestaña Git Staging, Eclipse puede mostrar una ventana de confirmación adicional para completar el push.  
_Asegúrate de hacer clic en “OK” o “Finish” en esa ventana para que los cambios se suban realmente a GitHub._  
Si no confirmas, el push no se realiza y los cambios quedan solo en local.

---

## Al terminar el día

1. **Asegúrate de hacer _push_ de tus últimos cambios en `pc-escritorio`.**

2. **Cambiar a la rama principal (`main`):**  
   - Branches > Local > `main` > _Checkout_

3. **Fusionar tu rama de trabajo a `main`:**  
   - Haz clic derecho sobre el repositorio, selecciona _Merge..._, elige `pc-escritorio` y confirma.

4. **Si es necesario, realiza un _commit_ del merge.**

5. **Haz _push_ en `main` para subir los cambios fusionados a GitHub.**

---

## Notas

- Si aparecen conflictos durante un merge, Eclipse te lo notificará y deberás resolverlos antes de continuar.
- Si trabajas en otra PC, repite el mismo ciclo.
- Si trabajas en equipo, este flujo ayuda a evitar conflictos y mantener todo sincronizado.

----
Te ayudo a crear el branch "notebook" directamente desde Eclipse. Esto es más seguro y consistente para compartir entre diferentes entornos de desarrollo.

## Crear branch desde Eclipse

### 1. Abrir la perspectiva Git (si no está visible)
- **Window → Perspective → Open Perspective → Git**
- O en la esquina superior derecha, click en el ícono de perspectiva y seleccionar **Git**

### 2. Crear el nuevo branch
1. **Click derecho en tu proyecto** en el Package Explorer
2. **Team → Switch To → New Branch...**
   
   O alternativamente:
   - Abre **Git Repositories** view
   - Expande tu repositorio
   - Click derecho en **Branches → Local**
   - **Create Branch...**

### 3. Configurar el nuevo branch
En el diálogo "Create Branch":
- **Branch name**: `notebook`
- **Source**: Asegúrate que sea `refs/heads/main` (o el branch principal)
- **☑️ Checkout new branch** (debe estar marcado)
- **☑️ Configure upstream for push and pull** (muy importante)
- **When pulling**: Selecciona **Merge** (recomendado para equipos)

### 4. Confirmar la creación
- Click **Finish**
- Eclipse automáticamente:
  - Crea el branch local
  - Te cambia al nuevo branch
  - Configura el tracking con el remoto

### 5. Verificar la configuración
En **Git Repositories** view deberías ver:
- **Branches → Local → notebook** (con ícono activo)
- El nombre del proyecto ahora muestra `[notebook]`
- En la barra de estado inferior aparece `notebook`

### 6. Push inicial al remoto
1. **Click derecho en el proyecto**
2. **Team → Push Branch 'notebook'...**
3. En el diálogo, confirma:
   - **Source ref**: `refs/heads/notebook`
   - **Destination**: `origin`
   - **☑️ Configure upstream for push and pull**
4. **Next → Finish**

## Configuración adicional recomendada

### Configurar comportamiento de Pull
1. **Window → Preferences**
2. **Team → Git → Configuration**
3. **Add Entry...**
   - **Key**: `pull.rebase`
   - **Value**: `false`
4. **Apply and Close**

### Verificar configuración del branch
En **Git Repositories → Branches → Local → notebook**:
- Click derecho → **Configure Branch...**
- Verifica que **Remote** sea `origin`
- **Upstream branch** debe ser `refs/heads/notebook`

## Ventajas de este método

✅ **Consistente**: Mismo proceso en cualquier Eclipse  
✅ **Configuración automática**: Eclipse maneja el tracking remoto  
✅ **Visual**: Puedes ver el estado del branch en la interfaz  
✅ **Integrado**: No necesitas cambiar entre terminal y IDE  

### Para sincronizar con main en el futuro:
1. **Team → Switch To → main**
2. **Team → Pull**
3. **Team → Switch To → notebook**
4. **Team → Merge... → Local → main**

¿Quieres que te ayude con algún paso específico o necesitas configurar algo más?