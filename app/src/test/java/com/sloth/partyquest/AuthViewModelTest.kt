package com.sloth.partyquest

import com.sloth.partyquest.viewmodel.AuthViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthViewModelTest {

    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        // Pass null so AuthViewModel uses mock mode without needing real Firebase services
        authViewModel = AuthViewModel(auth = null)
    }

    @Test
    fun testInitialState() {
        val state = authViewModel.uiState.value
        assertNull(state.userProfile)
        assertEquals(false, state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(false, state.isSignUpMode)
    }

    @Test
    fun testToggleAuthMode() {
        authViewModel.toggleAuthMode()
        assertTrue(authViewModel.uiState.value.isSignUpMode)

        authViewModel.toggleAuthMode()
        assertEquals(false, authViewModel.uiState.value.isSignUpMode)
    }

    @Test
    fun testSignInWithEmptyEmailOrPassword() {
        authViewModel.signInWithEmail("", "password")
        assertEquals("Email and password cannot be empty.", authViewModel.uiState.value.errorMessage)

        authViewModel.signInWithEmail("test@example.com", "")
        assertEquals("Email and password cannot be empty.", authViewModel.uiState.value.errorMessage)
    }

    @Test
    fun testSignUpShortPassword() {
        authViewModel.signUpWithEmail("test@example.com", "123")
        assertEquals("Password must be at least 6 characters.", authViewModel.uiState.value.errorMessage)
    }

    @Test
    fun testSuccessfulMockSignIn() {
        var successCalled = false
        authViewModel.signInWithEmail("cosmic@explorer.com", "secret123") {
            successCalled = true
        }

        assertTrue(successCalled)
        val profile = authViewModel.uiState.value.userProfile
        assertNotNull(profile)
        assertEquals("cosmic@explorer.com", profile?.email)
        assertEquals("cosmic", profile?.displayName)
    }

    @Test
    fun testAnonymousSignIn() {
        var successCalled = false
        authViewModel.signInAnonymously {
            successCalled = true
        }

        assertTrue(successCalled)
        val profile = authViewModel.uiState.value.userProfile
        assertNotNull(profile)
        assertTrue(profile?.isAnonymous == true)
        assertEquals("Guest Explorer", profile?.displayName)
    }

    @Test
    fun testSignOut() {
        authViewModel.signInAnonymously()
        assertNotNull(authViewModel.uiState.value.userProfile)

        authViewModel.signOut()
        assertNull(authViewModel.uiState.value.userProfile)
    }
}
