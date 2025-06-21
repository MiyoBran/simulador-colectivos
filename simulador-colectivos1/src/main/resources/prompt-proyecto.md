# Prompt del Proyecto: Simulador de Colectivos Urbanos

**Proyecto Integrador: Algorítmica y Programación II**  
Desarrollador : MiyoBran  
Desarrollador: Enzo  
Fecha de última actualización: 2025-06-20

---

## Estado actualizado (junio 2025)

- Refactor y limpieza completa de código, estructura y tests.
- Todos los tests pasan correctamente.
- Documentación y archivos de configuración revisados.
- Próximos pasos: nuevas funcionalidades y colaboración activa.

---

## 1. Contexto y Objetivo

Este proyecto simula el funcionamiento de líneas de colectivos urbanos en Java para la ciudad de Puerto Madryn. Se desarrolla en dos incrementos principales y sigue una arquitectura por capas estricta:

- **modelo**: entidades del dominio (Parada, Pasajero, Linea, Colectivo)
- **datos**: carga y gestión de datos (LectorArchivos, manejo de config.properties, etc)
- **logica**: simulación y procesamiento central (Simulador, GeneradorPasajeros, y para Inc.2: GestorEstadisticas, PlanificadorRutas, exportadores)
- **interfaz**: interacción con el usuario (SimuladorColectivosApp)
- **test**: pruebas automatizadas

Los objetivos principales:
- Simular el movimiento y la interacción de colectivos y pasajeros.
- Analizar la eficiencia, calcular rutas óptimas y proporcionar estadísticas relevantes.

---

## 2. Principios de Diseño y Referencias

- **Separación de lógica e interfaz**: la lógica (Simulador, etc.) es "silenciosa" y nunca imprime en consola, sólo devuelve datos/eventos. La interfaz (SimuladorColectivosApp) maneja toda la presentación.
- **Inspiración en el proyecto "subte" de la cátedra**, especialmente para el manejo de grafos y estructura de datos.
- **Uso de TADs híbridos**:  
  - Colecciones internas: Java Collections (`ArrayList`, `LinkedList`, etc.)
  - Colecciones globales de datos: Java `TreeMap` (Inc. 2)
  - Grafos y rutas: `net.datastructures.AdjacencyMapGraph`, `GraphAlgorithms` (Inc. 2)

---

## 3. Estado Actual

- **Incremento 1**: COMPLETADO
    - Refactoring finalizado. Arquitectura por capas respetada.
    - Pruebas unitarias implementadas.
    - Documentación y estructura de carpetas limpia.
    - Prompt y roadmap actualizados.
- **Incremento 2**: EN PROGRESO
    - Se inicia la refactorización previa y limpieza del código para facilitar la colaboración.
    - Enzo se suma al equipo y realizará onboarding y primeras tareas de comprensión/documentación.

---

## 4. Pasos Inmediatos para la Colaboración

### 4.1. Limpieza y Preparación

- Eliminar código y archivos obsoletos.
- Revisar nombres, JavaDoc, y estructura de paquetes.
- Asegurarse de que los tests existentes pasen (`mvn test`).

### 4.2. Documentación y Onboarding

- Actualizar README.md, roadmap-proyecto.md y prompt-proyecto.md con el estado real y guías para colaborar.
- Crear/actualizar:
  - `conventions-proyecto.md`: convenciones de codificación y estilo.
  - `instructions-proyecto.md`: instrucciones y tareas a seguir para onboarding y colaboración.
- Asegurarse de que los archivos de configuración y dependencias estén claros y actualizados.

### 4.3. Primeras tareas para Enzo

- Leer roadmap, prompt y convenciones.
- Ejecutar el proyecto y los tests localmente.
- Navegar el código, proponiendo dudas en issues/discusiones.
- Mejorar la documentación desde la perspectiva de un nuevo colaborador.

---

## 5. Plan de Desarrollo – Incremento 2

### 5.1. Objetivos

- Colectivos con múltiples recorridos y gestión de estado.
- Noción de tiempo en la simulación.
- Control estricto de capacidad (sentados/parados).
- Estadísticas: pasajeros, tiempos, ocupación, satisfacción.
- Red de transporte como grafo, cálculo de rutas óptimas.
- Refactorización para separar lógica de simulación y exportación/presentación.
- Tests y documentación ampliados.

### 5.2. Cambios técnicos

- **Nuevos parámetros en config.properties** para capacidades, frecuencias, recorridos, etc.
- **Refactor de Simulador**: separar generación de eventos/resultados y visualización/exportación.
- **Nuevos módulos/classes**:
  - `GestorEstadisticas` (métricas y reportes)
  - `PlanificadorRutas` (grafos y rutas óptimas)
  - `ExportadorResultadosSimulacion` (interfaz para mostrar/guardar resultados; implementaciones: consola, archivo, etc.)
- **Extensión de clases modelo** con atributos y métodos para estados, tiempos, satisfacción, etc.
- **Ampliación y refactor de tests**.

### 5.3. Fases tentativas

1. Limpieza y documentación
2. Onboarding colaborador
3. Refactorización de Simulador y separación de exportadores
4. Implementación de funcionalidades nuevas (estadísticas, rutas, etc.)
5. Testing y documentación final

---

## 6. Checklist previo a cada entrega

- [ ] Código limpio, comentado y sin impresiones indebidas
- [ ] Tests automáticos completos y exitosos
- [ ] Documentación y archivos de instrucciones claros
- [ ] README actualizado y útil para nuevos colaboradores
- [ ] Estructura de carpetas y dependencias respetadas
- [ ] Estado actual reflejado en roadmap y prompt

---

## 7. Mantenimiento y Mejora Continua

- Si se completa una etapa importante o se cambia el plan, actualizar este roadmap y el prompt.
- Usar issues y PRs para gestionar tareas y discutir cambios.
- Toda instrucción central para el desarrollo debe quedar documentada en los archivos de resources.

---

## 8. Mejoras Futuras (Post-Incremento 2)

- Gestión avanzada de horarios y demoras
- Estadísticas por chofer y colectivo
- Generación dinámica de pasajeros
- Cálculo de rutas avanzadas (distancia, tiempo real)
- Interfaz gráfica de usuario

---

> **Nota:**  
> Este archivo debe ser el punto de referencia principal para la planificación y organización del proyecto, tanto para el desarrollador original como para nuevos colaboradores.
