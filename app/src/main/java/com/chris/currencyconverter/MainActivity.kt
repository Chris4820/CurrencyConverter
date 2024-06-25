package com.chris.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.chris.currencyconverter.ui.theme.CurrencyConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CurrencyConverterApp()
                }
            }
        }
    }
}

@Composable
fun CurrencyConverterApp() {
    var inputAmount by remember { mutableStateOf("") }
    var convertedAmount by remember { mutableStateOf("Converted Amount: ") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("EUR") }
    val currencies = listOf("USD", "EUR", "BRL", "JPY")

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = inputAmount,
            onValueChange = { inputAmount = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Placeholder for conversion logic
                }
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CurrencyDropdownMenu(currencies, fromCurrency) { fromCurrency = it }
        Spacer(modifier = Modifier.height(8.dp))
        CurrencyDropdownMenu(currencies, toCurrency) { toCurrency = it }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Placeholder for button click logic
        }) {
            Text(text = "Convert")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = convertedAmount)
    }
}

@Composable
fun CurrencyDropdownMenu(currencies: List<String>, selectedCurrency: String, onCurrencySelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selectedCurrency)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(
                    text = { Text(currency) },
                    onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun convertCurrency(amount: Double?, fromCurrency: String, toCurrency: String): String {
    if (amount == null) {
        return "Invalid amount"
    }
    val conversionRates = mapOf(
        "USD" to mapOf("EUR" to 0.85, "BRL" to 5.20, "JPY" to 110.00),
        "EUR" to mapOf("USD" to 1.18, "BRL" to 6.10, "JPY" to 129.53),
        "BRL" to mapOf("USD" to 0.19, "EUR" to 0.16, "JPY" to 21.30),
        "JPY" to mapOf("USD" to 0.0091, "EUR" to 0.0077, "BRL" to 0.047)
    )

    val rate = conversionRates[fromCurrency]?.get(toCurrency) ?: return "Conversion rate not found"

    val convertedAmount = amount * rate;
    return "Converted Amount: $convertedAmount $toCurrency";
}
