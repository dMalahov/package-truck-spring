package ru.liga.packagetruckspring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.liga.packagetruckspring.dto.PackageDto;
import ru.liga.packagetruckspring.mapper.PackageRowMapper;
import ru.liga.packagetruckspring.mapper.StringArrayConverter;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Репозиторий для хранения и управления пакетами.
 */
@Repository
@RequiredArgsConstructor
public class PackageRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StringArrayConverter stringArrayConverter;

    /**
     * Возвращает список всех пакетов.
     *
     * @return список всех пакетов.
     */
    public List<PackageDto> findAll() {
        String sql = "SELECT * FROM package";
        return jdbcTemplate.query(sql, new PackageRowMapper());
    }

    /**
     * Сохраняет указанный пакет в репозиторий.
     *
     * @param pack пакет для сохранения.
     * @return сохранённый пакет.
     */
    public PackageDto save(PackageDto pack) {
        String sql = "INSERT INTO package (height, widthTop, widthBottom, pack) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, pack.getHeight(), pack.getWidthTop(), pack.getWidthBottom(),
                new StringArrayConverter().convertToDatabaseColumn(pack.getPack()));

        // Получим и вернем записанный пакет с назначенным ID
        String selectSql = "SELECT * FROM package WHERE id = (SELECT MAX(id) FROM package)";
        return jdbcTemplate.queryForObject(selectSql, new PackageRowMapper());
    }

    /**
     * Сохраняет указанный пакет в репозиторий.
     *
     * @param height      высота пакета.
     * @param widthTop    верхняя ширина пакета.
     * @param widthBottom нижняя ширина пакета.
     * @param packs       массив строк для преобразования и сохранения.
     * @return сохранённый пакет.
     */
    public PackageDto save(int height, int widthTop, int widthBottom, String[] packs) {
        String sql = "INSERT INTO package (height, widthTop, widthBottom, pack) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, height);
            ps.setInt(2, widthTop);
            ps.setInt(3, widthBottom);
            ps.setString(4, stringArrayConverter.convertToDatabaseColumn(packs));
            return ps;
        }, keyHolder);

        Long packageId = extractGeneratedKey(keyHolder);

        String selectSql = "SELECT * FROM package WHERE id = ?";
        return jdbcTemplate.queryForObject(selectSql, new PackageRowMapper(), packageId);
    }

    /**
     * Очищает все пакеты из репозитория.
     */
    public void clear() {
        String sql = "DELETE FROM package";
        jdbcTemplate.update(sql);
    }

    private Long extractGeneratedKey(KeyHolder keyHolder) {
        Map<String, Object> keyMap = keyHolder.getKeys();
        return keyMap != null ? ((Number) keyMap.get("id")).longValue() : null;
    }
}
