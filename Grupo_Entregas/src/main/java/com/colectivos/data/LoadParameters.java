package com.colectivos.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for loading configuration parameters from a {@code config.properties} file.
 * This class reads file paths for bus lines and stops from the properties file using
 * keys {@code linea} and {@code parada}. The file must be available on the classpath.
 * 
 * Call {@link #initialize()} once at the start of the application to load these values.
 */
public class LoadParameters {

	private static String lineFile;
	private static String stopFile;

	/**
	 * Loads the line and stop file paths from a {@code config.properties} file located in the classpath.
	 * Properties {@code linea} and {@code parada} must be defined in the file. If the file is missing,
	 * or the properties are not present, an {@link IOException} is thrown.
	 *
	 * @throws IOException if the configuration file is missing or incomplete
	*/
	public static void initialize() throws IOException {
		Properties prop = new Properties();
		
		try (InputStream input = LoadParameters.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new IOException("Unable to find config.properties in classpath.");
			}
			prop.load(input);
			
			lineFile = prop.getProperty("linea");		
			stopFile = prop.getProperty("parada");
			if (lineFile == null || stopFile == null) {
				throw new IOException("Missing 'linea' or 'parada' in config.properties.");
			}
		}
	}

	/**
	 * Returns the file path for the line data, loaded from the {@code config.properties} file.
	 * @return the path to the line data file
	 * @throws IllegalStateException if {@link #initialize()} has not been called
	 */
	public static String getLineFile() {
		if (lineFile == null) {
			throw new IllegalStateException("Files must be initialized first.");
		}
		return lineFile;
	}

	/**
	 * Returns the file path for the stop data, loaded from the {@code config.properties} file.
	 * @return the path to the stop data file
	 * @throws IllegalStateException if {@link #initialize()} has not been called
	 */
	public static String getStopFile() {
		if (stopFile == null) {
			throw new IllegalStateException("Files must be initialized first.");
		}
		return stopFile;
	}
	
}
