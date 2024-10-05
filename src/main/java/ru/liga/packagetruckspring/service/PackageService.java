package ru.liga.packagetruckspring.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.packagetruckspring.model.Truck;
import ru.liga.packagetruckspring.model.Package;
import ru.liga.packagetruckspring.repository.PackageRepository;

/**
 * Сервис, отвечающий за сортировку и объединение различных типов посылок.
 */
@Slf4j
@Service
public class PackageService {

	@Autowired
	private PackageRepository packageRepository;

	/**
	 * Сортирует простые заказы из массива необработанных строк посылок.
	 *
	 * @param unassembledPackagesList список строк, где каждая строка представляет
	 *                               необработанную посылку.
	 * @return список готовых посылок, отсортированных по ширине нижней части.
	 */
	public List<Package> sortSimpleOrders(List<String> unassembledPackagesList) {
		packageRepository.clear();
		log.info("Сортировка посылок простая");
		for (String unassembledPackages : unassembledPackagesList) {
			log.debug("Добавление посылки "+unassembledPackages);
			String[] unassembledPackage = unassembledPackages.split(":");
			int h = unassembledPackage.length;
			int wTop = unassembledPackage[0].length();
			int wBotton = unassembledPackage[h-1].length();
			packageRepository.save(new Package(h,wTop,wBotton,unassembledPackage));
		}
		return sortRevertReadyPackage(packageRepository.findAll());
	}

	/**
	 * Сортирует сложные заказы на основе списка грузовиков.
	 *
	 * @param listTrucks список грузовиков, содержащих посылки.
	 * @return список готовых посылок, отсортированных по ширине нижней части.
	 */
	public List<Package> sortComplexOrders(List<Truck> listTrucks) {
		packageRepository.clear();
		log.info("Сортировка посылок комлексная");
		for (Truck truck : listTrucks) {
			if(truck.getPackages().size()>1) {
				log.debug("Складывание посылок "+truck.getPackages().get(0)+" и "+truck.getPackages().get(1));
				packageRepository.save(new Package(
						Math.max(truck.getPackages().get(0).getHeight(), truck.getPackages().get(1).getHeight()),
						Math.max(truck.getPackages().get(0).getWidthTop(), truck.getPackages().get(1).getWidthTop()),
						truck.getPackages().get(0).getWidthBottom() + truck.getPackages().get(1).getWidthBottom(),
						mergePackages(truck.getPackages().get(0), truck.getPackages().get(1))));
			} else {
				log.debug("Складывание посылки "+ Arrays.toString(truck.getPackages().get(0).getPack()));
				packageRepository.save(new Package(
						truck.getPackages().get(0).getHeight(),
						truck.getPackages().get(0).getWidthTop(),
						truck.getPackages().get(0).getWidthBottom(),
						truck.getPackages().get(0).getPack()));
			}
		}
		return sortRevertReadyPackage(packageRepository.findAll());
	}

	/**
	 * Объединяет две посылки в одну.
	 *
	 * @param pkg1 первая посылка для объединения.
	 * @param pkg2 вторая посылка для объединения.
	 * @return массив строк, представляющий объединённую посылку.
	 */
	private String[] mergePackages(Package pkg1, Package pkg2) {
		// Получаем пакеты из обоих объектов Package
		String[] pack1 = pkg1.getPack();
		String[] pack2 = pkg2.getPack();

		// Определяем максимальную длину, чтобы избежать выхода за границы
		int maxLength = Math.max(pack1.length, pack2.length);
		List<String> mergedList = new ArrayList<>();

		for (int i = 0; i < maxLength; i++) {
			StringBuilder sb = new StringBuilder();
			if (i < pack2.length) {
				sb.append(pack2[i]);
			}
			if (i < pack1.length) {
				sb.append(pack1[i]);
			}
			mergedList.add(sb.toString());
		}

		// Преобразуем список в массив для возврата
		String[] mergedArray = new String[mergedList.size()];
		int j = 0;
		for (int i = mergedList.size()-1; i >= 0; i--) {
			mergedArray[j] = mergedList.get(i);
			j++;
		}
		return mergedArray;
	}

	/**
	 * Сортирует список посылок по ширине нижней части в порядке возрастания.
	 *
	 * @param readyPackages список посылок для сортировки.
	 * @return отсортированный список посылок.
	 */
	private List<Package> sortRevertReadyPackage(List<Package> readyPackages) {
		readyPackages.sort(Comparator.comparingInt(Package::getWidthBottom));
		return readyPackages;
	}

}
