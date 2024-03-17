package com.example.bondoman.Repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.bondoman.Room.TransactionDAO
import com.example.bondoman.Room.TransactionEntity
import javax.inject.Inject

class MainRepository @Inject constructor(
    val transactionDAO: TransactionDAO
){
    suspend fun getAllTransactions() = transactionDAO.getAllTransactions()

    suspend fun getTransaction(id:Int) = transactionDAO.getTransaction(id)
}