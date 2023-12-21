package com.alterpat.budgettracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import android.app.DatePickerDialog
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_add_transaction.*
import kotlinx.coroutines.launch
import java.util.Calendar
class AddTransactionActivity : AppCompatActivity() {

    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        pickDateBtn.setOnClickListener {
            showDatePickerDialog()
        }

        addTransactionBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty()) {
                labelLayout.error = "Please enter a valid label"
            } else if (amount == null) {
                amountLayout.error = "Please enter a valid amount"
            } else {
                val transaction = Transaction(0, label, amount, description)
                insert(transaction)
            }
        }

        closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        val currentYear = c.get(Calendar.YEAR)
        val currentMonth = c.get(Calendar.MONTH)
        val currentDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                selectedYear = year
                selectedMonth = month
                selectedDay = day
                dateTextView.text = "$day/${month + 1}/$year"
            },
            currentYear,
            currentMonth,
            currentDay
        )

        datePickerDialog.show()
    }

    private fun insert(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            transaction.date = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}