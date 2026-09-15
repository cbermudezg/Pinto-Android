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
package com.sloth.partyquest

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sloth.partyquest.components.GameScreen
import com.sloth.partyquest.components.LoginScreen
import com.sloth.partyquest.components.MenuScreen
import com.sloth.partyquest.components.ProfileScreen
import com.sloth.partyquest.ui.theme.AppTheme
import com.sloth.partyquest.viewmodel.AuthViewModel
import com.sloth.partyquest.viewmodel.GameViewModel

enum class AppScreen {
    LOGIN,
    MENU,
    PROFILE,
    GAME
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        showImmersiveMode()
        setContent {
            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun showImmersiveMode() {
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars.
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        name = "DefaultPreviewDark"
    )
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_NO,
        name = "DefaultPreviewLight"
    )
    @Composable
    fun PintoAppPreview() {
        AppTheme {
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                GameScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun MainApp(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel(),
    gameViewModel: GameViewModel = viewModel()
) {
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()
    var currentScreen by remember { mutableStateOf(AppScreen.LOGIN) }

    LaunchedEffect(authUiState.isAuthenticated) {
        if (authUiState.isAuthenticated && currentScreen == AppScreen.LOGIN) {
            currentScreen = AppScreen.MENU
        } else if (!authUiState.isAuthenticated) {
            currentScreen = AppScreen.LOGIN
        }
    }

    when (currentScreen) {
        AppScreen.LOGIN -> {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = { currentScreen = AppScreen.MENU },
                modifier = modifier
            )
        }
        AppScreen.MENU -> {
            MenuScreen(
                authViewModel = authViewModel,
                onLaunchGame = { currentScreen = AppScreen.GAME },
                onOpenProfile = { currentScreen = AppScreen.PROFILE },
                onSignOut = { currentScreen = AppScreen.LOGIN },
                modifier = modifier
            )
        }
        AppScreen.PROFILE -> {
            ProfileScreen(
                authViewModel = authViewModel,
                onBackToMenu = { currentScreen = AppScreen.MENU },
                modifier = modifier
            )
        }
        AppScreen.GAME -> {
            GameScreen(
                gameViewModel = gameViewModel,
                onBackToMenu = { currentScreen = AppScreen.MENU },
                modifier = modifier
            )
        }
    }
}
