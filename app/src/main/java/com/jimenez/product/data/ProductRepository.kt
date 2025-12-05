package com.jimenez.product.data

import com.google.firebase.firestore.Query
import com.jimenez.product.product.Product
import kotlinx.coroutines.tasks.await

class ProductRepository {

    private val db = FirebaseModule.db
    private val auth = FirebaseModule.auth

    private fun userId(): String = auth.currentUser?.uid ?: ""

    suspend fun addProduct(p: Product) {
        val id = db.collection("products").document().id
        db.collection("products").document(id)
            .set(p.copy(id = id, userId = userId()))
            .await()
    }

    fun getProductsRealTime(onChange: (List<Product>) -> Unit) {
        db.collection("products")
            .whereEqualTo("userId", userId())
            // Quitamos el orderBy temporalmente
            .addSnapshotListener { snap, _ ->
                if (snap != null) {
                    val lista = snap.toObjects(Product::class.java)
                    onChange(lista)
                } else {
                    onChange(emptyList())
                }
            }
    }

    suspend fun updateProduct(id: String, product: Product) {
        db.collection("products").document(id)
            .set(product.copy(id = id, userId = userId()))
            .await()
    }

    suspend fun deleteProduct(id: String) {
        db.collection("products").document(id).delete().await()
    }

    suspend fun getProductById(id: String): Product? {
        val doc = db.collection("products").document(id).get().await()
        return if (doc.exists()) doc.toObject(Product::class.java) else null
    }
}