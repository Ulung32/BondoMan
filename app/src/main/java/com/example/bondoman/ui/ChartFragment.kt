package com.example.bondoman.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.databinding.FragmentChartBinding
import com.example.bondoman.ui.Transaction.TransactionViewModel
import com.example.bondoman.ui.Transaction.TransactionViewModelFactory
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class ChartFragment : Fragment() {

    private lateinit var binding: FragmentChartBinding
    private lateinit var transactionViewModel : TransactionViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val repository = MainRepository(requireContext())
        val factory = TransactionViewModelFactory(repository)
        transactionViewModel = ViewModelProvider(this, factory)[TransactionViewModel::class.java]

        transactionViewModel.transactionList.observe(viewLifecycleOwner) { data ->
            val incomeFraction = data.filter { it.category == "PEMASUKAN" }.size.toFloat()/data.size*200
            val outcomeFraction = data.filter { it.category == "PENGELUARAN" }.size.toFloat()/data.size*200

            val pieChart = binding.donutChart

            val data = ArrayList<PieEntry>()
            data.add(PieEntry(incomeFraction, "Pemasukan"))
            data.add(PieEntry(outcomeFraction, "Pengeluaran"))

            val dataset = PieDataSet(data, "Transaksi")
            dataset.setColors(Color.BLUE, Color.CYAN)
            dataset.setValueTextColor(Color.BLACK)

            val pieData = PieData(dataset)

            pieChart.setData(pieData)
            pieChart.centerText = "Transaksi"
            pieChart.animate()
            pieChart.invalidate()
        }
    }
}