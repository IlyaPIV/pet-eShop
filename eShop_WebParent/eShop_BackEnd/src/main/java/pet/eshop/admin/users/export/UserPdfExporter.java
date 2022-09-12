package pet.eshop.admin.users.export;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import pet.eshop.common.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporter{
    public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/pdf", "pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setSize(18);
        font.setColor(Color.BLUE);
        font.setStyle(Font.BOLD);

        Paragraph paragraph = new Paragraph("List of Users", font);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);                                     //используем всю ширину
        table.setSpacingBefore(10);                                         //margin перед таблицей
        table.setWidths(new float[]{1.2f, 3.5f, 3.0f, 3.0f, 3.0f, 1.7f});   //ширина колонок

        writeTableHeader(table);

        writeTableData(table, listUsers);

        document.add(table);

        document.close();
    }

    private void writeTableData(PdfPTable table, List<User> listUsers) {
        for (User user:
             listUsers) {
            table.addCell(String.valueOf(user.getId()));
            table.addCell(user.getEmail());
            table.addCell(user.getFirstName());
            table.addCell(user.getLastName());
            table.addCell(user.getRoles().toString());
            table.addCell(String.valueOf(user.isEnabled()));
        }
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setSize(14);
        font.setColor(Color.WHITE);
        font.setStyle(Font.BOLD);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("E-mail", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("First Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Last Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Roles", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Enabled", font));
        table.addCell(cell);
    }
}
