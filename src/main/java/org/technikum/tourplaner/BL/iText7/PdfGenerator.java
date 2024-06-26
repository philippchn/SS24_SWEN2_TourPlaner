package org.technikum.tourplaner.BL.iText7;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.technikum.tourplaner.BL.models.TourLogModel;
import org.technikum.tourplaner.BL.models.TourModel;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PdfGenerator {
    public static final String TARGET_PDF = "target/report.pdf";
    public static void generateTourReport(TourModel tour, List<TourLogModel> tourLogs) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(TARGET_PDF);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph header = new Paragraph("Report of tour: " + tour.getName());
        header.setFontSize(20).setBold();
        document.add(header);

        for (TourLogModel tourLog : tourLogs){
            document.add(new Paragraph("Tour log").setBold());
            Paragraph paragraph = new Paragraph()
                    .add("Date: " + tourLog.getDate())
                    .add("\nComment: " + tourLog.getComment())
                    .add("\nDifficulty: " + tourLog.getDifficulty())
                    .add("\nTotal distance: " + tourLog.getTotalDistance())
                    .add("\nTotal time: " + tourLog.getTotalTime())
                    .add("\nRating: " + tourLog.getRating());
            document.add(paragraph);
        }

        openPdf();
        document.close();
    }

    public static void generateSummarizeReport(TourModel tour, List<TourLogModel> tourLogs) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(TARGET_PDF);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph header = new Paragraph("Summarize report of tour: " + tour.getName());
        header.setFontSize(20).setBold();
        document.add(header);

        int counter = 0;
        double difficulty = 0;
        double distance = 0;
        long time = 0;
        for (TourLogModel tourLog : tourLogs){
            counter++;
            difficulty = difficulty + tourLog.getDifficulty();
            distance = distance + tourLog.getTotalDistance();
            time = time + tourLog.getTotalTime();
        }

        Paragraph paragraph = new Paragraph()
                .add("Average difficulty: " + (difficulty/counter))
                .add("\nAverage distance: " + (distance/counter))
                .add("\nAverage time: " + (time/counter));
        document.add(paragraph);

        openPdf();
        document.close();
    }

    private static void openPdf() {
        try {
            File pdfFile = new File(TARGET_PDF);
            if (pdfFile.exists() && Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(pdfFile);
            } else {
                System.out.println("Error opening file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
