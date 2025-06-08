import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        // Создаем три состояния персонажа
        GameProgress progress1 = new GameProgress(100, 3, 1, 0.0);
        GameProgress progress2 = new GameProgress(85, 5, 2, 150.5);
        GameProgress progress3 = new GameProgress(30, 2, 5, 1200.8);

        // Абсолютный путь к папке сохранения
        String savePath = "C:/Users/Desktop/JAVA/Serializable/GunRunner/savegames";

        // Сохраняем прогресс в файлы
        String savePath1 = savePath + "/save1.dat";
        String savePath2 = savePath + "/save2.dat";
        String savePath3 = savePath + "/save3.dat";

        saveGame(savePath1, progress1);
        saveGame(savePath2, progress2);
        saveGame(savePath3, progress3);

        // Упаковываем файлы в архив
        String zipPath = savePath + "/saves.zip";
        zipFiles(zipPath, List.of(savePath1, savePath2, savePath3));

        // Удаляем исходные файлы
        deleteFiles(List.of(savePath1, savePath2, savePath3));

        // Разархивируем архив
        openZip(zipPath, savePath);

        // Десериализируем и выведем в консоль последнее сохранение
        String saveToOpen = savePath + "/save1.dat";
        GameProgress loadedProgress = openProgress(saveToOpen);

        if (loadedProgress != null) {
            System.out.println("\nЗагружено последнее сохранение: " + loadedProgress);
        }
    }

    public static void saveGame(String filePath, GameProgress progress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(progress);
            System.out.println("Сохранено успешно: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении: " + filePath);
            e.printStackTrace();
        }
    }

    public static void zipFiles(String zipPath, List<String> filesToZip) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String filePath : filesToZip) {
                File file = new File(filePath);
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    System.out.println("Файл добавлен в архив: " + filePath);
                } catch (IOException e) {
                    System.err.println("Ошибка при добавлении файла в архив: " + filePath);
                    e.printStackTrace();
                }
            }
            System.out.println("Архив создан: " + zipPath);
        } catch (IOException e) {
            System.err.println("Ошибка при создании архива: " + zipPath);
            e.printStackTrace();
        }
    }

    public static void deleteFiles(List<String> filesToDelete) {
        for (String filePath : filesToDelete) {
            File file = new File(filePath);
            if (file.delete()) {
                System.out.println("Файл удален: " + filePath);
            } else {
                System.err.println("Не удалось удалить файл: " + filePath);
            }
        }
    }

    // Разархивация
    public static void openZip(String zipPath, String unzipFolder) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String filePath = unzipFolder + File.separator + entry.getName();
                File newFile = new File(filePath);

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                    System.out.println("Файл распакован: " + filePath);
                }
                zis.closeEntry();
            }
            System.out.println("Архив полностью распакован в: " + unzipFolder);
        } catch (IOException e) {
            System.err.println("Ошибка при распаковке архива: " + zipPath);
            e.printStackTrace();
        }
    }

    // Десериализация
    public static GameProgress openProgress(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            GameProgress progress = (GameProgress) ois.readObject();
            System.out.println("Сохранение успешно загружено из: " + filePath);
            return progress;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при загрузке сохранения из: " + filePath);
            e.printStackTrace();
            return null;
        }
    }
}

