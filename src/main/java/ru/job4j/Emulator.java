package ru.job4j;

import ru.job4j.cache.DirFileCache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Emulator {
    private static final int TO_CASH = 1;
    private static final int FROM_CASH = 2;

    private static final String ENTER_FILE_NAME = "Введите имя файла:";
    private static final String ENTER_DIR_PATH = "Введите путь к кэшируемым файлам:";
    private static final String THE_END = "Работа с файлами завершена.";
    private static final String READ_FROM_CASH = "В кэше:" + System.lineSeparator();
    private static final String DOES_NOT_EXIST = " не существует! Повторите ввод:";
    private static final String MENU = """
            Введите 1 для загрузки файла в кэш.
            Введите 2 для чтения файла из кэша.
            Введите любое другое число для выхода.
            """;

    public static void main(String[] args) throws IOException {
        System.out.println(ENTER_DIR_PATH);
        Scanner scanner = new Scanner(System.in);
        DirFileCache dirFileCache = getDir(scanner);

        System.out.println(MENU);
        int choice = Integer.parseInt(scanner.nextLine());
        while (choice == TO_CASH || choice == FROM_CASH) {
            System.out.println(ENTER_FILE_NAME);
            getFromCash(scanner, dirFileCache);
            System.out.println(MENU);
            choice = Integer.parseInt(scanner.nextLine());
        }
        System.out.println(THE_END);
    }

    private static DirFileCache getDir(Scanner scanner) {
        String path = check(scanner, new DirFileCache(""));
        return new DirFileCache(path);
    }

    private static void getFromCash(Scanner scanner, DirFileCache dirFileCache) {
        String fileName = check(scanner, dirFileCache);
        System.out.println(READ_FROM_CASH + dirFileCache.get(fileName));
    }

    private static String check(Scanner scanner, DirFileCache dirFileCache) {
        String path = scanner.nextLine();
        while (!Files.exists(Path.of(dirFileCache.getCachingDir(), path))) {
            System.out.println(path + DOES_NOT_EXIST);
            path = scanner.nextLine();
        }
        return path;
    }
}
