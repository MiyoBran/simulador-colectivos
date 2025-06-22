package ar.edu.unpsjb.ayp2.proyectointegrador.interfaz;

import java.util.Properties;

public class SimuladorConfig {
    public static String obtenerNombreArchivoLineas(Properties configProperties) {
        return configProperties.getProperty("linea", "lineas_pm_mapeadas.txt");
    }
    public static String obtenerNombreArchivoParadas(Properties configProperties) {
        return configProperties.getProperty("parada", "paradas_pm_mapeadas.txt");
    }
    public static int obtenerCantidadPasajeros(Properties configProperties) {
        String valor = configProperties.getProperty("cantidadPasajeros");
        if (valor != null) {
            try {
                int cantidad = Integer.parseInt(valor);
                if (cantidad > 0) {
                    return cantidad;
                }
            } catch (NumberFormatException e) {
                System.err.println("Valor de cantidadPasajeros inválido en configuración, se usará 150 por defecto.");
            }
        }
        return 150;
    }
    public static int obtenerCapacidadColectivo(Properties configProperties) {
        String valor = configProperties.getProperty("capacidadColectivo");
        if (valor != null) {
            try {
                int capacidad = Integer.parseInt(valor);
                if (capacidad > 0) {
                    return capacidad;
                }
            } catch (NumberFormatException e) {
                System.err.println("Valor de capacidadColectivo inválido en configuración, se usará 30 por defecto.");
            }
        }
        return 30;
    }
    public static int obtenerCapacidadSentadosColectivo(Properties configProperties) {
        String valor = configProperties.getProperty("capacidadSentadosColectivo");
        if (valor != null) {
            try {
                int capacidad = Integer.parseInt(valor);
                if (capacidad >= 0) {
                    return capacidad;
                }
            } catch (NumberFormatException e) {
                System.err.println("Valor de capacidadSentadosColectivo inválido en configuración, se usará 20 por defecto.");
            }
        }
        return 20;
    }
    public static int obtenerCantidadColectivosSimultaneosPorLinea(Properties configProperties) {
        String valor = configProperties.getProperty("cantidad_de_colectivos_simultaneos_por_linea");
        if (valor != null) {
            try {
                int cantidad = Integer.parseInt(valor);
                if (cantidad > 0) {
                    return cantidad;
                }
            } catch (NumberFormatException e) {
                System.err.println("Valor de cantidad_de_colectivos_simultaneos_por_linea inválido en configuración, se usará 1 por defecto.");
            }
        }
        return 1;
    }
    public static int obtenerRecorridosPorColectivo(Properties configProperties) {
        String valor = configProperties.getProperty("recorridos_por_colectivo");
        if (valor != null) {
            try {
                int cantidad = Integer.parseInt(valor);
                if (cantidad > 0) {
                    return cantidad;
                }
            } catch (NumberFormatException e) {
                System.err.println("Valor de recorridos_por_colectivo inválido en configuración, se usará 1 por defecto.");
            }
        }
        return 1;
    }
    public static int obtenerFrecuenciaSalidaColectivosMinutos(Properties configProperties) {
        String valor = configProperties.getProperty("frecuencia_salida_colectivos_minutos");
        if (valor != null) {
            try {
                int cantidad = Integer.parseInt(valor);
                if (cantidad > 0) {
                    return cantidad;
                }
            } catch (NumberFormatException e) {
                System.err.println("Valor de frecuencia_salida_colectivos_minutos inválido en configuración, se usará 10 por defecto.");
            }
        }
        return 10;
    }
}