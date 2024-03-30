package com.example.bondoman.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.LocalTime

@Entity(tableName="transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val _id: Int = 0,
    val title: String,
    val category: String,
    val nominal: Int,
    val longitude: Int,
    val latitude: Int,
    val date: String
)
