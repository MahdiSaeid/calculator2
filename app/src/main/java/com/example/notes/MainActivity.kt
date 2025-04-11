package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var current = ""
    private var operator = ""
    private var firstNumber = ""
    private var justEvaluated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        display = findViewById(R.id.tvDisplay)

        val buttons = listOf(
            "AC", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "−",
            "1", "2", "3", "+",
            "0", ".", "="
        )

        for (b in buttons) {
            val btn = findViewById<Button>(resources.getIdentifier("btn_$b", "id", packageName))
            btn?.setOnClickListener { handleInput(b) }
        }
    }

    private fun handleInput(input: String) {
        when (input) {
            "AC" -> {
                current = ""
                operator = ""
                firstNumber = ""
                updateDisplay("0")
            }
            "+/-" -> {
                if (current.isNotEmpty()) {
                    current = if (current.startsWith("-")) current.drop(1) else "-$current"
                    updateDisplay(current)
                }
            }
            "%" -> {
                current = (current.toDoubleOrNull()?.div(100)).toString()
                updateDisplay(current)
            }
            "÷", "×", "−", "+" -> {
                if (current.isNotEmpty()) {
                    operator = input
                    firstNumber = current
                    current = ""
                }
            }
            "=" -> {
                val result = calculate()
                updateDisplay(result)
                current = result
                operator = ""
                firstNumber = ""
                justEvaluated = true
            }
            "." -> {
                if (!current.contains(".")) {
                    current += "."
                    updateDisplay(current)
                }
            }
            else -> {
                if (justEvaluated) {
                    current = ""
                    justEvaluated = false
                }
                current += input
                updateDisplay(current)
            }
        }
    }

    private fun updateDisplay(value: String) {
        display.text = if (value.endsWith(".0")) value.dropLast(2) else value
    }

    private fun calculate(): String {
        val num1 = firstNumber.toDoubleOrNull() ?: return "Error"
        val num2 = current.toDoubleOrNull() ?: return "Error"

        val result = when (operator) {
            "+" -> num1 + num2
            "−" -> num1 - num2
            "×" -> num1 * num2
            "÷" -> if (num2 != 0.0) num1 / num2 else return "Error"
            else -> return "Error"
        }
        return result.toString()
    }
}
