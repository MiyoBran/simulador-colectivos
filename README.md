# Simulador de Colectivos Urbanos

**Proyecto final para la materia Algorítmica y Programación II** **Licenciatura en Informática - UNPSJB**

El sistema simula el funcionamiento de líneas de colectivos urbanos, permitiendo analizar su eficiencia a través de un completo módulo de estadísticas y planificación de rutas.

El proyecto se encuentra funcionalmente completo, habiendo cumplido todos los objetivos de los Incrementos 1 y 2, y ha sido validado por una suite de más de 100 pruebas unitarias que aseguran su robustez y correcto funcionamiento.

---

## Características Principales

- **Simulación Basada en Pasos**: El motor de la simulación procesa ciclo a ciclo el movimiento de múltiples colectivos, así como la subida y bajada de pasajeros en cada parada.
- **Gestión de Capacidad**: Los colectivos respetan una capacidad máxima (diferenciada entre pasajeros sentados y parados), afectando las estadísticas de satisfacción si se llenan.
- **Cálculo de Rutas Óptimas**: Utiliza un grafo dirigido para modelar la red de transporte y el algoritmo de Dijkstra para encontrar el camino más corto entre dos paradas.
- **Módulo de Estadísticas Detalladas**:
  - **Índice de Satisfacción del Pasajero**: Calcula la satisfacción de los pasajeros basándose en si consiguieron asiento o si tuvieron que esperar a más de un colectivo.
  - **Ocupación Promedio de Colectivos**: Reporta el porcentaje de ocupación promedio para cada colectivo a lo largo de sus recorridos.
  - **Desglose de Viajes**: Informa cuántos pasajeros llegaron a destino, cuántos fueron bajados forzosamente y cuántos nunca pudieron subir.
- **Interfaz de Usuario por Consola**: Un menú interactivo permite controlar la simulación, solicitar reportes y calcular rutas.
- **Arquitectura por Capas**: Separación estricta entre **modelo**, **datos**, **lógica** e **interfaz**, siguiendo el patrón Controlador-UI para mayor mantenibilidad.

---

## Tecnologías Utilizadas

- **Lenguaje:** Java 21
- **Gestor de Dependencias y Construcción:** Apache Maven
- **Pruebas Unitarias:** JUnit 5
- **Framework de Mocks:** Mockito
- **Estructuras de Datos:** Java Collections Framework y librería `net.datastructures` (requiere instalación local).

---

## Instalación y Ejecución

El proyecto tiene una dependencia local que debe ser instalada primero. Sigue estos pasos cuidadosamente.

### 1. Requisitos Previos

- Java Development Kit (JDK) versión 21 o superior.
- Apache Maven 3.x o superior.
- Git para clonar el repositorio.

### 2. Clonar el Repositorio

```bash
git clone [https://github.com/MiyoBran/simulador-colectivos.git](https://github.com/MiyoBran/simulador-colectivos.git)
cd simulador-colectivos
```
---
3. Instalar la Librería Local (datastructures-library)

El proyecto depende de una librería de estructuras de datos que no está en los repositorios públicos. Primero debes instalarla en tu repositorio local de Maven:

```bash
cd datastructures-library
mvn clean install
```

Si este comando finaliza con BUILD SUCCESS, puedes continuar.
---
4. Construir el Proyecto Principal
Ahora, construye el proyecto principal del simulador. Maven podrá encontrar la dependencia que acabas de instalar.
```bash
# Vuelve a la carpeta raíz del proyecto y entra en la del simulador
cd ../simulador-colectivos1
mvn clean package
```
¡Listo! El proyecto está configurado y listo para ejecutarse.
---

4. **Configuración Recomendada para Eclipse*
Si utilizas Eclipse, activa las siguientes opciones para que Maven gestione las dependencias automáticamente:
Ve a Window -> Preferences
Navega a Maven
Asegúrate de marcar:
Download Artifact Sources
Download Artifact Javadoc
Download repository index updates on startup

Haz clic en Apply and Close

---
Cómo Ejecutar la Aplicación

Puedes ejecutar la simulación de dos maneras:

1. Desde tu IDE (Eclipse, IntelliJ, etc.)
Importa el proyecto simulador-colectivos1 como un proyecto Maven existente.
Localiza la clase ar.edu.unpsjb.ayp2.proyectointegrador.interfaz.SimuladorColectivosApp.java.
Ejecútala como una aplicación Java.
2. Desde la Terminal
Después de haber construido el proyecto con mvn clean package, ejecuta el siguiente comando desde la carpeta simulador-colectivos1:

```bash

java -jar target/simulador-colectivos1-0.0.1-SNAPSHOT.jar

```
---

#**Documentación Detallada del Proyecto*
Para un entendimiento profundo del diseño y la arquitectura, consulta los siguientes documentos en la carpeta simulador-colectivos1/src/main/resources/:

prompt-proyecto.md: El resumen general del proyecto, sus objetivos y estado final.
roadmap-proyecto.md: El plan de desarrollo detallado y el log de los incrementos.
conventions-proyecto.md: Las convenciones de código y estilo seguidas.
Laboratorio-requisitos-y-logica-simulacion2025.txt: El detalle de los requisitos funcionales y la lógica de la simulación.