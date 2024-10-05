package ru.liga.packagetruckspring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.packagetruckspring.model.Truck;
import ru.liga.packagetruckspring.model.Package;
import ru.liga.packagetruckspring.model.TruckPackageJson;
import ru.liga.packagetruckspring.repository.TruckRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления грузовиками.
 * Позволяет создавать грузовики различной сложности в зависимости от параметров пакетов.
 */
@Slf4j
@Service
public class TruckService {

	@Autowired
	private TruckRepository truckRepository;

	/**
	 * Простая загрузка грузовиков.
	 *
	 * @param params параметры грузовик доступных для погрузки
	 */
	public void createTruck(String params) {
		truckRepository.clear();
		String[] truckParams = params.split(",");
		for (String truckParam : truckParams) {
			String[] truckHeightWidth = truckParam.split("x");
			int height = Integer.parseInt(truckHeightWidth[0]);
			int width = Integer.parseInt(truckHeightWidth[1]);
			truckRepository.save(new Truck(height,width));
		}
	}

	/**
	 * Простая загрузка грузовиков.
	 *
	 * @param readyPackages список готовых пакетов для загрузки
	 * @return список созданных грузовиков
	 */
	public List<Truck> createSimplePack(List<Package> readyPackages) {
		List<Truck> trucks = truckRepository.findAll();
		int truckIndex = 0;

		for (Package readyPack : readyPackages) {
			while (truckIndex < trucks.size()) {
				Truck currentTruck = trucks.get(truckIndex);
				if (getFreeSpaceW(currentTruck.getPackages(),currentTruck.getWidth()) >= readyPack.getWidthBottom()) {
					currentTruck.getPackages().add(readyPack);
					truckIndex++;
					break;
				}
			}
			checkTrucksCount(readyPackages,trucks);
		}
		return truckRepository.findAll();
	}

	/**
	 * Загружает грузовики, учитывая ширину пакетов.
	 *
	 * @param readyPackages список готовых пакетов для загрузки
	 * @return список созданных грузовиков
	 */
	public List<Truck> createComplexPackForWidth(List<Package> readyPackages) {
		List<Truck> trucks = truckRepository.findAll();
		List<Truck> newTrucks = new ArrayList<>();
		int truckIndex = 0;

		for (Package readyPack : readyPackages) {
			while (truckIndex < trucks.size()) {
				Truck currentTruck = trucks.get(truckIndex);
				if (getFreeSpaceW(currentTruck.getPackages(), currentTruck.getWidth()) >= readyPack.getWidthBottom()) {
					currentTruck.getPackages().add(readyPack);
					break;
				} else {
					truckIndex++;
				}
			}
		}

		for (Truck truck : trucks) {
			if (!truck.getPackages().isEmpty()) {
				newTrucks.add(truck);
			}
		}
		return newTrucks;
	}

	/**
	 * Загружает грузовики, учитывая высоту пакетов.
	 *
	 * @param readyPackages список готовых пакетов для загрузки
	 * @return список созданных грузовиков
	 */
	public List<Truck> createComplexPackFoHeight(List<Package> readyPackages) {
		List<Truck> trucks = truckRepository.findAll();
		int truckIndex = 0;

		for (Package readyPack : readyPackages) {
			while (truckIndex < trucks.size()) {
				Truck currentTruck = trucks.get(truckIndex);
				if (getFreeSpaceH(currentTruck.getPackages(), currentTruck.getHeight()) >= readyPack.getHeight()) {
					currentTruck.getPackages().add(readyPack);
					break;
				} else {
					truckIndex++;
				}
			}
			checkTrucksCount(truckIndex,trucks.size());
		}
		return trucks;
	}

	/**
	 * Вычисляет доступное вертикальное пространство в грузовике на основе его упаковок.
	 *
	 * @param packages Список объектов Package для расчета пространства по высоте.
	 * @return Оставшееся пространство по высоте в грузовике.
	 */
	private int getFreeSpaceH(List<Package> packages,int height) {
		int sum = 0;
		for (Package aPackage : packages) {
			int i = aPackage.getHeight();
			sum += i;
		}
		return height-sum;
	}

	/**
	 * Вычисляет доступное пространство по ширине в грузовике на основе его упаковок.
	 *
	 * @param packages Список объектов Package для расчета пространства по ширине.
	 * @return Оставшееся пространство по ширине в грузовике.
	 */
	private int getFreeSpaceW(List<Package> packages,int width) {
		int sum = 0;
		for (Package aPackage : packages) {
			int i = aPackage.getWidthBottom();
			sum += i;
		}
		return width-sum;
	}

	/**
	 * Проверяет, достаточно ли грузовиков для загрузки по количеству грузовиков.
	 *
	 * @param packageList Список пакетов для погрузки.
	 * @param trucksList Список грузовиков для погрузки.
	 * @throws RuntimeException если недостаточно грузовиков для загрузки.
	 */
	private void checkTrucksCount(List<Package> packageList, List<Truck> trucksList) {
		if(packageList.size() > trucksList.size()) {
			log.error("Недостаточно грузовиков для погрузки простым способом");
		}
	}

	/**
	 * Проверяет, достаточно ли грузовиков для загрузки по количеству грузовиков.
	 *
	 * @param truckIndex Индекс использованного грузовика.
	 * @param trucksCount Кол-во доступных грузовиков.
	 * @throws RuntimeException если недостаточно грузовиков для загрузки.
	 */
	private void checkTrucksCount(int truckIndex, int trucksCount) {
		if(truckIndex >= trucksCount) {
			log.error("Недостаточно грузовиков для погрузки комплексной способом");
		}
	}

	/**
	 * Печатает детали предоставленного списка грузовиков в консоли.
	 *
	 * @param truckArrayList Список объектов Truck для печати. Если null, будет зафиксирована ошибка.
	 */
	public void printTruckInConsole(List<Truck> truckArrayList) {
		if(truckArrayList != null) {
			log.info("Печать в консоль...");
			for (Truck truckPrint : truckArrayList) {
				if(!truckPrint.getPackages().isEmpty()) {
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
	 * Генерирует JSON-репрезентацию предоставленного списка грузовиков.
	 *
	 * @param truckArrayList Список объектов Truck для преобразования в JSON. Если null, возвращается пустой JSON.
	 * @return Строка JSON, представляющая грузовики и их упаковки.
	 */
	public String createJsonForTruck(List<Truck> truckArrayList) {
		log.info("Формирование json...");
		String trucks;
		ArrayList<TruckPackageJson.Truck> truck = new ArrayList<>();
		if(truckArrayList != null) {
			for (int i = 0; i <= truckArrayList.size()-1; i++) {
				ArrayList<TruckPackageJson.Order> packages = new ArrayList<>();
				if(!truckArrayList.get(i).getPackages().isEmpty()) {
					for (int j = 0; j <= truckArrayList.get(i).getPackages().size() - 1; j++) {
						String[] packing = truckArrayList.get(i).getPackages().get(j).getPack();
						StringBuilder pack = new StringBuilder();
						for (String s : packing) {
							pack.append(s.replace("\n", ":")).append(":");
						}
						String pack2 = pack.toString().replace("::", ":");
						packages.add(TruckPackageJson.Order.builder().order(pack2.substring(0, pack2.length() - 1)).build());
					}
					truck.add(TruckPackageJson.Truck.builder().orders(packages).build());
				}
			}
		}
		try {
			trucks = new ObjectMapper().writeValueAsString(TruckPackageJson.builder().trucks(truck).build());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		log.info("Полученный json: "+trucks);
		return trucks;
	}


}
