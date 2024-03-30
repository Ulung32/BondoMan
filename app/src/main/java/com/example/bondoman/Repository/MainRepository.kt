package com.example.bondoman.Repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Query
import androidx.room.Transaction
import com.example.bondoman.Room.TransactionDAO
import com.example.bondoman.Room.TransactionDatabase
import com.example.bondoman.Room.TransactionEntity
import javax.inject.Inject

class MainRepository (
    context: Context
){
    val transactionDatabase = TransactionDatabase.getInstance(context)
    val transactionDAO = transactionDatabase.transactionDao()
    val transactionList: LiveData<Array<TransactionEntity>> = transactionDAO.getAllTransactions()
    suspend fun getAllTransactions() = transactionDAO.getAllTransactions()

    suspend fun getTransaction(id:Int) = transactionDAO.getTransaction(id)

    suspend fun insertTransaction(transaction: TransactionEntity) = transactionDAO.insertTransaction(transaction)

    suspend fun updateTransaction(transaction: TransactionEntity) = transactionDAO.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: TransactionEntity) = transactionDAO.deleteTransaction(transaction)
}