package ru.liga.packagetruckspring.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.liga.packagetruckspring.model.Structure;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Класс инициализации данных для загрузки структуры дефолтных пакетов из файла при старте приложения.
 */
@Component
@PropertySource(value = "classpath:application.properties")
public class DataInitializer {

    @Value("${filePath}")
    private String filePath;
    private final StructureRepository structureRepository;

    /**
     * Конструктор класса DataInitializer.
     *
     * @param structureRepository Репозиторий структур пакетов для сохранения данных.
     */
    @Autowired
    public DataInitializer(StructureRepository structureRepository) {
        this.structureRepository = structureRepository;
    }

    /**
     * Метод инициализации, который выполняется после создания бина.
     * Загружает данные из файла и сохраняет их в репозиторий.
     */
    @PostConstruct
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Structure> structures = objectMapper.readValue(new File(filePath), new TypeReference<>() {});
            structureRepository.saveAll(structures);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
