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

package com.sloth.partyquest.data

import com.sloth.partyquest.models.EffectType
import com.sloth.partyquest.models.PlayingCard

// Set with all the words for the Game
val allPlayingCard: MutableSet<PlayingCard> =
    mutableSetOf(
        PlayingCard(
            0,
            effectType = EffectType.REVERSE,
        ),
        PlayingCard(
            0,
            effectType = EffectType.REVERSE,
        ),
        PlayingCard(
            0,
            effectType = EffectType.REVERSE,
        ),
        PlayingCard(
            0,
            effectType = EffectType.REVERSE,
        ),
        PlayingCard(
            0,
            effectType = EffectType.CLEAR,
        ),
        PlayingCard(
            0,
            effectType = EffectType.CLEAR,
        ),
        PlayingCard(
            0,
            effectType = EffectType.CLEAR,
        ),
        PlayingCard(
            0,
            effectType = EffectType.CLEAR,
        ),
        PlayingCard(
            0,
            effectType = EffectType.ONLY_LESS,
        ),
        PlayingCard(
            0,
            effectType = EffectType.ONLY_LESS,
        ),
        PlayingCard(
            0,
            effectType = EffectType.ONLY_LESS,
        ),
        PlayingCard(
            0,
            effectType = EffectType.ONLY_LESS,
        ),
        PlayingCard(
            0,
            effectType = EffectType.ONLY_GREATERS,
        ),
        PlayingCard(
            0,
            effectType = EffectType.ONLY_GREATERS,
        ),
        PlayingCard(
            0,
            effectType = EffectType.ONLY_GREATERS,
        ),
        PlayingCard(
            0,
            effectType = EffectType.ONLY_GREATERS
        ),
    )

val cardPlaceHolder = PlayingCard(
    0,
    isOnHand = true
)

fun loadSet() : MutableSet<PlayingCard>{
    for (i in 2..<14) {
        allPlayingCard.add(PlayingCard(i))
        allPlayingCard.add(PlayingCard(i))
        allPlayingCard.add(PlayingCard(i))
        allPlayingCard.add(PlayingCard(i))
    }
    return allPlayingCard
}