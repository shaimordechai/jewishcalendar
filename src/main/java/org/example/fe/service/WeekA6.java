package org.example.fe.service;


import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.*;

import com.itextpdf.text.DocumentException;
import org.example.dto.DayDto;
import org.example.dto.WeekDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.itextpdf.text.Utilities.millimetersToPoints;

public class WeekA6 {
    private static final String SUNRISE = "זריחה:  ";
    private static final String SUNSET = "שקיעה:  ";


    public static void main(String[] args) throws IOException, DocumentException {

        List<WeekDto> weekDtoList = new ArrayList<>();
        WeekDto weekDto = new WeekDto();
        DayDto dayDto = new DayDto();
        dayDto.setDayInWeek("SUN");
        dayDto.setDayInMonth("22");
        dayDto.setSunrise("6.23");
        dayDto.setSunset("16.59");
        weekDto.setMonth("מרץ-אפריל");
        weekDto.setMonthHeb("אדר א-אדר ב");
        weekDto.setDayDtos(Arrays.asList(dayDto));
        for (int i = 0; i < 60; i++) {
            weekDtoList.add(weekDto);
        }

        FontProgram fontProgram = FontProgramFactory.createFont("Alef.ttf");
        PdfFont alefFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H);

        long startTime = System.currentTimeMillis();

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter("1.pdf"));
        Document doc = new Document(pdfDoc, PageSize.A6);
        doc.setMargins(0, 0, 0, 0);

        weekDtoList.forEach(week -> {
            Table table = new Table(6);
            table.setWidth(millimetersToPoints(105));
            table.setFixedLayout();
            table.setTextAlignment(TextAlignment.CENTER);
            table.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            Paragraph p1Title = new Paragraph();
            //p1Title.setFixedLeading(20);
            String s1Title = new StringBuffer(weekDto.getMonth()).reverse().toString();
            Text t1Title = new Text(s1Title).setFont(alefFont).setFontSize(18);
            p1Title.add(t1Title);
            p1Title.setTextAlignment(TextAlignment.CENTER);
            table.addCell(getCell(52.5f, p1Title, 1, 3, 0));

            Paragraph p2Title = new Paragraph();
            //p2Title.setFixedLeading(20);
            String s2Title = new StringBuffer(weekDto.getMonthHeb()).reverse().toString();
            Text t2Title = new Text(s2Title).setFont(alefFont).setFontSize(18);
            p2Title.add(t2Title);
            p2Title.setTextAlignment(TextAlignment.CENTER);
            table.addCell(getCell(52.5f, p2Title, 1, 3, 0));

            for (int i = 0; i < 7 ; i++) {
                Paragraph p1 = new Paragraph();
                p1.setFixedLeading(20);
                String dayInWeek = new StringBuffer(weekDto.getDayDtos().get(0).getDayInWeek()).append("\n").toString();
                Text tdayInWeek = new Text(dayInWeek).setFont(alefFont).setFontSize(7);
                String dayInMonth = new StringBuffer(weekDto.getDayDtos().get(0).getDayInMonth()).toString();
                Text tdayInMonth = new Text(dayInMonth).setFont(alefFont).setFontSize(22);
                p1.add(tdayInWeek);
                p1.add(tdayInMonth);
                p1.setTextAlignment(TextAlignment.CENTER);
                table.addCell(getCell(20f, p1, 1 ,1, i));


                Paragraph p2 = new Paragraph();
                p2.setFixedLeading(20);
                String sunriseTime = new StringBuffer(weekDto.getDayDtos().get(0).getSunrise()).toString();
                String sunrise = new StringBuffer(SUNRISE).reverse().append("\n").toString();
                Text tsunriseTime = new Text(sunriseTime).setFont(alefFont).setFontSize(6);
                Text tsunrise = new Text(sunrise).setFont(alefFont).setFontSize(6);
                String sunsetTime = new StringBuffer(weekDto.getDayDtos().get(0).getSunset()).toString();
                String sunset = new StringBuffer(SUNSET).reverse().append("\n").toString();
                Text tsunsetTime = new Text(sunsetTime).setFont(alefFont).setFontSize(6);
                Text tsunset = new Text(sunset).setFont(alefFont).setFontSize(6);
                p2.add(tsunriseTime);
                p2.add(tsunrise);
                p2.add(tsunsetTime);
                p2.add(tsunset);
                p2.setTextAlignment(TextAlignment.LEFT);
                table.addCell(getCell(65f, p2, 1, 4, i));

                Paragraph p3 = new Paragraph();
                p3.setFixedLeading(20);
                Text t3_1 = new Text("ןושאר\n").setFont(alefFont).setFontSize(9);
                Text t3_2 = new Text("טכ").setFont(alefFont).setFontSize(22);
                p3.add(t3_1);
                p3.add(t3_2);
                p3.setTextAlignment(TextAlignment.CENTER);
                table.addCell(getCell(20f, p3, 1, 1, i));
            }

            doc.add(table);
        });

        doc.close();

        long endTime = System.currentTimeMillis();
        System.out.println("total time is: " + (endTime - startTime) + " ms");
    }

    private static Cell getCell(float mm, Paragraph p, int row, int col, int i) {
        Cell cell = new Cell(row, col)
                .setBorder(null)
                .setHeight(millimetersToPoints(143/8));
        if(i < 6){
            cell.setBorderBottom(new DashedBorder(0.3f));
        }
        if(p != null){
            cell.add(p);
        }
        if(mm == 20){
            cell.setBackgroundColor(new DeviceRgb(238, 237, 236));
        }
        return cell;
    }
}
