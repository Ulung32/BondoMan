package com.example.bondoman.Room

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TransactionEntity::class], version = 2)
abstract class TransactionDatabase : RoomDatabase(){
    abstract fun transactionDao(): TransactionDAO

    companion object{
        @Volatile
        private var INSTANCE : TransactionDatabase? = null

        fun getInstance(context: Context) : TransactionDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "transaction_database"
                ).fallbackToDestructiveMigration().build().also{
                    Log.d("DB", "TransactionDatabase instance created")
                    INSTANCE = it
                }
            }
        }

    }
}