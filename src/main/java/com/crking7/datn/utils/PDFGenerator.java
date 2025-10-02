package com.crking7.datn.utils;

import com.crking7.datn.models.OrderItem;
import com.crking7.datn.web.dto.request.AddInvoiceRequest;
import com.crking7.datn.web.dto.request.OrderItemRequest;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PDFGenerator {
    public byte[] generateInvoicePdf(AddInvoiceRequest addInvoiceRequest){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // Tạo tài liệu với kích thước tùy chỉnh (80mm x 200mm)
            Rectangle pageSize = new Rectangle(300, 567); // 80mm x 200mm (1 inch = 72 points)
            Document document = new Document(pageSize, 10, 10, 10, 10);
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            // Font setup
            BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 14, Font.BOLD);
            Font regularFont = new Font(baseFont, 10, Font.NORMAL);
            Font boldFont = new Font(baseFont, 10, Font.BOLD);

            // Header
            Paragraph header = new Paragraph("Clothes Store", titleFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Paragraph address = new Paragraph("355 Phú Diễn, Nam Từ Liêm, Hà Nội", regularFont);
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(address);

            Paragraph phone = new Paragraph("ĐT: 024.666.9999 - 035.999.8888", regularFont);
            phone.setAlignment(Element.ALIGN_CENTER);
            document.add(phone);
            document.add(new Paragraph(" ", regularFont));

            // Sub-header
            Paragraph subHeader = new Paragraph("HÓA ĐƠN THANH TOÁN", boldFont);
            subHeader.setAlignment(Element.ALIGN_CENTER);
            document.add(subHeader);
            document.add(new Paragraph(" ", regularFont));

            // Details
            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(100);
            detailsTable.setWidths(new int[]{1, 2});

            detailsTable.addCell(createCell("Mã HĐ:", boldFont));
            detailsTable.addCell(createCell(addInvoiceRequest.getCode(), regularFont));

            detailsTable.addCell(createCell("Ngày in:", boldFont));
            detailsTable.addCell(createCell(LocalDateTime.now().format( DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), regularFont));

            detailsTable.addCell(createCell("Thu ngân:", boldFont));
            detailsTable.addCell(createCell("ADMIN", regularFont));

            detailsTable.addCell(createCell("Khách hàng:", boldFont));
            detailsTable.addCell(createCell(addInvoiceRequest.getFullName(), regularFont));

            document.add(detailsTable);
            document.add(new Paragraph(" ", regularFont));

            // Item Table
            PdfPTable itemTable = new PdfPTable(4);
            itemTable.setWidthPercentage(100);
            itemTable.setWidths(new int[]{5, 1, 2, 3});

            itemTable.addCell(createCell("Tên sản phẩm", boldFont));
            itemTable.addCell(createCell("SL", boldFont));
            itemTable.addCell(createCell("Đơn giá", boldFont));
            itemTable.addCell(createCell("T.tiền", boldFont));

            // Định dạng tiền tệ theo chuẩn quốc tế (ví dụ: Việt Nam)
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            long totalAmount = 0;

            for (OrderItemRequest item : addInvoiceRequest.getItems()) {
                itemTable.addCell(createCell(item.getProductName(), regularFont));
                itemTable.addCell(createCell(String.valueOf(item.getQuantity()), regularFont));
                itemTable.addCell(createCell(currencyFormatter.format(item.getSellPrice()), regularFont));
                itemTable.addCell(createCell(currencyFormatter.format((long) item.getSellPrice() * item.getQuantity()), regularFont));

                totalAmount += (long) item.getSellPrice() * item.getQuantity();
            }

            // Total
            document.add(itemTable);
            document.add(new Paragraph(" ", regularFont));

            Paragraph total = new Paragraph("Tổng cộng: " + currencyFormatter.format(totalAmount), boldFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            // Footer
            Paragraph footer = new Paragraph("Cảm ơn Quý Khách - Hẹn Gặp Lại", regularFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph(" ", regularFont));
            document.add(footer);

            document.close();

            // Trả về dữ liệu PDF dưới dạng byte array
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}
