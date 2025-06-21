package com.colectivos.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for generating output files for logging results from the simulation.
 * Creates a folder named {@code red-colectivos output} (if it doesn't already exist),
 * and generates a specified number of individual output files for each bus used in the bus network simulation,
*/
public class OutputFiles {

    /**
     * Creates a list of {@link PrintStream} objects, each linked to a separate output file.
     * The method generates a directory named {@code red-colectivos output} in the current working directory,
     * then creates {@code limit} number of files named {@code output-busX.txt}, where X ranges from 1 to {@code limit}.
     * These files can be used to write simulation output separately for each bus.
     *
     * @param limit the number of output files (and PrintStreams) to generate
     * @return a list of {@link PrintStream} objects corresponding to the created files
     * @throws FileNotFoundException if the files cannot be created or opened for writing
    */
    public static List<PrintStream> makeOutputFiles(int limit) throws FileNotFoundException {
        File outputFolder = new File("red-colectivos output");
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        List<PrintStream> filesOut = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            String fileName = String.format("red-colectivos output/output-bus%d.txt", i + 1);
            filesOut.add(new PrintStream(new FileOutputStream(fileName)));
        }
        return filesOut;
    }
}