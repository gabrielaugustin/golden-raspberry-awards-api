package com.augustin.gabriel.goldenraspberryawardsapi.services;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.FilmEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;
import com.augustin.gabriel.goldenraspberryawardsapi.entities.StudioEntity;

import java.util.List;

public interface FilmService {

    FilmEntity create(Integer nominationYear,
                      String title,
                      Boolean winner,
                      List<StudioEntity> studios,
                      List<ProducerEntity> producers);

}
