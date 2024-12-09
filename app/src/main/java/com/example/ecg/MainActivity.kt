package com.example.ecg

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ecg.ecgchart.EcgChartScreen
import com.example.ecg.ui.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.BLACK, Color.BLACK)
        )
        setContent {
            AppTheme {
                EcgChartScreen()
            }
        }
    }
}