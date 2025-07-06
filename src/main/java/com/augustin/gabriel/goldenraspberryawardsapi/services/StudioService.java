package com.augustin.gabriel.goldenraspberryawardsapi.services;

import com.augustin.gabriel.goldenraspberryawardsapi.entities.StudioEntity;

import java.util.List;

public interface StudioService {

    List<StudioEntity> findOrCreateByNames(List<String> names);

}
