package org.technikum.tourplaner.BL.iText7;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.technikum.tourplaner.BL.models.TourLogModel;
import org.technikum.tourplaner.BL.models.TourModel;

import java.io.FileNotFoundException;
import java.util.List;

public class PdfGenerator {
    public static final String TARGET_PDF = "target/report.pdf";
    public static void generatePdf(TourModel tour, List<TourLogModel> tourLogs) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(TARGET_PDF);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Paragraph header = new Paragraph("Report of tour: " + tour.getName());
        document.add(header);

        for (TourLogModel tourLog : tourLogs){
            Paragraph paragraph = new Paragraph(tourLog.getObjectStringView());
            document.add(paragraph);
        }

        document.close();
    }
}
