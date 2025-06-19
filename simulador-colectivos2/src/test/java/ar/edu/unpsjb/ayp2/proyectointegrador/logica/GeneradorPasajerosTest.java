package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada;
import ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Tests para la clase GeneradorPasajeros.
 * * Esta clase verifica el correcto funcionamiento del generador de pasajeros,
 * * asegurando que se generen pasajeros con orígenes y destinos válidos,
 * * y que se manejen correctamente las líneas y paradas. 
 */
class GeneradorPasajerosTest {

    private Map<String, Linea> lineasDePrueba;
    private Map<String, Parada> paradasDePrueba;
    private Properties configDePrueba;

    private Parada p1, p2, p3, p4, p5, p6, p7, p8, p9, p10; // Más paradas para líneas largas
    private Linea lCorta, lMediana, lLarga;

    @BeforeEach
    void setUp() {
        // Inicializar paradas de prueba
        p1 = new Parada("P1", "Calle A 100", 0.0, 0.0);
        p2 = new Parada("P2", "Calle B 200", 0.0, 0.0);
        p3 = new Parada("P3", "Calle C 300", 0.0, 0.0);
        p4 = new Parada("P4", "Calle D 400", 0.0, 0.0);
        p5 = new Parada("P5", "Calle E 500", 0.0, 0.0);
        p6 = new Parada("P6", "Calle F 600", 0.0, 0.0);
        p7 = new Parada("P7", "Calle G 700", 0.0, 0.0);
        p8 = new Parada("P8", "Calle H 800", 0.0, 0.0);
        p9 = new Parada("P9", "Calle I 900", 0.0, 0.0);
        p10 = new Parada("P10", "Calle J 1000", 0.0, 0.0);

        paradasDePrueba = new HashMap<>();
        paradasDePrueba.put("P1", p1);
        paradasDePrueba.put("P2", p2);
        paradasDePrueba.put("P3", p3);
        paradasDePrueba.put("P4", p4);
        paradasDePrueba.put("P5", p5);
        paradasDePrueba.put("P6", p6);
        paradasDePrueba.put("P7", p7);
        paradasDePrueba.put("P8", p8);
        paradasDePrueba.put("P9", p9);
        paradasDePrueba.put("P10", p10);

        // Limpiar colas de pasajeros de las paradas antes de cada test
        for (Parada parada : paradasDePrueba.values()) {
            while (parada.hayPasajerosEsperando()) {
                parada.removerSiguientePasajero();
            }
        }

        // Inicializar líneas de prueba
        lCorta = new Linea("LC", "Linea Corta");
        lCorta.agregarParadaAlRecorrido(p1);
        lCorta.agregarParadaAlRecorrido(p2); // Solo 2 paradas

        lMediana = new Linea("LM", "Linea Mediana");
        lMediana.agregarParadaAlRecorrido(p3);
        lMediana.agregarParadaAlRecorrido(p4);
        lMediana.agregarParadaAlRecorrido(p5); // 3 paradas

        lLarga = new Linea("LL", "Linea Larga");
        lLarga.agregarParadaAlRecorrido(p6);
        lLarga.agregarParadaAlRecorrido(p7);
        lLarga.agregarParadaAlRecorrido(p8);
        lLarga.agregarParadaAlRecorrido(p9);
        lLarga.agregarParadaAlRecorrido(p10); // 5 paradas

        lineasDePrueba = new HashMap<>();
        lineasDePrueba.put("LC - Linea Corta", lCorta);
        lineasDePrueba.put("LM - Linea Mediana", lMediana);
        lineasDePrueba.put("LL - Linea Larga", lLarga);
        
        configDePrueba = new Properties();
    }

    // --- Tests para el Constructor ---
    @Test
    @DisplayName("Constructor: Debe inicializar correctamente con propiedades válidas")
    void testConstructorExitoso() {
        configDePrueba.setProperty("cantidadPasajeros", "5");
        assertDoesNotThrow(() -> new GeneradorPasajeros(lineasDePrueba, paradasDePrueba, configDePrueba));
    }

