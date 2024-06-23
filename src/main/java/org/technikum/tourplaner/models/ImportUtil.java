package org.technikum.tourplaner.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.technikum.tourplaner.repositories.TourLogRepository;
import org.technikum.tourplaner.repositories.TourRepository;
import org.technikum.tourplaner.viewmodels.TourLogViewModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ImportUtil {
    private static final Logger logger = LogManager.getLogger(TourLogViewModel.class);
    public ObjectMapper objectMapper = new ObjectMapper();;


    private final TourRepository tourRepository;
    private final TourLogRepository tourLogRepository;
    public ImportUtil(TourRepository tourRepository, TourLogRepository tourLogRepository) {
        this.tourRepository = tourRepository;
        this.tourLogRepository = tourLogRepository;
    }

    public void importTourData(){
        String content;
        try{
            content = Files.readString(Path.of("src/main/resources/import.txt"), StandardCharsets.UTF_8);

            String jsonTour = content.substring(0, content.indexOf("---||---"));
            String jsonTourLogsList = content.substring(content.indexOf("---||---")).replace("---||---", "");

            TourModel tour = objectMapper.readValue(jsonTour, TourModel.class);
            tour.setId(null);
            List<TourLogModel> tourLogList = objectMapper.readValue(jsonTourLogsList, new TypeReference<List<TourLogModel>>(){});

            Long id = tourRepository.save(tour);
            for (TourLogModel tourLogModel : tourLogList){
                tourLogModel.setId(null);
                tourLogModel.setTourId(id);
                tourLogRepository.save(tourLogModel);
            }
        } catch (IOException e) {
            logger.warn("Error while trying to import tour: " + e.getMessage());
        }
    }
}
