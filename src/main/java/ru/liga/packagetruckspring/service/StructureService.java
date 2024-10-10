package ru.liga.packagetruckspring.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.packagetruckspring.dto.StructureDto;
import ru.liga.packagetruckspring.repository.StructureRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис для управления структурами страндартного формата для пакетов из файла и их формами.
 */
@Service
@AllArgsConstructor
public class StructureService {

    private StructureRepository structureRepository;

    /**
     * Возвращает список всех доступных форм пакетов.
     *
     * @return Список всех структур.
     */
    public List<StructureDto> getAllStructures() {
        return structureRepository.findAll();
    }

    /**
     * Возвращает список всех доступных форм пакетов.
     * * @return Список всех структур в формате строке
     */
    public String getAllStructuresString() {
        StringBuilder sb = new StringBuilder();
        structureRepository.findAll().forEach(s ->
                sb.append("Name: ").append(s.getName()).append("\n")
                        .append("From:\n").append(s.getForm().replace(":", "\n")).append("\n")
                        .append("Symbol: ").append(s.getSymbol()).append("\n\n"));
        return String.valueOf(sb);
    }

    /**
     * Возвращает пакет по наименованию.
     *
     * @return структура пакета.
     */
    public StructureDto getStructureByName(String name) {
        return structureRepository.findByName(name).get();
    }

    /**
     * Возвращает пакет по наименованию.
     *
     * @return структура пакета в формате строке
     */
    public String getStructureByNameString(String name) {
        StringBuilder sb = new StringBuilder();
        Optional<StructureDto> structureOpt = structureRepository.findByName(name);
        if (structureOpt.isPresent()) {
            StructureDto s = structureOpt.get();
            sb.append("Name: ").append(s.getName()).append("\n")
                    .append("From:\n").append(s.getForm().replace(":", "\n")).append("\n")
                    .append("Symbol: ").append(s.getSymbol()).append("\n\n");
        }
        return String.valueOf(sb);
    }

    /**
     * Возвращает список всех доступных форм пакетов.
     *
     * @return Список строк, содержащих формы всех структур.
     */
    public List<String> getAllForms() {
        List<String> packages = new ArrayList<>();
        structureRepository.findAll().forEach(s->
                packages.add(s.getForm()));
        return packages;
    }

    /**
     * Возвращает список форм пакетов для заданных имен.
     *
     * @param listName Список имен, разделенных запятыми.
     * @return Список строк, содержащих формы для заданных имен.
     */
    public List<String> getFormsForName(String listName) {
        return Arrays.stream(listName.split(","))
                .map(structureRepository::findByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(StructureDto::getForm)
                .collect(Collectors.toList());
    }

    /**
     * Перезагружает структуры пакетов новыми данными.
     *
     * @param structures Список структур для загрузки.
     * @return Список сохраненных структур
     */
    public List<StructureDto> reloadStructures(List<StructureDto> structures) {
        structureRepository.clear();
        return structureRepository.saveAll(structures);
    }

    /**
     * Добавляет новую структуру пакета с заданным именем и формой.
     *
     * @param name Имя новой структуры.
     * @param form Форма новой структуры.
     */
    public void addNewStructure(String name, String form) {
        StructureDto structure = new StructureDto();
        structure.setName(name);
        structure.setForm(form);
        structure.setSymbol(getFirstCharacter(form));
        structureRepository.save(structure);
    }

    /**
     * Обновляет существующую структуру пакета новыми именем и формой.
     *
     * @param oldName Текущее имя структуры.
     * @param structure Новая структура.
     */
    public StructureDto updateStructure(String oldName, StructureDto structure) {
        return structureRepository.update(oldName, structure).get();
    }

    /**
     * Обновляет существующую структуру пакета новыми именем и формой.
     *
     * @param name Текущее имя структуры.
     * @param newName Новое имя структуры.
     * @param newForm Новая форма структуры.
     */
    public void updateStructure(String name, String newName, String newForm) {
        structureRepository.findByName(name)
                .ifPresent(structure -> {
                    structure.setName(newName);
                    structure.setForm(newForm);
                    structure.setSymbol(getFirstCharacter(newForm));
                    structureRepository.update(structure.getName(), structure);
                });
    }

    /**
     * Обновляет форму существующей структуры пакета.
     *
     * @param name Текущее имя структуры.
     * @param newForm Новая форма структуры.
     */
    public void updateForm(String name, String newForm) {
        structureRepository.findByName(name)
                .ifPresent(structure -> {
                    structure.setForm(newForm);
                    structure.setSymbol(getFirstCharacter(newForm));
                    structureRepository.update(structure.getName(), structure);
                });
    }

    /**
     * Обновляет символ структуры пакета.
     *
     * @param name Текущее имя структуры.
     * @param newSymbol Новый символ структуры.
     */
    public void updateSymbol(String name, String newSymbol) {
        structureRepository.findByName(name)
                .ifPresent(structure -> {
                    structure.setForm(structure.getForm().replace(structure.getSymbol(), newSymbol));
                    structure.setSymbol(newSymbol);
                    structureRepository.update(structure.getName(), structure);
                });
    }

    /**
     * Обновляет имя структуры пакета.
     *
     * @param oldName Текущее имя структуры.
     * @param newName Новое имя структуры.
     */
    public void updateName(String oldName, String newName) {
        structureRepository.findByName(oldName)
                .ifPresent(structure -> {
                    structure.setName(newName);
                    structureRepository.update(structure.getName(), structure);
                });
    }

    /**
     * Возвращает первый символ строки.
     *
     * @param form Строка формы.
     * @return Первый символ строки.
     */
    private String getFirstCharacter(String form) {
        return form.substring(0, 1);
    }

}
