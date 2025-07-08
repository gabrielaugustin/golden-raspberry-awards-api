package com.augustin.gabriel.goldenraspberryawardsapi.integrations;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.FilmEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.StudioEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.FilmRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.ProducerRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.StudioRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.services.FilmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class FilmServiceIntegrationTest {

    @Autowired private FilmService filmService;

    @Autowired private FilmRepository filmRepository;
    @Autowired private ProducerRepository producerRepository;
    @Autowired private StudioRepository studioRepository;

    @BeforeEach
    void setUp() {
        filmRepository.deleteAll();
        producerRepository.deleteAll();
        studioRepository.deleteAll();
    }

    @Test
    void shouldCreateFilm_whenValidDataProvided() {
        ProducerEntity producer = new ProducerEntity();
        producer.setName("Test Producer");
        producer = producerRepository.save(producer);

        StudioEntity studio = new StudioEntity();
        studio.setName("Test Studio");
        studio = studioRepository.save(studio);

        FilmEntity result = filmService.create(
            2023,
            "Test Movie",
            true,
            List.of(studio),
            List.of(producer)
        );

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Test Movie", result.getTitle());
        assertEquals(2023, result.getNominationYear());
        assertTrue(result.getWinner());
        assertNotNull(result.getFilmProducers());
        assertEquals(1, result.getFilmProducers().size());
        assertEquals(producer.getName(), result.getFilmProducers().getFirst().getProducer().getName());
        assertNotNull(result.getFilmStudios());
        assertEquals(1, result.getFilmStudios().size());
        assertEquals(studio.getName(), result.getFilmStudios().getFirst().getStudio().getName());
    }

    @Test
    void shouldCreateFilm_whenMultipleProducersAndStudiosProvided() {
        ProducerEntity producer1 = new ProducerEntity();
        producer1.setName("Producer 1");
        producer1 = producerRepository.save(producer1);

        ProducerEntity producer2 = new ProducerEntity();
        producer2.setName("Producer 2");
        producer2 = producerRepository.save(producer2);

        StudioEntity studio1 = new StudioEntity();
        studio1.setName("Studio 1");
        studio1 = studioRepository.save(studio1);

        StudioEntity studio2 = new StudioEntity();
        studio2.setName("Studio 2");
        studio2 = studioRepository.save(studio2);

        FilmEntity result = filmService.create(
            2023,
            "Multi Producer Movie",
            false,
            Arrays.asList(studio1, studio2),
            Arrays.asList(producer1, producer2)
        );

        assertNotNull(result);
        assertEquals("Multi Producer Movie", result.getTitle());
        assertEquals(2, result.getFilmProducers().size());
        assertEquals(2, result.getFilmStudios().size());
        assertFalse(result.getWinner());
    }

    @Test
    void shouldCreateFilm_whenNoProducersAndStudiosProvided() {
        FilmEntity result = filmService.create(
            2023,
            "Independent Movie",
            false,
            List.of(),
            List.of()
        );

        assertNotNull(result);
        assertEquals("Independent Movie", result.getTitle());
        assertTrue(result.getFilmProducers().isEmpty());
        assertTrue(result.getFilmStudios().isEmpty());
    }

    @Test
    void shouldPersistFilmToDatabase_whenFilmCreated() {
        ProducerEntity producer = new ProducerEntity();
        producer.setName("Test Producer");
        producer = producerRepository.save(producer);

        StudioEntity studio = new StudioEntity();
        studio.setName("Test Studio");
        studio = studioRepository.save(studio);

        FilmEntity createdFilm = filmService.create(
            2023,
            "Persistent Movie",
            true,
            List.of(studio),
            List.of(producer)
        );

        FilmEntity retrievedFilm = filmRepository.findById(createdFilm.getId()).orElse(null);
        
        assertNotNull(retrievedFilm);
        assertEquals("Persistent Movie", retrievedFilm.getTitle());
        assertEquals(2023, retrievedFilm.getNominationYear());
        assertTrue(retrievedFilm.getWinner());
        assertEquals(1, retrievedFilm.getFilmProducers().size());
        assertEquals(1, retrievedFilm.getFilmStudios().size());
    }
}
