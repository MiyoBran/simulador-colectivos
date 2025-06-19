package cole.datos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import cole.modelo.Linea;
import cole.modelo.Parada;



public class Datos {

    public static ArrayList<Parada> cargarParadas(String fileName) throws FileNotFoundException {
        Scanner read = new Scanner(new File(fileName));
        ArrayList<Parada> paradas = new ArrayList<>();

        while (read.hasNextLine()) {
            String linea = read.nextLine().trim();

            if (linea.isEmpty() || linea.startsWith("#")) continue;

            String[] partes = linea.split(";");
            if (partes.length != 2) {
                System.out.println("Línea mal formateada: " + linea);
                continue;
            }

            try {
                int id = Integer.parseInt(partes[0].trim());
                String direccion = partes[1].trim();
                paradas.addLast(new Parada(id, direccion));
            } catch (NumberFormatException e) {
                System.out.println("ID inválido: " + partes[0]);
            }
        }

        read.close();
        return paradas;
    }


	

	public static TreeMap<String, Linea> cargarLineas(String fileName,List<Parada> paradas)  throws FileNotFoundException {
		
		Scanner read = new Scanner(new File(fileName));

		TreeMap<String, Linea> lineas = new TreeMap<String, Linea>();
		
		

        while (read.hasNextLine()) {
            String linea = read.nextLine().trim();

            if (linea.isEmpty() || linea.startsWith("#")) continue;

            String[] partes = linea.split(";");
            
     //       if (partes.length != 2) {
     //           System.out.println("Línea mal formateada: " + linea);
     //           continue;
     //       }

            
                String codigo = partes[0].trim();
                
                
                lineas.put(codigo, new Linea(codigo));
                
                for(int i=1;i<partes.length;i++){
                	
                	int idParada = Integer.parseInt(partes[i].trim())-1;
                	
                	lineas.get(codigo).agregarParada(paradas.get(idParada));

                }
                
                
            } 
        

        read.close();
		
		return lineas;
	}

	

	/*public static List<Tramo> cargarTramos(String fileName,
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
*/
}
