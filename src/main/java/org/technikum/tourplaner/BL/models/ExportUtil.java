package org.technikum.tourplaner.BL.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.technikum.tourplaner.BL.viewmodels.TourLogViewModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportUtil {
    private static final Logger logger = LogManager.getLogger(TourLogViewModel.class);
    public ObjectMapper objectMapper = new ObjectMapper();;

    public void exportTourData(TourModel tourModel, List<TourLogModel> tourLogModels){
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        try{
            String filePath = "src/main/resources/export.txt";
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String json = ow.writeValueAsString(tourModel);
            json = json + "\n---||---\n";
            json = json + ow.writeValueAsString(tourLogModels);

            bufferedWriter.write(json);
            bufferedWriter.close();
            logger.info("JSON data written to file successfully!");
        } catch (IOException e) {
            logger.warn("Error while trying to write tour" + e.getMessage());
        }
    }
}
