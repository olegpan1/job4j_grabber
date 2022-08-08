package ru.job4j.gc.prof;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Menu {

    private static final int BUBBLE_SORT = 1;
    private static final int INSERT_SORT = 2;
    private static final int MERGE_SORT = 3;

    private static void menu() {
        System.out.println("Выберите тип сортировки:");
        System.out.println("1.Сортировка пузырьком.");
        System.out.println("2.Сортировка вставками.");
        System.out.println("3.Сортировка слиянием");
        System.out.println("4.Выход");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RandomArray randomArray = new RandomArray(new Random());
        System.out.println("Введите размер массива: 250 000");
        int arraySize = scanner.nextInt();
        randomArray.insert(arraySize);
        Date start;
        menu();
        int menuNumber = scanner.nextInt();
        while (menuNumber != 4) {
            if (menuNumber == BUBBLE_SORT) {
                BubbleSort bubbleSort = new BubbleSort();
                start = new Date();
                System.out.println(bubbleSort.sort(randomArray)
                        + "\nСортировка пузырьком в секундах: " + (new Date().getTime() - start.getTime()) / 1000);
            }
            if (menuNumber == INSERT_SORT) {
                InsertSort insertSort = new InsertSort();
                start = new Date();
                System.out.println(insertSort.sort(randomArray)
                        + "\nСортировка вставками в секундах: " + (new Date().getTime() - start.getTime()) / 1000);
            }
            if (menuNumber == MERGE_SORT) {
                MergeSort mergeSort = new MergeSort();
                start = new Date();
                System.out.println(mergeSort.sort(randomArray)
                        + "\nСортировка слиянием в секундах: " + (new Date().getTime() - start.getTime()) / 1000);
            }
            menuNumber = scanner.nextInt();
        }
        System.out.println("Завершение работы.");
    }
}