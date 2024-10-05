package ru.liga.packagetruckspring.model;

import lombok.Getter;

/**
 * Класс, представляющий посылку с заданными параметрами.
 */
@Getter
public class Package {

	protected int height;
	protected int widthTop;
	protected int widthBottom;
	protected  String[] pack;

	/**
	 * Конструктор для создания посылки.
	 *
	 * @param height высота посылки.
	 * @param widthTop ширина верхней части.
	 * @param widthBottom ширина нижней части.
	 * @param pack содержимое посылки.
	 */
	public Package(int height, int widthTop, int widthBottom, String[] pack) {
		this.height = height;
		this.widthTop = widthTop;
		this.widthBottom = widthBottom;
		this.pack = pack;
	}


}
