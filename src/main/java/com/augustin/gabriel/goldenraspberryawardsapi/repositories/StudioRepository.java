package com.augustin.gabriel.goldenraspberryawardsapi.repositories;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.StudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudioRepository extends JpaRepository<StudioEntity, Long> {

    Optional<StudioEntity> findByName(String name);

}
