package com.example.bondoman.ui

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.example.bondoman.R
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.databinding.FragmentChartBinding
import com.example.bondoman.ui.Transaction.TransactionViewModel
import com.example.bondoman.ui.Transaction.TransactionViewModelFactory
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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
            val incomeList = data.filter { it.category == "PEMASUKAN" }
            val outcomeList = data.filter { it.category == "PENGELUARAN" }
            val incomeFraction = incomeList.size.toFloat()
            val outcomeFraction = outcomeList.size.toFloat()

            val pieChart = binding.donutChart

            val dataPie = ArrayList<PieEntry>()
            dataPie.add(PieEntry(incomeFraction, "Pemasukan"))
            dataPie.add(PieEntry(outcomeFraction, "Pengeluaran"))

            val dataset = PieDataSet(dataPie, "Transaksi")
            dataset.setColors(Color.BLUE, Color.CYAN)
            dataset.setValueTextColor(Color.WHITE)
            dataset.valueTextSize = 14f

            val pieData = PieData(dataset)

            pieChart.setData(pieData)
            pieChart.centerText = "Transaksi"
            pieChart.setCenterTextSize(20f)
            pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD)
            pieChart.legend.textColor = Color.WHITE
            pieChart.description.text = ""
            pieChart.animate()
            pieChart.invalidate()

            val lineChart = binding.lineChart

            val dataIncome = ArrayList<Entry>()
            incomeList.mapIndexed() { i, e ->
                dataIncome.add(Entry(i.toFloat(), e.nominal.toFloat()))
            }
            val dataOutcome = ArrayList<Entry>()
            outcomeList.mapIndexed() { i, e ->
                dataOutcome.add(Entry(i.toFloat(), e.nominal.toFloat()))
            }

            val datasetIn = LineDataSet(dataIncome, "Pemasukan")
            val datasetOut = LineDataSet(dataOutcome, "Pengeluaran")
            datasetIn.setValueTextColor(Color.BLUE)
            datasetOut.setValueTextColor(Color.RED)
            datasetIn.setColor(Color.BLUE)
            datasetOut.setColor(Color.RED)

            val lineData = LineData(datasetIn, datasetOut)
            lineData.setValueTextColor(Color.WHITE)


            lineChart.setData(lineData)
            lineChart.legend.textColor = Color.WHITE
            lineChart.description.text = ""
            lineChart.setNoDataTextColor(Color.WHITE)
            lineChart.animate()
            lineChart.invalidate()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val layout = binding.chartLayout
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layout.orientation = LinearLayout.HORIZONTAL
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layout.orientation = LinearLayout.VERTICAL
        }
    }
}