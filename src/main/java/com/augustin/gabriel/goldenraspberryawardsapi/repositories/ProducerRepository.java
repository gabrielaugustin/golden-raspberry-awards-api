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
            fp.producer.name,
            (fp.film.nominationYear - LAG(fp.film.nominationYear) OVER (
                PARTITION BY fp.producer 
                ORDER BY fp.film.nominationYear
            )) as interval,
            LAG(fp.film.nominationYear) OVER (
                PARTITION BY fp.producer 
                ORDER BY fp.film.nominationYear
            ) as previousWin,
            fp.film.nominationYear as followingWin
        FROM FilmProducerEntity fp
        WHERE fp.film.winner = true
        """)
    List<Object[]> findAwardsIntervalsWithSort(Sort sort);

}
