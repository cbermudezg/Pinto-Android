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

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Star(
    val relX: Float,
    val relY: Float,
    val baseRadius: Float,
    val color: Color,
    val speedY: Float,
    val twinklePhase: Float,
    val twinkleSpeed: Float,
    val isShiningStar: Boolean
)

private data class Meteor(
    val startRelX: Float,
    val startRelY: Float,
    val length: Float,
    val angleRad: Float,
    val speed: Float,
    val phaseOffset: Float
)

@Composable
fun SpaceBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SpaceAnim")

    // Time loop 0..1 for continuous drifting and twinkling
    val timeProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "timeProgress"
    )

    // Faster loop for twinkling and meteor cycles
    val twinkleTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "twinkleTime"
    )

    // Generate fixed random stars and meteors
    val stars = remember {
        val random = Random(42)
        val colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFE0F7FA),
            Color(0xFFFFF9C4),
            Color(0xFFF3E5F5),
            Color(0xFFBBDEFB),
            Color(0xFFFFCC80)
        )
        List(110) { index ->
            Star(
                relX = random.nextFloat(),
                relY = random.nextFloat(),
                baseRadius = random.nextFloat() * 2.5f + 1.2f,
                color = colors[random.nextInt(colors.size)],
                speedY = random.nextFloat() * 0.04f + 0.01f, // Parallax downward drift speed
                twinklePhase = random.nextFloat() * 6.28f,
                twinkleSpeed = random.nextFloat() * 1.5f + 0.5f,
                isShiningStar = index % 8 == 0 // 1 in 8 stars is a shining star with cross flare
            )
        }
    }

    val meteors = remember {
        val random = Random(123)
        List(3) {
            Meteor(
                startRelX = random.nextFloat() * 0.8f + 0.1f,
                startRelY = random.nextFloat() * 0.4f,
                length = random.nextFloat() * 120f + 80f,
                angleRad = (Math.PI / 4.0).toFloat() + (random.nextFloat() * 0.2f - 0.1f), // ~45 deg
                speed = random.nextFloat() * 0.3f + 0.5f,
                phaseOffset = random.nextFloat()
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Draw Space Gradient (Deep Cosmic Dark Blue/Purple background)
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF070B19), // Deep galaxy navy
                        Color(0xFF0F172A), // Cosmic midnight blue
                        Color(0xFF1E1B4B), // Deep nebula indigo
                        Color(0xFF090D16)  // Bottom space black
                    )
                )
            )

            // 2. Draw Subtle Glowing Nebulae (Cosmic Dust clouds)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x289333EA), // Purple nebula core
                        Color(0x009333EA)
                    ),
                    center = Offset(width * 0.25f, height * 0.35f),
                    radius = width * 0.6f
                ),
                center = Offset(width * 0.25f, height * 0.35f),
                radius = width * 0.6f
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x2006B6D4), // Cyan nebula glow
                        Color(0x0006B6D4)
                    ),
                    center = Offset(width * 0.75f, height * 0.7f),
                    radius = width * 0.7f
                ),
                center = Offset(width * 0.75f, height * 0.7f),
                radius = width * 0.7f
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x18EC4899), // Pink nebula glow
                        Color(0x00EC4899)
                    ),
                    center = Offset(width * 0.5f, height * 0.15f),
                    radius = width * 0.5f
                ),
                center = Offset(width * 0.5f, height * 0.15f),
                radius = width * 0.5f
            )

            // 3. Draw Animated Stars with Parallax Movement & Twinkling
            stars.forEach { star ->
                val currentY = ((star.relY + timeProgress * star.speedY) % 1f) * height
                val currentX = star.relX * width

                // Twinkle factor using sine wave: 0.2 to 1.0
                val twinkle = 0.2f + 0.8f * (0.5f + 0.5f * sin(twinkleTime * star.twinkleSpeed + star.twinklePhase))
                val alpha = twinkle.coerceIn(0.15f, 1.0f)
                val currentRadius = star.baseRadius * (0.8f + 0.4f * twinkle)

                // Outer soft halo for stars
                drawCircle(
                    color = star.color.copy(alpha = alpha * 0.3f),
                    radius = currentRadius * 2.2f,
                    center = Offset(currentX, currentY)
                )

                // Core star dot
                drawCircle(
                    color = star.color.copy(alpha = alpha),
                    radius = currentRadius,
                    center = Offset(currentX, currentY)
                )

                // Draw 4-point cross flare for shining stars
                if (star.isShiningStar) {
                    val flareLength = currentRadius * (3.5f + 2.5f * twinkle)
                    val flareAlpha = alpha * 0.85f
                    val flareColor = star.color.copy(alpha = flareAlpha)

                    // Vertical flare ray
                    drawLine(
                        color = flareColor,
                        start = Offset(currentX, currentY - flareLength),
                        end = Offset(currentX, currentY + flareLength),
                        strokeWidth = 1.2f
                    )

                    // Horizontal flare ray
                    drawLine(
                        color = flareColor,
                        start = Offset(currentX - flareLength, currentY),
                        end = Offset(currentX + flareLength, currentY),
                        strokeWidth = 1.2f
                    )
                }
            }

            // 4. Draw Moving Meteors / Shooting Stars
            meteors.forEach { meteor ->
                // Meteor cycle repeats over time
                val meteorCycle = (timeProgress * 5f + meteor.phaseOffset) % 1f
                if (meteorCycle < 0.35f) { // Active window for streak
                    val progress = meteorCycle / 0.35f
                    val dx = cos(meteor.angleRad)
                    val dy = sin(meteor.angleRad)

                    val startX = meteor.startRelX * width + progress * width * 0.5f * meteor.speed
                    val startY = meteor.startRelY * height + progress * height * 0.5f * meteor.speed
                    val headX = startX + dx * meteor.length
                    val headY = startY + dy * meteor.length

                    val tailAlpha = (1f - progress) * sin(progress * Math.PI.toFloat())

                    // Meteor tail line
                    drawLine(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0x8060A5FA),
                                Color.White
                            ),
                            start = Offset(startX, startY),
                            end = Offset(headX, headY)
                        ),
                        start = Offset(startX, startY),
                        end = Offset(headX, headY),
                        strokeWidth = 2.5f
                    )

                    // Glowing head point
                    drawCircle(
                        color = Color.White.copy(alpha = tailAlpha.coerceIn(0f, 1f)),
                        radius = 2.5f,
                        center = Offset(headX, headY)
                    )
                }
            }
        }

        // Content layer above the animated background
        content()
    }
}
