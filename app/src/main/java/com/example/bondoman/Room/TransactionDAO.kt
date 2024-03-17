package com.example.bondoman.Room

import androidx.lifecycle.LiveData
import androidx.room.Query

interface TransactionDAO {
    @Query("SELECT * FROM TRANSACTIONS")
    suspend fun getAllTransactions(): LiveData<List<TransactionEntity>>

    @Query("SELECT * FROM TRANSACTIONS WHERE _id = :id")
    suspend fun getTransaction(id:Int):TransactionEntity
}