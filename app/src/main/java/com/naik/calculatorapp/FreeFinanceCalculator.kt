package com.naik.calculatorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.lang.Exception

class FreeFinanceCalculator : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_finance)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Free Finance Calculator"

        val returnsRateInput: EditText = findViewById(R.id.returnsRateInput)
        val priceInput: EditText = findViewById(R.id.priceInput)
        val daysInput: EditText = findViewById(R.id.daysInput)
        val calculateButton: Button = findViewById(R.id.calculateButton)
        val resultText: TextView = findViewById(R.id.resultText)

        calculateButton.setOnClickListener {
            try {
                val returnsRate = returnsRateInput.text.toString().toDouble()
                val price = priceInput.text.toString().toDouble()
                val days = daysInput.text.toString().toInt()

                if (returnsRate <= 0 || price <= 0 || days <= 0) {
                    Toast.makeText(this, "All values must be greater than zero", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val investmentValue = (1 / ((returnsRate / 100) / 365)) * price / days
                resultText.text = String.format("Investment Value: $%.2f", investmentValue)
            } catch (e: Exception) {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        }
    }
}