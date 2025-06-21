# Convenciones de Codificación y Estilo

## Estilo General
- Usa indentación de 4 espacios (no tabs).
- Nombres de clases en UpperCamelCase (e.g., SimuladorColectivosApp).
- Nombres de métodos y variables en lowerCamelCase (e.g., inicializarColectivos).

## Idioma
- El código, los comentarios y los nombres deben estar en español claro y consistente.
- Los nombres de clases y métodos usan términos del dominio del problema.

## Organización del Código
- Cada clase en su propio archivo.
- Los archivos deben estar en el paquete correspondiente: modelo, datos, logica, interfaz, test.
- Métodos y atributos privados salvo que explícitamente se requiera acceso externo (usar getters/setters).

## JavaDoc y Comentarios
- Todas las clases públicas y métodos públicos deben tener JavaDoc explicativo.
- Comentar solo lo necesario para aclarar lógica compleja.

## Buenas Prácticas
- No usar System.out.println fuera de la capa de interfaz o tests.
- Validar parámetros en constructores y setters.
- Usar excepciones para manejo de errores en carga de datos.
- Mantener el código limpio: elimina código muerto o no usado.
- Prefiere métodos cortos y de única responsabilidad.

## Tests
- Cada clase importante debe tener su test correspondiente en el paquete test.
- Usa JUnit 5 y asegúrate que todos los tests pasen antes de hacer merge.

## Control de versiones
- Los mensajes de commit deben ser claros y descriptivos.
- Usa ramas para nuevas funcionalidades o refactors importantes.
