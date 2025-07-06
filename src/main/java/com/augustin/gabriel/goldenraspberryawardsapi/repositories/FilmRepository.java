package com.augustin.gabriel.goldenraspberryawardsapi.repositories;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.FilmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<FilmEntity, Long> {
}
