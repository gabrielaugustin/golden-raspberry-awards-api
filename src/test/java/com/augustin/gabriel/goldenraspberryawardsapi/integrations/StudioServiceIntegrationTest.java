package com.augustin.gabriel.goldenraspberryawardsapi.integrations;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.StudioEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.FilmRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.ProducerRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.StudioRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.services.StudioService;
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
class StudioServiceIntegrationTest {

    @Autowired private StudioService studioService;

    @Autowired private StudioRepository studioRepository;
    @Autowired private FilmRepository filmRepository;
    @Autowired private ProducerRepository producerRepository;

    @BeforeEach
    void setUp() {
        filmRepository.deleteAll();
        producerRepository.deleteAll();
        studioRepository.deleteAll();
    }

    @Test
    void shouldCreateNewStudios_whenNamesDoNotExist() {
        List<String> studioNames = Arrays.asList("Studio A", "Studio B", "Studio C");

        List<StudioEntity> result = studioService.findOrCreateByNames(studioNames);

        assertEquals(studioNames.size(), result.size());
        assertEquals(studioNames.getFirst(), result.getFirst().getName());
        assertEquals(studioNames.get(1), result.get(1).getName());
        assertEquals(studioNames.get(2), result.get(2).getName());

        List<StudioEntity> savedStudios = studioRepository.findAll();
        assertEquals(studioNames.size(), savedStudios.size());
        
        for (StudioEntity studio : result) {
            assertNotNull(studio.getId(), "Studio ID should not be null");
            assertNotNull(studio.getName(), "Studio name should not be null");
            assertFalse(studio.getName().trim().isEmpty(), "Studio name should not be empty");
        }
    }

    @Test
    void shouldReturnExistingStudios_whenNamesAlreadyExist() {
        StudioEntity existingStudio = new StudioEntity();
        existingStudio.setName("Existing Studio");
        existingStudio = studioRepository.save(existingStudio);

        List<String> studioNames = Arrays.asList("Existing Studio", "New Studio");

        List<StudioEntity> result = studioService.findOrCreateByNames(studioNames);

        assertEquals(studioNames.size(), result.size());
        assertEquals(existingStudio.getId(), result.getFirst().getId());
        assertEquals(existingStudio.getName(), result.getFirst().getName());
        assertEquals(studioNames.get(1), result.get(1).getName());

        List<StudioEntity> allStudios = studioRepository.findAll();
        assertEquals(studioNames.size(), allStudios.size());
        
        for (StudioEntity studio : result) {
            assertNotNull(studio.getId(), "Studio ID should not be null");
            assertNotNull(studio.getName(), "Studio name should not be null");
            assertFalse(studio.getName().trim().isEmpty(), "Studio name should not be empty");
        }
    }

    @Test
    void shouldReturnEmptyList_whenNoNamesProvided() {
        List<String> studioNames = List.of();

        List<StudioEntity> result = studioService.findOrCreateByNames(studioNames);

        assertTrue(result.isEmpty());
    }
}
