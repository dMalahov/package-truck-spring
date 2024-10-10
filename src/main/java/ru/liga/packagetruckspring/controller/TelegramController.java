package ru.liga.packagetruckspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.packagetruckspring.dto.StructureDto;
import ru.liga.packagetruckspring.exception.CustomException;
import ru.liga.packagetruckspring.enums.DialogMode;
import ru.liga.packagetruckspring.service.FileService;
import ru.liga.packagetruckspring.service.MultiSessionTelegramBot;

import java.io.File;
import java.util.List;

import ru.liga.packagetruckspring.service.PackService;
import ru.liga.packagetruckspring.service.StructureService;

@Component
public class TelegramController extends MultiSessionTelegramBot {

    @Autowired
    private final StructureService structureService;
    @Autowired
    private final FileService fileService;
    @Autowired
    private final PackService packService;
    private DialogMode currentMode = null;
    private final String outputFile = System.getProperty("user.dir") + "/src/main/resources/list.json";
    private int questionCount;
    private String oldName;
    private String name;
    private String form;
    private String symbol;
    private String mode;
    private String size;
    private String format;
    private String listName;

    public TelegramController(@Value("${bot.token}") String botToken,
                              StructureService structureService,
                              FileService fileService,
                              PackService packService) {
        super(botToken);
        this.structureService = structureService;
        this.fileService = fileService;
        this.packService = packService;
    }

