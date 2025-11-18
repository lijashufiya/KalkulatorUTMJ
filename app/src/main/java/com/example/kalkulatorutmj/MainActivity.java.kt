package com.example.kalkulatorutmj

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {
    
    private lateinit var txtScreen: TextView
    
    private var lastNumeric: Boolean = false 
    private var stateError: Boolean = false  
    private var lastDot: Boolean = false     

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtScreen = findViewById(R.id.txtScreen)
        setClickListeners()
    }

    private fun setClickListeners() {
        
        val numericListener = View.OnClickListener { v ->
            val button = v as Button
            if (stateError) {
                
                txtScreen.text = button.text
                stateError = false
            } else {
                
                txtScreen.append(button.text)
            }
            lastNumeric = true
        }
        
        val numericButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        numericButtons.forEach { id ->
            findViewById<Button>(id).setOnClickListener(numericListener)
        }
        
        val operatorListener = View.OnClickListener { v ->
            if (lastNumeric && !stateError) {
                val button = v as Button
                txtScreen.append(button.text)
                lastNumeric = false
                lastDot = false 
            }
        }
        
        val operatorButtons = listOf(R.id.btnAdd, R.id.btnSubtract, R.id.btnMultiply, R.id.btnDivide)
        operatorButtons.forEach { id ->
            findViewById<Button>(id).setOnClickListener(operatorListener)
        }
        
        findViewById<Button>(R.id.btnDot).setOnClickListener {
            if (lastNumeric && !stateError && !lastDot) {
                txtScreen.append(".")
                lastNumeric = false
                lastDot = true
            }
        }

        
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            txtScreen.text = ""
            lastNumeric = false
            stateError = false
            lastDot = false
        }

        
        findViewById<Button>(R.id.btnBackspace).setOnClickListener {
            val currentText = txtScreen.text.toString()
            if (currentText.isNotEmpty()) {
                txtScreen.text = currentText.substring(0, currentText.length - 1)
            }
        }

        
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            onEqual()
        }
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val expressionString = txtScreen.text.toString()
            try {
                
                val expression = ExpressionBuilder(expressionString).build()
                val result = expression.evaluate()
                txtScreen.text = result.toString()
                lastDot = true 
            } catch (ex: ArithmeticException) {
                
                txtScreen.text = "Error"
                stateError = true
                lastNumeric = false
            } catch (ex: Exception) {
                
                txtScreen.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}
