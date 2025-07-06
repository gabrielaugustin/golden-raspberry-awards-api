package com.augustin.gabriel.goldenraspberryawardsapi.configs;

import com.augustin.gabriel.goldenraspberryawardsapi.services.DataLoaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupRunner implements CommandLineRunner {

    private final DataLoaderService dataLoaderService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Inserting data into the database...");
        dataLoaderService.loadDataFromCsvFile();
        log.info("Data has been successfully inserted into the database.");
    }

}
