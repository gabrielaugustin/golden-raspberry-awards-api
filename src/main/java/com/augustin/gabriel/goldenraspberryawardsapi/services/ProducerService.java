package com.augustin.gabriel.goldenraspberryawardsapi.services;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.ProducerEntity;

import java.util.List;

public interface ProducerService {

    List<ProducerEntity> findOrCreateByNames(List<String> names);

}
