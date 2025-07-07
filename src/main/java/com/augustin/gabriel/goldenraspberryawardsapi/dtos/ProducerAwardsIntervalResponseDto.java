package com.augustin.gabriel.goldenraspberryawardsapi.dtos;

import java.util.List;

public record ProducerAwardsIntervalResponseDto(
        List<AwardsIntervalResponseDto> min,
        List<AwardsIntervalResponseDto> max
) {
}
