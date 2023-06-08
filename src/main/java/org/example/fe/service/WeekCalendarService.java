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
import com.itextpdf.layout.borders.SolidBorder;
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

    private static final PdfFont alefFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H);
    private static float fontCalTitleSize;
    private static float fontTitleSize;
    private static float fontSubTitleSize;
    private static float fontDaySize;
    private static float fontTimesSize;
    private static float fontDateSize;
    private static float fontEventSize;
    private static float fontEventWithoutImageSize;
    private static float imageSize;
    private static float lineHeight;
    private static float fixedLeading;
    private static PageSize pageSize;
    private static DeviceRgb lineBorderColor;
    private static DeviceRgb shabathColor;
    private static DeviceRgb noInYearColor;
    private static DeviceRgb textColor;
    private static float titleLineBorderWidth;
    private static float lineBorderWidth;




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
        weekFE.setMonth(createWeekTitle(weekBE.getMonth(), weekBE.getYear()));
        weekFE.setMonthHeb(createWeekTitle(weekBE.getMonthHeb(), weekBE.getYearHeb()));
        weekFE.setDays(createDays(weekBE.getDays()));
        weekFE.setAdditionalData(createAdditionalData(weekBE.getAdditionalData()));

        res.addCell(weekFE.getMonth())
                .addCell(weekFE.getMonthHeb());
        weekFE.getDays().forEach(item -> {
            res.addCell(item.getDay())
                    .addCell(item.getDayTimes())
                    .addCell(item.getDayData());
            if(item.getDayEvent2() != null){
                res.addCell(item.getDayEvent2());
            }
            if(item.getDayEvent1() != null){
                res.addCell(item.getDayEvent1());
            }
            if(item.getDayEvent() != null){
                res.addCell(item.getDayEvent());
            }
            res.addCell(item.getDayHeb());
        });
        res.addCell(weekFE.getAdditionalData());

        return res;
    }

    private Cell createAdditionalData(List<String> additionalData) {
        List<Paragraph> paragraphList = new ArrayList<>();
        additionalData.forEach(item -> {
            Text text = new Text(item).setFont(alefFont).setFontSize(fontTimesSize);
            Paragraph p = new Paragraph()
                    .setFixedLeading(fixedLeading)
                    .add(text);
            paragraphList.add(p);
        });

        Cell res = new Cell(1, 6)
                .setHeight(lineHeight)
                .setBorder(null)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.TOP);
        paragraphList.forEach(item -> {
            res.add(item);
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
            res.setDayEvent(createDayEventCellWithoutImage(dayBE));
        }
        res.setDayHeb(createDayHebCell(dayBE));
        return res;
    }

    private Cell createDayEventCellWithoutImage(DayBE dayBE) {
        Cell res = new Cell(1, 2)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.TOP)
                .setBackgroundColor(createDayColor(dayBE));
        List<Paragraph> paragraphList = new ArrayList<>();
        dayBE.getEventFEList().forEach(item -> {
            Paragraph p = new Paragraph();
            Text text = new Text(item.getText()).setFont(alefFont).setFontSize(fontEventWithoutImageSize).setFontColor(createTextColor(dayBE));
            p.add(text);
            paragraphList.add(p);
        });
        paragraphList.forEach(item -> {
            res.add(item);
        });

        return res;
    }

    private Cell createDayHebCell(DayBE dayBE) {
        Text dayText = new Text(dayBE.getDayInWeekHeb()).setFont(alefFont).setFontSize(fontDaySize).setFontColor(createTextColor(dayBE));
        Paragraph p1 = new Paragraph()
                .setFixedLeading(fixedLeading)
                .add(dayText);
        Text dateText = new Text(dayBE.getDayInMonthHeb()).setFont(alefFont).setFontSize(fontDateSize).setFontColor(createTextColor(dayBE));
        Paragraph p2 = new Paragraph()
                .setFixedLeading(fixedLeading)
                .add(dateText);

        return new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new SolidBorder(lineBorderColor, lineBorderWidth))
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
                .setBorderBottom(new SolidBorder(lineBorderColor, lineBorderWidth))
                .setTextAlignment(TextAlignment.RIGHT)
                .setVerticalAlignment(VerticalAlignment.TOP)
                .setBackgroundColor(createDayColor(dayBE));
        Paragraph p1 = new Paragraph();
        Paragraph p2 = new Paragraph();
        try {
            EventFE eventFE = dayBE.getEventFEList().get(index);
            Text text = new Text(eventFE.getText()).setFont(alefFont).setFontSize(fontEventSize).setFontColor(createTextColor(dayBE));
            p1.add(text);
            Image image = null;
            image = new Image(ImageDataFactory.create(eventFE.getPath())).setHeight(imageSize).setWidth(imageSize).setBorderRadius(new BorderRadius(10));
            p2.add(image);
            res.add(p1);
            res.add(p2);

            return res;
        } catch (Exception e) {
            res.add(p1);
            res.add(p2);
            return res;
        }
    }

    private Cell createDayDataCell(DayBE dayBE) {
        List<String> data = Arrays.asList(dayBE.getRoshChodesh(), dayBE.getYomTovName(), dayBE.getCandleLighting(), dayBE.getTzaisSabath()
                , dayBE.getTzaisYomTov(), dayBE.getParsha(), dayBE.getSpecialParsha());
        List<Paragraph> p = data.stream()
                .filter(Objects::nonNull)
                .map(item -> new Paragraph(new Text(item).setFont(alefFont).setFontSize(fontTimesSize).setFontColor(createTextColor(dayBE))))
                .collect(Collectors.toList());
        Cell res = new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new SolidBorder(lineBorderColor, lineBorderWidth))
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
                .map(item -> new Paragraph(new Text(item).setFont(alefFont).setFontSize(fontTimesSize).setFontColor(createTextColor(dayBE))))
                .collect(Collectors.toList());
        Cell res = new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new SolidBorder(lineBorderColor, lineBorderWidth))
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.BOTTOM)
                .setBackgroundColor(createDayColor(dayBE));
        p.forEach(item -> {
            res.add(item);
        });
        return res;
    }

    private Color createDayColor(DayBE dayBE) {
        return (dayBE.isShabath() || dayBE.isYomTov())? shabathColor : null;
    }

    private Color createTextColor(DayBE dayBE) {
        return (dayBE.isCurrentYear() ? textColor : noInYearColor);
    }

    private Cell createWeekTitle(String month, String year){
        Text monthText = new Text(month).setFont(alefFont).setFontSize(fontTitleSize);
        Paragraph p1 = new Paragraph()
                .setFixedLeading(fixedLeading)
                .add(monthText);
        Text yearText = new Text(year).setFont(alefFont).setFontSize(fontSubTitleSize);
        Paragraph p2 = new Paragraph()
                .setFixedLeading(fixedLeading)
                .add(yearText);

        return new Cell(1, 3)
                .setHeight(lineHeight)
                //  .setBorder(new DashedBorder(0.5f))
                .setBorder(null)
                .setBorderBottom(new SolidBorder(lineBorderColor, titleLineBorderWidth))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .add(p1)
                .add(p2);
    }


    private Cell createDayCell(DayBE dayBE){
        Text dayText = new Text(dayBE.getDayInWeek()).setFont(alefFont).setFontSize(fontDaySize).setFontColor(createTextColor(dayBE));
        Paragraph p1 = new Paragraph()
                .setFixedLeading(fixedLeading)
                .add(dayText);
        Text dateText = new Text(dayBE.getDayInMonth()).setFont(alefFont).setFontSize(fontDateSize).setFontColor(createTextColor(dayBE));
        Paragraph p2 = new Paragraph()
                .setFixedLeading(fixedLeading)
                .add(dateText);

        return new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new SolidBorder(lineBorderColor, lineBorderWidth))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBackgroundColor(createDayColor(dayBE))
                .add(p1)
                .add(p2);
    }

    public void init(PageSize pageSize) {
        this.pageSize = pageSize;
        this.fontCalTitleSize = pageSize.getWidth()/6;
        this.fontTitleSize = pageSize.getWidth()/20;
        this.fontSubTitleSize = pageSize.getWidth()/30;
        this.fontDaySize = pageSize.getWidth()/40;
        this.fontTimesSize = pageSize.getWidth()/53;
        this.fontEventSize = pageSize.getWidth()/70;
        this.fontEventWithoutImageSize = pageSize.getWidth()/80;
        this.imageSize = pageSize.getWidth()/12;
        this.fontDateSize = pageSize.getWidth()/15;
        this.lineHeight = (pageSize.getHeight() - 2 * WEEK_PAGE_MARGINS)/9*0.9f;
        this.fixedLeading = pageSize.getHeight() / 21;
        this.lineBorderColor = new DeviceRgb(176, 176, 176);
        this.shabathColor = new DeviceRgb(238, 237, 236);
        this.noInYearColor = new DeviceRgb(200, 200, 200);
        this.textColor = new DeviceRgb(0, 0, 0);
        this.titleLineBorderWidth = pageSize.getHeight()/400;
        this.lineBorderWidth = titleLineBorderWidth/2;
    }
}
