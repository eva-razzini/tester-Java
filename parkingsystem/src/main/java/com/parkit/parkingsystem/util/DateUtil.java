package com.parkit.parkingsystem.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.Date;

public class DateUtil {
    @Contract("_ -> new")
    public static @NotNull Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }
}
