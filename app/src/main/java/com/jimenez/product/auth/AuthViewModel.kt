package com.jimenez.product.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jimenez.product.data.FirebaseModule
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth = FirebaseModule.auth

    fun currentUserUid(): String? = auth.currentUser?.uid

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun login(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            onResult(false, "Completa todos los campos")
            return
        }

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, pass).await()
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun register(email: String, pass: String, confirm: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank() || pass.isBlank() || confirm.isBlank()) {
            onResult(false, "Completa todos los campos")
            return
        }
        if (pass != confirm) {
            onResult(false, "Las contraseñas no coinciden")
            return
        }
        if (pass.length < 6) {
            onResult(false, "La contraseña debe tener al menos 6 caracteres")
            return
        }

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, pass).await()
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.message ?: "Error al registrar usuario")
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}