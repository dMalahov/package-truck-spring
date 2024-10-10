package ru.liga.packagetruckspring.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import ru.liga.packagetruckspring.dto.PackageDto;
import ru.liga.packagetruckspring.dto.TruckDto;
import ru.liga.packagetruckspring.repository.PackageRepository;

/**
 * Сервис, отвечающий за сортировку и объединение различных типов посылок.
 */
@Slf4j
@Service
@AllArgsConstructor
public class PackageService {

	private PackageRepository packageRepository;

	/**
	 * Сортирует простые заказы из массива необработанных строк посылок.
	 *
	 * @param unassembledPackagesList список строк, где каждая строка представляет необработанную посылку.
	 * @return список готовых посылок, отсортированных по ширине нижней части.
	 */
	public List<PackageDto> sortSimpleOrders(List<String> unassembledPackagesList) {
		packageRepository.clear();
		log.info("Сортировка посылок простая");

		for (String unassembledPackage : unassembledPackagesList) {
			log.debug("Добавление посылки {}", unassembledPackage);
			String[] packageComponents = unassembledPackage.split(":");
			int firstComponentIndex = 0;
			int lastComponentIndex = packageComponents.length - 1;
			int height = packageComponents.length;
			int widthTop = packageComponents[firstComponentIndex].length();
			int widthBottom = packageComponents[lastComponentIndex].length();

			packageRepository.save(height, widthTop, widthBottom, packageComponents);
		}

		return sortRevertReadyPackage(packageRepository.findAll());
	}

	/**
	 * Сортирует сложные заказы на основе списка грузовиков.
	 *
	 * @param listTrucks список грузовиков, содержащих посылки.
	 * @return список готовых посылок, отсортированных по ширине нижней части.
	 */
	public List<PackageDto> sortComplexOrders(List<TruckDto> listTrucks) {
		packageRepository.clear();
		log.info("Сортировка посылок комлексная");

		listTrucks.forEach(truck -> {
			if (!truck.getPackages().isEmpty() && truck.getPackages().size() > 1) {
				log.debug("Складывание посылок {} и {}", truck.getPackages().get(0), truck.getPackages().get(1));
				mergeTwoPackages(truck.getPackages().get(0), truck.getPackages().get(1));
			} else if (!truck.getPackages().isEmpty()) {
				log.debug("Складывание посылки {}", Arrays.toString(truck.getPackages().get(0).getPack()));
				packageRepository.save(truck.getPackages().get(0));
			}
		});

		return sortRevertReadyPackage(packageRepository.findAll());
	}

	/**
	 * Объединяет две посылки в одну.
	 *
	 * @param package1 первая посылка.
	 * @param package2 вторая посылка.
	 * @return объедененная посылка.
	 */
	private PackageDto mergeTwoPackages(PackageDto package1, PackageDto package2) {
		int height = Math.max(package1.getHeight(), package2.getHeight());
		int widthTop = Math.max(package1.getWidthTop(), package2.getWidthTop());
		int widthBottom = package1.getWidthBottom() + package2.getWidthBottom();
		String[] mergedPack = mergePackages(package1, package2);

		return packageRepository.save(height, widthTop, widthBottom, mergedPack);
	}

	/**
	 * Объединяет две посылки в одну.
	 *
	 * @param pkg1 первая посылка для объединения.
	 * @param pkg2 вторая посылка для объединения.
	 * @return массив строк, представляющий объединённую посылку.
	 */
	private String[] mergePackages(PackageDto pkg1, PackageDto pkg2) {
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
	private List<PackageDto> sortRevertReadyPackage(List<PackageDto> readyPackages) {
		readyPackages.sort(Comparator.comparingInt(PackageDto::getWidthBottom));
		return readyPackages;
	}

}
