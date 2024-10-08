package ru.liga.packagetruckspring.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.liga.packagetruckspring.model.Package;
import ru.liga.packagetruckspring.model.Structure;
import ru.liga.packagetruckspring.model.Truck;
import ru.liga.packagetruckspring.repository.StructureRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для печати
 */
@Slf4j
@Service
@AllArgsConstructor
public class PrintService {

    private StructureRepository structureRepository;

    /**
     * Печатает структуру пакета по имени.
     *
     * @param name Имя пакета.
     */
    public void printStructureByName(String name) {
        StringBuilder sb = new StringBuilder();
        Optional<Structure> structureOpt = structureRepository.findByName(name);
        if (structureOpt.isPresent()) {
            Structure s = structureOpt.get();
            sb.append("Name: ").append(s.getName()).append("\n")
                    .append("From:\n").append(s.getForm().replace(":", "\n")).append("\n")
                    .append("Symbol: ").append(s.getSymbol()).append("\n\n");
            System.out.println(sb);
        }
    }

    /**
     * Печатает структуры всех пакетов.
     */
    public void printStructureList() {
        StringBuilder sb = new StringBuilder();
        structureRepository.findAll().forEach(s ->
                sb.append("Name: ").append(s.getName()).append("\n")
                        .append("From:\n").append(s.getForm().replace(":", "\n")).append("\n")
                        .append("Symbol: ").append(s.getSymbol()).append("\n\n"));
        System.out.println(sb);
    }

    /**
     * Печатает детали предоставленного списка грузовиков в консоли.
     *
     * @param truckArrayList Список объектов Truck для печати. Если null, будет зафиксирована ошибка.
     */
    public void printTrucks(List<Truck> truckArrayList) {
        if (truckArrayList != null) {
            log.info("Печать в консоль...");
            for (Truck truckPrint : truckArrayList) {
                if (!truckPrint.getPackages().isEmpty()) {
                    StringBuilder truckString = new StringBuilder();
                    int width = truckPrint.getWidth();
                    int freeSpaceHeight = Math.max(0, getFreeSpaceH(truckPrint.getPackages(), truckPrint.getHeight()));
                    String line = "+" + " ".repeat(width) + "+\n";
                    truckString.append(line.repeat(freeSpaceHeight));
                    for (Package aPackage : truckPrint.getPackages()) {
                        for (String packString : aPackage.getPack()) {
                            String[] numberList = packString.split("\n");
                            for (String number : numberList) {
                                truckString.append("+");
                                truckString.append(padRight(number, truckPrint.getWidth()));
                                truckString.append("+").append(System.lineSeparator());
                            }
                        }
                    }
                    for (int i = 0; i <= truckPrint.getWidth(); i++) {
                        truckString.append("+");
                    }
                    truckString.append("+").append(System.lineSeparator()).append("\r");
                    System.out.println(truckString);
                }
            }
        } else {
            log.error("Что то пошло не так - Список грузовиков пуст");
        }
    }

    /**
     * Дополняет данную строку справа до заданной длины.
     *
     * @param s Строка для дополнения.
     * @param n Общая длина после дополнения.
     * @return Дополненная строка.
     */
    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    /**
     * Вычисляет доступное вертикальное пространство в грузовике на основе его упаковок.
     *
     * @param packages Список объектов Package для расчета пространства по высоте.
     * @return Оставшееся пространство по высоте в грузовике.
     */
    private int getFreeSpaceH(List<Package> packages, int height) {
        int sum = 0;
        for (Package aPackage : packages) {
            int i = aPackage.getHeight();
            sum += i;
        }
        return height - sum;
    }
}
