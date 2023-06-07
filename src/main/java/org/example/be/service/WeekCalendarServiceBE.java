package org.example.be.service;

import com.ibm.icu.util.HebrewCalendar;
import com.kosherjava.zmanim.ZmanimCalendar;
import com.kosherjava.zmanim.hebrewcalendar.HebrewDateFormatter;
import com.kosherjava.zmanim.hebrewcalendar.JewishCalendar;
import com.kosherjava.zmanim.hebrewcalendar.JewishDate;
import com.kosherjava.zmanim.util.GeoLocation;
import org.example.dto.*;
import org.example.enums.Region;
import org.example.utils.TextUtils;

import java.text.SimpleDateFormat;
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
    private final int WEEK_SIZE = 7;
    private final AtomicInteger counter = new AtomicInteger();
    private final Locale hebrewLocale = new Locale("he", "IL"); // Hebrew locale
    private final Locale locale = Locale.getDefault();
    private final SimpleDateFormat dayAndMonthFormatter = new SimpleDateFormat("dd/MM");
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", hebrewLocale);
    private final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("YYYY", hebrewLocale);
    private final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE", locale);
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm", locale);
    private final HebrewDateFormatter hebrewDateFormatter = new HebrewDateFormatter();
    private final JewishCalendar jewishCalendar = new JewishCalendar();
    private final  ZmanimCalendar zmanimCalendar = new ZmanimCalendar();
    private final  Calendar cal = Calendar.getInstance();

    public WeekCalBE createWeekBECal(int year, String regionName, List<EventBE> eventBEList){
        WeekCalBE res = new WeekCalBE();
        resetCalendar();
        hebrewDateFormatter.setHebrewFormat(true);
        List<WeekBE> weekBEList = new ArrayList<>();
        Region region = Region.fromName(regionName);
        GeoLocation geoLocation = new GeoLocation(region.getName(), region.getLatitude(), region.getLongitude(), region.getElevation(), region.getTimeZone());
        zmanimCalendar.setGeoLocation(geoLocation);
        zmanimCalendar.setCandleLightingOffset(region.getCandleLightingOffset());

        jewishCalendar.setInIsrael(region.isInIsrael());
        jewishCalendar.setJewishYear(year);
        jewishCalendar.setJewishMonth(7);
        jewishCalendar.setJewishDayOfMonth(1);
        LocalDate localDate = jewishCalendar.getLocalDate();
        LocalDate firstDayOfCalendar = localDate.with(DayOfWeek.MONDAY).minusDays(1);
        int numDaysInYear = jewishCalendar.getDaysInJewishYear();
        LocalDate lastDayOfCalendar = localDate.plusDays(numDaysInYear).with(DayOfWeek.SATURDAY);
        jewishCalendar.setDate(firstDayOfCalendar);

       /* Map<Date, List<EventBE>> eventBEMap = eventBEList.stream()
                .collect(Collectors.groupingBy(EventBE::getDate));*/
        HebrewCalendar hc = new HebrewCalendar();
       /* eventBEMap.keySet().forEach(key -> {
            hc.setTime(key);
            hc.roll(Calendar.YEAR, year - hc.get(Calendar.YEAR));
            cal.setTime(hc.getTime());
            key.setTime(cal.getTime().getTime());
        });*/

       /* Map<String, List<EventBE>> map = eventBEMap.entrySet().stream()
                .collect(Collectors.toMap(item -> dayAndMonthFormatter.format(item.getKey()), Map.Entry::getValue));*/

        Map<String, List<EventBE>> map1 = new HashMap<>();
        eventBEList.forEach(item -> {
            hc.setTime(item.getDate());
            hc.roll(Calendar.YEAR, year - hc.get(Calendar.YEAR));
            cal.setTime(hc.getTime());
            String key = dayAndMonthFormatter.format(cal.getTime());
            List<EventBE> events = map1.get(key);
            if(events != null){
                events.add(item);
            } else {
                events = new ArrayList<>();
                events.add(item);
                map1.put(key, events);
            }
        });


        List<DayBE> days = new ArrayList<>();
        while (jewishCalendar.getLocalDate().isBefore(lastDayOfCalendar.plusDays(1))){
            zmanimCalendar.setCalendar(jewishCalendar.getGregorianCalendar());
            DayBE day = new DayBE();
            day.setDate(jewishCalendar.getGregorianCalendar().getTime());
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
            if(jewishCalendar.getJewishYear() == year){
                day.setEventFEList(getEvents(jewishCalendar, eventBEList));
            }

            List<EventBE> events = map1.get(dayAndMonthFormatter.format(day.getDate()));
            if(events != null){
                List<EventFE> eventFEList = new ArrayList<>();
                events.forEach(item -> {
                    EventFE eventFE = new EventFE();
                    eventFE.setPath(item.getPath());
                    int years = year - new JewishDate(item.getDate()).getJewishYear();
                    eventFE.setText(TextUtils.createEventText(item, years));
                    eventFEList.add(eventFE);
                });
                day.setEventFEList(eventFEList);
            }
            days.add(day);
            jewishCalendar.setDate(jewishCalendar.getLocalDate().plusDays(1));
        }

        Collection<List<DayBE>> weeks = days.stream()
                .collect(Collectors.groupingBy(item -> counter.getAndIncrement() / WEEK_SIZE))
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

    private void resetCalendar() {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private List<EventFE> getEvents(JewishCalendar jewishCalendar, List<EventBE> eventBEList) {
        List<EventFE> res = new ArrayList<>();
        eventBEList.forEach(item -> {
        });

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
