package org.optaplanner.examples.vehiclerouting.persistence;

import org.apache.commons.io.IOUtils;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.examples.common.persistence.TxtInputBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class VehicleRoutingImporter {

    public static Solution readSolution(String inputFileName, InputStream inputFileStream) {
        Solution solution;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(inputFileStream));
            TxtInputBuilder txtInputBuilder = new VehicleRoutingInputBuilder();
            txtInputBuilder.setInputFile(inputFileName);
            txtInputBuilder.setBufferedReader(bufferedReader);
            try {
                solution = txtInputBuilder.readSolution();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Exception in inputFile (" + inputFileName + ")", e);
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Exception in inputFile (" + inputFileName + ")", e);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read the file (" + inputFileName + ").", e);
        } finally {
            IOUtils.closeQuietly(bufferedReader);
        }
        /** TODO enable logger
        logger.info("Imported: {}", inputFile);
         */
        return solution;
    }

}
