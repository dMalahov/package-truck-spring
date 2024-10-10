package ru.liga.packagetruckspring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.liga.packagetruckspring.dto.StructureDto;
import ru.liga.packagetruckspring.exception.CustomException;
import ru.liga.packagetruckspring.model.Structure;
import ru.liga.packagetruckspring.service.FileService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class FileServiceTest {

    private final String testFile = System.getProperty("user.dir") + "\\src\\test\\resources\\test.json";
    private final String testFileError = System.getProperty("user.dir") + "\\src\\test\\resources\\error.json";
    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileService = new FileService(new ObjectMapper());
    }

    @Test
    void testReadFileJson() {
        List<StructureDto> list = fileService.readFileData(testFile);
        String name = "";
        String form = "";
        String symbol = "";
        for (StructureDto structure : list) {
            name = structure.getName();
            form = structure.getForm();
            symbol = structure.getSymbol();
        }
        assertThat(name.equals("Пять")).isTrue();
        assertThat(form.equals("55555:55555")).isTrue();
        assertThat(symbol.equals("5")).isTrue();
    }

    @Test
    void testFailReadFileJson() {
        assertThatThrownBy(() -> fileService.readFileData(testFileError))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("Ошибка чтения файла");
    }


}
