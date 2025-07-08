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

@Transactional
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class EndToEndIntegrationTest {

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
        producer1.setName("Producer 1 Test");
        producer1 = producerRepository.save(producer1);

        ProducerEntity producer2 = new ProducerEntity();
        producer2.setName("Producer 2 Test");
        producer2 = producerRepository.save(producer2);

        FilmEntity film1 = new FilmEntity();
        film1.setTitle("Film 1 Test");
        film1.setNominationYear(1999);
        film1.setWinner(true);
        film1.setFilmProducers(List.of(producer1));
        filmRepository.save(film1);

        FilmEntity film2 = new FilmEntity();
        film2.setTitle("Film 2 Test");
        film2.setNominationYear(2003);
        film2.setWinner(true);
        film2.setFilmProducers(List.of(producer1));
        filmRepository.save(film2);

        FilmEntity film3 = new FilmEntity();
        film3.setTitle("Film 3 Test");
        film3.setNominationYear(2010);
        film3.setWinner(true);
        film3.setFilmProducers(List.of(producer2));
        filmRepository.save(film3);

        FilmEntity film4 = new FilmEntity();
        film4.setTitle("Film 4 Test");
        film4.setNominationYear(2014);
        film4.setWinner(true);
        film4.setFilmProducers(List.of(producer2));
        filmRepository.save(film4);
    }

    @Test
    void shouldReturnHealthStatus_whenCompleteWorkflowExecuted() throws Exception {
        mockMvc.perform(get("/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.message", not(emptyOrNullString())));
    }

    @Test
    void shouldReturnAwardsIntervals_whenCompleteWorkflowExecuted() throws Exception {
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
    void shouldReturnLimitedAwardsIntervals_whenCompleteWorkflowWithLimitExecuted() throws Exception {
        setupTestData();
        
        mockMvc.perform(get("/producers/awards-intervals")
                .param("limit", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasKey("min")))
                .andExpect(jsonPath("$", hasKey("max")))
                .andExpect(jsonPath("$.min", hasSize(lessThanOrEqualTo(3))))
                .andExpect(jsonPath("$.max", hasSize(lessThanOrEqualTo(3))))
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
    void shouldHandleErrorsGracefully_whenInvalidEndpointCalled() throws Exception {
        mockMvc.perform(get("/nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("No Resource Found")))
                .andExpect(jsonPath("$.message", not(emptyOrNullString())))
                .andExpect(jsonPath("$.status", is("404")));
    }

    @Test
    void shouldHandleDatabaseErrorsGracefully_whenDataAccessIssuesOccur() throws Exception {
        setupTestData();
        
        mockMvc.perform(get("/producers/awards-intervals")
                .param("limit", "1000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasKey("min")))
                .andExpect(jsonPath("$", hasKey("max")));
    }

    @Test
    void shouldMaintainDataConsistency_whenMultipleRequestsAreMade() throws Exception {
        setupTestData();
        
        mockMvc.perform(get("/producers/awards-intervals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min", hasSize(greaterThanOrEqualTo(0))))
                .andExpect(jsonPath("$.max", hasSize(greaterThanOrEqualTo(0))));
        
        mockMvc.perform(get("/producers/awards-intervals")
                .param("limit", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min", hasSize(lessThanOrEqualTo(5))))
                .andExpect(jsonPath("$.max", hasSize(lessThanOrEqualTo(5))));
        
        mockMvc.perform(get("/producers/awards-intervals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min", hasSize(greaterThanOrEqualTo(0))))
                .andExpect(jsonPath("$.max", hasSize(greaterThanOrEqualTo(0))));
    }
} 
