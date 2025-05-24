package com.naik.calculatorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.lang.Exception
import kotlin.math.log

class TimePeriodCalculator : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_period)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Time Period Calculator"

        val initialValueInput: EditText = findViewById(R.id.initialValueInput)
        val finalValueInput: EditText = findViewById(R.id.finalValueInput)
        val cagrInput: EditText = findViewById(R.id.cagrInput)
        val calculateTimePeriodButton: Button = findViewById(R.id.calculateTimePeriodButton)
        val timePeriodResultText: TextView = findViewById(R.id.timePeriodResultText)

        calculateTimePeriodButton.setOnClickListener {
            try {
                val initialValue = initialValueInput.text.toString().toDouble()
                val finalValue = finalValueInput.text.toString().toDouble()
                val cagr = cagrInput.text.toString().toDouble()

                if (initialValue <= 0 || finalValue <= 0 || cagr <= 0) {
                    Toast.makeText(this, "All values must be greater than zero", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val timePeriod = log(finalValue / initialValue, cagr / 100 + 1)
                timePeriodResultText.text = String.format("Time Period: %.2f periods", timePeriod)
            } catch (e: Exception) {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        }
    }
}