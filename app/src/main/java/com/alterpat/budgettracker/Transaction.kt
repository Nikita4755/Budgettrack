package com.alterpat.budgettracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val label: String,
    val amount: Double,
    val description: String,
    @ColumnInfo(name = "date") var date: String = "" // Добавленное поле для хранения даты
) : Serializable

