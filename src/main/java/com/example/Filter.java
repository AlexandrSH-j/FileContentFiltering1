package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Alexander Shishkin
 */

@SuppressWarnings("ALL")
public class Filter {

    private static String outputPath = ".";
    private static String prefix = "";
    private static boolean appendMode = false;
    private static boolean shortStats = false;
    private static boolean fullStats = false;

    private static int integerCount = 0;
    private static int floatCount = 0;
    private static int stringCount = 0;

    private static final List<Integer> integers = new ArrayList<>();
    private static final List<Float> floats = new ArrayList<>();
    private static final List<String> strings = new ArrayList<>();

    public static void main(String[] args) {
        parseArguments(args);
        processInput();
        writeFiles();
        printStatistics();
    }

    private static void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (i + 1 < args.length) {
                        outputPath = args[i + 1];
                    }
                    break;
                case "-p":
                    if (i + 1 < args.length) {
                        prefix = args[i + 1];
                    }
                    break;
                case "-a":
                    appendMode = true;
                    break;
                case "-s":
                    shortStats = true;
                    break;
                case "-f":
                    fullStats = true;
                    break;
            }
        }
    }

    private static void processInput() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            try {
                int intValue = Integer.parseInt(line);
                integers.add(intValue);
                integerCount++;
            } catch (NumberFormatException e1) {
                try {
                    float floatValue = Float.parseFloat(line);
                    floats.add(floatValue);
                    floatCount++;
                } catch (NumberFormatException e2) {
                    strings.add(line);
                    stringCount++;
                }
            }
        }
        scanner.close();
    }

    private static void writeFiles() {
        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        writeFile(prefix + "integers.txt", integers);
        writeFile(prefix + "floats.txt", floats);
        writeFile(prefix + "strings.txt", strings);
    }

    private static void writeFile(String fileName, List<?> data) {
        File file = new File(outputPath, fileName);
        try (FileWriter writer = new FileWriter(file, appendMode)) {
            for (Object item : data) {
                writer.write(item.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи файла: " + e.getMessage());
        }
    }

    private static void printStatistics() {
        if (shortStats) {
            System.out.println("Краткая статистика:");
            System.out.println("Целые числа: " + integerCount);
            System.out.println("Вещественные числа: " + floatCount);
            System.out.println("Строки: " + stringCount);
        }

        if (fullStats) {
            System.out.println("Полная статистика:");
            if (integerCount > 0) {
                int minInt = integers.stream().min(Integer::compare).orElse(0);
                int maxInt = integers.stream().max(Integer::compare).orElse(0);
                int sumInt = integers.stream().mapToInt(Integer::intValue).sum();
                double avgInt = integers.stream().mapToInt(Integer::intValue).average().orElse(0);

                System.out.println("Целые числа:");
                System.out.println("  Количество: " + integerCount);
                System.out.println("  Минимальное: " + minInt);
                System.out.println("  Максимальное: " + maxInt);
                System.out.println("  Сумма: " + sumInt);
                System.out.println("  Среднее: " + avgInt);
            }

            if (floatCount > 0) {
                float minFloat = floats.stream().min(Float::compare).orElse(0.0f);
                float maxFloat = floats.stream().max(Float::compare).orElse(0.0f);
                double sumFloat = floats.stream().mapToDouble(Float::floatValue).sum();
                double avgFloat = floats.stream().mapToDouble(Float::floatValue).average().orElse(0);

                System.out.println("Вещественные числа:");
                System.out.println("  Количество: " + floatCount);
                System.out.println("  Минимальное: " + minFloat);
                System.out.println("  Максимальное: " + maxFloat);
                System.out.println("  Сумма: " + sumFloat);
                System.out.println("  Среднее: " + avgFloat);
            }

            if (stringCount > 0) {
                int minLength = strings.stream().mapToInt(String::length).min().orElse(0);
                int maxLength = strings.stream().mapToInt(String::length).max().orElse(0);

                System.out.println("Строки:");
                System.out.println("  Количество: " + stringCount);
                System.out.println("  Самая короткая строка: " + minLength + " символов");
                System.out.println("  Самая длинная строка: " + maxLength + " символов");

            }
        }
    }
}