package com.naik.calculatorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.lang.Exception
import kotlin.math.pow

class CAGRCalculator : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cagr)

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "CAGR Calculator"

        val initialValueInput: EditText = findViewById(R.id.initialValueInput)
        val finalValueInput: EditText = findViewById(R.id.finalValueInput)
        val timePeriodInput: EditText = findViewById(R.id.timePeriodInput)
        val calculateCAGRButton: Button = findViewById(R.id.calculateCAGRButton)
        val cagrResultText: TextView = findViewById(R.id.cagrResultText)

        calculateCAGRButton.setOnClickListener {
            try {
                val initialValue = initialValueInput.text.toString().toDouble()
                val finalValue = finalValueInput.text.toString().toDouble()
                val timePeriod = timePeriodInput.text.toString().toDouble()

                if (initialValue <= 0 || finalValue <= 0 || timePeriod <= 0) {
                    Toast.makeText(this, "All values must be greater than zero", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val cagr = (finalValue / initialValue).pow(1.0 / timePeriod) - 1
                cagrResultText.text = String.format("CAGR: %.2f%%", cagr * 100)
            } catch (e: Exception) {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        }
    }
}