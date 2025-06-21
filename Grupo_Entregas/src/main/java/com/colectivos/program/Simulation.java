package com.colectivos.program;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import com.colectivos.data.LoadParameters;
import com.colectivos.data.OutputFiles;
import com.colectivos.logic.NetworkTraversal;
import com.colectivos.model.Bus;
import com.colectivos.model.Stop;
import com.colectivos.setup.TransitDataGenerator;

/**
 * Entry point for the bus network simulation program.
 * This class is responsible for coordinating the initialization, data generation,
 * and execution of the simulation. It performs the following steps:
 *   Loads simulation parameters and configuration files
 *   Generates stops populated with passengers
 *   Generates buses based on transit lines
 *   Runs the simulation for each bus, outputting results to individual files
 *
 * The output for each bus's journey is redirected to a separate file located in the
 * folder defined by {@link OutputFiles}, allowing for detailed inspection of each bus in the simulation.
 */
public class Simulation {
    public static void main (String arg[]) {
        // Save original terminal output to restore later
        PrintStream terminalOutput = System.out;
        List<PrintStream> outputFiles = null;

        try {
            //      ===== Initialization =====
            System.out.println("Starting bus network simulation.");
            LoadParameters.initialize();
            System.out.println("Configuration files loaded succesfully.");

            // Creates an output file for each bus
            outputFiles = OutputFiles.makeOutputFiles(12);
            System.out.println("Output files created succesfully.");

            // Generate stops and populate them with up to x passengers
            Map<Integer, Stop> stops = TransitDataGenerator.loadAndPopulateStops(100);
            System.out.println("Stops generated succesfully.");

            // Generate one bus per line
            List<Bus> buses = TransitDataGenerator.generateBuses(stops);
            System.out.println("Buses generated for " + buses.size() + " lines.");

            //      ===== Simulation =====
            System.out.println("Starting simulation for each bus");
            for (int i = 0; i < buses.size(); i++) {
                Bus bus = buses.get(i);

                //Redirects all output to the current bus' output file
                System.setOut(outputFiles.get(i));

                //Simulates the traversal of the bus, adding and removing passengers as they reach stops
                NetworkTraversal.busTraversal(bus);

                //Makes sure all output is written 
                outputFiles.get(i).flush();
            }

            //      ===== Post-simulation =====
            //Restores output to terminal
            System.setOut(terminalOutput);
            System.out.println("\nSimulation ended succesfully!");
            System.out.println("Check the 'red-colectivos output' folder to see detailed information for each bus run" );

        }   catch (IOException error) {
            //Restores output to the terminal and prints error logs
            System.setOut(terminalOutput);
            System.err.println("Error during file operations: " + error.getMessage());
            error.printStackTrace();

        }   finally {
            //Always closes array of PrintStream objects and restores output to terminal
            if (outputFiles != null) {
                for (PrintStream print : outputFiles) {
                    print.close();
                }
            }
            System.setOut(terminalOutput);
        }
    }
}
