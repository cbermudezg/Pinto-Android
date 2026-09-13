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

package com.sloth.partyquest.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sloth.partyquest.models.Player
import com.sloth.partyquest.models.PlayingCard
import com.sloth.partyquest.states.GameUiState
import com.sloth.partyquest.viewmodel.GameViewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel(), modifier: Modifier = Modifier) {
    val gameUiState by gameViewModel.uiState.collectAsStateWithLifecycle()
    Surface(
        modifier = modifier.fillMaxSize(),
        tonalElevation = 5.dp,
        shadowElevation = 5.dp,
    ) {
        SpaceBackground(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                MultiplayerLandscapeLayout(
                    uiState = gameUiState,
                    viewModel = gameViewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun MultiplayerLandscapeLayout(
    uiState: GameUiState,
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val players = uiState.players
    val activePlayerIdx = uiState.playerTurnIdx

    // Layout arrangement for 5 players in landscape:
    // Bottom: Active / Local Human Player (Player 1 by default, or indexed by players[0])
    // Top Row: Opponent 2 and Opponent 3
    // Left Column: Opponent 1
    // Right Column: Opponent 4
    val localPlayer = players.getOrNull(0) ?: Player(1)
    val leftOpponent = players.getOrNull(1)
    val topOpponent1 = players.getOrNull(2)
    val topOpponent2 = players.getOrNull(3)
    val rightOpponent = players.getOrNull(4)

    Column(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Opponents Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            topOpponent1?.let { player ->
                val isTurn = players.indexOf(player) == activePlayerIdx
                OpponentBadge(player = player, isTurn = isTurn, viewModel = viewModel)
            }
            topOpponent2?.let { player ->
                val isTurn = players.indexOf(player) == activePlayerIdx
                OpponentBadge(player = player, isTurn = isTurn, viewModel = viewModel)
            }
        }

        // Center Area (Left Opponent, Center Pile/Deck, Right Opponent)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Opponent Spot
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                leftOpponent?.let { player ->
                    val isTurn = players.indexOf(player) == activePlayerIdx
                    OpponentBadge(player = player, isTurn = isTurn, viewModel = viewModel)
                }
            }

            // Center View: Draw deck & Last played card
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                CenterView(lastCard = uiState.currentPlayingCard, viewModel = viewModel)
            }

            // Right Opponent Spot
            Box(
                contentAlignment = Alignment.CenterEnd
            ) {
                rightOpponent?.let { player ->
                    val isTurn = players.indexOf(player) == activePlayerIdx
                    OpponentBadge(player = player, isTurn = isTurn, viewModel = viewModel)
                }
            }
        }

        // Bottom Area: Local Active Player Controls & Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Player Badge & Table House Cards
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                val isLocalTurn = activePlayerIdx == 0
                LocalPlayerBadge(player = localPlayer, isTurn = isLocalTurn)
                Spacer(modifier = Modifier.size(4.dp))
                HouseOfCards(player = localPlayer, viewModel = viewModel)
            }

            // Player Hand Cards
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                CardsInHand(player = viewModel.getCurrentPlayer(), viewModel = viewModel)
            }

            // User Action Buttons (Eat / Draw)
            UserActions(viewModel = viewModel)
        }
    }
}

@Composable
fun LocalPlayerBadge(
    player: Player,
    isTurn: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "turnPulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isTurn) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isTurn) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFFD700)) else null,
        modifier = Modifier.scale(if (isTurn) scale else 1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (isTurn) Color(0xFFFFD700) else Color.Gray)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "P${player.id} (YOU)",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (isTurn) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun OpponentBadge(
    player: Player,
    isTurn: Boolean,
    viewModel: GameViewModel
) {
    val infiniteTransition = rememberInfiniteTransition(label = "turnPulseOpponent")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleOpponent"
    )

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isTurn) Color(0xFF673AB7) else Color(0xAA1C1B1F),
        border = if (isTurn) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFFD700)) else androidx.compose.foundation.BorderStroke(1.dp, Color.DarkGray),
        modifier = Modifier.scale(if (isTurn) scale else 1f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isTurn) Color(0xFFFFD700) else Color.LightGray)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Player ${player.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.size(2.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🃏 ${player.onHandCards.size}",
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
                Text(
                    text = "🎴 ${player.faceDownCards.size + player.faceUpCards.size}",
                    fontSize = 10.sp,
                    color = Color.LightGray
                )
            }
            CardsInBoard(player = player, viewModel = viewModel)
        }
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun MultiplayerLandscapeLayoutPreview() {
    GameScreen()
}
