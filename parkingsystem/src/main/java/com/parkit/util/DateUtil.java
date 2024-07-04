package com.parkit.util;

import java.sql.Timestamp;
import java.util.Date;

public class DateUtil {
    public static Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }
}
