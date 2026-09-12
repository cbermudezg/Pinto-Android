package com.sloth.partyquest.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserProfile(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val isAnonymous: Boolean
)

data class AuthUiState(
    val userProfile: UserProfile? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSignUpMode: Boolean = false
)

class AuthViewModel(
    private val auth: FirebaseAuth? = try { FirebaseAuth.getInstance() } catch (_: Exception) { null }
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        _uiState.update { state ->
            state.copy(
                userProfile = user?.toUserProfile(),
                isLoading = false
            )
        }
    }

    init {
        auth?.addAuthStateListener(authStateListener)
        val currentUser = auth?.currentUser
        if (currentUser != null) {
            _uiState.update { it.copy(userProfile = currentUser.toUserProfile()) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        auth?.removeAuthStateListener(authStateListener)
    }

    fun toggleAuthMode() {
        _uiState.update {
            it.copy(
                isSignUpMode = !it.isSignUpMode,
                errorMessage = null
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun signInWithEmail(email: String, password: String, onSuccess: () -> Unit = {}) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email and password cannot be empty.") }
            return
        }

        if (auth == null) {
            _uiState.update {
                it.copy(
                    userProfile = UserProfile("mock_uid", email, email.substringBefore("@"), false),
                    isLoading = false,
                    errorMessage = null
                )
            }
            onSuccess()
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            userProfile = task.result?.user?.toUserProfile(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = task.exception?.localizedMessage ?: "Authentication failed."
                        )
                    }
                }
            }
    }

    fun signUpWithEmail(email: String, password: String, onSuccess: () -> Unit = {}) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email and password cannot be empty.") }
            return
        }
        if (password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Password must be at least 6 characters.") }
            return
        }

        if (auth == null) {
            _uiState.update {
                it.copy(
                    userProfile = UserProfile("mock_uid", email, email.substringBefore("@"), false),
                    isLoading = false,
                    errorMessage = null
                )
            }
            onSuccess()
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            userProfile = task.result?.user?.toUserProfile(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = task.exception?.localizedMessage ?: "Registration failed."
                        )
                    }
                }
            }
    }

    fun signInAnonymously(onSuccess: () -> Unit = {}) {
        if (auth == null) {
            _uiState.update {
                it.copy(
                    userProfile = UserProfile("guest_uid", null, "Guest Explorer", true),
                    isLoading = false,
                    errorMessage = null
                )
            }
            onSuccess()
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            userProfile = task.result?.user?.toUserProfile(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = task.exception?.localizedMessage ?: "Guest sign-in failed."
                        )
                    }
                }
            }
    }

    fun signOut() {
        auth?.signOut()
        _uiState.update {
            it.copy(
                userProfile = null,
                isLoading = false,
                errorMessage = null
            )
        }
    }
}

private fun FirebaseUser.toUserProfile(): UserProfile {
    return UserProfile(
        uid = uid,
        email = email,
        displayName = displayName ?: email?.substringBefore("@") ?: if (isAnonymous) "Guest" else "User",
        isAnonymous = isAnonymous
    )
}