    @Test
	@DisplayName("Constructor: Debe lanzar excepción si las líneas están nulas o vacías")
    void testConstructorLineasNulas() {
        configDePrueba.setProperty("cantidadPasajeros", "5");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new GeneradorPasajeros(null, paradasDePrueba, configDePrueba);
        });
        assertEquals("Las líneas disponibles no pueden ser nulas o vacías.", thrown.getMessage());
    }

    @Test
    @DisplayName("Constructor: Debe lanzar excepción si las líneas están vacías")
    void testConstructorLineasVacias() {
        configDePrueba.setProperty("cantidadPasajeros", "5");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new GeneradorPasajeros(new HashMap<>(), paradasDePrueba, configDePrueba);
        });
        assertEquals("Las líneas disponibles no pueden ser nulas o vacías.", thrown.getMessage());
    }

    @Test
	@DisplayName("Constructor: Debe lanzar excepción si las paradas están nulas")
    void testConstructorParadasNulas() {
        configDePrueba.setProperty("cantidadPasajeros", "5");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new GeneradorPasajeros(lineasDePrueba, null, configDePrueba);
        });
        assertEquals("Las paradas disponibles no pueden ser nulas o vacías.", thrown.getMessage());
    }

    @Test
    @DisplayName("Constructor: Debe lanzar excepción si las paradas están vacías")
    void testConstructorParadasVacias() {
        configDePrueba.setProperty("cantidadPasajeros", "5");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new GeneradorPasajeros(lineasDePrueba, new HashMap<>(), configDePrueba);
        });
        assertEquals("Las paradas disponibles no pueden ser nulas o vacías.", thrown.getMessage());
    }

    @Test
    @DisplayName("Constructor: Debe lanzar excepción si las propiedades de configuración son nulas")
    void testConstructorConfigNulas() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new GeneradorPasajeros(lineasDePrueba, paradasDePrueba, null);
        });
        assertEquals("Las propiedades de configuración no pueden ser nulas.", thrown.getMessage());
    }

    @Test
    void testConstructorCantidadPasajerosNoDefinida() {
        // No se setea "cantidadPasajeros" en configDePrueba
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new GeneradorPasajeros(lineasDePrueba, paradasDePrueba, configDePrueba);
        });
        assertEquals("La propiedad 'cantidadPasajeros' no está definida o está vacía en config.properties.", thrown.getMessage());
    }

    @Test
    @DisplayName("Constructor: Debe lanzar excepción si la cantidad de pasajeros es cero o negativa")
    void testConstructorCantidadPasajerosNoValida() {
        configDePrueba.setProperty("cantidadPasajeros", "-10");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new GeneradorPasajeros(lineasDePrueba, paradasDePrueba, configDePrueba);
        });
        assertEquals("La cantidad de pasajeros a generar debe ser un número positivo.", thrown.getMessage());

        configDePrueba.setProperty("cantidadPasajeros", "cero");
        thrown = assertThrows(IllegalArgumentException.class, () -> {
            new GeneradorPasajeros(lineasDePrueba, paradasDePrueba, configDePrueba);
        });
        assertTrue(thrown.getMessage().contains("no es un número válido."));
    }

    // --- Tests para generarPasajeros ---
    @Test
    @DisplayName("Generación: debería generar una lista de pasajeros no nula y con la cantidad correcta")
    void testGenerarPasajerosCantidadCorrecta() {
        int cantidadEsperada = 10;
        configDePrueba.setProperty("cantidadPasajeros", String.valueOf(cantidadEsperada));
        GeneradorPasajeros generador = new GeneradorPasajeros(lineasDePrueba, paradasDePrueba, configDePrueba);
        
        List<Pasajero> pasajerosGenerados = generador.generarPasajeros();
        
        assertNotNull(pasajerosGenerados);
        assertEquals(cantidadEsperada, pasajerosGenerados.size(), "Debería generar la cantidad correcta de pasajeros.");
    }


    @Test
    @DisplayName("Generación: debería funcionar correctamente solo con líneas cortas (2 paradas)")
    void testGenerarPasajerosConLineasDemasiadoCortas() {
        // Arrange
        Map<String, Linea> lineasSoloDosParadas = new HashMap<>();
        lineasSoloDosParadas.put("LC - Linea Corta", lCorta); // lCorta tiene solo 2 paradas

        configDePrueba.setProperty("cantidadPasajeros", "10");
        GeneradorPasajeros generador = new GeneradorPasajeros(lineasSoloDosParadas, paradasDePrueba, configDePrueba);

        // Act
        List<Pasajero> pasajerosGenerados = generador.generarPasajeros();

        // Assert
        assertEquals(10, pasajerosGenerados.size(), "Deberían generarse 10 pasajeros para la línea de 2 paradas.");
    }
    
    @Test
    @DisplayName("Generación: Origen y Destino deben ser válidos y distintos en la misma línea")
    void testGenerarPasajerosOrigenYDestinoValidosYDiferentes() {
        // Arrange: Preparamos la configuración para generar 50 pasajeros.
        int cantidad = 50;
        configDePrueba.setProperty("cantidadPasajeros", String.valueOf(cantidad));
        GeneradorPasajeros generador = new GeneradorPasajeros(lineasDePrueba, paradasDePrueba, configDePrueba);

        // Act: Ejecutamos la generación.
        List<Pasajero> pasajerosGenerados = generador.generarPasajeros();

        // Assert: Verificamos los resultados.
        assertEquals(cantidad, pasajerosGenerados.size(), "Debería generar la cantidad esperada de pasajeros.");

        for (Pasajero p : pasajerosGenerados) {
            // Verificaciones básicas para cada pasajero.
            assertNotNull(p.getParadaOrigen(), "La parada de origen no debería ser nula.");
            assertNotNull(p.getParadaDestino(), "La parada de destino no debería ser nula.");
            assertNotEquals(p.getParadaOrigen(), p.getParadaDestino(), "La parada de origen y destino no pueden ser la misma.");

            // Verificamos que el pasajero pertenece a una línea válida y que el viaje tiene sentido.
            boolean foundLine = false;
            for (Linea l : lineasDePrueba.values()) {
                int indexOrigen = l.getIndiceParada(p.getParadaOrigen());
                int indexDestino = l.getIndiceParada(p.getParadaDestino());
                // Si ambas paradas existen en esta línea, verificamos la lógica del viaje.
                if (indexOrigen != -1 && indexDestino != -1) {
                    
                    // --- ASERCIÓN CLAVE ---
                    // Esta es la única regla que necesitamos: el destino debe estar después del origen.
                    // Esto implícitamente valida que el origen no puede ser la última parada.
                    assertTrue(indexDestino > indexOrigen, 
                               "La parada de destino (" + p.getParadaDestino().getId() + " - " + indexDestino +
                               ") debería estar después de la de origen (" + p.getParadaOrigen().getId() + " - " + indexOrigen +
                               ") en la línea " + l.getId() + ".");
                    foundLine = true;
                    break; // Encontramos la línea del pasajero, no necesitamos seguir buscando.
                }
            }
            assertTrue(foundLine, "Cada pasajero ("+ p.getId() +") debe estar asociado a paradas válidas dentro de una línea existente.");
        }
    }

    @Test
    @DisplayName("Generación: asigna correctamente cada pasajero a la cola de su parada de origen")
    void testGenerarPasajerosAsignacionCorrectaAColaDeParada() {
        // Arrange
        int cantidad = 5;
        configDePrueba.setProperty("cantidadPasajeros", String.valueOf(cantidad));
        GeneradorPasajeros generador = new GeneradorPasajeros(lineasDePrueba, paradasDePrueba, configDePrueba);
        
        // Act
        List<Pasajero> pasajerosGenerados = generador.generarPasajeros();
        
        // Assert
        // Verificación 1: El número total de pasajeros en todas las colas debe coincidir con los generados.
        int pasajerosEnColas = 0;
        for (Parada p : paradasDePrueba.values()) {
            pasajerosEnColas += p.cantidadPasajerosEsperando();
        }
        assertEquals(cantidad, pasajerosEnColas, "Todos los pasajeros generados deberían estar en la cola de alguna parada de origen.");

        // Verificación 2: Cada pasajero individual debe estar en la cola de SU parada de origen específica.
        for (Pasajero pasajero : pasajerosGenerados) {
            // Usamos el nuevo método que no rompe el encapsulamiento.
            assertTrue(pasajero.getParadaOrigen().tienePasajeroEnCola(pasajero),
                       "El pasajero " + pasajero.getId() + " debería estar en la cola de su parada de origen.");
        }
    }
}