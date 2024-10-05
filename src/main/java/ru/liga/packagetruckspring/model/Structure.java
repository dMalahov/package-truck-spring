package ru.liga.packagetruckspring.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Модель для работы с грузовиками и посылками в JSON.
 */
@Setter
@Getter
public class Structure {

    private String name;
    private String form;
    private String symbol;

}
