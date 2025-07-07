package com.augustin.gabriel.goldenraspberryawardsapi.services;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.FilmEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.StudioEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.FilmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository repository;

    @Override
    public FilmEntity create(Integer nominationYear, String title, Boolean winner, List<StudioEntity> studios, List<ProducerEntity> producers) {
        log.info("Creating film: {} - {}", nominationYear, title);

        FilmEntity film = FilmEntity.builder()
                .nominationYear(nominationYear)
                .title(title.trim())
                .winner(winner)
                .build();

        film.setFilmStudios(studios);
        film.setFilmProducers(producers);

        repository.save(film);
        log.info("SUCCESS - Film created (id {}): {} - {}", film.getId(), nominationYear, title);

        return film;
    }

}
