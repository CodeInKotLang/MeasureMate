package com.example.measuremate.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.measuremate.presentation.navigation.NavGraph
import com.example.measuremate.presentation.theme.MeasureMateTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            MeasureMateTheme {
                val windowSizeClass = calculateWindowSizeClass(activity = this)
                val navController = rememberNavController()
                Scaffold { paddingValues ->
                    NavGraph(
                        navController = navController,
                        windowSize = windowSizeClass.widthSizeClass,
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}