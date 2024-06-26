package com.example.bondoman.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.bondoman.LoginActivity
import com.example.bondoman.MainActivity
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentSettingBinding
import com.example.bondoman.utils.ExcelUtil
import com.example.bondoman.utils.TokenManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    private lateinit var binding : FragmentSettingBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val excelUtil by lazy { ExcelUtil(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveTransaction.setOnClickListener{
            findNavController().navigate(R.id.saveTransactionFragment)
        }

        binding.randomizeTransactionBtn.setOnClickListener{
//            val intent = Intent("ACTION_RANDOMIZE_TRANSACTION")
            val intent = Intent(requireContext(), MainActivity::class.java).apply {
                action = "ACTION_RANDOMIZE_TRANSACTION"
            }
            requireContext().sendBroadcast(intent)
        }

        binding.twibbonBtn.setOnClickListener{
            findNavController().navigate(R.id.twibbonFragment)
        }

        binding.LogoutButton.setOnClickListener {
            val tokenManager = TokenManager(requireContext())
            tokenManager.removeToken()
            Toast.makeText(requireContext(), "Loged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //clear back stack
            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}