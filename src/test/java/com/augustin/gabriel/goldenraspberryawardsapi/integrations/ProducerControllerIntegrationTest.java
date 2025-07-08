package com.augustin.gabriel.goldenraspberryawardsapi.integrations;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.FilmEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.FilmRepository;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.ProducerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class ProducerControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext webApplicationContext;

    @Autowired private ProducerRepository producerRepository;
    @Autowired private FilmRepository filmRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    private void setupTestData() {
        ProducerEntity producer1 = new ProducerEntity();
        producer1.setName("Producer 1");
        producer1 = producerRepository.save(producer1);

        ProducerEntity producer2 = new ProducerEntity();
        producer2.setName("Producer 2");
        producer2 = producerRepository.save(producer2);

        FilmEntity film1 = new FilmEntity();
        film1.setTitle("Movie 1");
        film1.setNominationYear(2020);
        film1.setWinner(true);
        film1.setFilmProducers(List.of(producer1));
        filmRepository.save(film1);

        FilmEntity film2 = new FilmEntity();
        film2.setTitle("Movie 2");
        film2.setNominationYear(2023);
        film2.setWinner(true);
        film2.setFilmProducers(List.of(producer2));
        filmRepository.save(film2);
    }

    @Test
    void shouldReturnAwardsIntervals_whenDefaultLimitUsed() throws Exception {
        setupTestData();
        
        mockMvc.perform(get("/producers/awards-intervals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasKey("min")))
                .andExpect(jsonPath("$", hasKey("max")))
                .andExpect(jsonPath("$.min", hasSize(greaterThanOrEqualTo(0))))
                .andExpect(jsonPath("$.max", hasSize(greaterThanOrEqualTo(0))))
                .andExpect(jsonPath("$.min[*].producer", not(emptyOrNullString())))
                .andExpect(jsonPath("$.min[*].interval", not(emptyOrNullString())))
                .andExpect(jsonPath("$.min[*].previousWin", not(emptyOrNullString())))
                .andExpect(jsonPath("$.min[*].followingWin", not(emptyOrNullString())))
                .andExpect(jsonPath("$.max[*].producer", not(emptyOrNullString())))
                .andExpect(jsonPath("$.max[*].interval", not(emptyOrNullString())))
                .andExpect(jsonPath("$.max[*].previousWin", not(emptyOrNullString())))
                .andExpect(jsonPath("$.max[*].followingWin", not(emptyOrNullString())));
    }

    @Test
    void shouldReturnLimitedAwardsIntervals_whenCustomLimitProvided() throws Exception {
        setupTestData();
        
        mockMvc.perform(get("/producers/awards-intervals")
                .param("limit", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasKey("min")))
                .andExpect(jsonPath("$", hasKey("max")))
                .andExpect(jsonPath("$.min", hasSize(lessThanOrEqualTo(5))))
                .andExpect(jsonPath("$.max", hasSize(lessThanOrEqualTo(5))))
                .andExpect(jsonPath("$.min[*].producer", not(emptyOrNullString())))
                .andExpect(jsonPath("$.min[*].interval", not(emptyOrNullString())))
                .andExpect(jsonPath("$.min[*].previousWin", not(emptyOrNullString())))
                .andExpect(jsonPath("$.min[*].followingWin", not(emptyOrNullString())))
                .andExpect(jsonPath("$.max[*].producer", not(emptyOrNullString())))
                .andExpect(jsonPath("$.max[*].interval", not(emptyOrNullString())))
                .andExpect(jsonPath("$.max[*].previousWin", not(emptyOrNullString())))
                .andExpect(jsonPath("$.max[*].followingWin", not(emptyOrNullString())));
    }

    @Test
    void shouldReturnBadRequest_whenInvalidLimitProvided() throws Exception {
        mockMvc.perform(get("/producers/awards-intervals")
                .param("limit", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenZeroLimitProvided() throws Exception {
        mockMvc.perform(get("/producers/awards-intervals")
                .param("limit", "0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
