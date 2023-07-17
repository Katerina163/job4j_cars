package ru.job4j.cars.dto;

import javax.validation.constraints.NotNull;

public record PriceDto(@NotNull
                       Long id,
                       @NotNull
                       Long price) {
}
