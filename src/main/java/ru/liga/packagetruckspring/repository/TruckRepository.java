package ru.liga.packagetruckspring.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.liga.packagetruckspring.dto.PackageDto;
import ru.liga.packagetruckspring.dto.TruckDto;
import ru.liga.packagetruckspring.mapper.PackageRowMapper;
import ru.liga.packagetruckspring.mapper.StringArrayConverter;
import ru.liga.packagetruckspring.mapper.TruckRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class TruckRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StringArrayConverter stringArrayConverter;

    /**
     * Возвращает список всех грузовиков.
     *
     * @return список всех грузовиков.
     */
    public List<TruckDto> findAll() {
        String sql = "SELECT * FROM truck";
        List<TruckDto> trucks = jdbcTemplate.query(sql, new TruckRowMapper());

        for (TruckDto truck : trucks) {
            truck.setPackages(findPackagesByTruckId(truck.getId()));
        }

        return trucks;
    }

    private List<PackageDto> findPackagesByTruckId(Long truckId) {
        String sql = "SELECT * FROM package WHERE truck_id = ?";
        return jdbcTemplate.query(sql, new PackageRowMapper(), truckId);
    }

    /**
     * Сохраняет указанный грузовик в репозиторий.
     *
     * @param truckDto грузовик для сохранения.
     * @return сохранённый грузовик.
     */
    public TruckDto save(TruckDto truckDto) {
        String sqlTruck = "INSERT INTO truck (height, width) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlTruck, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, truckDto.getHeight());
            ps.setInt(2, truckDto.getWidth());
            return ps;
        }, keyHolder);

        Long truckId = extractGeneratedKey(keyHolder, "id");
        truckDto.setId(truckId);

        for (PackageDto packageDto : truckDto.getPackages()) {
            savePackage(packageDto, truckId);
        }

        return truckDto;
    }

    /**
     * Сохраняет указанный грузовик с параметрами height и width в репозиторий.
     *
     * @param height высота грузовика.
     * @param width  ширина грузовика.
     * @return сохранённый грузовик.
     */
    public TruckDto save(int height, int width) {
        String sqlTruck = "INSERT INTO truck (height, width) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlTruck, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, height);
            ps.setInt(2, width);
            return ps;
        }, keyHolder);

        Long truckId = extractGeneratedKey(keyHolder, "id");

        TruckDto truckDto = new TruckDto();
        truckDto.setId(truckId);
        truckDto.setHeight(height);
        truckDto.setWidth(width);
        truckDto.setPackages(new ArrayList<>()); // Инициализируем пустой список пакетов

        return truckDto;
    }

    /**
     * Сохраняет указанный пакет в репозиторий.
     *
     * @param packageDto пакет для сохранения.
     * @param truckId    идентификатор грузовика.
     * @return сохранённый пакет.
     */
    public PackageDto savePackage(PackageDto packageDto, Long truckId) {
        String sql = "INSERT INTO package (height, widthTop, widthBottom, pack, truck_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, packageDto.getHeight());
            ps.setInt(2, packageDto.getWidthTop());
            ps.setInt(3, packageDto.getWidthBottom());
            ps.setString(4, stringArrayConverter.convertToDatabaseColumn(packageDto.getPack()));
            ps.setLong(5, truckId);
            return ps;
        }, keyHolder);

        Long packageId = extractGeneratedKey(keyHolder, "id");
        packageDto.setId(packageId);

        return packageDto;
    }

    /**
     * Очищает все грузовики из репозитория.
     */
    public void clear() {
        String sqlDeletePackages = "DELETE FROM package";
        jdbcTemplate.update(sqlDeletePackages);

        String sqlDeleteTrucks = "DELETE FROM truck";
        jdbcTemplate.update(sqlDeleteTrucks);
    }

    /**
     * Извлекает сгенерированный ключ из KeyHolder.
     *
     * @param keyHolder KeyHolder с информацией о сгенерированных ключах.
     * @param keyName   Имя ключа, который нужно извлечь.
     * @return Значение сгенерированного ключа.
     */
    private Long extractGeneratedKey(KeyHolder keyHolder, String keyName) {
        Map<String, Object> keyMap = keyHolder.getKeys();
        return keyMap != null ? ((Number) keyMap.get(keyName)).longValue() : null;
    }

}
