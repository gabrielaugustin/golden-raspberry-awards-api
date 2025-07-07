package com.augustin.gabriel.goldenraspberryawardsapi.controllers;

import com.augustin.gabriel.goldenraspberryawardsapi.dtos.HealthResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping
    public HealthResponseDto get() {
        return success();
    }

    @GetMapping("health")
    public HealthResponseDto getHealth() {
        return success();
    }

    private HealthResponseDto success() {
        return new HealthResponseDto("UP", "👍 Server is running 🏃💨");
    }
}
