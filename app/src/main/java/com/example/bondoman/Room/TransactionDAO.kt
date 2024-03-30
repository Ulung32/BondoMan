package com.example.bondoman.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
@Dao
interface TransactionDAO {
    @Query("SELECT * FROM TRANSACTIONS")
    fun getAllTransactions(): LiveData<Array<TransactionEntity>>

    @Query("SELECT * FROM TRANSACTIONS WHERE _id = :id")
    fun getTransaction(id:Int):TransactionEntity

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun  deleteTransaction(transaction: TransactionEntity)
}