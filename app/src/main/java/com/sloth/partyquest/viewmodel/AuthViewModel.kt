/*
 * Copyright (c) 2024 Cesar Bermudez.
 *
 * Permission is hereby not granted, not free of charge, to any person
 * obtaining a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software. Notwithstanding the
 * foregoing, you may not use, copy, modify, merge, publish,distribute,
 * sublicense, create a derivative work,and/or sell copies of the Software in
 * any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application
 * development, or information technology.  Permission for such use,
 * copying, modification, merger, publication, distribution, sublicensing,
 * creation of derivative works, or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.sloth.partyquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.sloth.partyquest.models.SpaceAvatars
import com.sloth.partyquest.models.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false
)

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val database: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkCurrentUser()
    }

    fun checkCurrentUser() {
        try {
            val user = auth.currentUser
            if (user != null) {
                fetchUserProfile(user)
            } else {
                _uiState.update { it.copy(isAuthenticated = false, userProfile = null) }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isAuthenticated = false, userProfile = null) }
        }
    }

    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email and password cannot be empty.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        try {
            auth.signInWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        fetchUserProfile(user)
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "User not found.") }
                    }
                }
                .addOnFailureListener { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.localizedMessage ?: "Authentication failed."
                        )
                    }
                }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Authentication error."
                )
            }
        }
    }

    fun signUpWithEmail(email: String, password: String, displayName: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email and password cannot be empty.") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        try {
            auth.createUserWithEmailAndPassword(email.trim(), password)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        val name = displayName.ifBlank { "Cosmic Traveler" }
                        val defaultAvatar = SpaceAvatars.options.first()
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()

                        user.updateProfile(profileUpdate).addOnCompleteListener {
                            val profile = UserProfile(
                                uid = user.uid,
                                email = user.email ?: email,
                                displayName = name,
                                avatarId = defaultAvatar,
                                isGuest = false
                            )
                            syncProfileToDatabase(profile)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    userProfile = profile,
                                    isAuthenticated = true
                                )
                            }
                        }
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = "Sign up failed.") }
                    }
                }
                .addOnFailureListener { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.localizedMessage ?: "Registration failed."
                        )
                    }
                }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Registration error."
                )
            }
        }
    }

    fun signInGuest() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        try {
            auth.signInAnonymously()
                .addOnSuccessListener { result ->
                    val user = result.user
                    val guestProfile = UserProfile(
                        uid = user?.uid ?: "guest_${System.currentTimeMillis()}",
                        email = "guest@space.quest",
                        displayName = "Guest Astronaut",
                        avatarId = SpaceAvatars.options[0],
                        isGuest = true
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userProfile = guestProfile,
                            isAuthenticated = true
                        )
                    }
                }
                .addOnFailureListener {
                    // Fallback to local guest mode if Firebase Anonymous Auth is disabled
                    val localGuest = UserProfile(
                        uid = "guest_${System.currentTimeMillis()}",
                        email = "guest@space.quest",
                        displayName = "Guest Astronaut",
                        avatarId = SpaceAvatars.options[0],
                        isGuest = true
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userProfile = localGuest,
                            isAuthenticated = true
                        )
                    }
                }
        } catch (e: Exception) {
            val localGuest = UserProfile(
                uid = "guest_${System.currentTimeMillis()}",
                email = "guest@space.quest",
                displayName = "Guest Astronaut",
                avatarId = SpaceAvatars.options[0],
                isGuest = true
            )
            _uiState.update {
                it.copy(
                    isLoading = false,
                    userProfile = localGuest,
                    isAuthenticated = true
                )
            }
        }
    }

    fun updateProfile(displayName: String, avatarId: String, customImageUri: String? = null) {
        val currentProfile = _uiState.value.userProfile ?: return
        val updatedProfile = currentProfile.copy(
            displayName = displayName,
            avatarId = avatarId,
            customImageUri = customImageUri
        )

        _uiState.update { it.copy(isLoading = true, userProfile = updatedProfile) }

        val currentUser = auth.currentUser
        if (currentUser != null && !currentProfile.isGuest) {
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()

            currentUser.updateProfile(profileUpdate).addOnCompleteListener {
                syncProfileToDatabase(updatedProfile)
                _uiState.update { it.copy(isLoading = false) }
            }
        } else {
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun signOut() {
        try {
            auth.signOut()
        } catch (_: Exception) {
        }
        _uiState.update {
            AuthUiState(
                isLoading = false,
                userProfile = null,
                isAuthenticated = false
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun fetchUserProfile(user: FirebaseUser) {
        val uid = user.uid
        val defaultProfile = UserProfile(
            uid = uid,
            email = user.email ?: "",
            displayName = user.displayName?.ifBlank { "Cosmic Traveler" } ?: "Cosmic Traveler",
            avatarId = SpaceAvatars.options.first(),
            isGuest = user.isAnonymous
        )

        try {
            database.getReference("users").child(uid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val dbAvatar = snapshot.child("avatarId").getValue(String::class.java)
                        val dbCustomUri = snapshot.child("customImageUri").getValue(String::class.java)
                        val dbName = snapshot.child("displayName").getValue(String::class.java)

                        val finalProfile = defaultProfile.copy(
                            displayName = dbName ?: defaultProfile.displayName,
                            avatarId = dbAvatar ?: defaultProfile.avatarId,
                            customImageUri = dbCustomUri
                        )
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userProfile = finalProfile,
                                isAuthenticated = true
                            )
                        }
                    } else {
                        syncProfileToDatabase(defaultProfile)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userProfile = defaultProfile,
                                isAuthenticated = true
                            )
                        }
                    }
                }
                .addOnFailureListener {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userProfile = defaultProfile,
                            isAuthenticated = true
                        )
                    }
                }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    userProfile = defaultProfile,
                    isAuthenticated = true
                )
            }
        }
    }

    private fun syncProfileToDatabase(profile: UserProfile) {
        try {
            val userMap = mapOf(
                "uid" to profile.uid,
                "email" to profile.email,
                "displayName" to profile.displayName,
                "avatarId" to profile.avatarId,
                "customImageUri" to (profile.customImageUri ?: ""),
                "isGuest" to profile.isGuest
            )
            database.getReference("users").child(profile.uid).setValue(userMap)
        } catch (_: Exception) {
        }
    }
}
