package com.example.bondoman.ui.Transaction

import android.nfc.Tag
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bondoman.Room.TransactionEntity
import com.example.bondoman.databinding.TransactionItemBinding


class TransactionAdapter (private val listData: Array<TransactionEntity>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
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
        holder.binding.locationTv.text = listData[position].latitude.toString() + ", " + listData[position].longitude.toString()
        holder.binding.dateTv.text = listData[position].date
    }

}