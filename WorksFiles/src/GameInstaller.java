import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameInstaller {

    public static void main(String[] args) {
        // Путь к папке Games
        String basePath = "C:/Users/Desktop/JAVA/WorksFiles/Games";

        StringBuilder log = new StringBuilder();
        timeLog(log, "Начало установки игры");

        // Создание директорий
        File srcDir = createDirectory(basePath + "/src", log);
        File resDir = createDirectory(basePath + "/res", log);
        File savegamesDir = createDirectory(basePath + "/savegames", log);
        File tempDir = createDirectory(basePath + "/temp", log);

        // Создание поддиректорий в src
        File mainDir = createDirectory(srcDir.getPath() + "/main", log);
        File testDir = createDirectory(srcDir.getPath() + "/test", log);

        // Создание файлов в main
        createFile(mainDir.getPath() + "/Main.java", log);
        createFile(mainDir.getPath() + "/Utils.java", log);

        // Создание поддиректорий в res
        createDirectory(resDir.getPath() + "/drawables", log);
        createDirectory(resDir.getPath() + "/vectors", log);
        createDirectory(resDir.getPath() + "/icons", log);

        // Создание файла temp.txt
        File tempFile = createFile(tempDir.getPath() + "/temp.txt", log);

        timeLog(log, "Завершение установки игры");

        // Запись лога в temp.txt
        if (tempFile != null) {
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(log.toString());
                timeLog(log, "Лог успешно записан в temp.txt");
            } catch (IOException e) {
                timeLog(log, "Ошибка при записи лога в temp.txt: " + e.getMessage());
            }
        }
    }

    private static File createDirectory(String path, StringBuilder log) {
        File dir = new File(path);
        if (dir.mkdir()) {
            timeLog(log, "Директория создана: " + path);
        } else {
            timeLog(log, "Не удалось создать директорию: " + path);
        }
        return dir;
    }

    private static File createFile(String path, StringBuilder log) {
        File file = new File(path);
        try {
            if (file.createNewFile()) {
                timeLog(log, "Файл создан: " + path);
            } else {
                timeLog(log, "Не удалось создать файл: " + path);
            }
        } catch (IOException e) {
            timeLog(log, "Ошибка при создании файла " + path + ": " + e.getMessage());
            return null;
        }
        return file;
    }

    private static void timeLog(StringBuilder log, String message) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.append("[").append(time).append("] ").append(message).append("\n");
    }
}