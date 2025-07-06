package com.augustin.gabriel.goldenraspberryawardsapi.services;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.StudioEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.repositories.StudioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudioServiceImpl implements StudioService {

    private final StudioRepository repository;

    @Override
    public List<StudioEntity> findOrCreateByNames(List<String> names) {
        log.info("Finding or create studios by names (size: {})", names.size());
        List<StudioEntity> entities = new ArrayList<>();

        for (String n : names) {
            String name = n.trim();
            log.info("Finding studio by name: {}", name);

            Optional<StudioEntity> opt = repository.findByName(name);

            StudioEntity entity;
            if (opt.isPresent()) {
                entity = opt.get();
                log.info("Studio found with id {}", entity.getId());
            } else {
                log.info("Studio not found with name {}. It will be inserted into the database.", name);
                entity = new StudioEntity();
                entity.setName(name);
            }

            entities.add(entity);
        }

        saveAll(entities);
        log.info("SUCCESS - Studios found and/or created by names (size: {})", names.size());

        return entities;
    }

    private List<StudioEntity> saveAll(List<StudioEntity> entities) {
        return repository.saveAll(entities);
    }

}
