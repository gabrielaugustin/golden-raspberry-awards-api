package com.augustin.gabriel.goldenraspberryawardsapi.repositories;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProducerRepository extends JpaRepository<ProducerEntity, Long> {

    Optional<ProducerEntity> findByName(String name);

    @Query("""
        SELECT
            fp1.producer.name,
            (fp2.film.nominationYear - fp1.film.nominationYear) as interval,
            fp1.film.nominationYear,
            fp2.film.nominationYear
        FROM FilmProducerEntity fp1
        JOIN FilmProducerEntity fp2 ON fp2.producer = fp1.producer
        WHERE fp1.film.winner = true
        AND fp2.film.winner = true
        AND fp1.film.nominationYear < fp2.film.nominationYear
        """)
    List<Object[]> findAwardsIntervalsWithSort(Sort sort);

}
