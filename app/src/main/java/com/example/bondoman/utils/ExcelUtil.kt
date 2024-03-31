package com.example.bondoman.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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

}
