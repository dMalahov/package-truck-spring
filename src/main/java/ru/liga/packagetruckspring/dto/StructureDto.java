package ru.liga.packagetruckspring.dto;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "stucture", schema = "postgres")
public class StructureDto {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name обязательное поле.")
    private String name;
    private String form;
    private String symbol;
}
