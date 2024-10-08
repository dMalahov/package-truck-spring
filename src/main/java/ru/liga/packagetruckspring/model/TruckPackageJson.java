package ru.liga.packagetruckspring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Модель для работы с грузовиками и посылками в JSON-результата погрузки.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TruckPackageJson {

	private List<Truck> trucks;

	/**
	 * Вложенный класс, описывающий массив посылок.
	 */
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Truck {
		@JsonProperty("packages")
		private List<Order> orders;
	}

	/**
	 * Вложенный класс, описывающий каждую посылку.
	 */
	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Order {
		@JsonProperty("package")
		private String order;
	}

}
