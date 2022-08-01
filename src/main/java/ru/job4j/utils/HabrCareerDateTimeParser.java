package ru.job4j.utils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        ZonedDateTime result = ZonedDateTime.parse(parse, DateTimeFormatter.ISO_DATE_TIME);
        return result.toLocalDateTime();
    }
}
