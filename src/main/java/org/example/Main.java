package org.example;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.element.Table;
import org.example.be.service.WeekCalendarServiceBE;
import org.example.dto.WeekCalBE;
import org.example.fe.service.WeekCalendarService;

import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    private final static WeekCalendarService feService = new WeekCalendarService();
    private final static WeekCalendarServiceBE beService = new WeekCalendarServiceBE();

    private static final String PATH = "test.pdf";

    public static void main(String[] args) throws FileNotFoundException {

        WeekCalBE weekCalBE = beService.createWeekBECal(5798, "ירושלים");
        PageSize size = PageSize.A6;
        List<Table> weekCal = feService.createWeekCal(weekCalBE, size);
        feService.createPdf(weekCal, PATH, size);

    }


}