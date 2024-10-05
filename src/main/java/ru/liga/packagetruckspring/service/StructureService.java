package ru.liga.packagetruckspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.liga.packagetruckspring.model.Structure;
import ru.liga.packagetruckspring.repository.StructureRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления структурами страндартного формата для пакетов из файла и их формами.
 */
@Service
public class StructureService {

    @Autowired
    private StructureRepository structureRepository;

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
        List<String> packages = new ArrayList<>();
        String[] names = listName.split(",");
        for (String name : names) {
            Optional<Structure> structureOpt = structureRepository.findByName(name);
            structureOpt.ifPresent(structure -> packages.add(structure.getForm()));
        }
        return packages;
    }

    /**
     * Перезагружает структуры пакетов новыми данными.
     *
     * @param structures Список структур для загрузки.
     */
    public void reloadStructures(List<Structure> structures) {
        structureRepository.clear();
        structureRepository.saveAll(structures);
    }

    /**
     * Добавляет новую структуру пакета с заданным именем и формой.
     *
     * @param name Имя новой структуры.
     * @param form Форма новой структуры.
     */
    public void addNewStructure(String name, String form) {
        Structure structure = new Structure();
        structure.setName(name);
        structure.setForm(form);
        structure.setSymbol(String.valueOf(form.charAt(0)));
        structureRepository.save(structure);
    }

    /**
     * Обновляет существующую структуру пакета новыми именем и формой.
     *
     * @param name Текущее имя структуры.
     * @param newName Новое имя структуры.
     * @param newForm Новая форма структуры.
     */
    public void updateStructure(String name, String newName, String newForm) {
        Optional<Structure> structureOpt = structureRepository.findByName(name);
        if (structureOpt.isPresent()) {
            Structure structure = structureOpt.get();
            structure.setName(newName);
            structure.setForm(newForm);
            structure.setSymbol(String.valueOf(newForm.charAt(0)));
            structureRepository.update(structure.getName(),structure);
        }
    }

    /**
     * Обновляет форму существующей структуры пакета.
     *
     * @param name Текущее имя структуры.
     * @param newForm Новая форма структуры.
     */
    public void updateForm(String name, String newForm) {
        Optional<Structure> structureOpt = structureRepository.findByName(name);
        if (structureOpt.isPresent()) {
            Structure structure = structureOpt.get();
            String newSymbol = String.valueOf(newForm.charAt(0));
            if(!structure.getSymbol().equals(newSymbol)){
                structure.setSymbol(newSymbol);
            }
            structure.setForm(newForm);
            structureRepository.update(structure.getName(),structure);
        }
    }

    /**
     * Обновляет символ структуры пакета.
     *
     * @param name Текущее имя структуры.
     * @param newSymbol Новый символ структуры.
     */
    public void updateSymbol(String name, String newSymbol) {
        Optional<Structure> structureOpt = structureRepository.findByName(name);
        if (structureOpt.isPresent()) {
            Structure structure = structureOpt.get();
            if(!structure.getForm().contains(newSymbol)){
                structure.setForm(structure.getForm().replace(structure.getSymbol(),newSymbol));
            }
            structure.setSymbol(newSymbol);
            structureRepository.update(structure.getName(),structure);
        }
    }

    /**
     * Обновляет имя структуры пакета.
     *
     * @param oldName Текущее имя структуры.
     * @param newName Новое имя структуры.
     */
    public void updateName(String oldName, String newName) {
        Optional<Structure> structureOpt = structureRepository.findByName(oldName);
        if (structureOpt.isPresent()) {
            Structure structure = structureOpt.get();
            structure.setName(newName);
            structureRepository.update(structure.getName(),structure);
        }
    }

    /**
     * Печатает структуру пакета по имени.
     *
     * @param name Имя пакета.
     */
    public void printByName(String name) {
        StringBuilder sb = new StringBuilder();
        Optional<Structure> structureOpt = structureRepository.findByName(name);
        if (structureOpt.isPresent()) {
            Structure s = structureOpt.get();
            sb.append("Name: ").append(s.getName()).append("\n")
                    .append("From:\n").append(s.getForm().replace(":", "\n")).append("\n")
                    .append("Symbol: ").append(s.getSymbol()).append("\n\n");
            System.out.println(sb);
        }
    }

    /**
     * Печатает структуры всех пакетов.
     *
     */
    public void printList() {
        StringBuilder sb = new StringBuilder();
        structureRepository.findAll().forEach(s ->
                sb.append("Name: ").append(s.getName()).append("\n")
                        .append("From:\n").append(s.getForm().replace(":","\n")).append("\n")
                        .append("Symbol: ").append(s.getSymbol()).append("\n\n"));
        System.out.println(sb);
    }

}
