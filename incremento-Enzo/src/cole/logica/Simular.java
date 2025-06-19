package cole.logica;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import cole.datos.CargarParametros;
import cole.datos.Datos;
import cole.modelo.Colectivo;
import cole.modelo.Linea;
import cole.modelo.Parada;
import cole.modelo.Pasajero;

public class Simular {
	

public static void main(String[] args) throws IOException {
    	
	 	Random random = new Random();
	 	

	 	
    	
    	CargarParametros.parametros();
    	
    	ArrayList<String> codigoLinea = new ArrayList<>();
   
    	ArrayList<Colectivo> coles = new ArrayList<>();
    	
    	ArrayList<Pasajero> pasajeros = new ArrayList<>();
    	
    	ArrayList<Parada> listaParadas = Datos.cargarParadas(CargarParametros.getArchivoParadas());
    	
    	TreeMap<String, Linea> lineas = null;
    	
    	lineas = Datos.cargarLineas(CargarParametros.getArchivoLinea(), listaParadas);
    	
    	int x=0;
    	for(Linea l: lineas.values()) {
    		
    		codigoLinea.addLast(l.getCodigo());
    		
    		coles.addLast(new Colectivo(x,l));
    		
    		x++;
    	}
    	
    	
    	
    	for(int i=0; i<CargarParametros.getCantPasajeros();i++) {
    		
    		int numRandomLinea = random.nextInt(codigoLinea.size());
    		    
    		int numRandomParada = random.nextInt(lineas.get(codigoLinea.get(numRandomLinea)).getParadas().size());
    			
    		while(lineas.get(codigoLinea.get(numRandomLinea)).getParadas().getLast().equals(lineas.get(codigoLinea.get(numRandomLinea)).getParadas().get(numRandomParada)) ){	
    		
    			numRandomParada = random.nextInt(lineas.get(codigoLinea.get(numRandomLinea)).getParadas().size()); 
    		
    		}
    		pasajeros.addLast(new Pasajero(i,lineas.get(codigoLinea.get(numRandomLinea)).getParadas().get(numRandomParada),lineas.get(codigoLinea.get(numRandomLinea)).getCodigo()));
    		
    		
    			
    		
    	}
    	for(int i=0; i<CargarParametros.getCantPasajeros();i++) {
    		
    		ArrayList<Parada> lineaParada = lineas.get(pasajeros.get(i).getLinea()).getParadas() ; 
    		
    		int numRandomParada = random.nextInt(lineaParada.indexOf(pasajeros.get(i).getDestino()));
			
    		listaParadas.get(lineaParada.get(numRandomParada).getId()).agregarPasajero(pasajeros.get(i));
    		

    	}
    	
    	for(Colectivo c: coles) {
			
    		for(Parada par: c.getLinea().getParadas()) {
    			
    			for(Pasajero pas: pasajeros) {
    				
    				if(c.getLinea().getCodigo().equals(pas.getLinea())) {
    					
    					c.subirPasajero(pas);
    					
    				}
    				if(par.getId() == pas.getDestino().getId()) {
						
						c.bajarPasajero(pas);
						
					}
    			
    			}
    			System.out.println("Colectivo ID: " + c.getId() + " en la parada: " + par.getId() + " tiene los siguientes pasajeros:"+ " " + c.getPasajeros());
			//if (c.getLinea().getParadas().size() 
			
			
				
			}
			
		}
    	
    	
    	
    	
    	
    	
    	
    	
  
}
}
