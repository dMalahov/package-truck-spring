package ru.liga.packagetruckspring.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Модель для запроса упаковки посылок в грузовики через rest.
 */
@Setter
@Getter
public class Pack {

    @NotBlank(message = "Mode обязательное поле.")
    private String mode;
    @NotBlank(message = "Trucks Size обязательное поле.")
    private String trucksSize;
    private String names;
    @NotBlank(message = "Format обязательное поле.")
    private String format;

}
