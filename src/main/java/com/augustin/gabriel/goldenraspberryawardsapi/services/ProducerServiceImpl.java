package com.augustin.gabriel.goldenraspberryawardsapi.services;

import com.augustin.gabriel.goldenraspberryawardsapi.dtos.AwardsIntervalResponseDto;
import com.augustin.gabriel.goldenraspberryawardsapi.dtos.ProducerAwardsIntervalResponseDto;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.ProducerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProducerServiceImpl implements ProducerService {

    private final ProducerRepository repository;

    @Override
    public List<ProducerEntity> findOrCreateByNames(List<String> names) {
        log.info("Finding or create producers by names (size: {})", names.size());
        List<ProducerEntity> entities = new ArrayList<>();

        for (String n : names) {
            String name = n.trim();
            log.info("Finding producer by name: {}", name);

            Optional<ProducerEntity> opt = repository.findByName(name);

            ProducerEntity entity;
            if (opt.isPresent()) {
                entity = opt.get();
                log.info("Producer found with id {}", entity.getId());
            } else {
                log.info("Producer not found with name {}. It will be inserted into the database.", name);
                entity = new ProducerEntity();
                entity.setName(name);
            }

            entities.add(entity);
        }

        repository.saveAll(entities);
        log.info("SUCCESS - Producers found and/or created by names (size: {})", names.size());

        return entities;
    }

    @Override
    public ProducerAwardsIntervalResponseDto getAwardsIntervals() {
        log.info("Getting awards intervals");

        List<Object[]> minIntervals = repository.findAwardsIntervalsWithSort(
                Sort.by(Sort.Direction.ASC, "interval")
        );

        List<Object[]> maxIntervals = repository.findAwardsIntervalsWithSort(
                Sort.by(Sort.Direction.DESC, "interval")
        );

        log.info("Found {} min intervals and {} max intervals", minIntervals.size(), maxIntervals.size());

        return new ProducerAwardsIntervalResponseDto(
                mapAwardsIntervals(minIntervals),
                mapAwardsIntervals(maxIntervals)
        );
    }

    private List<AwardsIntervalResponseDto> mapAwardsIntervals(List<Object[]> awardsIntervals) {
        return awardsIntervals.stream()
                .map(row -> new AwardsIntervalResponseDto(
                        (String)  row[0], // producer
                        (Integer) row[1], // interval
                        (Integer) row[2], // previousWin
                        (Integer) row[3]  // followingWin
                ))
                .toList();
    }

}
