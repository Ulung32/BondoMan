package com.example.bondoman.ui.Transaction

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.EditTransactionActivity
import com.example.bondoman.Repository.MainRepository
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.databinding.TransactionItemBinding
import com.example.bondoman.utils.LocationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TransactionAdapter (private val context: Context,private val listData: Array<TransactionEntity>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    val locationClient = LocationClient(context, AppCompatActivity())
    class TransactionViewHolder private constructor(val binding: TransactionItemBinding) : RecyclerView.ViewHolder(binding.root){
        companion object{
            fun from (parent: ViewGroup): TransactionViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TransactionItemBinding.inflate(layoutInflater, parent, false)
                return TransactionViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.binding.titleTv.text = listData[position].title
        holder.binding.categoryTv.text = listData[position].category
        holder.binding.priceTv.text = "Rp. ".plus(listData[position].nominal)
        holder.binding.locationTv.text = locationClient.getLocationName(listData[position].latitude, listData[position].longitude)
        holder.binding.dateTv.text = listData[position].date

        holder.binding.locationTv.setOnClickListener {
            val latitude = listData[position].latitude
            val longitude = listData[position].longitude
            val locationUri = "geo:$latitude,$longitude?q=$latitude,$longitude"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(locationUri))
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Google Maps app is not installed", Toast.LENGTH_SHORT).show()
            }
        }

        holder.binding.deleteTransactionButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch{
                val repository = MainRepository(context)
                repository.deleteTransaction(listData[position])
            }
        }
        holder.binding.editTransactionButton.setOnClickListener {
            val intent = Intent(context, EditTransactionActivity::class.java)
            intent.putExtra("id", listData[position]._id)
            intent.putExtra("title", listData[position].title)
            intent.putExtra("category", listData[position].category)
            intent.putExtra("nominal", listData[position].nominal)
            intent.putExtra("latitude", listData[position].latitude)
            intent.putExtra("longitude", listData[position].longitude)
            intent.putExtra("date", listData[position].date)
            context.startActivity(intent)
        }
    }

}