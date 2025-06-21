# Guía de Colaboración para el Simulador de Colectivos Urbanos

## 1. Requisitos previos
- Java 21 instalado y configurado (obligatorio).
- Apache Maven 3.x o superior.
- IDE recomendado: Eclipse o VS Code (con extensiones de Java y Maven).

## 2. Primeros pasos
1. **Clona el repositorio:**
   ```sh
   git clone git@github.com:MiyoBran/simulador-colectivos.git
   ```
2. **Instala la librería de estructuras de datos:**
   ```sh
   cd simulador-colectivos/datastructures-library
   mvn clean install
   ```
3. **Importa ambos proyectos en tu IDE.**
4. **Crea tu rama de trabajo:**
   ```sh
   git checkout -b trabajo-tu-nombre
   git push -u origin trabajo-tu-nombre
   ```
5. **Ejecuta los tests:**
   ```sh
   cd simulador-colectivos/simulador-colectivos1
   mvn test
   ```
   Todos los tests deben pasar antes de proponer cambios.
6. **Ejecuta la aplicación:**
   - Desde el IDE: Ejecuta `SimuladorColectivosApp.java` como aplicación Java.
   - Desde terminal:
     ```sh
     mvn compile exec:java -Dexec.mainClass="ar.edu.unpsjb.ayp2.proyectointegrador.interfaz.SimuladorColectivosApp"
     ```

## 3. Buenas prácticas de colaboración
- Trabaja siempre en una rama propia.
- Haz commits y PRs descriptivos y atómicos.
- Consulta y actualiza la documentación (`instructions-proyecto.md`, `roadmap-proyecto.md`, `prompt-proyecto.md`).
- Usa issues para dudas, sugerencias o reportes.
- Lee y sigue las convenciones en `conventions-proyecto.md`.
- No dejes código muerto ni comentarios innecesarios.
- Asegúrate de que todas las clases y métodos públicos tengan JavaDoc.
- Solo debe haber `System.out.println` en la interfaz o tests.
- Documenta cada avance relevante en los archivos de instrucciones y en los issues/PRs.

## 4. Flujo de trabajo recomendado
1. Crea una rama para cada nueva funcionalidad, refactor o fix.
2. Realiza tus cambios y asegúrate de que los tests pasen.
3. Haz commit y push de tus cambios a tu rama.
4. Abre un Pull Request (PR) hacia `main` o la rama de integración.
5. Solicita revisión y responde a los comentarios.
6. Solo haz merge cuando el PR esté aprobado y los tests pasen.

## 5. Recursos útiles
- `README.md`: primeros pasos y estado general.
- `instructions-proyecto.md`: checklist y avances de la fase actual.
- `roadmap-proyecto.md`: planificación y arquitectura general.
- `prompt-proyecto.md`: contexto y objetivos del proyecto.
- `conventions-proyecto.md`: convenciones de código y estilo.

---

**¡Gracias por colaborar! Tu aporte suma a la calidad y crecimiento del proyecto.**
