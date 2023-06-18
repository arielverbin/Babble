package com.example.babble;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateGenerator {
    public static String getCurrentHour() {
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        Date now = new Date();
        return timeFormat.format(now);
    }

    public static String getCurrentTimeDay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault());
        Date now = new Date();
        return dateFormat.format(now);
    }
}
