package com.naik.calculatorapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import com.naik.calculatorapp.ui.theme.CalculatorAppTheme
import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.sqrt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import kotlin.math.pow

fun cumulativeNormalDistribution(x: Double): Double {
    val t = 1.0 / (1.0 + 0.2316419 * abs(x))
    val d = 0.3989423 * exp(-x * x / 2.0)
    val prob = d * (0.319381530 + t * (-0.356563782 + t * (1.781477937 + t * (-1.821255978 + t * 1.330274429))))
    return if (x > 0) 1.0 - prob else prob
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorAppTheme {
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet {
                            Text(
                                text = "Calculators",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .padding(top = 32.dp)
                            )
                            NavigationDrawerItem(
                                label = { Text("Black-Scholes Calculator") },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                modifier = Modifier.padding(16.dp)
                            )
                            NavigationDrawerItem(
                                label = { Text("Free Finance Calculator") },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        val intent = Intent(this@MainActivity, FreeFinanceCalculator::class.java)
                                        startActivity(intent)
                                    }
                                },
                                modifier = Modifier.padding(16.dp)
                            )
                            NavigationDrawerItem(
                                label = { Text("CAGR Calculator") },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        val intent = Intent(this@MainActivity, CAGRCalculator::class.java)
                                        startActivity(intent)
                                    }
                                },
                                modifier = Modifier.padding(16.dp)
                            )
                            NavigationDrawerItem(
                                label = { Text("Time Period Calculator") },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        val intent = Intent(this@MainActivity, TimePeriodCalculator::class.java)
                                        startActivity(intent)
                                    }
                                },
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    },
                    drawerState = drawerState
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopAppBar(
                                title = { Text("Calculator App") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        TabbedCalculatorScreen(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun TabbedCalculatorScreen(modifier: Modifier = Modifier) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Calculate Price", "Calculate Spot")

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        when (tabIndex) {
            0 -> PriceCalculatorScreen()
            1 -> SpotPriceCalculatorScreen()
        }
    }
}

@Composable
fun PriceCalculatorScreen() {
    var spotPrice by remember { mutableStateOf("") }
    var strikePrice by remember { mutableStateOf("") }
    var timeToMaturity by remember { mutableStateOf("") }
    var riskFreeRate by remember { mutableStateOf("") }
    var volatility by remember { mutableStateOf("") }
    var optionPrice by remember { mutableStateOf("") }
    var isCallOption by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { isCallOption = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isCallOption) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (isCallOption) Color.White else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Call Option")
            }
            OutlinedButton(
                onClick = { isCallOption = false },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (!isCallOption) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (!isCallOption) Color.White else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Put Option")
            }
        }

        OutlinedTextField(
            value = spotPrice,
            onValueChange = { spotPrice = it },
            label = { Text("Spot Price (S)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = strikePrice,
            onValueChange = { strikePrice = it },
            label = { Text("Strike Price (K)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = timeToMaturity,
            onValueChange = { timeToMaturity = it },
            label = { Text("Time to Maturity (T) in days") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = riskFreeRate,
            onValueChange = { riskFreeRate = it },
            label = { Text("Risk-Free Rate (r) in %") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = volatility,
            onValueChange = { volatility = it },
            label = { Text("Volatility (σ) in %") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Button(
            onClick = {
                val s = spotPrice.toDoubleOrNull() ?: 0.0
                val k = strikePrice.toDoubleOrNull() ?: 0.0
                val t = timeToMaturity.toDoubleOrNull() ?: 0.0
                val r = riskFreeRate.toDoubleOrNull() ?: 0.0
                val sigma = volatility.toDoubleOrNull() ?: 0.0

                val T_years = t / 365.0
                val r_annual = r / 100
                val sigma_annual = sigma / 100

                optionPrice = if (isCallOption) {
                    BlackScholesCalculator.calculateCallOptionPrice(
                        s, k, T_years, r_annual, sigma_annual
                    ).toString()
                } else {
                    BlackScholesCalculator.calculatePutOptionPrice(
                        s, k, T_years, r_annual, sigma_annual
                    ).toString()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate")
        }

        Text(
            text = "Option Price: ${String.format("%.2f", optionPrice.toDoubleOrNull() ?: 0.0)}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun SpotPriceCalculatorScreen() {
    var strikePrice by remember { mutableStateOf("") }
    var timeToMaturity by remember { mutableStateOf("") }
    var riskFreeRate by remember { mutableStateOf("") }
    var volatility by remember { mutableStateOf("") }
    var optionPrice by remember { mutableStateOf("") }
    var isCallOption by remember { mutableStateOf(true) }
    var spotPrice by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { isCallOption = true },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isCallOption) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (isCallOption) Color.White else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Call Option")
            }
            OutlinedButton(
                onClick = { isCallOption = false },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (!isCallOption) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (!isCallOption) Color.White else MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Put Option")
            }
        }

        OutlinedTextField(
            value = strikePrice,
            onValueChange = { strikePrice = it },
            label = { Text("Strike Price (K)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = timeToMaturity,
            onValueChange = { timeToMaturity = it },
            label = { Text("Time to Maturity (T) in days") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = riskFreeRate,
            onValueChange = { riskFreeRate = it },
            label = { Text("Risk-Free Rate (r) in %") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = volatility,
            onValueChange = { volatility = it },
            label = { Text("Volatility (σ) in %") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            value = optionPrice,
            onValueChange = { optionPrice = it },
            label = { Text("Option Price") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Button(
            onClick = {
                val k = strikePrice.toDoubleOrNull() ?: 0.0
                val t = timeToMaturity.toDoubleOrNull() ?: 0.0
                val r = riskFreeRate.toDoubleOrNull() ?: 0.0
                val sigma = volatility.toDoubleOrNull() ?: 0.0
                val p = optionPrice.toDoubleOrNull() ?: 0.0

                val T_years = t / 365.0
                val r_annual = r / 100
                val sigma_annual = sigma / 100

                val spot = BlackScholesCalculator.calculateSpotPrice(
                    p, k, T_years, r_annual, sigma_annual, isCallOption
                )

                spotPrice = String.format("%.2f", spot)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate Spot Price")
        }

        Text(
            text = "Spot Price: $spotPrice",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
