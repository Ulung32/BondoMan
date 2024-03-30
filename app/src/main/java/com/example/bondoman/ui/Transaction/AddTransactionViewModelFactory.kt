package com.example.bondoman.ui.Transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.Repository.MainRepository

class AddTransactionViewModelFactory (val repository: MainRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddTransactionViewModel(repository) as T
    }
}