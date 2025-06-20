# Simulador de Colectivos Urbanos

Este repositorio contiene dos proyectos Maven:
1. **datastructures-library**: Librería de estructuras de datos utilizada por el simulador.
2. **simulador-colectivos**: Proyecto principal del simulador.

---

## 1. Clonar el Repositorio

Abre una terminal (Git Bash, CMD o PowerShell) y ejecuta:

```sh
git clone git@github.com:MiyoBran/simulador-colectivos.git
```

Esto descargará el repositorio en una nueva carpeta llamada `simulador-colectivos`.

---

## 2. Instalar la Librería `datastructures-library` en Maven Local

La librería **no** se encuentra en los repositorios públicos de Maven. Por eso, primero debes instalarla localmente:

1. Abre una terminal.
2. Navega a la carpeta `datastructures-library` dentro del repo clonado:

    ```sh
    cd simulador-colectivos/datastructures-library
    ```

3. Ejecuta el siguiente comando para compilar e instalar la librería:

    ```sh
    mvn clean install
    ```

Esto instalará el artefacto en tu repositorio Maven local (`~/.m2/repository/`).

---

## 3. Importar los Proyectos en Eclipse

1. Abre Eclipse.
2. Ve a `File > Import...`.
3. Selecciona `Maven > Existing Maven Projects` y haz clic en `Next`.
4. En "Root Directory", haz clic en `Browse...` y navega hasta la carpeta del repo clonado (`simulador-colectivos`).
5. Eclipse detectará ambos proyectos (`datastructures-library` y `simulador-colectivos`). Asegúrate de que ambos estén seleccionados y haz clic en `Finish`.

---

## 4. Actualizar Dependencias Maven

Para asegurarte de que Eclipse reconoce la librería instalada:

1. En el `Package Explorer`, haz clic derecho sobre el proyecto `simulador-colectivos`.
2. Selecciona `Maven > Update Project...`.
3. Marca `simulador-colectivos` y activa la casilla **"Force Update of Snapshots/Releases"**.
4. Haz clic en `OK`.

---

## 5. Crear una Rama para Trabajar

Por buenas prácticas, cada colaborador debe trabajar en su propia rama. Para crear una rama nueva desde Eclipse:

1. Abre la vista **Git** (Window > Show View > Other... > Git > Git Repositories).
2. Haz clic derecho sobre el repo `simulador-colectivos` y selecciona `Switch To > New Branch...`.
3. Nombra la rama, por ejemplo, `trabajo-tu-nombre` y confirma.
4. Trabaja normalmente en esta rama. Para subir tus cambios ejecuta:
    - `Add` y `Commit` desde el panel Git Staging.
    - Luego `Push` para subir la rama al remoto.

Si prefieres hacerlo desde consola:

```sh
git checkout -b trabajo-tu-nombre
git push -u origin trabajo-tu-nombre
```

---

## 6. (Opcional) Ejecutar Pruebas y la Aplicación

Para verificar que todo funciona:

- Ejecuta las pruebas unitarias (`src/test/java/.../LectorArchivosTest.java`) con `Run As > JUnit Test`.
- Ejecuta la app principal (`src/main/java/.../SimuladorColectivosApp.java`) con `Run As > Java Application`.

---

**¡Listo! Ya puedes trabajar colaborativamente sobre el repositorio.**