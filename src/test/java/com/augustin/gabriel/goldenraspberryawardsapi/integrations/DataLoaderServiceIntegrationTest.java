package com.augustin.gabriel.goldenraspberryawardsapi.integrations;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.FilmEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.FilmRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.ProducerRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.StudioRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.services.DataLoaderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class DataLoaderServiceIntegrationTest {

    @Autowired private DataLoaderService dataLoaderService;

    @Autowired private FilmRepository filmRepository;
    @Autowired private StudioRepository studioRepository;
    @Autowired private ProducerRepository producerRepository;

    @Test
    void shouldLoadDataSuccessfully_whenCsvFileIsValid() throws Exception {
        dataLoaderService.loadDataFromCsvFile();

        long filmCount = filmRepository.count();
        long producerCount = producerRepository.count();
        long studioCount = studioRepository.count();

        assertTrue(filmCount > 0, "Should have films loaded from CSV");
        assertTrue(producerCount > 0, "Should have producers loaded from CSV");
        assertTrue(studioCount > 0, "Should have studios loaded from CSV");
    }

    @Test
    void shouldMaintainDataIntegrity_whenLoadingFromCsv() throws Exception {
        dataLoaderService.loadDataFromCsvFile();

        List<FilmEntity> films = filmRepository.findAll();

        for (FilmEntity film : films) {
            assertNotNull(film.getTitle(), "Film title should not be null");
            assertNotNull(film.getNominationYear(), "Nomination year should not be null");
            assertNotNull(film.getWinner(), "Winner field should not be null");

            assertTrue(film.getNominationYear() > 1900, "Year should be greater than 1900");
            assertTrue(film.getNominationYear() <= 2030, "Year should be less than or equal to 2030");
        }
    }

    @Test
    void shouldEstablishProducerRelationships_whenLoadingFromCsv() throws Exception {
        dataLoaderService.loadDataFromCsvFile();

        List<FilmEntity> films = filmRepository.findAll();

        for (FilmEntity film : films) {
            assertNotNull(film.getFilmProducers(), "Producer list should not be null");
        }
    }

    @Test
    void shouldEstablishStudioRelationships_whenLoadingFromCsv() throws Exception {
        dataLoaderService.loadDataFromCsvFile();

        List<FilmEntity> films = filmRepository.findAll();

        for (FilmEntity film : films) {
            assertNotNull(film.getFilmStudios(), "Studio list should not be null");
        }
    }

    @Test
    void shouldLoadWinnerFilms_whenCsvContainsWinners() throws Exception {
        dataLoaderService.loadDataFromCsvFile();

        long winnerCount = filmRepository.findAll().stream()
                .filter(FilmEntity::getWinner)
                .count();

        assertTrue(winnerCount > 0, "Should have at least one winning film");
    }

    @Test
    void shouldLoadNonWinnerFilms_whenCsvContainsNonWinners() throws Exception {
        dataLoaderService.loadDataFromCsvFile();

        long nonWinnerCount = filmRepository.findAll().stream()
                .filter(film -> !film.getWinner())
                .count();

        assertTrue(nonWinnerCount > 0, "Should have at least one non-winning film");
    }
}
