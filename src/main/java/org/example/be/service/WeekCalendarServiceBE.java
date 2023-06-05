package org.example.be.service;

import com.kosherjava.zmanim.ZmanimCalendar;
import com.kosherjava.zmanim.hebrewcalendar.HebrewDateFormatter;
import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.kosherjava.zmanim.util.GeoLocation;
import org.example.dto.DayBE;
import org.example.dto.WeekBE;
import org.example.dto.WeekCalBE;
import org.example.enums.Region;
import org.example.utils.TextUtils;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.time.ZoneId;


public class WeekCalendarServiceBE {

    private final String WEEK_CAL_TITLE = "יעובש חול תנשל";
    private final String SPACE = " ";
    private final String EMPTY = "";
    private final int WEEK_SIZE = 7;
    private final AtomicInteger counter = new AtomicInteger();
    Locale hebrewLocale = new Locale("he", "IL"); // Hebrew locale
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", hebrewLocale);
    private final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("YYYY", hebrewLocale);
    private final Locale locale = Locale.getDefault();
    private final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE", locale);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm", locale);
    private final HebrewDateFormatter hebrewDateFormatter = new HebrewDateFormatter();

    public WeekCalBE createWeekBECal(int year, String regionName){
        WeekCalBE res = new WeekCalBE();
        hebrewDateFormatter.setHebrewFormat(true);
        List<WeekBE> weekBEList = new ArrayList<>();
        Region region = Region.fromName(regionName);
        GeoLocation geoLocation = new GeoLocation(region.getName(), region.getLatitude(), region.getLongitude(), region.getElevation(), region.getTimeZone());
        JewishCalendar jewishCalendar = new JewishCalendar(year, 7, 1);
        jewishCalendar.setInIsrael(region.isInIsrael());
        LocalDate localDate = jewishCalendar.getLocalDate();
        LocalDate firstDayOfCalendar = localDate.with(DayOfWeek.MONDAY).minusDays(1);
        int numDaysInYear = jewishCalendar.getDaysInJewishYear();
        LocalDate lastDayOfCalendar = localDate.plusDays(numDaysInYear).with(DayOfWeek.SATURDAY);

        jewishCalendar.setDate(firstDayOfCalendar);
        ZmanimCalendar zmanimCalendar = new ZmanimCalendar(geoLocation);
        zmanimCalendar.setCandleLightingOffset(40.0);


        List<DayBE> days = new ArrayList<>();

        while (jewishCalendar.getLocalDate().isBefore(lastDayOfCalendar.plusDays(1))){
            zmanimCalendar.setCalendar(jewishCalendar.getGregorianCalendar());
            DayBE day = new DayBE();
            day.setDate(Date.valueOf(jewishCalendar.getLocalDate()));
            day.setYear(jewishCalendar.getLocalDate().format(yearFormatter));
            day.setMonth(jewishCalendar.getLocalDate().format(monthFormatter));
            day.setMonthHeb(hebrewDateFormatter.formatMonth(jewishCalendar));
            day.setYearHeb(hebrewDateFormatter.formatHebrewNumber(jewishCalendar.getJewishYear()));
            day.setDayInWeek(jewishCalendar.getLocalDate().format(dayFormatter));
            day.setDayInWeekHeb(hebrewDateFormatter.formatDayOfWeek(jewishCalendar));
            day.setDayInMonth(String.valueOf(jewishCalendar.getLocalDate().getDayOfMonth()));
            day.setDayInMonthHeb(hebrewDateFormatter.formatHebrewNumber(jewishCalendar.getJewishDayOfMonth()));
            day.setSunrise(LocalDateTime.ofInstant(zmanimCalendar.getSunrise().toInstant(), ZoneId.systemDefault()).format(timeFormatter));
            day.setSunset(LocalDateTime.ofInstant(zmanimCalendar.getSunset().toInstant(), ZoneId.systemDefault()).format(timeFormatter));
            day.setParsha(hebrewDateFormatter.formatParsha(jewishCalendar));
            day.setYomTov(jewishCalendar.isYomTovAssurBemelacha());
            day.setShabath(jewishCalendar.getDayOfWeek() == 7);
            day.setRoshChodesh(hebrewDateFormatter.formatRoshChodesh(jewishCalendar));
            day.setYomTovName(hebrewDateFormatter.formatYomTov(jewishCalendar));
            if(jewishCalendar.hasCandleLighting()){
                day.setCandleLighting(LocalDateTime.ofInstant(
                        zmanimCalendar.getCandleLighting().toInstant()
                        , ZoneId.systemDefault()).format(timeFormatter));

            }
            if((jewishCalendar.getDayOfWeek() == 7 && !jewishCalendar.hasCandleLighting())){
                day.setTzaisSabath(LocalDateTime.ofInstant(zmanimCalendar.getTzais().toInstant(), ZoneId.systemDefault()).format(timeFormatter));
            }
            if((jewishCalendar.isYomTovAssurBemelacha() && jewishCalendar.getDayOfWeek() != 7 && !jewishCalendar.hasCandleLighting())){
                day.setTzaisYomTov(LocalDateTime.ofInstant(zmanimCalendar.getTzais().toInstant(), ZoneId.systemDefault()).format(timeFormatter));
            }
            days.add(day);
            jewishCalendar.setDate(jewishCalendar.getLocalDate().plusDays(1));
        }

        Collection<List<DayBE>> weeks = days.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / WEEK_SIZE))
                .values();

        weeks.forEach(w -> {
            WeekBE weekBE = new WeekBE();
            weekBE.setDays(w);
            weekBE.setMonth(createMonth(w.get(0).getMonth(), w.get(6).getMonth()));
            weekBE.setMonthHeb(createMonth(w.get(0).getMonthHeb(), w.get(6).getMonthHeb()));
            weekBEList.add(weekBE);
        });

        res.setWeeks(weekBEList);
        res.setTitle(createTitle(hebrewDateFormatter.formatHebrewNumber(year)));
        return res;

    }

    private String createTitle(String year) {
        StringBuilder sb = new StringBuilder();
        sb.append(WEEK_CAL_TITLE)
                .append(SPACE)
                .append(TextUtils.setDirectionRtl(year));


        return sb.toString();
    }

    private String createMonth(String start, String end) {
        return Stream.of(start, end)
                .distinct()
                .collect(Collectors.joining("-"));
    }
}
