package com.devbytes.brainory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devbytes.brainory.data.database.User
import com.devbytes.brainory.data.repository.PreferencesRepository
import com.devbytes.brainory.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle    : AuthState()
    object Loading : AuthState()
    data class Success(val email: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val prefs: PreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    val isLoggedIn    = prefs.isLoggedIn
    val onboardingDone= prefs.onboardingDone

    fun login(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            if (email.isBlank() || password.isBlank()) {
                _state.value = AuthState.Error("Please fill all fields")
                return@launch
            }
            val user = userRepo.login(email.trim(), password)
            if (user != null) {
                if (rememberMe) prefs.setLoggedIn(user.email)
                else prefs.setLoggedIn(user.email)
                _state.value = AuthState.Success(user.email)
            } else {
                _state.value = AuthState.Error("Invalid email or password")
            }
        }
    }

    fun signUp(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _state.value = AuthState.Loading
            when {
                username.isBlank() || email.isBlank() || password.isBlank() ->
                    _state.value = AuthState.Error("Please fill all fields")
                password != confirmPassword ->
                    _state.value = AuthState.Error("Passwords do not match")
                password.length < 6 ->
                    _state.value = AuthState.Error("Password must be at least 6 characters")
                userRepo.findByEmail(email) != null ->
                    _state.value = AuthState.Error("Email already registered")
                else -> {
                    userRepo.register(User(username = username.trim(), email = email.trim(), password = password))
                    prefs.setLoggedIn(email.trim())
                    _state.value = AuthState.Success(email.trim())
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch { prefs.setLoggedOut() }
    }

    fun completeOnboarding() {
        viewModelScope.launch { prefs.setOnboardingDone() }
    }

    fun resetState() { _state.value = AuthState.Idle }
}