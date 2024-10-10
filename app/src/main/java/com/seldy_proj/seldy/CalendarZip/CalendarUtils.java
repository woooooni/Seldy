package com.seldy_proj.seldy.CalendarZip;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarUtils {
    public static LocalDate selectedDate = LocalDate.now();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY MMMM");
        return date.format(formatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = CalendarUtils.selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek)
                daysInMonthArray.add("");
            else
                daysInMonthArray.add(String.valueOf(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i-dayOfWeek)));
        }
        //달력이 한줄띄움 제거
        if (daysInMonthArray.get(6).equals("")){
            for(int i =0;i<7;i++){
                daysInMonthArray.remove(0);
            }

        }
        else if(daysInMonthArray.get(35).equals("")){
            for(int i =0 ;i<7;i++){
                daysInMonthArray.remove(35);
            }
        }
        return daysInMonthArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<String> daysInWeekArray(LocalDate selectedDate){
        ArrayList<LocalDate> days = new ArrayList<>();

        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);

        while(current.isBefore(endDate)){
            days.add(current);
            current = current.plusDays(1);
        }

        ArrayList<String> dayString = new ArrayList<>();
        for(int i =0;i<days.size();i++){
            dayString.add(String.valueOf(days.get(i)));
        }
        return dayString;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static LocalDate sundayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);

        while(current.isAfter(oneWeekAgo)){
            if(current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;
            current = current.minusDays(1);
        }
        return  null;
    }


}
