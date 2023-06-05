package org.example.fe.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class PdfCreator {

    public static void main(String[] args) throws IOException, DocumentException, URISyntaxException {

        long startTime = System.currentTimeMillis();
        Document document = new Document(PageSize.A4);
        String filename = "1.pdf"; // Set the relative path and name for the output file
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        BaseFont bf = BaseFont.createFont("Alef.ttf", BaseFont.IDENTITY_H, true);
        Font font = new Font(bf, 14);
        ColumnText column = new ColumnText(writer.getDirectContent());
        column.setSimpleColumn(36, 770, 569, 36);
        column.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

        for (int i = 0; i < 100; i++) {
            String text = "שלום עולם" + i + "\n\n";
            Chunk c = new Chunk(text, font);
            // c.setBackground(BaseColor.RED);
            Paragraph paragraph = new Paragraph(c);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            column.addElement(paragraph);
            if(column.go() == 2){
                document.newPage();
                column.setSimpleColumn(36, 770, 569, 36);
                column.go();
            }
        }

        document.close();

        long endTime = System.currentTimeMillis();

        System.out.println("total time is: " + (endTime - startTime) + " ms");



     /*   Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));

        document.open();
        Font font = FontFactory.getFont("Alef.ttf");
        document.add(new Paragraph("שלום", font));
        document.add(new Paragraph("A Hello World PDF document שלום."));
        document.close();*/
    }

    private static void addTableHeader(PdfPTable table) {
        Stream.of("column header 1", "column header 2", "column header 3")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private static void addRows(PdfPTable table) {
        table.addCell("row 1, col 1");
        table.addCell("row 1, col 2");
        table.addCell("row 1, col 3");
    }

    private static void addCustomRows(PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {
        Path path = Paths.get(ClassLoader.getSystemResource("Java_logo.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(10);

        PdfPCell imageCell = new PdfPCell(img);
        table.addCell(imageCell);

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }
}
