package com.example.bondoman.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bondoman.R

/**
 * A simple [Fragment] subclass.
 * Use the [ScannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScannerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scanner, container, false)
    }
}