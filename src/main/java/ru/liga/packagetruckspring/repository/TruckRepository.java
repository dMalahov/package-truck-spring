package ru.liga.packagetruckspring.repository;

import org.springframework.stereotype.Repository;
import ru.liga.packagetruckspring.model.Truck;

import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления грузовиками.
 */
@Repository
public class TruckRepository {

    private final List<Truck> trucks = new ArrayList<>();

    /**
     * Возвращает список всех грузовиков.
     *
     * @return список всех грузовиков.
     */
    public List<Truck> findAll() {
        return new ArrayList<>(trucks);
    }

    /**
     * Сохраняет указанный грузовик в репозиторий.
     *
     * @param truck грузовик для сохранения.
     * @return сохранённый грузовик.
     */
    public Truck save(Truck truck) {
        trucks.add(truck);
        return truck;
    }

    /**
     * Очищает все грузовики из репозитория.
     */
    public void clear() {
        this.trucks.clear();
    }

}
