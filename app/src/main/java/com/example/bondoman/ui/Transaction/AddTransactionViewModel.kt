package com.example.bondoman.ui.Transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionEntity
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    val repository: MainRepository
): ViewModel() {
    fun insertTransaction(transaction: TransactionEntity) = viewModelScope.launch {
        repository.insertTransaction(transaction)
    }
}