package com.augustin.gabriel.goldenraspberryawardsapi.dtos;

public record ErrorResponseDto(
        String error,
        String message,
        String status
) {
}
