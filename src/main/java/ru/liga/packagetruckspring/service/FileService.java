package ru.liga.packagetruckspring.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.liga.packagetruckspring.dto.StructureDto;
import ru.liga.packagetruckspring.exception.CustomException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Сервис для работы с файлами, включая чтение и запись данных.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

	@Value("${file.json.result}")
	private String filResultName;
	private final ObjectMapper objectMapper;

	/**
	 * Читает текстовый файл и возвращает его содержимое в виде массива строк.
	 *
	 * @param file файл.
	 * @return список строк, содержащих содержимое посылок.
	 */
	public List<StructureDto> readFileData(File file) {
		log.info("Чтение файла");
		List<StructureDto> structures;
		try {
			structures = objectMapper.readValue(file, new TypeReference<>() {
			});
		} catch (IOException e) {
			log.error("Ошибка чтения файла", e);
			throw new CustomException("Ошибка чтения файла", e);
		}
		return structures;
	}

	/**
	 * Читает текстовый файл и возвращает его содержимое в виде массива строк.
	 *
	 * @param filePath путь до файла.
	 * @return список строк, содержащих содержимое посылок.
	 */
	public List<StructureDto> readFileData(String filePath) {
		log.info("Чтение файла {}", filePath);
		List<StructureDto> structures;
		try {
			structures = objectMapper.readValue(new File(filePath), new TypeReference<>() {
			});
		} catch (IOException e) {
			log.error("Ошибка чтения файла {}", filePath, e);
			throw new CustomException("Ошибка чтения файла", e);
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
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(filResultName), StandardCharsets.UTF_8))) {
			writer.write(jsonBody);
		} catch (IOException e) {
			log.error("Ошибка записи json");
			throw new CustomException("Ошибка записи json", e);
		}
	}
}
