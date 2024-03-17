package com.example.bondoman.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentTransactionBinding

class TransactionFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentTransactionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }
}