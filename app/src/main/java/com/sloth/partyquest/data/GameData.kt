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

const val reverseIcon = "🌀"
const val cleanIcon = "🌬️"
const val knotIcon = "🪢"
const val onlyGreaterIcon = "🎰"
const val firstSet = "🪆"
const val secondSet = "🍀"
const val thirdSet = "🍄"
const val fourthSet = "🧿"

// Set with all the words for the Game
val allPlayingCard: Set<PlayingCard> =
    setOf(
        PlayingCard(
            reverseIcon,
            0,
            effectType = EffectType.REVERSE,
        ),
        PlayingCard(
            reverseIcon,
            0,
            effectType = EffectType.REVERSE,
        ),
        PlayingCard(
            reverseIcon,
            0,
            effectType = EffectType.REVERSE,
        ),
        PlayingCard(
            reverseIcon,
            0,
            effectType = EffectType.REVERSE,
        ),
        PlayingCard(
            cleanIcon,
            0,
            effectType = EffectType.CLEAR,
        ),
        PlayingCard(
            cleanIcon,
            0,
            effectType = EffectType.CLEAR,
        ),
        PlayingCard(
            cleanIcon,
            0,
            effectType = EffectType.CLEAR,
        ),
        PlayingCard(
            cleanIcon,
            0,
            effectType = EffectType.CLEAR,
        ),
        PlayingCard(
            knotIcon,
            0,
            effectType = EffectType.ONLY_LESS,
        ),
        PlayingCard(
            knotIcon,
            0,
            effectType = EffectType.ONLY_LESS,
        ),
        PlayingCard(
            knotIcon,
            0,
            effectType = EffectType.ONLY_LESS,
        ),
        PlayingCard(
            knotIcon,
            0,
            effectType = EffectType.ONLY_LESS,
        ),
        PlayingCard(
            onlyGreaterIcon,
            0,
            effectType = EffectType.ONLY_GREATERS,
        ),
        PlayingCard(
            onlyGreaterIcon,
            0,
            effectType = EffectType.ONLY_GREATERS,
        ),
        PlayingCard(
            onlyGreaterIcon,
            0,
            effectType = EffectType.ONLY_GREATERS,
        ),
        PlayingCard(
            onlyGreaterIcon,
            0,
            effectType = EffectType.ONLY_GREATERS
        ),
        PlayingCard(
            firstSet,
            3,
        ),
        PlayingCard(
            firstSet,
            4,
        ),
        PlayingCard(
            firstSet,
            5,
        ),
        PlayingCard(
            firstSet,
            6,
        ),
        PlayingCard(
            firstSet,
            9,
        ),
        PlayingCard(
            firstSet,
            11,
        ),
        PlayingCard(
            firstSet,
            12,
        ),
        PlayingCard(
            firstSet,
            13,
        ),
        PlayingCard(
            firstSet,
            14,
        ),
        PlayingCard(
            secondSet,
            3,
        ),
        PlayingCard(
            secondSet,
            4,
        ),
        PlayingCard(
            secondSet,
            5,
        ),
        PlayingCard(
            secondSet,
            6,
        ),
        PlayingCard(
            secondSet,
            9,
        ),
        PlayingCard(
            secondSet,
            11,
        ),
        PlayingCard(
            secondSet,
            12,
        ),
        PlayingCard(
            secondSet,
            13,
        ),
        PlayingCard(
            secondSet,
            14,
        ),
        PlayingCard(
            thirdSet,
            3,
        ),
        PlayingCard(
            thirdSet,
            4,
        ),
        PlayingCard(
            thirdSet,
            5,
        ),
        PlayingCard(
            thirdSet,
            6,
        ),
        PlayingCard(
            thirdSet,
            9,
        ),
        PlayingCard(
            thirdSet,
            11,
        ),
        PlayingCard(
            thirdSet,
            12,
        ),
        PlayingCard(
            thirdSet,
            13,
        ),
        PlayingCard(
            thirdSet,
            14,
        ),
        PlayingCard(
            fourthSet,
            3,
        ),
        PlayingCard(
            fourthSet,
            4,
        ),
        PlayingCard(
            fourthSet,
            5,
        ),
        PlayingCard(
            fourthSet,
            6,
        ),
        PlayingCard(
            fourthSet,
            9,
        ),
        PlayingCard(
            fourthSet,
            11,
        ),
        PlayingCard(
            fourthSet,
            12,
        ),
        PlayingCard(
            fourthSet,
            13,
        ),
        PlayingCard(
            fourthSet,
            14,
        )
    )

val cardPlaceHolder = PlayingCard(
    "",
    0,
    isOnHand = true
)