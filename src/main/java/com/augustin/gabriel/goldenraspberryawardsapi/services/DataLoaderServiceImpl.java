package com.augustin.gabriel.goldenraspberryawardsapi.services;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.FilmEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.StudioEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.utils.StringUtils;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataLoaderServiceImpl implements DataLoaderService {

    private static final String PATH_CSV_DATA_FILE = "data.csv";

    @Value("${csv.parser.separator-char:;}")
    private char separator;

    private final FilmService filmService;
    private final StudioService studioService;
    private final ProducerService producerService;

    @Override
    public void loadDataFromCsvFile() throws Exception {
        log.info("Loading data from CSV file...");

        InputStream inputStream = getClass().getResourceAsStream("/" + PATH_CSV_DATA_FILE);
        if (inputStream == null) {
            String message = String.format("Unable to load data from CSV file (classpath:%s)", PATH_CSV_DATA_FILE);
            log.error(message);
            throw new FileNotFoundException(message);
        }

        CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        ).withCSVParser(
                new CSVParserBuilder().withSeparator(separator).build()
        ).withSkipLines(1); // Ignoring first line (header)

        try (CSVReader reader = csvReaderBuilder.build()) {
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
                log.info("Reading line: {} - {} - {} - {} - {}", line[0], line[1], line[2], line[3], line[4]);

                List<StudioEntity> studios = studioService.findOrCreateByNames(StringUtils.splitFromString(line[2]));
                List<ProducerEntity> producers = producerService.findOrCreateByNames(StringUtils.splitFromString(line[3]));

                FilmEntity film = filmService.create(
                        Integer.parseInt(line[0].trim()),
                        line[1],
                        StringUtils.toBoolean(line[4]),
                        studios,
                        producers
                );
            }
        }

        log.info("CSV file data loaded successfully...");
    }

}
