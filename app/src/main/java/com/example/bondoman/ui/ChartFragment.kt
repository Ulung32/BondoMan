package com.example.bondoman.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentChartBinding

class ChartFragment : Fragment() {

    private lateinit var binding: FragmentChartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChartBinding.inflate(inflater, container, false)
        binding.apply {
            donutChart.donutColors = intArrayOf(
                Color.parseColor("#FFFFFF"),
                Color.parseColor("#DDDDDD"),
                Color.parseColor("#111111")
            )
            donutChart.animation.duration = animationDuration
            donutChart.animate(donutSet)
        }
        return binding.root
    }
    companion object {

        private val donutSet = listOf(
            20f,
            50f,
            30f
        )

        private const val animationDuration = 1000L
    }
}