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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sloth.partyquest.R
import com.sloth.partyquest.models.Player
import com.sloth.partyquest.models.PlayingCard
import com.sloth.partyquest.viewmodel.GameViewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel(), modifier: Modifier) {
    val gameUiState by gameViewModel.uiState.collectAsStateWithLifecycle()
//    var offsetX by remember { mutableFloatStateOf(0f) }
//    var offsetY by remember { mutableFloatStateOf(0f) }
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
                GameLayout(
                    gameUiState.players,
                    currentPlayingCard = gameUiState.currentPlayingCard,
                    viewModel = gameViewModel,
                    modifier = modifier
                )
            }
        }
    }
//    GameLayout(gameUiState.players, modifier = modifier
//        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
//        .pointerInput(Unit) {
//            detectDragGestures { change, dragAmount ->
//                change.consume()
//                offsetX += dragAmount.x
//                offsetY += dragAmount.y
//            }
//        }
//    )
}

@Composable
fun GameLayout(
    players: List<Player>,
    currentPlayingCard: PlayingCard,
    viewModel: GameViewModel,
    modifier: Modifier
) {
    val margin = 8.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        HouseOfCards(players[1], viewModel)
        CardsInHand(player = players[1], viewModel = viewModel)
        Spacer(modifier = Modifier.size(margin))
        CenterView(lastCard = currentPlayingCard, viewModel)
        Spacer(modifier = Modifier.size(margin))
        HouseOfCards(players[0], viewModel)
        Spacer(modifier = Modifier.size(13.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.size(13.dp))
        CardsInHand(player = players[0], viewModel = viewModel)
        Spacer(modifier = Modifier.size(21.dp))
        UserActions(viewModel = viewModel)
    }
}

@Preview
@Composable
fun GameLayoutPreview() {
    GameScreen(modifier = Modifier)
}