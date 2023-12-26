package com.alterpat.budgettracker
import android.widget.DatePicker
import android.app.DatePickerDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.widget.Button

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_filter_transactions.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FilterTransactionsActivity : AppCompatActivity() {

    private lateinit var textViewStartDate: TextView
    private lateinit var textViewEndDate: TextView
    private lateinit var transactionAdapter: TransactionAdapter
    private var startDate: String = ""
    private var endDate: String = ""

    private lateinit var db: AppDatabase
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_transactions)

        db = Room.databaseBuilder(this, AppDatabase::class.java, "transactions")
            .fallbackToDestructiveMigration()
            .build()


        recyclerViewTransactions.layoutManager = LinearLayoutManager(this)
        transactionAdapter =
            TransactionAdapter(emptyList()) // Инициализируйте свой адаптер под свои нужды
        recyclerViewTransactions.layoutManager = LinearLayoutManager(this)
        recyclerViewTransactions.adapter = transactionAdapter
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "transactions")
            .fallbackToDestructiveMigration()
            .build()

        textViewStartDate = findViewById(R.id.textViewStartDate)
        textViewEndDate = findViewById(R.id.textViewEndDate)

        val buttonSelectStartDate = findViewById<Button>(R.id.buttonSelectStartDate)
        val buttonSelectEndDate = findViewById<Button>(R.id.buttonSelectEndDate)
        val buttonApplyFilter = findViewById<Button>(R.id.buttonApplyFilter)

        buttonSelectStartDate.setOnClickListener {
            showDatePickerDialog { date ->
                startDate = date
                textViewStartDate.text = startDate
            }
        }

        buttonSelectEndDate.setOnClickListener {
            showDatePickerDialog { date ->
                endDate = date
                textViewEndDate.text = endDate
            }
        }


        buttonApplyFilter.setOnClickListener {
            if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
                applyFilter(startDate, endDate)
            } else {
                // Обработка случая, когда startDate или endDate равны null
            }
        }
    }

    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val selectedDate = dateFormat.format(calendar.time)
                onDateSelected(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    private fun applyFilter(startDate: String, endDate: String) {
        lifecycleScope.launch {
            // Перемещаем выполнение операций с базой данных в фоновый поток
            withContext(Dispatchers.IO) {
                val filteredTransactions =
                    db.transactionDao().getTransactionsBetweenDatesSorted(startDate, endDate)

                // Вернемся в основной поток для обновления UI
                withContext(Dispatchers.Main) {

                    // Например, обновляем RecyclerView с новым списком транзакций
                    transactionAdapter.newsetData(filteredTransactions)
                }
            }
        }
    }
}


