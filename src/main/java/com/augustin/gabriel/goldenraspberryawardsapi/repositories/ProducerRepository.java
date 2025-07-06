package com.augustin.gabriel.goldenraspberryawardsapi.repositories;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<ProducerEntity, Long> {
}
