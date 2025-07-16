package com.augustin.gabriel.goldenraspberryawardsapi.integrations;

import com.augustin.gabriel.goldenraspberryawardsapi.dtos.AwardsIntervalResponseDto;
import com.augustin.gabriel.goldenraspberryawardsapi.dtos.ProducerAwardsIntervalResponseDto;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.FilmEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.FilmRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.ProducerRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.services.ProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProducerServiceIntegrationTest {

    @Autowired private ProducerService producerService;

    @Autowired private ProducerRepository producerRepository;
    @Autowired private FilmRepository filmRepository;

    @BeforeEach
    void setUp() {
        filmRepository.deleteAll();
        producerRepository.deleteAll();
    }

    @Test
    void shouldCreateNewProducers_whenNamesDoNotExist() {
        List<String> producerNames = Arrays.asList("Producer A", "Producer B", "Producer C");

        List<ProducerEntity> result = producerService.findOrCreateByNames(producerNames);

        assertEquals(producerNames.size(), result.size());
        assertEquals(producerNames.getFirst(), result.getFirst().getName());
        assertEquals(producerNames.get(1), result.get(1).getName());
        assertEquals(producerNames.get(2), result.get(2).getName());

        List<ProducerEntity> savedProducers = producerRepository.findAll();
        assertEquals(producerNames.size(), savedProducers.size());

        for (ProducerEntity producer : result) {
            assertNotNull(producer.getId(), "Producer ID should not be null");
            assertNotNull(producer.getName(), "Producer name should not be null");
            assertFalse(producer.getName().trim().isEmpty(), "Producer name should not be empty");
        }
    }

    @Test
    void shouldReturnExistingProducers_whenNamesAlreadyExist() {
        ProducerEntity existingProducer = new ProducerEntity();
        existingProducer.setName("Existing Producer");
        existingProducer = producerRepository.save(existingProducer);

        List<String> producerNames = Arrays.asList("Existing Producer", "New Producer");

        List<ProducerEntity> result = producerService.findOrCreateByNames(producerNames);

        assertEquals(producerNames.size(), result.size());
        assertEquals(existingProducer.getId(), result.getFirst().getId());
        assertEquals(existingProducer.getName(), result.getFirst().getName());
        assertEquals(producerNames.get(1), result.get(1).getName());

        List<ProducerEntity> allProducers = producerRepository.findAll();
        assertEquals(producerNames.size(), allProducers.size());

        for (ProducerEntity producer : result) {
            assertNotNull(producer.getId(), "Producer ID should not be null");
            assertNotNull(producer.getName(), "Producer name should not be null");
            assertFalse(producer.getName().trim().isEmpty(), "Producer name should not be empty");
        }
    }

    @Test
    void shouldReturnEmptyList_whenNoNamesProvided() {
        List<String> producerNames = List.of();

        List<ProducerEntity> result = producerService.findOrCreateByNames(producerNames);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyIntervals_whenNoAwardsDataExists() {
        ProducerAwardsIntervalResponseDto result = producerService.getAwardsIntervals();

        assertNotNull(result);
        assertTrue(result.min().isEmpty());
        assertTrue(result.max().isEmpty());
    }

    @Test
    void shouldReturnProducerIntervals_whenMultipleWinsExist() {
        ProducerEntity producer1 = new ProducerEntity();
        producer1.setName("Producer 1");
        producer1 = producerRepository.save(producer1);

        FilmEntity film1 = new FilmEntity();
        film1.setTitle("Movie 1");
        film1.setNominationYear(2020);
        film1.setWinner(true);
        film1 = filmRepository.save(film1);

        FilmEntity film2 = new FilmEntity();
        film2.setTitle("Movie 2");
        film2.setNominationYear(2023);
        film2.setWinner(true);
        film2 = filmRepository.save(film2);

        film1.setFilmProducers(new java.util.ArrayList<>(java.util.List.of(producer1)));
        film2.setFilmProducers(new java.util.ArrayList<>(java.util.List.of(producer1)));
        filmRepository.saveAndFlush(film1);
        filmRepository.saveAndFlush(film2);

        ProducerAwardsIntervalResponseDto result = producerService.getAwardsIntervals();

        assertNotNull(result);
        assertFalse(result.min().isEmpty(), "Should have minimum interval data");
        assertFalse(result.max().isEmpty(), "Should have maximum interval data");

        assertEquals(producer1.getName(), result.min().getFirst().producer());
        assertEquals(film2.getNominationYear() - film1.getNominationYear(), result.min().getFirst().interval());
        assertEquals(film1.getNominationYear(), result.min().getFirst().previousWin());
        assertEquals(film2.getNominationYear(), result.min().getFirst().followingWin());

        assertEquals(producer1.getName(), result.max().getFirst().producer());
        assertEquals(film2.getNominationYear() - film1.getNominationYear(), result.max().getFirst().interval());
        assertEquals(film1.getNominationYear(), result.max().getFirst().previousWin());
        assertEquals(film2.getNominationYear(), result.max().getFirst().followingWin());

        for (AwardsIntervalResponseDto interval : result.min()) {
            assertNotNull(interval.producer(), "Producer name should not be null");
            assertFalse(interval.producer().trim().isEmpty(), "Producer name should not be empty");
            assertTrue(interval.interval() >= 0, "Interval should be non-negative");
            assertTrue(interval.previousWin() > 1900, "Previous win year should be valid");
            assertTrue(interval.followingWin() > 1900, "Following win year should be valid");
            assertTrue(interval.followingWin() > interval.previousWin(), "Following win should be after previous win");
        }

        for (AwardsIntervalResponseDto interval : result.max()) {
            assertNotNull(interval.producer(), "Producer name should not be null");
            assertFalse(interval.producer().trim().isEmpty(), "Producer name should not be empty");
            assertTrue(interval.interval() >= 0, "Interval should be non-negative");
            assertTrue(interval.previousWin() > 1900, "Previous win year should be valid");
            assertTrue(interval.followingWin() > 1900, "Following win year should be valid");
            assertTrue(interval.followingWin() > interval.previousWin(), "Following win should be after previous win");
        }
    }

    @Test
    void shouldReturnLimitedIntervals_whenCustomLimitProvided() {
        ProducerAwardsIntervalResponseDto result = producerService.getAwardsIntervals();

        assertNotNull(result);
    }
}
