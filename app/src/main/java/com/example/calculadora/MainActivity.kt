package com.example.calculadora

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculadora.ui.theme.CalculadoraTheme
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "login") {

                composable("login") {
                    GoogleLoginScreen(
                        onLoginSuccess = {
                            signInWithGoogle { success ->
                                if (success) navController.navigate("calculadora")
                            }
                        }
                    )
                }
                composable("calculadora") {
                    Calculadora()
                }
            }
        }
    }

    private fun signInWithGoogle(onResult: (Boolean) -> Unit) {
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("829701961126-6a4cjjisvgbnriltveqs7g7e65250l0n.apps.googleusercontent.com")
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(this@MainActivity, request)
                android.util.Log.d("GOOGLE_AUTH", "¡Éxito! Credencial recibida.")
                onResult(true)
            } catch (e: androidx.credentials.exceptions.GetCredentialException) {
                // ESTO NOS DARÁ LA CLAVE
                android.util.Log.e("GOOGLE_AUTH", "Error de Credenciales: ${e.type}")
                android.util.Log.e("GOOGLE_AUTH", "Mensaje: ${e.message}")
                onResult(false)
            } catch (e: Exception) {
                android.util.Log.e("GOOGLE_AUTH", "Error inesperado: ${e.message}", e)
                onResult(false)
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

    Column (
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Calculadora", style = MaterialTheme.typography.headlineMedium)
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

        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
            Button(onClick = { operar("+") }) { Text("+") }
            Button(onClick = { operar("-") }) { Text("-") }
            Button(onClick = { operar("*") }) { Text("x") }
            Button(onClick = { operar("/") }) { Text("÷") }
            Button(onClick =  {
                numero1 = ""
                numero2 = ""
                resultado = ""
            }) {
                Text("c")
            }
        }
        Text("Resultado: $resultado", style = MaterialTheme.typography.bodyLarge)

    }
}
@Preview(showBackground = true)
@Composable
fun CalculadoraPreview() {
    CalculadoraTheme {
        Calculadora()
    }
}