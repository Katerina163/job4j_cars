package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.job4j.cars.model.Color;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostModifyDTO {
    @NotNull
    private Long postId;

    @NotNull
    private Long carId;

    @Length(min = 2, max = 26, message = "Название машины должно быть больше 2 и меньше 26 символов")
    private String carName;

    @NotNull
    @Max(value = 7L)
    private Long markId;

    @NotNull
    private Color color;

    @Length(min = 10, max = 500, message = "Описание должно быть больше 10 и меньше 500 символов")
    private String description;

    @NotNull
    private String owners;
}
