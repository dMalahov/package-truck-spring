package ru.liga.packagetruckspring.repository;

import org.springframework.stereotype.Repository;
import ru.liga.packagetruckspring.model.Package;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для хранения и управления пакетами.
 */
@Repository
public class PackageRepository {

    private final List<Package> packages = new ArrayList<>();

    /**
     * Возвращает список всех пакетов.
     *
     * @return список всех пакетов.
     */
    public List<Package> findAll() {
        return new ArrayList<>(packages);
    }

    /**
     * Сохраняет указанный пакет в репозиторий.
     *
     * @param pack пакет для сохранения.
     * @return сохранённый пакет.
     */
    public Package save(Package pack) {
        packages.add(pack);
        return pack;
    }

    /**
     * Очищает все пакеты из репозитория.
     */
    public void clear() {
        this.packages.clear();
    }

}
