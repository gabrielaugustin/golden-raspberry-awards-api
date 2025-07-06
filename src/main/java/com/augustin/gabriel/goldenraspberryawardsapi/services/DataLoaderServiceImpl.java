package com.augustin.gabriel.goldenraspberryawardsapi.services;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
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
public class DataLoaderServiceImpl implements DataLoaderService {

    private static final String PATH_CSV_DATA_FILE = "data.csv";

    @Value("${csv.parser.separator-char:;}")
    private char separator;

    @Override
    public void loadDataFromCsvFile() throws Exception {
        log.info("Loading data from csv file...");

        InputStream inputStream = getClass().getResourceAsStream("/" + PATH_CSV_DATA_FILE);
        if (inputStream == null) {
            String message = String.format("Unable to load data from csv file (classpath:%s)", PATH_CSV_DATA_FILE);
            log.error(message);
            throw new FileNotFoundException(message);
        }

        CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        ).withCSVParser(
                new CSVParserBuilder().withSeparator(separator).build()
        ).withSkipLines(1); // Ignoring first line (header)

        try (CSVReader reader = csvReaderBuilder.build()) {
            List<String[]> linhas = reader.readAll();
            for (String[] linha : linhas) {
                log.info("{} - {} - {} - {} - {}", linha[0], linha[1], linha[2], linha[3], linha[4]);
            }
        }
    }

}
