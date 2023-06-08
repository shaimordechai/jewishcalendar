package org.example;

import com.ibm.icu.util.HebrewCalendar;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Table;
import com.kosherjava.zmanim.hebrewcalendar.HebrewDateFormatter;
import com.kosherjava.zmanim.hebrewcalendar.JewishDate;
import org.example.be.service.WeekCalendarServiceBE;
import org.example.dto.EventBE;
import org.example.dto.WeekCalBE;
import org.example.enums.Event;
import org.example.fe.service.WeekCalendarService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main {

    private final static WeekCalendarService feService = new WeekCalendarService();
    private final static WeekCalendarServiceBE beService = new WeekCalendarServiceBE();

    private static final String PATH = "test.pdf";

    public static void main(String[] args) throws FileNotFoundException {

        List<EventBE> eventBEList = new ArrayList<>();
        EventBE event1 = new EventBE();
        event1.setEvent(Event.WEDDINGDAY);
        event1.setName("שי ויאירה");
        event1.setPath("1.png");
        Calendar cal = Calendar.getInstance();
        cal.set(2006, Calendar.MARCH, 22);
        event1.setDate(cal.getTime());
        eventBEList.add(event1);
        //eventBEList.add(event1);

        WeekCalBE weekCalBE = beService.createWeekBECal(5784, "ירושלים", eventBEList);
        PageSize size = PageSize.A4;
        List<Table> weekCal = feService.createWeekCal(weekCalBE, size);
        feService.createPdf(weekCal, PATH, size);

    }


}