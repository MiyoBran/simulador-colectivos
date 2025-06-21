package cole.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import cole.datos.CargarParametros;
import cole.datos.Datos;
import cole.modelo.Linea;
import cole.modelo.Parada;


public class TestDatos {
    public static void main(String[] args) throws IOException {
    	
    	
    	CargarParametros.parametros();
    	
    	ArrayList<Parada> lista = Datos.cargarParadas(CargarParametros.getArchivoParadas());
    	
    	TreeMap<String, Linea> lineas = null;
    	
    	lineas = Datos.cargarLineas(CargarParametros.getArchivoLinea(), lista);
    		
    	for(Linea l: lineas.values()) {
    		
    		System.out.println(l);
    	}
    	
    	System.out.println(lineas.size());
    	
    	
    	
       /* try {
        	CargarParametros.parametros();
          //  List<Parada> lista = Datos.cargarParadas(CargarParametros.getArchivoParadas());
            for (Parada p : lista) {
                System.out.println(p);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado");
        }*/
    }
}

