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
import org.example.dto.WeekBE;
import org.example.dto.WeekCalBE;
import org.example.utils.TextUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@Data
public class WeekCalendarService {

    private static final String SUNRISE = "זריחה:";
    private static final String SUNSET = "שקיעה:";
    //private static final String PARSHA = "פרשת השבוע: ";
    private static final String CANDLE_LIGHTING = "הדלקת נרות: ";
    private static final String TZAIS_SHABATH = "צאת השבת: ";
    private static final String TZAIS_YOM_TOV = "צאת החג: ";
    private static final String EMPTY = "";
    private static final int NUM_COLUMNS_CAL_TITLE = 1;
    private static final int NUM_COLUMNS_TITLE = 2;
    private static final int NUM_COLUMNS_WEEK = 6;
    private static final int NUM_COLUMNS_DAY = 3;
    private static final int NUM_RAWS_DAY = 3;
    private static final int WEEK_PAGE_MARGINS = 3;
    private static FontProgram fontProgram;

    static {
        try {
            fontProgram = FontProgramFactory.createFont("Alef.ttf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static PdfFont alefFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H);
    private static float fontCalTitleSize;
    private static float fontTitleSize;
    private static float fontDaySize;
    private static float fontParshaSize;
    private static float fontDateSize;
    private static float imageSize;
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

        List<Table> WeekCal = new ArrayList<>();

        WeekCal.add(createWeekCalTitle(weekCalBE.getTitle(), pageSize));
        weekCalBE.getWeeks().forEach(week ->{
            WeekCal.add(createWeek(week, pageSize));
        });

        return WeekCal;
    }

    private Table createWeekCalTitle(String title, PageSize pageSize) {

        Table table = new Table(NUM_COLUMNS_CAL_TITLE)
                .setWidth(pageSize.getWidth()*3/4)
                .setHeight(pageSize.getHeight()*3/4)
                .setFixedPosition(pageSize.getWidth() / 8, pageSize.getHeight() / 8,pageSize.getWidth()*3/4);
        ;
        Text text = new Text(title).setFont(alefFont).setFontSize(fontCalTitleSize);
        Paragraph paragraph = new Paragraph()
                .add(text);
        table.addCell(new Cell(1, 1).add(paragraph)
                        .setTextAlignment(TextAlignment.CENTER))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        return table;
    }

    private Table createWeek(WeekBE weekBE, PageSize pageSize) {
        float weekWidth = pageSize.getWidth() - 2 * WEEK_PAGE_MARGINS;
        float lineHeight = (pageSize.getHeight() - 2 * WEEK_PAGE_MARGINS)/9*0.9f;


        Table week = new Table(NUM_COLUMNS_WEEK)
                .setWidth(weekWidth)
                .setFixedLayout();

        createTitle(week, lineHeight, weekBE);
        for (int i = 0; i < 7; i++) {
            createDay(week, lineHeight, weekBE.getDays().get(i));
        }

        return week;
    }

    private void createDay(Table week, float lineHeight, DayBE dayBE) {
        week.addCell(createDayCell(dayBE.getDayInWeek(), dayBE.getDayInMonth(), lineHeight)
                .setBackgroundColor(createDayColor(dayBE)));
        week.addCell(createSunTimesCell(dayBE.getSunrise(), dayBE.getSunset(), lineHeight)
                .setBackgroundColor(createDayColor(dayBE)));
        week.addCell(createAdditionalDataCell(dayBE.getYomTovName(), dayBE.getRoshChodesh()
                , dayBE.getParsha(), dayBE.getCandleLighting()
                , dayBE.getTzaisSabath()
                , dayBE.getTzaisYomTov()
                , lineHeight)
                .setBackgroundColor(createDayColor(dayBE)));
        week.addCell(createPrivateCell(lineHeight)
                .setBackgroundColor(createDayColor(dayBE)));
        week.addCell(createPrivateCell(lineHeight)
                .setBackgroundColor(createDayColor(dayBE)));
        week.addCell(createDayCell(dayBE.getDayInWeekHeb(), dayBE.getDayInMonthHeb(), lineHeight)
                .setBackgroundColor(createDayColor(dayBE)));
    }

    private Color createDayColor(DayBE dayBE) {
        return (dayBE.isShabath() || dayBE.isYomTov())? new DeviceRgb(238, 237, 236) : null;
    }

    private Cell createPrivateCell(float lineHeight) {
        Text text1 = new Text("test").setFont(alefFont).setFontSize(fontDaySize);
        Paragraph p1 = new Paragraph()
                .add(text1);
        Image image = null;
        try {
            image = new Image(ImageDataFactory.create("test.jpg")).setHeight(imageSize).setWidth(imageSize);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Paragraph p2 = new Paragraph()
                .add(image);

        return new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.RIGHT)
                .setVerticalAlignment(VerticalAlignment.TOP)
                .add(p1)
                .add(p2);
    }

    private Cell createAdditionalDataCell(String yomTovName, String roshChodesh, String parsha, String candleLighting, String tzaisSabath, String tzaisYomTov, float lineHeight) {
        yomTovName = TextUtils.updateDirection(yomTovName);
        roshChodesh =  TextUtils.updateDirection(roshChodesh);
        if(!parsha.isEmpty()) {
            parsha = TextUtils.updateDirection(parsha) /*+ EMPTY + TextUtils.updateDirection(PARSHA)*/;
        }
        if(!candleLighting.isEmpty()) {
            candleLighting = TextUtils.updateDirection(candleLighting) + EMPTY + TextUtils.updateDirection(CANDLE_LIGHTING);
        }
        if(!tzaisSabath.isEmpty()) {
            tzaisSabath = TextUtils.updateDirection(tzaisSabath) + EMPTY + TextUtils.updateDirection(TZAIS_SHABATH);
        }
        if(!tzaisYomTov.isEmpty()) {
            tzaisYomTov = TextUtils.updateDirection(tzaisYomTov) + EMPTY + TextUtils.updateDirection(TZAIS_YOM_TOV);
        }
        Text text1 = new Text(yomTovName).setFont(alefFont).setFontSize(fontParshaSize).setBold();
        Paragraph p1 = new Paragraph()
                .add(text1);
        Text text2 = new Text(roshChodesh).setFont(alefFont).setFontSize(fontParshaSize).setBold();
        Paragraph p2 = new Paragraph()
                .add(text2);
        Text text3 = new Text(parsha).setFont(alefFont).setFontSize(fontParshaSize).setBold();
        Paragraph p3 = new Paragraph()
                .add(text3);
        Text text4 = new Text(candleLighting).setFont(alefFont).setFontSize(fontParshaSize).setBold();
        Paragraph p4 = new Paragraph()
                .add(text4);
        Text text5 = new Text(tzaisSabath).setFont(alefFont).setFontSize(fontParshaSize).setBold();
        Paragraph p5 = new Paragraph()
                .add(text5);
        Text text6 = new Text(tzaisYomTov).setFont(alefFont).setFontSize(fontParshaSize).setBold();
        Paragraph p6 = new Paragraph()
                .add(text6);

        return new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.LEFT)
                .setVerticalAlignment(VerticalAlignment.BOTTOM)
                .add(p1)
                .add(p2)
                .add(p3)
                .add(p4)
                .add(p5)
                .add(p6);
    }

    private Cell createSunTimesCell(String sunrise, String sunset, float lineHeight) {
        //sunrise = TextUtils.updateDirection(sunrise);
        // sunset = TextUtils.updateDirection(sunset);
        sunrise = sunrise + TextUtils.updateDirection(SUNRISE);
        sunset = sunset + TextUtils.updateDirection(SUNSET);
        Text text1 = new Text(sunrise).setFont(alefFont).setFontSize(fontParshaSize);
        Paragraph p1 = new Paragraph()
                //.setFixedLeading(20)
                .add(text1);
        Text text2 = new Text(sunset).setFont(alefFont).setFontSize(fontParshaSize);
        Paragraph p2 = new Paragraph()
                // .setFixedLeading(20)
                .add(text2);

        return new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(null)
                .setBorderBottom(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.BOTTOM)
                .add(p1)
                .add(p2);
    }

    private void createTitle(Table week, float lineHeight, WeekBE weekBE) {
        week.addCell(createTitleCell(weekBE.getMonth(), lineHeight));
        week.addCell(createTitleCell(weekBE.getMonthHeb(), lineHeight));
    }

    private Cell createTitleCell(String titleText, float lineHeight){
        titleText = TextUtils.updateDirection(titleText);

        Text text = new Text(titleText).setFont(alefFont).setFontSize(fontTitleSize);
        Paragraph paragraph = new Paragraph()
                .setFixedLeading(20)
                .add(text);

        return new Cell(1, 3)
                .setHeight(lineHeight)
                .setBorder(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .add(paragraph);
    }


    private Cell createDayCell(String dayText, String dateText, float lineHeight){
        dayText = TextUtils.updateDirection(dayText);
        dateText = TextUtils.updateDirection(dateText);
        Text text1 = new Text(dayText).setFont(alefFont).setFontSize(fontDaySize).setBaseDirection(BaseDirection.DEFAULT_BIDI);
        Paragraph p1 = new Paragraph()
                .setFixedLeading(20)
                .add(text1);
        Text text2 = new Text(dateText).setFont(alefFont).setFontSize(fontDateSize);
        Paragraph p2 = new Paragraph()
                .setFixedLeading(20)
                .add(text2);

        return new Cell(1, 1)
                .setHeight(lineHeight)
                .setBorder(new DashedBorder(0.5f))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .add(p1)
                .add(p2);
    }

    public void init(PageSize pageSize) {
        this.pageSize = pageSize;
        this.fontCalTitleSize = pageSize.getWidth()/6;
        this.fontTitleSize = pageSize.getWidth()/17;
        this.fontDaySize = pageSize.getWidth()/40;
        this.fontParshaSize = pageSize.getWidth()/53;
        this.imageSize = pageSize.getWidth()/15;
        this.fontDateSize = pageSize.getWidth()/15;

    }
}
