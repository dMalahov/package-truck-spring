package ru.liga.packagetruckspring.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий грузовик, который содержит список пакетов.
 */
@Getter
@RequiredArgsConstructor
public class Truck {

	private List<Package> packages = new ArrayList<>();
	private final int height;
	private final int width;

}
