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

import android.util.Log
import androidx.lifecycle.ViewModel
import com.sloth.partyquest.data.cardPlaceHolder
import com.sloth.partyquest.data.loadSet
import com.sloth.partyquest.models.EffectType
import com.sloth.partyquest.models.Player
import com.sloth.partyquest.models.PlayingCard
import com.sloth.partyquest.states.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())

    //asStateFlow() makes this mutable state flow a read-only state flow.
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var cardsOnPlay: MutableSet<PlayingCard> = mutableSetOf()
    private var cardsPlayed: MutableSet<PlayingCard> = mutableSetOf()

    init {
        dealCards()
    }

    private fun dealCards() {
        cardsOnPlay.addAll(loadSet())
        uiState.value.players.forEach { player ->
            pickRandomSetOfCards(player)
        }
    }

    private fun pickRandomSetOfCards(player: Player) {
        for (i in 0..<3) {
            drawNewCard()?.let {
                it.isFaceUp = true
                player.faceUpCards.add(it)
            }
            drawNewCard()?.let {
                player.faceDownCards.add(it)
            }
            drawNewCard()?.let {
                it.isOnHand = true
                player.onHandCards.add(it)
            }

        }
    }

    fun handleMove(player: Player, clickedCard: PlayingCard) {
        when (clickedCard.effectType) {
            EffectType.NONE -> {
                val currentCard = _uiState.value.currentPlayingCard
                if (clickedCard.value >= currentCard.value &&
                    !cardsPlayed.contains(clickedCard)
                ) {
                    updateLists(player, clickedCard)
                    _uiState.update { currentState ->
                        currentState.copy(
                            isFinalCardPlaced = true,
                            currentPlayingCard = clickedCard
                        )
                    }
                    while (player.onHandCards.count() < 3) {
                        cardDraw()
                    }
                    endTurn()
                }
            }

            EffectType.REVERSE -> {
                updateLists(player, clickedCard)
                _uiState.update { currentState ->
                    currentState.copy(currentPlayingCard = clickedCard)
                }
            }
            EffectType.ONLY_GREATERS -> {
                updateLists(player, clickedCard)
                _uiState.update { currentState ->
                    currentState.copy(currentPlayingCard = clickedCard)
                }
            }
            EffectType.ONLY_LESS -> {
                updateLists(player, clickedCard)
                _uiState.update { currentState ->
                    currentState.copy(currentPlayingCard = clickedCard)
                }
            }
            EffectType.CLEAR -> {
                updateLists(player, clickedCard)
                cardsPlayed.clear()
                // TODO: Do animation when clearing something fun
                _uiState.update { currentState ->
                    currentState.copy(currentPlayingCard = cardPlaceHolder)
                }
            }
        }
        Log.i("checkMove", " ${clickedCard.value}")
    }

    private fun updateLists(player: Player, clickedCard: PlayingCard) {
        player.onHandCards.remove(clickedCard)
        cardsPlayed.add(clickedCard)
    }

    fun checkTurn(playerFromCard: Player): Boolean {
        return _uiState.value.getCurrentPlayer().id == playerFromCard.id
    }

    fun eatClick() {
        val currentPlayer = uiState.value.players[uiState.value.playerTurnIdx]
        currentPlayer.onHandCards.addAll(cardsPlayed)
        currentPlayer.onHandCards.sortBy { it.value }
        cardsPlayed.clear()
        _uiState.update { currentState ->
            currentState.copy(currentPlayingCard = PlayingCard( 0))
        }
    }

    private fun drawNewCard(): PlayingCard? {
        if (cardsOnPlay.isEmpty()) return null
        val temp = cardsOnPlay.random()
        return if (cardsOnPlay.remove(temp))
            temp
        else drawNewCard()
    }

    fun resetGame() {
        cardsOnPlay.clear()
        _uiState.value = GameUiState()
    }

    fun cardDraw() {
        val newCard = drawNewCard()
        if (newCard != null) {
            _uiState.update { currentState ->
                val player = currentState.getCurrentPlayer()
                player.onHandCards.add(newCard)
                currentState.copy(
                    isGameOver = false,
                    playerTurnIdx = getNextIndx(),
                    isFinalCardPlaced = false
                )
            }
            endTurn()
        } else {
            //TODO: refresh UI to remove the image showing that are no more cards left
        }
    }

    private fun endTurn() {
        if (_uiState.value.isFinalCardPlaced) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = isGameOver(currentState.getCurrentPlayer()),
                    playerTurnIdx = getNextIndx(),
                    isFinalCardPlaced = false
                )
            }
            // TODO: Block all of the user controls until is its turn again and grant user controls to next user
        }
    }

    private fun isGameOver(player: Player): Boolean {
        return player.onHandCards.isEmpty() && player.faceUpCards.isEmpty() && player.faceDownCards.isEmpty()
    }

    private fun getNextIndx(): Int {
        val count = uiState.value.players.count()
        return if (uiState.value.playerTurnIdx + 1 >= count) 0
        else uiState.value.playerTurnIdx + 1
    }

    fun removeCardFromPlayerHand(player: Player, card: PlayingCard) {
        player.onHandCards.remove(card)
        cardDraw()
    }

    fun noMoreCardsOnPlay(): Boolean {
        return cardsOnPlay.isEmpty() || _uiState.value.currentPlayingCard.value == cardPlaceHolder.value
    }

    fun getCurrentPlayer(): Player {
        return _uiState.value.getCurrentPlayer()
    }

}