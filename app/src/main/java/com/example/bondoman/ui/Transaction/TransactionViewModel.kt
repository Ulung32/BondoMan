package com.example.bondoman.ui.Transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionEntity
import kotlinx.coroutines.launch

class TransactionViewModel(val repository: MainRepository): ViewModel() {
    var transactionList: LiveData<Array<TransactionEntity>> = repository.transactionList

    fun insertTransaction(transaction: TransactionEntity) = viewModelScope.launch {
        repository.insertTransaction(transaction)
    }

    fun updateTransaction(transaction: TransactionEntity) = viewModelScope.launch {
        repository.updateTransaction(transaction)
    }

    fun deleteTransaction(transaction: TransactionEntity) = viewModelScope.launch {
        repository.deleteTransaction(transaction)
    }
}