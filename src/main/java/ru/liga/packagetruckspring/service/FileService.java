package ru.liga.packagetruckspring.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.liga.packagetruckspring.model.Structure;

import java.io.*;
import java.util.List;

/**
 * Сервис для работы с файлами, включая чтение и запись данных.
 */
@Slf4j
@PropertySource(value = "classpath:application.properties")
@Service
public class FileService {

	@Value("${filePathData}")
	private String filePath;

	@Value("${fileResultJson}")
	private String fileName;

	public FileService() {}

	/**
	 * Читает текстовый файл и возвращает его содержимое в виде массива строк.
	 *
	 * @param fileName имя файла.
	 * @return список строк, содержащих содержимое посылок.
	 */
	public List<Structure> readFileData(String fileName) {
		log.info("Чтение файла "+fileName);
		ObjectMapper objectMapper = new ObjectMapper();
		List<Structure> structures = List.of();
		try {
			structures = objectMapper.readValue(new File(filePath+fileName), new TypeReference<>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return structures;
	}

	/**
	 * Записывает JSON данные в файл result.json.
	 *
	 * @param jsonBody тело JSON для записи.
	 */
	public void writeResultJson(String jsonBody) {
		log.info("Создание json...");
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), "utf-8"));
			writer.write(jsonBody);
			writer.close();
		} catch (IOException e) {
			log.error("Ошибка записи json");
			throw new RuntimeException("Ошибка записи json");
		}
	}


}
