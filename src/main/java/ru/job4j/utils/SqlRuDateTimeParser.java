package ru.job4j.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static java.util.Map.entry;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, String> MONTHS = Map.ofEntries(
            entry("1", "январь"),
            entry("2", "февраль"),
            entry("3", "март"),
            entry("4", "апрель"),
            entry("5", "май"),
            entry("6", "июнь"),
            entry("7", "июль"),
            entry("8", "август"),
            entry("9", "сентябрь"),
            entry("10", "октябрь"),
            entry("11", "ноябрь"),
            entry("12", "декабрь")
    );

    public int getMonth(String month) {
        String result = "0";
        for (Map.Entry<String, String> entry : MONTHS.entrySet()) {
            if (entry.getValue().contains(month.substring(0, 3))) {
                result = entry.getKey();
                break;
            }
        }
        return Integer.parseInt(result);
    }

    @Override
    public LocalDateTime parse(String parse) {

        LocalDateTime result = LocalDateTime.MIN;

        String[] date = parse.split(", | ");

        if (date[0].matches("\\d+")) {
            result = LocalDateTime.of(Integer.parseInt(date[2]), getMonth(date[1]), Integer.parseInt(date[0]),
                    Integer.parseInt(date[3].substring(0, 2)), Integer.parseInt(date[3].substring(3, 5)));
        } else {
            LocalTime time = LocalTime.of(
                    Integer.parseInt(date[1].substring(0, 2)), Integer.parseInt(date[1].substring(3, 5)));
            if (date[0].equals("сегодня")) {
                result = LocalDateTime.of(LocalDate.now(), time);
            }
            if (date[0].equals("вчера")) {
                result = LocalDateTime.of(LocalDate.now().minusDays(1), time);
            }
        }
        return result;
    }
}
