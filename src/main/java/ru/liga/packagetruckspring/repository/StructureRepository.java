package ru.liga.packagetruckspring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.liga.packagetruckspring.dto.StructureDto;
import ru.liga.packagetruckspring.mapper.StructureRowMapper;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для управления структурой пакетов из json.
 */
@Repository
@RequiredArgsConstructor
public class StructureRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Возвращает список всех структур.
     *
     * @return список всех структур.
     */
    public List<StructureDto> findAll() {
        String sql = "SELECT * FROM postgres.stucture";
        return jdbcTemplate.query(sql, new StructureRowMapper());
    }

    /**
     * Находит структуру по имени.
     *
     * @param name имя структуры.
     * @return Optional, содержащий найденную структуру, или пустой Optional, если структура не найдена.
     */
    public Optional<StructureDto> findByName(String name) {
        String sql = "SELECT * FROM postgres.stucture WHERE name = ?";
        return jdbcTemplate.query(sql, new Object[]{name}, new StructureRowMapper())
                .stream()
                .findFirst();
    }

    /**
     * Обновляет существующую структуру новыми данными.
     *
     * @param name имя структуры для обновления.
     * @param updatedStructure обновлённые данные для структуры.
     * @return обновлённая структура, или пустой Optional, если структура не найдена.
     */
    public Optional<StructureDto> update(String name, StructureDto updatedStructure) {
        String sqlUpdate = "UPDATE postgres.stucture SET name = ?, form = ?, symbol = ? WHERE name = ?";
        int updated = jdbcTemplate.update(sqlUpdate, updatedStructure.getName(), updatedStructure.getForm(), updatedStructure.getSymbol(), name);

        if (updated > 0) {
            return findByName(updatedStructure.getName());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Сохраняет указанную структуру в репозиторий.
     *
     * @param structure структура для сохранения.
     * @return сохранённая структура.
     */
    public StructureDto save(StructureDto structurePack) {
        String sqlInsert = "INSERT INTO postgres.stucture (name, form, symbol) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlInsert, structurePack.getName(), structurePack.getForm(), structurePack.getSymbol());

        return findByName(structurePack.getName()).orElse(null);
    }

    /**
     * Сохраняет список структур в репозиторий.
     *
     * @param structures список структур для сохранения.
     * @return сохраненный список структур.
     */
    public List<StructureDto> saveAll(List<StructureDto> structures) {
        structures.forEach(this::save);
        return findAll();
    }

    /**
     * Очищает все структуры из репозитория.
     */
    public void clear() {
        String sqlDelete = "DELETE FROM postgres.stucture";
        jdbcTemplate.update(sqlDelete);
    }
}
