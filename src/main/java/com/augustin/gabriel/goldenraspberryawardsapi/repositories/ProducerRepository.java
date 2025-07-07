package com.augustin.gabriel.goldenraspberryawardsapi.repositories;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProducerRepository extends JpaRepository<ProducerEntity, Long> {

    Optional<ProducerEntity> findByName(String name);

}
