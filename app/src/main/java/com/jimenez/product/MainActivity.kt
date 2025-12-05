package com.jimenez.product

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jimenez.product.navigation.AppNavigation
import com.jimenez.product.ui.theme.ProductManager_JimenezC24BTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductManager_JimenezC24BTheme {
                AppNavigation()
            }
        }
    }
}