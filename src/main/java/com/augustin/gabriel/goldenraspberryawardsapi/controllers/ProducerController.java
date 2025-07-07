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
    public ProducerAwardsIntervalResponseDto getAwardsIntervals(
        @RequestParam(value = "limit", required = false, defaultValue = "1") Integer limit
    ) {
        log.info("Requesting awards intervals with limit: {}", limit);
        return service.getAwardsIntervals(limit);
    }

}
