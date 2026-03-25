package com.juagri.shared.ui.ledger

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.juagri.shared.R
import com.juagri.shared.domain.model.ledger.ExportPDFOld
import com.juagri.shared.domain.model.ledger.LedgerItemOld
import com.juagri.shared.utils.AppContextHolder
import com.juagri.shared.utils.getIndianCurrencyFormat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Android actual: PDF generation with iText can be added here.
 * For now returns NotAvailable; replace with real implementation using Context + iText.
 */
@SuppressLint("SimpleDateFormat", "UseCompatLoadingForDrawables")
actual fun createLedgerPdf(exportPDF: ExportPDFOld): DealerLedgerOldPdfResult {
    println("JUAgriAppTestLogs: ExportToPDF: start")
    println("JUAgriAppTestLogs: ExportToPDF: finYear=${exportPDF.finYear} finMonth=${exportPDF.finMonth}")
    println(
        "JUAgriAppTestLogs: ExportToPDF: dealer=${exportPDF.dealerItem.cCode} name=${exportPDF.dealerItem.cName}"
    )
    val catFont = Font(Font.FontFamily.TIMES_ROMAN, 14f, Font.BOLD)
    val subFont = Font(Font.FontFamily.TIMES_ROMAN, 12f, Font.BOLD)
    val smallBold = Font(Font.FontFamily.TIMES_ROMAN, 10f, Font.BOLD)
    val small = Font(Font.FontFamily.TIMES_ROMAN, 10f, Font.NORMAL)
    val context = AppContextHolder.get() ?: return DealerLedgerOldPdfResult.NotAvailable
    val document = Document()
    val filename = buildString {
        append("Report_")
        append(exportPDF.finYear)
        exportPDF.finMonth?.let { append("_").append(it) }
        append("_")
        append(SimpleDateFormat("HHmmss").format(Date()))
        append(".pdf")
    }
    return try {
        val outputUri: Uri?
        val outputStream = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val values = android.content.ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/JU_Agri_Files/Ledger/")
            }
            outputUri = context.contentResolver.insert(
                MediaStore.Files.getContentUri("external"),
                values
            )
            outputUri?.let { context.contentResolver.openOutputStream(it) }
        } else {
            val publicDir = File(Environment.getExternalStorageDirectory(), "Documents/JU_Agri_Files/Ledger")
            if (!publicDir.exists() && !publicDir.mkdirs()) {
                println("JUAgriAppTestLogs: ExportToPDF: failed to create ${publicDir.absolutePath}")
                return DealerLedgerOldPdfResult.NotAvailable
            }
            val pdfFile = File(publicDir, filename)
            outputUri = Uri.fromFile(pdfFile)
            FileOutputStream(pdfFile)
        }
        if (outputStream == null) {
            println("JUAgriAppTestLogs: ExportToPDF: output stream null")
            return DealerLedgerOldPdfResult.NotAvailable
        }
        println("JUAgriAppTestLogs: ExportToPDF: output=${outputUri}")
        PdfWriter.getInstance(document, outputStream)
        document.open()
        addMetaData(document)
        println("JUAgriAppTestLogs: ExportToPDF: ${outputUri}")
        val juLogo: Drawable? = ContextCompat.getDrawable(context, R.drawable.ju_logo)
        val bitDw = juLogo as BitmapDrawable
        val bmp = bitDw.bitmap
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        image.scaleAbsolute(160f, 55f)
        image.alignment = Image.MIDDLE
        document.add(image)

        var preface = Paragraph()
        preface = paragraph(preface, "JU Agri Sciences Pvt. Ltd", catFont)
        preface =
            paragraph(preface, "Unit No.2302, Tower 2, Express Trade Tower II B 36", subFont)
        preface = paragraph(preface, "Sector 132, Gautam Buddha Nagar", subFont)
        preface = paragraph(preface, "Noida, Uttar Pradesh - 201301", subFont)
        preface = paragraph(preface, "Tel: 0120 – 4328671", subFont)
        preface = addEmptyLine(preface, 2)
        preface = paragraph(
            preface,
            "Ledger Report of Mr. " + exportPDF.dealerItem.cName + " (" + exportPDF.dealerItem.cCode + ") as on " + SimpleDateFormat("dd-MM-yyyy").format(Date()),
            smallBold
        )
        val line = LineSeparator()
        preface.add(line)
        addEmptyLine(preface, 2)
        document.add(preface)
        val table = PdfPTable(6)
        table.widthPercentage = 100f
        table.addCell(headerCell("Invoice Date", smallBold))
        table.addCell(headerCell("Invoice No", smallBold))
        table.addCell(headerCell("Cheque No", smallBold))
        table.addCell(headerCell("Debit", smallBold))
        table.addCell(headerCell("Credit", smallBold))
        table.addCell(headerCell("Balance", smallBold))
        table.headerRows = 1

        table.addCell(getCell(Paragraph("Opening Balance", small), PdfPCell.ALIGN_RIGHT, 3))
        table.addCell(getCell(Paragraph("", small), PdfPCell.ALIGN_RIGHT))
        table.addCell(getCell(Paragraph("", small), PdfPCell.ALIGN_RIGHT))
        table.addCell(
            getCell(
                Paragraph(getIndianCurrencyFormat(exportPDF.dealerLedgerItem.openingBalanceAmount.toString()), small),
                PdfPCell.ALIGN_RIGHT
            )
        )

        exportPDF.dealerLedgerItem.ledgerItems.forEach { itm ->
            addLedgerRow(table, itm, small)
        }

        table.addCell(getCell(Paragraph("Closing Balance", small), PdfPCell.ALIGN_RIGHT, 3))
        table.addCell(getCell(Paragraph("", small), PdfPCell.ALIGN_RIGHT))
        table.addCell(getCell(Paragraph("", small), PdfPCell.ALIGN_RIGHT))
        table.addCell(
            getCell(
                Paragraph(getIndianCurrencyFormat(exportPDF.dealerLedgerItem.closingBalanceAmount.toString()), small),
                PdfPCell.ALIGN_RIGHT
            )
        )

        table.addCell(getCell(Paragraph("Total", small), PdfPCell.ALIGN_RIGHT, 3))
        table.addCell(
            getCell(
                Paragraph(getIndianCurrencyFormat(exportPDF.dealerLedgerItem.debit.toString()), small),
                PdfPCell.ALIGN_RIGHT
            )
        )
        table.addCell(
            getCell(
                Paragraph(getIndianCurrencyFormat(exportPDF.dealerLedgerItem.credit.toString()), small),
                PdfPCell.ALIGN_RIGHT
            )
        )
        table.addCell(getCell(Paragraph("", small), PdfPCell.ALIGN_RIGHT))

        document.add(table)
        document.close()
        println("JUAgriAppTestLogs: ExportToPDF: completed")
        outputStream.close()
        DealerLedgerOldPdfResult.FilePath(outputUri.toString(), filename)
    } catch (e: IOException) {
        e.printStackTrace()
        println("JUAgriAppTestLogs: ExportToPDF: IOException=${e.message}")
        DealerLedgerOldPdfResult.NotAvailable
    } catch (e: Exception) {
        e.printStackTrace()
        println("JUAgriAppTestLogs: ExportToPDF: Exception=${e.message}")
        DealerLedgerOldPdfResult.NotAvailable
    }
}

