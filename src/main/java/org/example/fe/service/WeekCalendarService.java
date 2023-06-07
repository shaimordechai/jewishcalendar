package org.example.fe.service;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;
import lombok.Data;
import org.example.dto.DayBE;
import org.example.dto.EventFE;
import org.example.dto.WeekBE;
import org.example.dto.WeekCalBE;
import org.example.fe.weekCal.DayFE;
import org.example.fe.weekCal.WeekFE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class WeekCalendarService {

    private static final int NUM_COLUMNS_CAL_TITLE = 1;
    private static final int NUM_COLUMNS_TITLE = 2;
    private static final int NUM_COLUMNS_WEEK = 6;
    private static final int NUM_COLUMNS_DAY = 3;
    private static final int NUM_RAWS_DAY = 3;
    private static final int WEEK_PAGE_MARGINS = 3;
    private static final int MAX_EVENT = 2;
    private static FontProgram fontProgram;

    static {
        try {
            fontProgram = FontProgramFactory.createFont("font.ttf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static PdfFont alefFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H);
    private static float fontCalTitleSize;
    private static float fontTitleSize;
    private static float fontDaySize;
    private static float fontTimesSize;
    private static float fontDateSize;
    private static float imageSize;
    private static float lineHeight;

    private static PageSize pageSize;


    public void createPdf( List<Table> weekCal, String path,  PageSize pageSize) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(path));
        Document doc = new Document(pdfDoc,pageSize);
        doc.setMargins(WEEK_PAGE_MARGINS, WEEK_PAGE_MARGINS, WEEK_PAGE_MARGINS, WEEK_PAGE_MARGINS);
        for (int i = 0; i < weekCal.size(); i++) {
            doc.add(weekCal.get(i));
            if(i == weekCal.size() - 1){
                break;
            }
            doc.add(new AreaBreak());
        }

        doc.close();
    }

    public List<Table> createWeekCal(WeekCalBE weekCalBE, PageSize pageSize){

        List<Table> res = new ArrayList<>();

        init(pageSize);

        Table title = createWeekCalTitle(weekCalBE.getTitle(), pageSize);

        List<Table> weeks = weekCalBE.getWeeks().stream()
                .map(item -> createWeek(item, pageSize))
                .collect(Collectors.toList());

        res.add(title);
        res.addAll(weeks);

        return res;

    }

    private Table createWeekCalTitle(String title, PageSize pageSize) {

        Table res = new Table(NUM_COLUMNS_CAL_TITLE)
                .setWidth(pageSize.getWidth()*3/4)
                .setHeight(pageSize.getHeight()*3/4)
                .setFixedPosition(pageSize.getWidth() / 8, pageSize.getHeight() / 8,pageSize.getWidth()*3/4);
        ;
        Text titleText = new Text(title).setFont(alefFont).setFontSize(fontCalTitleSize);
        Paragraph paragraph = new Paragraph().add(titleText);
        Cell titleCell = new Cell(1, 1).add(paragraph)
                .setTextAlignment(TextAlignment.CENTER);
        res.addCell(titleCell)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        return res;
    }

    private Table createWeek(WeekBE weekBE, PageSize pageSize) {

        float weekWidth = pageSize.getWidth() - 2 * WEEK_PAGE_MARGINS;

        Table res = new Table(NUM_COLUMNS_WEEK)
                .setWidth(weekWidth)
                .setFixedLayout();

        WeekFE weekFE = new WeekFE();
        weekFE.setMonth(createWeekTitle(weekBE.getMonth()));
        weekFE.setMontHeb(createWeekTitle(weekBE.getMonthHeb()));
        weekFE.setDays(createDays(weekBE.getDays()));

        res.addCell(weekFE.getMonth())
                .addCell(weekFE.getMontHeb());
        weekFE.getDays().forEach(item -> {
            res.addCell(item.getDay())
                    .addCell(item.getDayTimes())
                    .addCell(item.getDayData())
                    .addCell(item.getDayEvent1())
                    .addCell(item.getDayEvent2())
                    .addCell(item.getDayHeb());
        });

        return res;
    }

    private List<DayFE> createDays(List<DayBE> days) {
        return days.stream()
                .map(item -> createDay(item))
                .collect(Collectors.toList());
    }

    private DayFE createDay(DayBE dayBE) {
        DayFE res = new DayFE();
        res.setDay(createDayCell(dayBE));
        res.setDayTimes(createTimesCell(dayBE));
        res.setDayData(createDayDataCell(dayBE));
        if(dayBE.getEventFEList() == null || dayBE.getEventFEList().size() <= MAX_EVENT){
            res.setDayEvent2(createDayEventCell(dayBE, 1));
            res.setDayEvent1(createDayEventCell(dayBE, 0));
        }else{

        }
        res.setDayHeb(createDayHebCell(dayBE));
        return res;
    }

    private Cell createDayHebCell(DayBE dayBE) {
        Text dayText = new Text(dayBE.getDayInWeekHeb()).setFont(alefFont).setFontSize(fontDaySize);//.setBaseDirection(BaseDirection.DEFAULT_BIDI);
        Paragraph p1 = new Paragraph()
                .setFixedLeading(20)
                .add(dayText);
        Text dateText = new Text(dayBE.getDayInMonthHeb()).setFont(alefFont).setFontSize(fontDateSize);
        Paragraph p2 = new Paragraph()
                .setFixedLeading(20)
                .add(dateText);

        return new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBackgroundColor(createDayColor(dayBE))
                .add(p1)
                .add(p2);
    }

    private Cell createDayEventCell(DayBE dayBE, int index) {
        Cell res = new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.RIGHT)
                .setVerticalAlignment(VerticalAlignment.TOP)
                .setBackgroundColor(createDayColor(dayBE));
        Paragraph p1 = new Paragraph();
        Paragraph p2 = new Paragraph();
        try {
            EventFE eventFE = dayBE.getEventFEList().get(index);
            Text text = new Text(eventFE.getText()).setFont(alefFont).setFontSize(fontTimesSize);
            p1.add(text);
            Image image = null;
            image = new Image(ImageDataFactory.create(eventFE.getPath())).setHeight(imageSize).setWidth(imageSize).setBorderRadius(new BorderRadius(10));
            p2.add(image);
            res.add(p1);
            try {
                res.add(p2);
            }catch (Exception e){
                int a = 1;
            }
            return res;
        } catch (Exception e) {
            res.add(p1);
            res.add(p2);
            return res;
        }
    }

    private Cell createDayDataCell(DayBE dayBE) {
        List<String> data = Arrays.asList(dayBE.getCandleLighting(), dayBE.getTzaisSabath(),dayBE.getTzaisYomTov(), dayBE.getParsha());
        List<Paragraph> p = data.stream()
                .filter(Objects::nonNull)
                .map(item -> new Paragraph(new Text(item).setFont(alefFont).setFontSize(fontTimesSize)))
                .collect(Collectors.toList());
        Cell res = new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.RIGHT)
                .setVerticalAlignment(VerticalAlignment.BOTTOM)
                .setBackgroundColor(createDayColor(dayBE));
        p.forEach(item -> {
            res.add(item);
        });
        return res;
    }

    private Cell createTimesCell(DayBE dayBE) {
        List<String> data = Arrays.asList(dayBE.getSunrise(), dayBE.getSunset());
        List<Paragraph> p = data.stream()
                .map(item -> new Paragraph(new Text(item).setFont(alefFont).setFontSize(fontTimesSize)))
                .collect(Collectors.toList());
        Cell res = new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.BOTTOM)
                .setBackgroundColor(createDayColor(dayBE));
        p.forEach(item -> {
            res.add(item);
        });
        return res;
    }

    private Color createDayColor(DayBE dayBE) {
        return (dayBE.isShabath() || dayBE.isYomTov())? new DeviceRgb(238, 237, 236) : null;
    }

    private Cell createWeekTitle(String titleText){
        Text text = new Text(titleText).setFont(alefFont).setFontSize(fontTitleSize);
        Paragraph p = new Paragraph()
                .setFixedLeading(20)
                .add(text);

        return new Cell(1, 3)
                .setHeight(lineHeight)
                .setBorder(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .add(p);
    }


    private Cell createDayCell(DayBE dayBE){
        Text dayText = new Text(dayBE.getDayInWeek()).setFont(alefFont).setFontSize(fontDaySize);//.setBaseDirection(BaseDirection.DEFAULT_BIDI);
        Paragraph p1 = new Paragraph()
                .setFixedLeading(20)
                .add(dayText);
        Text dateText = new Text(dayBE.getDayInMonth()).setFont(alefFont).setFontSize(fontDateSize);
        Paragraph p2 = new Paragraph()
                .setFixedLeading(20)
                .add(dateText);

        return new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBackgroundColor(createDayColor(dayBE))
                .add(p1)
                .add(p2);
    }

    public void init(PageSize pageSize) {
        this.pageSize = pageSize;
        this.fontCalTitleSize = pageSize.getWidth()/6;
        this.fontTitleSize = pageSize.getWidth()/17;
        this.fontDaySize = pageSize.getWidth()/40;
        this.fontTimesSize = pageSize.getWidth()/53;
        this.imageSize = pageSize.getWidth()/10;
        this.fontDateSize = pageSize.getWidth()/15;
        this.lineHeight = (pageSize.getHeight() - 2 * WEEK_PAGE_MARGINS)/9*0.9f;


    }
}
