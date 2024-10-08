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

    private final List<Structure> structures = new ArrayList<>();

    /**
     * Возвращает список всех структур.
     *
     * @return список всех структур.
     */
    public List<Structure> findAll() {
        return new ArrayList<>(structures);
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
     * @return обновлённая структура, или пустой Optional, если структура не найдена.
     */
    public Optional<Structure> update(String name, Structure updatedStructure) {
        return findByName(name).map(originalStructure -> {
            originalStructure.setName(updatedStructure.getName());
            originalStructure.setForm(updatedStructure.getForm());
            originalStructure.setSymbol(updatedStructure.getSymbol());
            return originalStructure;
        });
    }

    /**
     * Сохраняет указанную структуру в репозиторий.
     *
     * @param structure структура для сохранения.
     * @return сохранённая структура.
     */
    public Structure save(Structure structure) {
        structures.add(structure);
        return structure;
    }

    /**
     * Сохраняет список структур в репозиторий.
     *
     * @param structures список структур для сохранения.
     * @return сохраненный список структур.
     */
    public List<Structure> saveAll(List<Structure> structures) {
        this.structures.addAll(structures);
        return new ArrayList<>(this.structures);
    }

    /**
     * Очищает все структуры из репозитория.
     */
    public void clear() {
        this.structures.clear();
    }

}
