package ru.liga.packagetruckspring.model;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

/**
 * Класс, представляющий посылку с заданными параметрами.
 */
@Getter
@RequiredArgsConstructor
public class Package {

	private final int height;
	private final int widthTop;
	private final int widthBottom;
	private final String[] pack;


}
