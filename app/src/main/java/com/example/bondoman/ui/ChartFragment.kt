package com.example.bondoman.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.databinding.FragmentChartBinding
import com.example.bondoman.ui.Transaction.TransactionViewModel
import com.example.bondoman.ui.Transaction.TransactionViewModelFactory

class ChartFragment : Fragment() {

    private lateinit var binding: FragmentChartBinding
    private lateinit var transactionViewModel : TransactionViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartBinding.inflate(inflater, container, false)
        binding.apply {
            donutChart.donutColors = intArrayOf(
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#AAAAAA"),
                Color.parseColor("#444444")
            )
            donutChart.animation.duration = 1000L
            donutChart.donutThickness = 100F
            donutChart.animate(listOf(40F, 40F, 120F))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val repository = MainRepository(requireContext())
        val factory = TransactionViewModelFactory(repository)
        transactionViewModel = ViewModelProvider(this, factory)[TransactionViewModel::class.java]

        transactionViewModel.transactionList.observe(viewLifecycleOwner) { data ->
            var incomeFraction = data.filter { it.category == "PEMASUKAN" }.size.toFloat()/data.size*200
            var outcomeFraction = data.filter { it.category == "PENGELUARAN" }.size.toFloat()/data.size*200
            var otherFraction = 200f-incomeFraction-outcomeFraction

            Log.v("wf", incomeFraction.toString())
            Log.v("wf", outcomeFraction.toString())
            Log.v("wf", otherFraction.toString())


            binding.donutChart.animate(listOf(incomeFraction, outcomeFraction, otherFraction))
        }
    }
}