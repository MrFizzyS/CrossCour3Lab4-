package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.anifantakis.lib.ksafe.KSafe

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val ksafe = KSafe(applicationContext)

        setContent {
            App(ksafe = ksafe)                             
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}