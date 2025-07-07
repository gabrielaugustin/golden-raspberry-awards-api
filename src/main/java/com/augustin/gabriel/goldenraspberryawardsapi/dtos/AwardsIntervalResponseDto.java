package com.augustin.gabriel.goldenraspberryawardsapi.dtos;

public record AwardsIntervalResponseDto(
        String producer,
        Integer interval,
        Integer previousWin,
        Integer followingWin
) {
}
