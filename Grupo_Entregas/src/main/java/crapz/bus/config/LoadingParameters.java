package crapz.bus.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadingParameters {

	private static String linesFile;
	private static String stopsFile;
	private static int passengerCount;

	/**
	 * Loads configuration parameters from a config.properties file.
	 *
	 * @param configFile The path to the configuration file.
	 * @throws IOException           If an error occurs while reading the file.
	 * @throws NumberFormatException If 'passengerCount' is not a valid number.
	 */
	private static void loadParameters() throws IOException, NumberFormatException {
		Properties prop = new Properties();
		InputStream input = LoadingParameters.class.getClassLoader().getResourceAsStream("config.properties");
		if (input == null) {
			throw new IOException("Unable to find config.properties in classpath.");
		}

		prop.load(input);
		linesFile = prop.getProperty("linea");
		stopsFile = prop.getProperty("parada");
		passengerCount = Integer.parseInt(prop.getProperty("cantidadPasajeros"));
	}

	// Getters to access the loaded values
	public static String getLinesFile() {
		return linesFile;
	}

	public static String getStopsFile() {
		return stopsFile;
	}

	public static int getPassengerCount() {
		return passengerCount;
	}

	public static void parameters() throws IOException, NumberFormatException {
		loadParameters();
	}
}