// MainActivity.kt con navegación básica
package com.example.steiner2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.steiner2.ui.FertilizerCalculatorScreen
import com.example.steiner2.ui.NutrientRecommendationScreen
import com.example.steiner2.ui.theme.Steiner2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Steiner2Theme {
                AppNavigation()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Hidropónica") },
                actions = {
                    Button(onClick = { navController.navigate("steiner") }) {
                        Text("Steiner")
                    }
                    Button(onClick = { navController.navigate("nutrientes") }) {
                        Text("Nutrientes")
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "steiner",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("steiner") { FertilizerCalculatorScreen() }
            composable("nutrientes") { NutrientRecommendationScreen() }
        }
    }
}