private fun addEmptyLine(paragraph: Paragraph, number: Int): Paragraph {
    for (i in 0 until number) {
        paragraph.add(Paragraph(" "))
    }
    return paragraph
}

private fun paragraph(paragraph: Paragraph, content: String, font: Font): Paragraph {
    val pg = Paragraph(content, font)
    pg.alignment = Paragraph.ALIGN_CENTER
    paragraph.add(pg)
    return paragraph
}

private fun headerCell(text: String, font: Font): PdfPCell {
    val cell = PdfPCell(Paragraph(text, font))
    cell.horizontalAlignment = Element.ALIGN_CENTER
    return cell
}

private fun addLedgerRow(table: PdfPTable, item: LedgerItemOld, font: Font) {
    table.addCell(getCell(Paragraph(item.invdate ?: "", font), PdfPCell.ALIGN_CENTER))
    table.addCell(getCell(Paragraph(item.docno ?: "", font), PdfPCell.ALIGN_CENTER))
    table.addCell(getCell(Paragraph(item.chqno ?: "", font), PdfPCell.ALIGN_CENTER))
    table.addCell(getCell(Paragraph(getIndianCurrencyFormat(item.dbamt.toString()), font), PdfPCell.ALIGN_RIGHT))
    table.addCell(getCell(Paragraph(getIndianCurrencyFormat(item.cramt.toString()), font), PdfPCell.ALIGN_RIGHT))
    table.addCell(getCell(Paragraph(getIndianCurrencyFormat(item.balamt.toString()), font), PdfPCell.ALIGN_RIGHT))
}

private fun getCell(text: Paragraph, alignment: Int, colSpan: Int = 1): PdfPCell {
    val cell = PdfPCell(text)
    cell.horizontalAlignment = alignment
    cell.colspan = colSpan
    return cell
}

private fun addMetaData(document: Document) {
    document.addTitle("Customer Ledger Report")
    document.addSubject("Ledger Report")
    document.addKeywords("Java, PDF, iText")
    document.addAuthor("JU Agri Sciences")
    document.addCreator("JU Agri Sciences")
}

actual fun shareLedger(path: String) {
    val context = AppContextHolder.get() ?: return
    val uri = if (path.startsWith("content://")) {
        Uri.parse(path)
    } else {
        val file = File(path)
        if (!file.exists()) return
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    val chooser = Intent.createChooser(shareIntent, "Share Ledger").apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(chooser)
}