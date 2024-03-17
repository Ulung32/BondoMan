package com.example.bondoman.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentNavbarBinding

class NavbarFragment : Fragment() {
    private lateinit var binding: FragmentNavbarBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNavbarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = requireActivity().findNavController(R.id.nav_host_fragment)

        binding.transactionButton.setOnClickListener {
            navController.navigate(R.id.transactionFragment)
        }
        binding.scannerButton.setOnClickListener {
            navController.navigate(R.id.scannerFragment)
        }
        binding.chartButton.setOnClickListener {
            navController.navigate(R.id.chartFragment)
        }
        binding.settingButton.setOnClickListener {
            navController.navigate(R.id.settingFragment)
        }
    }

}