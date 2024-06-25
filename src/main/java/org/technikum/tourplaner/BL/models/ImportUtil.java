package org.technikum.tourplaner.BL.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.technikum.tourplaner.BL.viewmodels.TourLogViewModel;
import org.technikum.tourplaner.DAL.repositories.TourLogRepository;
import org.technikum.tourplaner.DAL.repositories.TourRepository;

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
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void importTourData() throws IOException
    {
        String content = Files.readString(Path.of("src/main/resources/import.txt"), StandardCharsets.UTF_8);

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
    }
}
