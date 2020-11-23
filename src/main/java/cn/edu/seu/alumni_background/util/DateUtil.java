package cn.edu.seu.alumni_background.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final DateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String toYearString(Date date) {
        return transDateTime(
            YEAR_FORMAT, date
        );
    }

    public static String toDateString(Date date) {
        return transDateTime(
            DATE_FORMAT, date
        );
    }

    public static String transDateTime(DateFormat dateFormat, Date date) {
        return dateFormat.format(date);
    }
}
