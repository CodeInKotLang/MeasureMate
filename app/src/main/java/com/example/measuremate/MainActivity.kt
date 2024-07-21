package com.example.measuremate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.measuremate.presentation.add_item.AddItemScreen
import com.example.measuremate.presentation.dashboard.DashboardScreen
import com.example.measuremate.presentation.details.DetailsScreen
import com.example.measuremate.presentation.signin.SignInScreen
import com.example.measuremate.presentation.theme.MeasureMateTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeasureMateTheme {
                val windowSizeClass = calculateWindowSizeClass(activity = this)
                DetailsScreen(windowSizeClass = windowSizeClass.widthSizeClass)
            }
        }
    }
}