package cole.datos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CargarParametros {

	private static String archivoLinea;
	private static String archivoParadas;
	private static int cantPasajeros;

	public static void parametros() throws IOException {
		
		Properties prop = new Properties();		
			InputStream input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			// get the property value
			archivoLinea = prop.getProperty("linea");		
			archivoParadas = prop.getProperty("parada");
			String valorStr = prop.getProperty("cantidadPasajeros");
			//cantPasajeros  = prop.getProperty("cantidadPasajeros");
            cantPasajeros = Integer.parseInt(valorStr);
			//cantPasajeros = prop.getProperty("pasajeros");
           
	}

	public static String getArchivoLinea() {
		return archivoLinea;
	}

	public static String getArchivoParadas() {
		return archivoParadas;
	}

	public static int getCantPasajeros() {
		return cantPasajeros;
	}

	
}