    public void onUpdateEventReceived(Update update) {

        if (isMessageDocument()) {

            //command update_packages
            if (currentMode == DialogMode.UPDATE_PACKAGES) {
                Message msg = sendTextMessage("Подождите пару секунд.");
                File file;
                GetFile getFile = getGetFile(update);
                try {
                    String filePath = execute(getFile).getFilePath();
                    file = downloadFile(filePath, new File(outputFile));
                } catch (TelegramApiException e) {
                    throw new CustomException("Не удалось скачать файл из telegram", e);
                }
                List<StructureDto> structure = fileService.readFileData(file);
                structureService.reloadStructures(structure);
                updateTextMessage(msg, "Список пакетов обновился");
                return;
            }
        } else {
            String message = getMessageText();

            //command start
            if (message.equals("/start")) {
                currentMode = DialogMode.MAIN;
                sendTextMessage(loadMessage("main"));

                showMainMenu(
                        " главное меню бота", "/start",
                        "получить список и структуру посылок", "/get_list_packages",
                        "обновить весь список посылок", "/update_packages",
                        "получить посылку по наименованию", "/get_package",
                        "добавить посылку", "/add_package",
                        "обновить значения посылки", "/update_package",
                        "обновить наименование посылки", "/update_name",
                        "обновить форму посылки", "/update_form",
                        "обновить символ посылки", "/update_symbol",
                        "упаковать посылки в грузовики", "/pack_truck"
                );
                return;
            }

            //command list_packages
            if (message.equals("/get_list_packages")) {
                currentMode = DialogMode.LIST_PACKAGES;
                sendTextMessage(structureService.getAllStructuresString());
                return;
            }

            //command update_packages
            if (message.equals("/update_packages")) {
                currentMode = DialogMode.UPDATE_PACKAGES;
                sendTextMessage("Добавьте файл для загрузки в формате json");
                return;
            }

            //command get_package
            if (message.equals("/get_package")) {
                currentMode = DialogMode.GET_PACKAGE;
                sendTextMessage("Укажите наименование посылки");
                return;
            }

            if (currentMode == DialogMode.GET_PACKAGE && !isMessageCommand()) {
                sendTextMessage(structureService.getStructureByNameString(message));
                return;
            }

            //command add_package
            if (message.equals("/add_package")) {
                currentMode = DialogMode.ADD_PACKAGE;
                sendTextMessage("Укажите наименование новой посылки");
                questionCount = 1;
                return;
            }

            if (currentMode == DialogMode.ADD_PACKAGE && !isMessageCommand()) {
                switch (questionCount) {
                    case 1:
                        name = message;
                        sendTextMessage("Укажите форму новой посылки");
                        questionCount = 2;
                        return;
                    case 2:
                        form = message;
                }
                Message msg = sendTextMessage("Добавляем посылку");
                structureService.addNewStructure(name, form);
                updateTextMessage(msg, "Добавление завершено:\n" + structureService.getStructureByNameString(name));
                return;
            }

            //command update_package
            if (message.equals("/update_package")) {
                currentMode = DialogMode.UPDATE_PACKAGE;
                sendTextMessage("Укажите наименование нужной посылки");
                questionCount = 1;
                return;
            }

            if (currentMode == DialogMode.UPDATE_PACKAGE && !isMessageCommand()) {
                switch (questionCount) {
                    case 1:
                        oldName = message;
                        sendTextMessage("Укажите новое имя посылки");
                        questionCount = 2;
                        return;
                    case 2:
                        name = message;
                        sendTextMessage("Укажите новую форму посылки");
                        questionCount = 3;
                        return;
                    case 3:
                        form = message;
                }
                Message msg = sendTextMessage("Обновляем посылку");
                structureService.updateStructure(oldName, name, form);
                updateTextMessage(msg, "Обновление завершено:\n" + structureService.getStructureByNameString(name));
                return;
            }

            //command update_name
            if (message.equals("/update_name")) {
                currentMode = DialogMode.UPDATE_NAME;
                sendTextMessage("Укажите наименование нужной посылки");
                questionCount = 1;
                return;
            }

            if (currentMode == DialogMode.UPDATE_NAME && !isMessageCommand()) {
                switch (questionCount) {
                    case 1:
                        oldName = message;
                        sendTextMessage("Укажите новое имя посылки");
                        questionCount = 2;
                        return;
                    case 2:
                        name = message;
                }
                Message msg = sendTextMessage("Обновляем посылку");
                structureService.updateName(oldName, name);
                updateTextMessage(msg, "Обновление завершено:\n" + structureService.getStructureByNameString(name));
                return;
            }

            //command update_form
            if (message.equals("/update_form")) {
                currentMode = DialogMode.UPDATE_FORM;
                sendTextMessage("Укажите наименование нужной посылки");
                questionCount = 1;
                return;
            }

            if (currentMode == DialogMode.UPDATE_FORM && !isMessageCommand()) {
                switch (questionCount) {
                    case 1:
                        name = message;
                        sendTextMessage("Укажите новую форму посылки в формате: 000:000");
                        questionCount = 2;
                        return;
                    case 2:
                        form = message;
                }
                Message msg = sendTextMessage("Обновляем посылку");
                structureService.updateForm(name, form);
                updateTextMessage(msg, "Обновление завершено:\n" + structureService.getStructureByNameString(name));
                return;
            }

            //command update_symbol
            if (message.equals("/update_symbol")) {
                currentMode = DialogMode.UPDATE_SYMBOL;
                sendTextMessage("Укажите наименование нужной посылки");
                questionCount = 1;
                return;
            }

            if (currentMode == DialogMode.UPDATE_SYMBOL && !isMessageCommand()) {
                switch (questionCount) {
                    case 1:
                        name = message;
                        sendTextMessage("Укажите новый символ посылки");
                        questionCount = 2;
                        return;
                    case 2:
                        symbol = message;
                }
                Message msg = sendTextMessage("Обновляем посылку");
                structureService.updateSymbol(name, symbol);
                updateTextMessage(msg, "Обновление завершено:\n" + structureService.getStructureByNameString(name));
                return;
            }

            //command pack_truck
            if (message.equals("/pack_truck")) {
                currentMode = DialogMode.PACK_TRUCK;
                sendTextMessage("Укажите режим погрузки: s или c");
                questionCount = 1;
                return;
            }

            if (currentMode == DialogMode.PACK_TRUCK && !isMessageCommand()) {
                switch (questionCount) {
                    case 1:
                        mode = message.trim();
                        sendTextMessage("Укажите размеры грузовиков в формате: 6x6,7x7,8x8,6x6,6x6,6x6,6x6");
                        questionCount = 2;
                        return;
                    case 2:
                        size = message.trim();
                        sendTextMessage("Укажите в каком формате получить результат: json или console");
                        questionCount = 3;
                        return;
                    case 3:
                        format = message.trim();
                        sendTextMessage("Укажите наименование нужных посылок в формате: Девятка,Штанка\nЕсли нужны все то напишите слово: Все");
                        questionCount = 4;
                        return;
                    case 4:
                        listName = message.trim();
                }
                Message msg = sendTextMessage("Собираем посылки");
                listName = !listName.trim().equals("Все") ? listName : "";
                String finalTruck = packService.packTruck(mode, listName, size, format);
                updateTextMessage(msg, "Результат:\n" + finalTruck);
                return;
            }

            //default
            sendTextMessage("Выберите режим работы бота");
        }
    }

    private static GetFile getGetFile(Update update) {
        String doc_id = update.getMessage().getDocument().getFileId();
        String doc_name = update.getMessage().getDocument().getFileName();
        String doc_mine = update.getMessage().getDocument().getMimeType();
        Long doc_size = update.getMessage().getDocument().getFileSize();

        Document document = new Document();
        document.setMimeType(doc_mine);
        document.setFileName(doc_name);
        document.setFileSize(doc_size);
        document.setFileId(doc_id);

        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        return getFile;
    }


}
