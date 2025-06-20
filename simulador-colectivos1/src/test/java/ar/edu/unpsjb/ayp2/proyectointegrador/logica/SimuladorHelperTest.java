package ar.edu.unpsjb.ayp2.proyectointegrador.logica;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Pruebas unitarias para SimuladorHelper.
 */
@DisplayName("Tests para SimuladorHelper")
class SimuladorHelperTest {

	private DummyColectivo colectivo;
	private DummyPasajero pasajero1;
	private DummyPasajero pasajero2;
	private DummyPasajero pasajero3;

	@BeforeEach
	void setUp() {
		colectivo = new DummyColectivo();
		pasajero1 = new DummyPasajero("P1");
		pasajero2 = new DummyPasajero("P2");
		pasajero3 = new DummyPasajero("P3");
	}

	@Test
	@DisplayName("forzarBajadaDePasajeros: debería bajar a todos los pasajeros y retornar la cantidad correcta")
	void testForzarBajadaDePasajeros() {
		colectivo.subirPasajero(pasajero1);
		colectivo.subirPasajero(pasajero2);
		colectivo.subirPasajero(pasajero3);
		assertEquals(3, colectivo.getCantidadPasajerosABordo(), "El colectivo debe tener 3 pasajeros antes de forzar la bajada.");

		int bajados = SimuladorHelper.forzarBajadaDePasajeros(colectivo);

		assertEquals(3, bajados, "Debe retornar la cantidad de pasajeros bajados.");
		assertEquals(0, colectivo.getCantidadPasajerosABordo(), "El colectivo debe quedar vacío después de forzar la bajada.");
	}

	@Test
	@DisplayName("forzarBajadaDePasajeros: colectivo vacío o null debe retornar 0")
	void testForzarBajadaDePasajerosVacioONull() {
		assertEquals(0, SimuladorHelper.forzarBajadaDePasajeros(colectivo), "Colectivo vacío debe retornar 0.");
		assertEquals(0, SimuladorHelper.forzarBajadaDePasajeros(null), "Colectivo null debe retornar 0.");
	}

	/**
	 * Dummy de Pasajero para pruebas unitarias.
	 */
	static class DummyPasajero extends ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero {
		private final String nombre;
		private static final ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada DUMMY_PARADA =
			new ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Parada("P0", "DummyParada");
		/**
		 * Crea un dummy de pasajero con nombre identificador.
		 * @param nombre Nombre identificador del pasajero.
		 */
		DummyPasajero(String nombre) {
			super(nombre, DUMMY_PARADA, DUMMY_PARADA); // id, paradaOrigen, paradaDestino
			this.nombre = nombre;
		}
		@Override public String toString() { return nombre; }
	}

	/**
	 * Dummy de Colectivo para pruebas unitarias.
	 * Incluye manejo de errores para evitar estados inconsistentes.
	 */
	static class DummyColectivo extends ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Colectivo {
		private final List<ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero> pasajeros = new ArrayList<>();
		public DummyColectivo() {
			super("dummy", new ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Linea("L0", "DummyLinea"), 50);
		}
		/**
		 * Devuelve una copia de la lista de pasajeros a bordo.
		 * @return Lista de pasajeros actualmente a bordo.
		 */
		@Override
		public List<ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero> getPasajerosABordo() {
			return new ArrayList<>(pasajeros);
		}
		/**
		 * Devuelve la cantidad de pasajeros a bordo.
		 * @return Cantidad de pasajeros.
		 */
		@Override
		public int getCantidadPasajerosABordo() {
			return pasajeros.size();
		}
		/**
		 * Baja un pasajero del colectivo.
		 * @param p Pasajero a bajar.
		 * @return true si el pasajero estaba a bordo y fue removido.
		 * @throws IllegalArgumentException si el pasajero es null.
		 */
		@Override
		public boolean bajarPasajero(ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero p) {
			if (p == null) {
				throw new IllegalArgumentException("No se puede bajar un pasajero null");
			}
			return pasajeros.remove(p);
		}
		/**
		 * Sube un pasajero al colectivo (solo para pruebas).
		 * @param p Pasajero a subir.
		 * @return true si el pasajero fue subido, false si ya estaba a bordo.
		 * @throws IllegalArgumentException si el pasajero es null.
		 */
		@Override
		public boolean subirPasajero(ar.edu.unpsjb.ayp2.proyectointegrador.modelo.Pasajero p) {
			if (p == null) {
				throw new IllegalArgumentException("No se puede subir un pasajero null");
			}
			if (pasajeros.contains(p)) {
				return false;
			}
			pasajeros.add(p);
			return true;
		}
	}
}