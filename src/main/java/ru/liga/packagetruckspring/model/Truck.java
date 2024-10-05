package ru.liga.packagetruckspring.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий грузовик, который содержит список пакетов.
 */
@Getter
public class Truck {

	protected List<Package> packages = new ArrayList<>();
	protected int height;
	protected int width;

	/**
	 * Конструктор для создания грузовика.
	 *
	 * @param height высота.
	 * @param width ширина.
	 */
	public Truck(int height, int width) {
		this.height = height;
		this.width = width;
	}

}
