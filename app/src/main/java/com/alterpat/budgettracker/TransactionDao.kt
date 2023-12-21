package com.alterpat.budgettracker
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * from transactions")
    fun getAll(): List<Transaction>

    @Insert
    fun insertAll(vararg transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Update
    fun update(vararg transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getTransactionsBetweenDatesSorted(startDate: String, endDate: String): List<Transaction>

}