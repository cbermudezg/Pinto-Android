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

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sloth.partyquest.models.SpaceAvatars
import com.sloth.partyquest.viewmodel.AuthViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()
    val userProfile = uiState.userProfile

    var displayName by remember(userProfile) { mutableStateOf(userProfile?.displayName ?: "") }
    var selectedAvatar by remember(userProfile) { mutableStateOf(userProfile?.avatarId ?: SpaceAvatars.options.first()) }
    var customImageUri by remember(userProfile) { mutableStateOf<String?>(userProfile?.customImageUri) }
    var isSavedMessageVisible by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            customImageUri = it.toString()
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Black
    ) {
        SpaceBackground(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .widthIn(max = 520.dp)
                        .fillMaxWidth()
                        .border(1.dp, Color(0x40818CF8), RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xCC0F172A)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header Navigation Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = onBackToMenu,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF475569)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF94A3B8))
                            ) {
                                Text("⬅️ Back to Menu", fontSize = 12.sp)
                            }

                            Text(
                                text = "COSMIC PROFILE",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF38BDF8)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Avatar Display Frame
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                                .border(2.dp, Color(0xFF38BDF8), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!customImageUri.isNull_or_empty_str()) {
                                Text(
                                    text = "🖼️",
                                    fontSize = 42.sp
                                )
                            } else {
                                val emoji = selectedAvatar.split(" ").firstOrNull() ?: "👩‍🚀"
                                Text(
                                    text = emoji,
                                    fontSize = 42.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (userProfile?.isGuest == true) "Guest Explorer" else (userProfile?.email ?: ""),
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display Name Editor
                        OutlinedTextField(
                            value = displayName,
                            onValueChange = {
                                displayName = it
                                isSavedMessageVisible = false
                            },
                            label = { Text("Explorer Display Name", color = Color(0xFF94A3B8)) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF38BDF8),
                                unfocusedBorderColor = Color(0xFF334155),
                                focusedContainerColor = Color(0xFF020617),
                                unfocusedContainerColor = Color(0xFF020617)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Avatar Selection Section
                        Text(
                            text = "Choose Space Avatar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE2E8F0),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            SpaceAvatars.options.forEach { avatar ->
                                val isSelected = avatar == selectedAvatar && customImageUri.isNullOrEmpty()
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) Color(0xFF2563EB) else Color(0xFF1E293B),
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF38BDF8)) else null,
                                    modifier = Modifier
                                        .clickable {
                                            selectedAvatar = avatar
                                            customImageUri = null
                                            isSavedMessageVisible = false
                                        }
                                ) {
                                    Text(
                                        text = avatar,
                                        fontSize = 13.sp,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Custom Image/Avatar Picker
                        OutlinedButton(
                            onClick = {
                                imagePickerLauncher.launch("image/*")
                                isSavedMessageVisible = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38BDF8)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF38BDF8)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("🖼️ Select Custom Photo / Avatar Image")
                        }

                        if (!customImageUri.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Custom Image Selected",
                                fontSize = 11.sp,
                                color = Color(0xFF4ADE80)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Save Changes Button
                        Button(
                            onClick = {
                                authViewModel.updateProfile(
                                    displayName = displayName,
                                    avatarId = selectedAvatar,
                                    customImageUri = customImageUri
                                )
                                isSavedMessageVisible = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        ) {
                            Text("Save Profile Changes", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        if (isSavedMessageVisible) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "✓ Profile updated successfully!",
                                color = Color(0xFF4ADE80),
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun String?.isNull_or_empty_str(): Boolean = this == null || this.isEmpty()
