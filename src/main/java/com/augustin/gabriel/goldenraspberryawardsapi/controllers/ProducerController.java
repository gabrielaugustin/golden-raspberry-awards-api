package com.augustin.gabriel.goldenraspberryawardsapi.controllers;

import com.augustin.gabriel.goldenraspberryawardsapi.dtos.ProducerAwardsIntervalResponseDto;
import com.augustin.gabriel.goldenraspberryawardsapi.services.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("producers")
public class ProducerController {

    private final ProducerService service;

    @GetMapping("awards-intervals")
    public ProducerAwardsIntervalResponseDto getAwardsIntervals() {
        log.info("Requesting awards intervals");
        ProducerAwardsIntervalResponseDto response = service.getAwardsIntervals();

        log.info("Requesting awards intervals");
        return response;
    }

}
