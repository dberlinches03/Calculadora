package com.example.calculadora

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculadora.ui.theme.CalculadoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraTheme {
                Calculadora()
            }
        }
    }
}

@Composable
fun Calculadora() {

    var numero1 by remember { mutableStateOf("") }
    var numero2 by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    fun operar(operador: String) {
        val n1 = numero1.toDoubleOrNull()
        val n2 = numero2.toDoubleOrNull()

        resultado = if (n1 != null && n2 != null) {
            when (operador) {
                "+" -> (n1 + n2).toString()
                "-" -> (n1 - n2).toString()
                "*" -> (n1 * n2).toString()
                "/" -> if (n2.toInt() != 0) (n1/n2).toString() else "Error: no se puede dividir por cero"
                else -> "Operador inválido"
            }
        } else {
            "Introduce números válidos"
        }
    }

    OutlinedTextField(
        value = numero1,
        onValueChange = { numero1 = it },
        label = { Text("Número 1") }
    )

    OutlinedTextField(
        value = numero2,
        onValueChange = { numero2 = it },
        label = { Text("Número 2") }
    )
}

@Preview(showBackground = true)
@Composable
fun CalculadoraPreview() {
    CalculadoraTheme {
        Calculadora()
    }
}