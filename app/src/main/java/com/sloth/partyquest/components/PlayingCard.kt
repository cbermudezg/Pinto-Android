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

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sloth.partyquest.R
import com.sloth.partyquest.models.EffectType
import com.sloth.partyquest.models.Player
import com.sloth.partyquest.models.PlayingCard
import com.sloth.partyquest.viewmodel.GameViewModel

@Composable
fun FaceUpCard(
    card: PlayingCard,
    isEnabled: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(63.dp)
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            ),
    ) {
        when(card.effectType) {
            EffectType.NONE -> {
                Image(
                    painterResource(R.drawable.skill_bg_2), "bg2"
                )
                Text(
                    text = "${card.value}",
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            EffectType.REVERSE -> {
                Image(
                    painterResource(R.drawable.skill_bg), "bg2",
                )
                //TODO: make the btn action
                IconButton(onClick = { }) {
                    Image(
                        painterResource(R.drawable.skill_reverse_2),
                        "reverse action",
                    )
                }
            }
            EffectType.ONLY_GREATERS -> {
                Image(
                    painterResource(R.drawable.skill_bg), "bg2"
                )
                //TODO: make the btn action
                IconButton(onClick = { }) {
                    Image(
                        painterResource(R.drawable.skill_only_greaters),
                        "only greaters action",
                    )
                }
            }
            EffectType.ONLY_LESS -> {
                Image(
                    painterResource(R.drawable.skill_bg), "bg2"
                )
                //TODO: make the btn action
                IconButton(onClick = { }) {
                    Image(
                        painterResource(R.drawable.skill_only_less),
                        "only smalls",
                    )
                }
            }
            EffectType.CLEAR -> {
                Image(
                    painterResource(R.drawable.skill_bg), "bg2"
                )
                //TODO: make the btn action
                IconButton(onClick = { }) {
                    Image(
                        painterResource(R.drawable.skill_clean),
                        "clear action",
                    )
                }
            }
        }
    }
}

@Composable
fun FaceDownCard(
    card: PlayingCard, isEnabled: Boolean = false, onClick: () -> Unit
) {
    if (!isEnabled) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
            onClick = onClick,
            enabled = false,
            modifier = Modifier.size(55.dp, 55.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.facedown),
                contentDescription = "facedown card",
                modifier = Modifier
                    .size(55.dp)
                    .scale(1.6f)
            )
        }
    } else {
        FaceUpCard(card = card, isEnabled = true, onClick = onClick, Modifier)
    }
}

@Composable
fun DrawingCard(
    onClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        onClick = onClick,
        modifier = Modifier.size(55.dp, 55.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.facedown),
            contentDescription = "facedown card",
            modifier = Modifier
                .size(55.dp)
                .scale(1.6f)
        )
    }
}

@Composable
fun LastCardPlaceHolder(
    lastCard: PlayingCard
) {
    if (lastCard.value != 0) {
        FaceUpCard(card = lastCard, isEnabled = true, modifier = Modifier, onClick = {})
    } else {
        val stroke = Stroke(
            width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        Canvas(Modifier.size(55.dp)) {
            drawRoundRect(color = Color.White, style = stroke)
        }
    }
}

@Composable
fun HouseOfCards(
    player: Player, viewModel: GameViewModel
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Row {
            Box(contentAlignment = Alignment.Center) {
                CardsInBoard(player = player, viewModel)
            }
        }

    }
}

@Composable
fun CenterView(
    lastCard: PlayingCard,
    viewModel: GameViewModel,
) {
//    val gameUiState by viewModel.uiState.collectAsState()
    Row {
        DrawingCard(onClick = { viewModel.cardDraw() })
        Spacer(modifier = Modifier.size(13.dp))
        LastCardPlaceHolder(lastCard)
    }
}

@Composable
fun CardsInHand(
    player: Player, viewModel: GameViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(player.onHandCards, key = { it.id }) {

            FaceUpCard(
                card = it,
                isEnabled = viewModel.checkTurn(player),
                onClick = {
                    viewModel.handleMove(player, it)
                },
                modifier = Modifier.animateItem(
                    fadeInSpec = tween(durationMillis = 1000), fadeOutSpec = spring(
                        stiffness = Spring.StiffnessHigh,
                        dampingRatio = Spring.DampingRatioHighBouncy
                    ), placementSpec = spring(
                        stiffness = Spring.StiffnessLow,
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                ),
            )

        }
    }
}

@Composable
fun CardsInBoard(player: Player, viewModel: GameViewModel) {
    Box {
        Row {
            player.faceDownCards.forEach {
                FaceDownCard(card = it, isEnabled = player.onHandCards.isEmpty()) {
                    viewModel.handleMove(player, it)
                }
            }
        }
        Row(modifier = Modifier.padding(8.dp)) {
            player.faceUpCards.forEach {
                FaceUpCard(
                    card = it,
                    isEnabled = player.onHandCards.isEmpty(),
                    modifier = Modifier,
                    onClick = {
                        viewModel.handleMove(player, it)
                    })
                Spacer(Modifier.size(1.dp))
            }
        }
    }
}

@Composable
fun UserActions(viewModel: GameViewModel) {
    Column(horizontalAlignment = Alignment.End) {
        Box(
            modifier = Modifier.size(55.dp), contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.skill_bg), "bg2", modifier = Modifier.fillMaxSize()
            )
            IconButton(onClick = { viewModel.eatClick() }) {
                Image(
                    painterResource(R.drawable.skill_get_one),
                    "Hand",
                )
            }
        }
        Spacer(modifier = Modifier.size(21.dp))
        Box(
            modifier = Modifier.size(55.dp), contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(R.drawable.skill_bg), "bg2", modifier = Modifier.fillMaxSize()
            )
            IconButton(onClick = { viewModel.eatClick() }) {
                Image(
                    painterResource(R.drawable.skill_new_card),
                    "Hand",
                )
            }
        }
    }
}

@Composable
fun BackGroundShape() {
    val colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
    val brush = Brush.horizontalGradient(colors, tileMode = TileMode.Repeated)
    Canvas(modifier = Modifier.size(200.dp)) {

        val path = Path().apply {
            lineTo(size.width, size.height / 2)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path = path, brush = brush)
    }
}

@Preview
@Composable
fun CardsInBoardPreview() {
    val gameViewModel: GameViewModel = viewModel()
    val gameUiState by gameViewModel.uiState.collectAsState()

    gameUiState.players.forEach { player ->
        CardsInBoard(player, gameViewModel)
    }
}

@Preview
@Composable
fun HouseOfCardsPreview() {
    val gameViewModel: GameViewModel = viewModel()
    val gameUiState by gameViewModel.uiState.collectAsState()

    gameUiState.players.forEach { player ->
        HouseOfCards(player, viewModel = gameViewModel)
    }
}

@Preview
@Composable
fun UserActionsPreview() {
    val gameViewModel: GameViewModel = viewModel()
    UserActions(gameViewModel)
}