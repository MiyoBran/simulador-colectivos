# Proyectos del Simulador de Colectivos Urbanos

Este archivo contiene dos proyectos Maven de código fuente:
1.  `datastructures-library`: La librería de estructuras de datos utilizada por el proyecto principal.
2.  `simulador-colectivos`: El proyecto principal del simulador.

## Requisitos del Sistema

* **Java Development Kit (JDK):** Versión 21 o superior.
* **Apache Maven:** Versión 3.x o superior.
* **IDE (Recomendado):** Eclipse (versión reciente).

## Configuración y Ejecución de los Proyectos

Siga estos pasos para configurar y ejecutar la simulación. **Es crucial seguir el orden:**

### Paso 1: Instalar la Librería `datastructures-library`

Esta librería **no se encuentra en repositorios Maven públicos**. Debe ser compilada e instalada localmente en su repositorio Maven para que el proyecto principal pueda encontrarla.

1.  Abra una **terminal** (o el terminal integrado de su IDE, como Eclipse o VS Code).
2.  Navegue hasta la carpeta raíz del proyecto `datastructures-library` (la que acaba de descomprimir, donde se encuentra su `pom.xml`).
    * Ejemplo: `cd /ruta/donde/descomprimio/datastructures-library`
3.  Ejecute el siguiente comando Maven para compilar e instalar la librería:
    ```bash
    mvn clean install
    ```
    * Esto instalará el artefacto `net.datastructures:datastructures-library:6.0.0-custom` en su repositorio Maven local (`~/.m2/repository/`).

### Paso 2: Importar el Proyecto `simulador-colectivos` en Eclipse

Una vez instalada la librería, puede importar el proyecto principal en su IDE.

1.  Abra Eclipse IDE.
2.  Vaya a `File > Import...`.
3.  Seleccione `Maven > Existing Maven Projects`. Clic en `Next`.
4.  En "Browse...", navegue hasta la carpeta raíz de su proyecto `simulador-colectivos` (la que acaba de descomprimir, donde se encuentra su `pom.xml`).
5.  Asegúrese de que el `pom.xml` del proyecto sea detectado y marcado. Clic en `Finish`.

### Paso 3: Actualizar el Proyecto Maven `simulador-colectivos` en Eclipse

Después de importar, es crucial forzar a Maven a resolver las dependencias, incluida la que acaba de instalar localmente.

1.  En el `Package Explorer` de Eclipse, haga clic derecho sobre el proyecto `simulador-colectivos`.
2.  Seleccione `Maven > Update Project...`.
3.  Asegúrese de que `simulador-colectivos` esté marcado y **active la casilla "Force Update of Snapshots/Releases"**.
4.  Clic en `OK`.
    * Este paso es vital para que Maven reconozca la `datastructures-library` instalada localmente.

### Paso 4: Ejecutar las Pruebas Unitarias (Verificación)

Para verificar que el proyecto fue configurado correctamente y todas las dependencias están resueltas:

1.  En Eclipse, navegue a `src/test/java/ar.edu.unpsjb.ayp2.proyectointegrador.datos/LectorArchivosTest.java`.
2.  Haga clic derecho sobre el archivo en el editor o en el `Package Explorer`.
3.  Seleccione `Run As > JUnit Test`.
    * Debería ver una barra **verde** en la vista JUnit, indicando que todas las pruebas pasaron. Esto confirma que el proyecto está correctamente configurado.

### Paso 5: Ejecutar la Aplicación Principal

1.  En Eclipse, navegue a `src/main/java/ar.edu.unpsjb.ayp2.proyectointegrador.interfaz/SimuladorColectivosApp.java`.
2.  Haga clic derecho sobre el archivo.
3.  Seleccione `Run As > Java Application`.
    * La simulación se ejecutará y mostrará la salida en la consola.

---

Este plan detallado guiará a cualquier persona a configurar y ejecutar tu proyecto sin problemas, manejando la dependencia local de `datastructures-library` de manera efectiva.