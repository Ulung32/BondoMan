package com.example.bondoman.ui.Transaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bondoman.AddTransactionActivity
import com.example.bondoman.R
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionDatabase
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.databinding.FragmentTransactionBinding
import java.time.LocalDateTime
import java.util.ArrayList

class TransactionFragment() : Fragment() {
    private lateinit var binding : FragmentTransactionBinding
    private lateinit var transactionViewModel : TransactionViewModel
//    private val cartAdapter by lazy { TransactionAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.transactionRv.layoutManager = LinearLayoutManager(context)

        val repository = MainRepository(requireContext())
        val factory = TransactionViewModelFactory(repository)
        Log.v("TESS", "Masuk")
        transactionViewModel = ViewModelProvider(this, factory)[TransactionViewModel::class.java]

        transactionViewModel.transactionList.observe(viewLifecycleOwner){ data ->
            // Update RecyclerView adapter with new transaction data
            binding.transactionRv.adapter = TransactionAdapter(requireContext(), data)
            binding.addTransactionBtn.setOnClickListener{
                val intent = Intent(context, AddTransactionActivity::class.java)
                context?.startActivity(intent)
            }
        }
    }

}