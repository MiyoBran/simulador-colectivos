package subte.datos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import net.datastructures.TreeMap;
import subte.modelo.Estacion;
import subte.modelo.Linea;
import subte.modelo.Tramo;

public class Dato {

	public static TreeMap<String, Linea> cargarLineas(String fileName) throws FileNotFoundException {
		Scanner read;

		TreeMap<String, Linea> lineas = new TreeMap<String, Linea>();
		
			read = new Scanner(new File(fileName));
			read.useDelimiter("\\s*;\\s*");
			String codigo, nombre;

			while (read.hasNext()) {
				codigo = read.next();
				nombre = read.next();
				lineas.put(codigo, new Linea(codigo, nombre));
			}
			read.close();
		
		return lineas;
	}

	public static TreeMap<String, Estacion> cargarEstaciones(String fileName,
			TreeMap<String, Linea> lineas) throws FileNotFoundException {
		Scanner read;
		TreeMap<String, Estacion> estaciones = new TreeMap<String, Estacion>();
		
			read = new Scanner(new File(fileName));
			read.useDelimiter("\\s*;\\s*");
			String codigo, nombre, linea;
			while (read.hasNext()) {				
				codigo = read.next();				
				nombre = read.next();
				linea = read.next();
				estaciones.put(codigo,
						new Estacion(codigo, nombre, lineas.get(linea)));
			}
			read.close();
		
		return estaciones;
	}

	public static List<Tramo> cargarTramos(String fileName,
			TreeMap<String, Estacion> estaciones) throws FileNotFoundException {
		Scanner read;
		List<Tramo> tramos = new ArrayList<Tramo>();
		
			read = new Scanner(new File(fileName));
			read.useDelimiter("\\s*;\\s*");
			Estacion v1, v2;
			int tiempo, tipo;
			while (read.hasNext()) {
				v1 = estaciones.get(read.next());
				v2 = estaciones.get(read.next());
				tiempo = read.nextInt();
				tipo = read.nextInt();
				tramos.add(0, new Tramo(v1, v2, tiempo, tipo));
			}
			read.close();
		
		return tramos;
	}

}
