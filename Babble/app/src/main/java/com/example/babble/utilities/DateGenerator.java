package com.example.babble.utilities;

import java.text.DateFormat;
import java.text.ParseException;
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

    public static String timeDayToDay(String timeDay) {
        try {
            DateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.getDefault());
            DateFormat outputDateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());

            Date date = inputDateFormat.parse(timeDay);
            if(date == null) return "";
            String convertedDate = outputDateFormat.format(date);

            // Extract the month abbreviation and convert it to uppercase
            String monthAbbreviation = convertedDate.substring(3, 6).toUpperCase(Locale.getDefault());

            // Replace the month abbreviation in the converted date string
            convertedDate = convertedDate.replace(convertedDate.substring(3, 6), monthAbbreviation);

            return convertedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // Return empty string if the parsing fails
        }
    }

    public static String toTimeDay(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.US);

        try {
            Date date = inputFormat.parse(dateString);
            if(date != null) {
                return outputFormat.format(date);
            } return "null";
        } catch (ParseException e) {
            e.printStackTrace();
            return "null";
        }
    }

    public static String toHour(String dateString){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS",
                Locale.getDefault());
        Date date;

        try {
            date = inputFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return "null";
        }

        DateFormat outputFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        if(date != null) {
            return outputFormat.format(date);
        } return "null";
    }
}
