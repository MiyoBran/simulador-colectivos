package subte.datos;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CargarParametros {

	private static String archivoLinea;
	private static String archivoEstacion;
	private static String archivoTramo;

	public static void parametros() throws IOException {
		
		Properties prop = new Properties();		
			InputStream input = new FileInputStream("config.properties");
			// load a properties file
			prop.load(input);
			// get the property value
			archivoLinea = prop.getProperty("linea");		
			archivoEstacion = prop.getProperty("estacion");
			archivoTramo = prop.getProperty("tramo");
	}

	public static String getArchivoLinea() {
		return archivoLinea;
	}

	public static String getArchivoEstacion() {
		return archivoEstacion;
	}

	public static String getArchivoTramo() {
		return archivoTramo;
	}

	
}
