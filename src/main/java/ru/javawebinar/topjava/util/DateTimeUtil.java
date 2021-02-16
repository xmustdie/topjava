package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T localDateOrTime, T startPeriod, T endPeriod) {
        return (localDateOrTime instanceof LocalTime) ?
                localDateOrTime.compareTo(startPeriod) >= 0 && localDateOrTime.compareTo(endPeriod) < 0 :
                localDateOrTime.compareTo(startPeriod) >= 0 && localDateOrTime.compareTo(endPeriod) <= 0;
    }

    public static String toString(LocalDateTime localDateTime) {
        return localDateTime == null ? "" : localDateTime.format(DATE_TIME_FORMATTER);
    }
}

