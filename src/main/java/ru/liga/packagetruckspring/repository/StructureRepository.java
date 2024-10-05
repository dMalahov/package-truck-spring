package ru.liga.packagetruckspring.repository;

import org.springframework.stereotype.Repository;
import ru.liga.packagetruckspring.model.Structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления структурой пакетов из json.
 */
@Repository
public class StructureRepository {

    private List<Structure> structures = new ArrayList<>();

    /**
     * Возвращает список всех структур.
     *
     * @return список всех структур.
     */
    public List<Structure> findAll() {
        return structures;
    }

    /**
     * Находит структуру по имени.
     *
     * @param name имя структуры.
     * @return Optional, содержащий найденную структуру, или пустой Optional, если структура не найдена.
     */
    public Optional<Structure> findByName(String name) {
        return structures.stream()
                .filter(structure -> structure.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Обновляет существующую структуру новыми данными.
     *
     * @param name имя структуры для обновления.
     * @param updatedStructure обновлённые данные для структуры.
     */
    public void update(String name, Structure updatedStructure) {
        findByName(name).ifPresent(originalStructure -> {
            originalStructure.setName(updatedStructure.getName());
            originalStructure.setForm(updatedStructure.getForm());
            originalStructure.setSymbol(updatedStructure.getSymbol());
        });
    }

    /**
     * Сохраняет указанную структуру в репозиторий.
     *
     * @param structure структура для сохранения.
     */
    public void save(Structure structure) {
        structures.add(structure);
    }

    /**
     * Сохраняет список структур в репозиторий.
     *
     * @param structures список структур для сохранения.
     */
    public void saveAll(List<Structure> structures) {
        this.structures = structures;
    }

    /**
     * Очищает все структуры из репозитория.
     */
    public void clear() {
        this.structures.clear();
    }

}
