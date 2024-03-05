package com.quiz.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime stringToLocalDateTime(String strDateTime) {
        if(strDateTime == null) return null;
        return LocalDateTime.parse(strDateTime, FORMATTER);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        if(localDateTime == null) return null;
        return localDateTime.format(FORMATTER);
    }
}
