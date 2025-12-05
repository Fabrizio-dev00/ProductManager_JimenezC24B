package com.jimenez.product.product

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(nav: NavController, productId: String?, vm: ProductViewModel = viewModel()) {
    val coroutine = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        if (!productId.isNullOrEmpty()) {
            loading = true
            val p = vm.loadProduct(productId)
            p?.let {
                name = it.name
                price = it.price.toString()
                stock = it.stock.toString()
                category = it.category
            }
            loading = false
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(if (productId.isNullOrEmpty()) "Crear Producto" else "Editar Producto") })
    }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio (ej: 12.50)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock (ej: 10)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                // validations
                if (name.isBlank()) return@Button
                val pr = price.toDoubleOrNull() ?: 0.0
                val st = stock.toIntOrNull() ?: 0
                val product = Product(
                    id = productId ?: "",
                    name = name.trim(),
                    price = pr,
                    stock = st,
                    category = category.trim()
                )
                vm.saveProduct(product) { ok, msg ->
                    if (ok) {
                        nav.popBackStack()
                    } else {
                        // show snackbar
                    }
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(if (productId.isNullOrEmpty()) "Guardar" else "Actualizar")
            }
        }
    }
}
