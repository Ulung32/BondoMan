package com.example.bondoman.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName="transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val _id: Int,
    val title: String,
    val category: String,
    val nominal: Number,
    val longitude: Number,
    val latitude: Number,
    val date: LocalDateTime
)
