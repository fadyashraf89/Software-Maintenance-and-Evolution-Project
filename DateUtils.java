package org.unitime.timetable.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static int getWeek(Date date) {
        Calendar c = Calendar.getInstance(Locale.US);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getDayOfYear(Date date) {
        Calendar c = Calendar.getInstance(Locale.US);
        c.setTime(date);
        return c.get(Calendar.DAY_OF_YEAR);
    }

    public static int getFirstDayOfWeek(int year, int week) {
        Calendar c = Calendar.getInstance(Locale.US);
        c.set(year,1,1,0,0,0);
        c.set(Calendar.WEEK_OF_YEAR,week);
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        int dayOfYear = c.get(Calendar.DAY_OF_YEAR);
        if (c.get(Calendar.YEAR)<year) {
            Calendar x = Calendar.getInstance(Locale.US);
            x.set(c.get(Calendar.YEAR),11,31,0,0,0);
            dayOfYear -= x.get(Calendar.DAY_OF_YEAR);
        } else if (c.get(Calendar.YEAR)>year) {
            Calendar x = Calendar.getInstance(Locale.US);
            x.set(year,11,31,0,0,0);
            dayOfYear += x.get(Calendar.DAY_OF_YEAR);
        }
        return dayOfYear;
    }

    public static Date getDate(int year, int dayOfYear) {
        Calendar c = Calendar.getInstance(Locale.US);
        c.set(year,1,1,0,0,0);
        c.set(Calendar.DAY_OF_YEAR,dayOfYear);
        return c.getTime();
    }

    public static Date getDate(int day, int month, int year) {
        return calendarFor(day, month, year).getTime();
    }

    public static Date getStartDate(int year, int week) {
        Calendar c = Calendar.getInstance(Locale.US);
        c.set(year,1,1,0,0,0);
        c.set(Calendar.WEEK_OF_YEAR,week);
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date getEndDate(int year, int week) {
        Calendar c = Calendar.getInstance(Locale.US);
        c.set(year,1,1,0,0,0);
        c.set(Calendar.WEEK_OF_YEAR,week);
        c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
        c.add(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date getStartDate(int year, int week, int offset) {
        Calendar c = Calendar.getInstance(Locale.US);
        c.set(year,1,1,0,0,0);
        c.set(Calendar.WEEK_OF_YEAR,week);
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        c.add(Calendar.DAY_OF_YEAR, 1 + offset);
        return c.getTime();
    }

    public static Date getEndDate(int year, int week, int offset) {
        Calendar c = Calendar.getInstance(Locale.US);
        c.set(year,1,1,0,0,0);
        c.set(Calendar.WEEK_OF_YEAR,week);
        c.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
        c.add(Calendar.DAY_OF_YEAR, 1 + offset);
        return c.getTime();
    }

    public static int getEndMonth(Date sessionEnd, int year, int excessDays) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(sessionEnd);
        cal.add(Calendar.DAY_OF_MONTH, +excessDays);
        int m = cal.get(Calendar.MONTH);
        if (cal.get(Calendar.YEAR) != year)
            m += (12 * (cal.get(Calendar.YEAR) - year));
        return m;
    }

    public static int getStartMonth(Date sessionBegin, int year, int excessDays) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(sessionBegin);
        cal.add(Calendar.DAY_OF_MONTH, -excessDays);
        int m = cal.get(Calendar.MONTH);
        if (cal.get(Calendar.YEAR) != year)
            m -= (12 * (year - cal.get(Calendar.YEAR)));
        return m;
    }

    public static int getNrDaysOfMonth(int month, int year) {
        Calendar cal = calendarForFirstDayOf(month, year);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int calculateActualYear(int month, int year){
        return(year + (month < 0 ? (month/12)-1 : month >= 12 ? (month/12) : 0));
    }

    private static Calendar calendarForFirstDayOf(int month, int year){
        return(calendarFor(1, month, year));
    }

    public static int getDayOfYear(int day, int month, int year) {
        Calendar cal = calendarFor(day, month, year);
        int idx = cal.get(Calendar.DAY_OF_YEAR);
        if (month < 0 || month >= 12) {
            int actualYear = calculateActualYear(month, year);
            if (month < 0){
                while (actualYear < year){
                    cal.set(actualYear, 11, 31);
                    idx += (-1  * cal.get(Calendar.DAY_OF_YEAR));
                    actualYear++;
                }
            } else {
                while(actualYear > year){
                    actualYear--;
                    cal.set(actualYear, 11, 31);
                    idx += cal.get(Calendar.DAY_OF_YEAR);
                }
            }
        }
        return idx - 1;
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static Date addWeeks(Date date, int weeks) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, weeks);
        return cal.getTime();
    }

    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    public static long durationInDays(Date start, Date end) {
        long diffInMillis = end.getTime() - start.getTime();
        return diffInMillis / (24 * 60 * 60 * 1000);
    }

    private static Calendar calendarFor(int day, int month, int year){
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(calculateActualYear(month, year),
                (month < 0 ? (12 + (month%12)) : month % 12), day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return(cal);
    }
}