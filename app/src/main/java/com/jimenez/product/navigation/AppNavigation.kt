package com.jimenez.product.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jimenez.product.auth.LoginScreen
import com.jimenez.product.auth.RegisterScreen
import com.jimenez.product.product.ProductFormScreen
import com.jimenez.product.product.ProductListScreen

@Composable
fun AppNavigation() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = "login") {
        composable("login") { LoginScreen(nav) }
        composable("register") { RegisterScreen(nav) }
        composable("products") { ProductListScreen(nav) }
        composable("product_form") { ProductFormScreen(nav, null) }
        composable(
            "product_edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            ProductFormScreen(nav, id)
        }
    }
}
