package ru.liga.packagetruckspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.liga.packagetruckspring.mapper.StringArrayConverter;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "package")
public class PackageDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int height;
    private int widthTop;
    private int widthBottom;

    @Convert(converter = StringArrayConverter.class)
    private String[] pack;
}
