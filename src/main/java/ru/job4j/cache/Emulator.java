package ru.job4j.cache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Emulator {
    private static final int TO_CASH = 1;
    private static final int FROM_CASH = 2;

    private static final String MENU = """
            Введите 1 для загрузки файла в кэш.
            Введите 2 для чтения файла из кэша.
            Введите любое другое число для выхода.
            """;

    public static void main(String[] args) throws IOException {
        System.out.println("Введите путь к кэшируемым файлам:");
        Scanner scanner = new Scanner(System.in);
        DirFileCache dirFileCache = getDir(scanner);

        System.out.println(MENU);
        int choice = Integer.parseInt(scanner.nextLine());
        while (choice == TO_CASH || choice == FROM_CASH) {
            System.out.println("Введите имя файла:");
            if (TO_CASH == choice) {
                addToCash(scanner, dirFileCache);
            } else {
                getFromCash(scanner, dirFileCache);
            }
            System.out.println(MENU);
            choice = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("Работа с файлами завершена.");
    }

    private static DirFileCache getDir(Scanner scanner) {
        String path = check(scanner, new DirFileCache(null));
        return new DirFileCache(path);
    }


    private static void addToCash(Scanner scanner, DirFileCache dirFileCache) {
        String fileName = check(scanner, dirFileCache);
        dirFileCache.put(fileName, dirFileCache.load(fileName));
        System.out.println("Добавлено в кэш: \n" + dirFileCache.get(fileName));
    }

    private static void getFromCash(Scanner scanner, DirFileCache dirFileCache) {
        String fileName = check(scanner, dirFileCache);
        dirFileCache.get(fileName);
        System.out.println("Прочтено из кэша: \n" + dirFileCache.get(fileName));
    }

    private static String check(Scanner scanner, DirFileCache dirFileCache) {
        String path = scanner.nextLine();
        while (!Files.exists(Paths.get(new File(dirFileCache.getCachingDir(), path)
                .toString()))) {
            System.out.println(path + " не существует! Повторите ввод:");
            path = scanner.nextLine();
        }
        return path;
    }
}
