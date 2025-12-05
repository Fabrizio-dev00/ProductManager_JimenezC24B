package com.jimenez.product.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenez.product.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val repo = ProductRepository()

    val products = MutableStateFlow<List<Product>>(emptyList())

    init {
        repo.getProductsRealTime { lista ->
            products.value = lista
        }
    }

    fun saveProduct(product: Product, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            try {
                if (product.id.isEmpty()) {
                    repo.addProduct(product)
                } else {
                    repo.updateProduct(product.id, product)
                }
                onComplete(true, null)
            } catch (e: Exception) {
                onComplete(false, e.message)
            }
        }
    }

    fun deleteProduct(id: String, onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            try {
                repo.deleteProduct(id)
                onComplete(true, null)
            } catch (e: Exception) {
                onComplete(false, e.message)
            }
        }
    }

    suspend fun loadProduct(id: String): Product? {
        return repo.getProductById(id)
    }
}
