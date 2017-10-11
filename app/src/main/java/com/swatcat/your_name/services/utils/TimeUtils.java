package com.swatcat.your_name.services.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Class for different manipulation with date/time and time constants used in project
 */
public class TimeUtils {

    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60 * ONE_SECOND;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;

    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
    private static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static final String DURATION_FORMAT = "%02d:%02d:%02d";
    private static final StringBuilder durationFormatStringBuilder = new StringBuilder(); //copied from Android playback control sources
    private static final Formatter durationFormatter = new Formatter(durationFormatStringBuilder, Locale.getDefault());

    private static final String POPULAR_TODAY = "today";
    private static final String POPULAR_ALL = "all";
    private static final String POPULAR_MONTH = "month";
    public static final String POPULAR_WEEK = "week";

    /**
     * Method convert string with date to Date object
     *
     * @param date - String like '1986-01-31'
     * @return Date object or null of convert was unsuccessful
     */
    public static Date convertToDate(String date) {
        if (Tools.isNullOrEmpty(date)) {
            return null;
        }

        synchronized (dateFormatter) {
            try {
                return dateFormatter.parse(date);
            } catch (ParseException e) {
                //Log.wtf(TAG, e);
                return null;
            }
        }
    }

    /**
     * Method convert string with date to Date object
     *
     * @return Date object or null of convert was unsuccessful
     */
    public static Date convertToDateTime(String dateTime) {
        synchronized (dateTimeFormatter) {
            try {
                return dateTimeFormatter.parse(dateTime);
            } catch (ParseException e) {
                //Log.wtf(TAG, e);
                return null;
            }
        }
    }

    /**
     * Method convert Date object to string like '1986-01-31'
     *
     * @param date Date object to convert
     * @return String represented date or null if in param is null
     */
    public static String convertDateToString(Date date) {
        synchronized (dateFormatter) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return null;
            }
        }
    }

    /**
     * Method convert Date object to string like '1986-01-31 12:03:25'
     *
     * @param date Date object to convert
     * @return String represented date or null if in param is null
     */
    public static String convertDateTimeToString(Date date) {
        synchronized (dateTimeFormatter) {
            if (date != null) {
                return dateTimeFormatter.format(date);
            } else {
                return null;
            }
        }
    }

    public static Date convertToTimeStamp(String timeStamp){
        synchronized (dateTimeFormatter){
            Date date = new Date(Long.parseLong(timeStamp));
            return  date;
        }
    }

    public static String convertTimeStampToString(Date timeStamp){
        synchronized (dateTimeFormatter) {
            return String.valueOf(timeStamp.getTime());
        }
    }

    public static String convertMilliesToDurationString(int ms) {
        return convertSecondsToDurationString((int) (ms / ONE_SECOND));
    }

    /**
     * Convert seconds to String like "02:34:23" (hours:minutes:seconds)
     *
     * @param sec amount of seconds to convert
     * @return converted String
     */
    public static String convertSecondsToDurationString(int sec) {
        if (sec < 0) { // unable to convert negative seconds
            return null;
        }

        int seconds = sec % 60;
        int minutes = (sec / 60) % 60;
        int hours = sec / 3600;

        return formatDuration(hours, minutes, seconds);
    }

    public static String formatDuration(int hours, int minutes, int seconds) {
        durationFormatStringBuilder.setLength(0);
        return durationFormatter.format(DURATION_FORMAT, hours, minutes, seconds).toString();
    }

    public static Date createPopularDate(String popular) {
        if (popular == null) {
            return null;
        }

        if (POPULAR_ALL.equals(popular)) {
            return new Date(0);
        }

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        if (POPULAR_TODAY.equals(popular)) {
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            return cal.getTime();
        }

        if (POPULAR_MONTH.equals(popular)) {
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
            return cal.getTime();
        }

        if (POPULAR_WEEK.equals(popular)) {
            while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
                cal.roll(Calendar.DAY_OF_YEAR, false);
            }
            cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
            return cal.getTime();
        }
        return null;
    }

    public static String localizedDate(Date date) {
        return DateFormat.getDateInstance().format(date);
    }

    public static String formatTimeForAdsPause(int seconds) {
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    public static String getTimeOnly(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendHourOfDay(2)
                .appendLiteral(":")
                .appendMinuteOfHour(2).toFormatter();
        return dateTimeFormatter.print(time);
    }
    public static String getTimeOnly(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        DateTime dateTime = new DateTime().withHourOfDay(hour).withMinuteOfHour(minute);
        calendar.setTime(dateTime.toDate());
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendHourOfDay(2)
                .appendLiteral(":")
                .appendMinuteOfHour(2).toFormatter();
        return dateTimeFormatter.print(dateTime);
    }
}
