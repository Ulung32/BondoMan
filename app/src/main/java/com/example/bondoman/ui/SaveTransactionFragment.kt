package com.example.bondoman.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.databinding.FragmentSaveTransactionBinding
import com.example.bondoman.utils.ExcelUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SaveTransactionFragment : Fragment() {
    private lateinit var binding: FragmentSaveTransactionBinding
    private val excelUtil by lazy { ExcelUtil(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSaveTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = MainRepository(requireContext())


        binding.createExcelBtn.setOnClickListener {
            val fileName = binding.fileNameEditText.text.toString()

            if (fileName.isNotEmpty()) {
                repository.transactionList.observe(viewLifecycleOwner){it ->
                    lifecycleScope.launch{
                        excelUtil.saveExcelToFile(requireContext(), it, fileName)
                    }
                }
                Toast.makeText(requireContext(), "Creating Excel", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(requireContext(), "File name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sendEmailBtn.setOnClickListener {
            val fileName = binding.fileNameEditText.text.toString()

            if (fileName.isNotEmpty()) {
                repository.transactionList.observe(viewLifecycleOwner){it ->
                    lifecycleScope.launch{
                        excelUtil.saveExcelToFileAndSendEmail(it, fileName, "saktiwidyatmaja@gmail.com", "Bondoman Receipt List", "Here is the list")
                    }
                }
                Toast.makeText(requireContext(), "Creating Excel and sending to email", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(requireContext(), "File name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

    }

}