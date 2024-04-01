package com.example.bondoman.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.bondoman.Room.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream

class ExcelUtil(private val context: Context) {
    suspend fun saveExcelToFile(
        context: Context,
        listData: Array<TransactionEntity>,
        fileName: String
    ) {
        val workbook = XSSFWorkbook()

        val sheet = workbook.createSheet("Sheet")

        val columnName: Row = sheet.createRow(0)

        val headers = arrayOf("id", "Title", "Category", "Nominal", "Location", "Date")
        for ((index, header) in headers.withIndex()) {
            val cell: Cell = columnName.createCell(index)
            cell.setCellValue(header)
        }

        for ((rowIndex, rowData) in listData.withIndex()) {
            val row: Row = sheet.createRow(rowIndex + 1)

            val id: Cell = row.createCell(0)
            val title: Cell = row.createCell(1)
            val category: Cell = row.createCell(2)
            val nominal: Cell = row.createCell(3)
            val location: Cell = row.createCell(4)
            val date: Cell = row.createCell(5)

            val locationClient = LocationClient(context, AppCompatActivity())
            val locationName = locationClient.getLocationName(rowData.latitude, rowData.longitude)

            id.setCellValue(rowData._id.toString())
            title.setCellValue(rowData.title)
            category.setCellValue(rowData.category)
            nominal.setCellValue(rowData.nominal.toDouble())
            location.setCellValue(locationName)
            date.setCellValue(rowData.date)
        }

        val file = File(context.getExternalFilesDir(null), fileName+".xlsx")
        val fileOutputStream = FileOutputStream(file)
        workbook.write(fileOutputStream)
        fileOutputStream.close()
    }

    suspend fun saveExcelToFileAndSendEmail(
        listData: Array<TransactionEntity>,
        fileName: String,
        recipientEmail: String,
        subject: String,
        message: String
    ) {
        // Membuat file Excel
        saveExcelToFile(context, listData, fileName)

        // Mendapatkan URI dari file Excel yang disimpan
        val fileUri = getFileUri(fileName)

        // Mengirim email dengan melampirkan file Excel
        sendEmailWithAttachment(fileUri, recipientEmail, subject, message)
    }

    private fun getFileUri(fileName: String): Uri {
        val file = File(context.getExternalFilesDir(null), "$fileName.xlsx")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
        } else {
            Uri.fromFile(file)
        }
    }

    private fun sendEmailWithAttachment(
        fileUri: Uri,
        recipientEmail: String,
        subject: String,
        message: String
    ) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "application/vnd.ms-excel"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)
        emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }
}
