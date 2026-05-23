package com.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Homework
import com.example.data.Lesson
import com.example.data.UserProfile
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

// --- PRE-BUILT PREMIUM AVATARS ---
data class AvatarPreset(val id: Int, val emoji: String, val bgStart: Color, val bgEnd: Color)

val AVATARS = listOf(
    AvatarPreset(0, "🦁", Color(0xFFFF9500), Color(0xFFFF2D55)), // Lion Fire
    AvatarPreset(1, "🚀", Color(0xFF007AFF), Color(0xFFAF52DE)), // Rocket Space
    AvatarPreset(2, "🐱", Color(0xFF30D158), Color(0xFF00C6FF)), // Neon Cat
    AvatarPreset(3, "⭐", Color(0xFFDE37FF), Color(0xFFFF3021)), // Magic Star
    AvatarPreset(4, "🐲", Color(0xFFFF453A), Color(0xFF00F2FE)), // Cyber Dragon
    AvatarPreset(5, "📚", Color(0xFF5AC8FA), Color(0xFF5856D6))  // Learn Blue
)

// --- AURORA LIVE BACKGROUND ---
@Composable
fun AuroraBackground(
    isDark: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    // Shifting offsets using InfiniteTransition to represent active high-fidelity neural glowing
    val infiniteTransition = rememberInfiniteTransition(label = "AuroraOffset")
    
    val time1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "t1"
    )
    val time2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "t2"
    )

    // Slower burgundy shifter for background breathing
    val time3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(34000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "t3"
    )

    val baseBg = if (isDark) Color(0xFF070709) else Color(0xFFF2F2F7)

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                // Draw base color
                drawRect(baseBg)

                // Aurora coordinates
                val cx = size.width / 2
                val cy = size.height / 2
                
                val radius1 = size.width * 1.1f
                val radius2 = size.width * 0.9f
                val radius3 = size.width * 1.0f

                // Blue/Green/Pink circles drifting
                val x1 = cx + cos(time1) * (size.width * 0.28f)
                val y1 = cy + sin(time1) * (size.height * 0.15f)

                val x2 = cx + sin(time2) * (size.width * 0.24f)
                val y2 = cy + cos(time2) * (size.height * 0.18f)

                val x3 = cx + cos(time3 + 1.2f) * (size.width * 0.22f)
                val y3 = cy + sin(time3 + 0.8f) * (size.height * 0.22f)

                // Colors adapting based on Dark Mode or Light Mode
                val cBlue = if (isDark) AuroraBlue.copy(alpha = 0.35f) else AuroraBlue.copy(alpha = 0.15f)
                val cTeal = if (isDark) AuroraTeal.copy(alpha = 0.30f) else AuroraTeal.copy(alpha = 0.12f)
                val cBurgundy = if (isDark) AuroraBurgundy.copy(alpha = 0.32f) else AuroraBurgundy.copy(alpha = 0.14f)

                // Radial gradient brushes
                val brushBlue = Brush.radialGradient(
                    colors = listOf(cBlue, Color.Transparent),
                    center = Offset(x1, y1),
                    radius = radius1
                )
                val brushTeal = Brush.radialGradient(
                    colors = listOf(cTeal, Color.Transparent),
                    center = Offset(x2, y2),
                    radius = radius2
                )
                val brushBurgundy = Brush.radialGradient(
                    colors = listOf(cBurgundy, Color.Transparent),
                    center = Offset(x3, y3),
                    radius = radius3
                )

                // Blending overlay draws
                drawRect(brushBlue, blendMode = if (isDark) BlendMode.Plus else BlendMode.SrcOver)
                drawRect(brushTeal, blendMode = if (isDark) BlendMode.Plus else BlendMode.SrcOver)
                drawRect(brushBurgundy, blendMode = if (isDark) BlendMode.Plus else BlendMode.SrcOver)
            },
        content = content
    )
}

// --- LIQUID EFFECT NAVIGATION BAR ---
@Composable
fun LiquidBottomBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onPlusClicked: () -> Unit,
    lang: String,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    var itemWidths by remember { mutableStateOf(mutableMapOf<Int, Float>()) }
    var barWidth by remember { mutableStateOf(0f) }

    // Convert tab index to index layout slots: Left side (0, 1), center space represents Plus, Right side (2) is Settings.
    // Let's map screen indexes:
    // Tab Index 0 -> Layout Slot 0 (Schedule)
    // Tab Index 1 -> Layout Slot 1 (Homework)
    // Tab Index 2 -> Layout Slot 3 (Settings) - Slot 2 is for center plus!
    val selectedLayoutSlot = when (selectedTab) {
        0 -> 0
        1 -> 1
        else -> 3
    }

    // Determine target offset for the liquid drop bubble based on active layout width ratios
    val targetX = remember(selectedLayoutSlot, itemWidths, barWidth) {
        val totalSlots = 4
        if (barWidth > 0 && itemWidths.size >= 3) {
            val slotWidth = barWidth / totalSlots
            when (selectedLayoutSlot) {
                0 -> slotWidth * 0.5f
                1 -> slotWidth * 1.5f
                3 -> slotWidth * 3.5f
                else -> slotWidth * 0.5f
            }
        } else {
            0f
        }
    }

    // Animate targetX using liquid physics spring
    val elasticX by animateFloatAsState(
        targetValue = targetX,
        animationSpec = spring(
            dampingRatio = 0.54f, // organic bouncy feel
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "elasticX"
    )

    // Calculate instantaneous offset distance to determine stretch factor
    val deltaX = targetX - elasticX
    val absDeltaX = abs(deltaX)

    // Elongate bubble and squeeze thickness based on speed (deltaX)
    val maxStretchWidth = 110f
    val baseWidth = 90f
    val animateWidth = (baseWidth + (absDeltaX * 0.42f)).coerceAtMost(maxStretchWidth)

    val minHeight = 44f
    val baseHeight = 56f
    val animateHeight = (baseHeight - (absDeltaX * 0.18f)).coerceAtLeast(minHeight)

    val surfaceColor = if (isDark) Color(0x350F0F14) else Color(0x7FFFFFFF)
    val indicatorColor = if (isDark) Color(0x1Affffff) else Color(0x1F000000)
    val activeTint = if (isDark) Color(0xFF30D158) else Color(0xFF007AFF)
    val inactiveTint = if (isDark) Color(0x7Fffffff) else Color(0x7F1C1C1E)

    Box(
        modifier = modifier
            .testTag("liquid_bottom_bar")
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .navigationBarsPadding() // avoid overlapping with gesture navigation line
            .fillMaxWidth()
            .height(72.dp)
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(24.dp),
                clip = false,
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.25f)
            )
            .background(surfaceColor, RoundedCornerShape(24.dp))
            .border(
                1.dp,
                if (isDark) Color(0x19FFFFFF) else Color(0x33000000),
                RoundedCornerShape(24.dp)
            )
            .onGloballyPositioned { coords ->
                barWidth = coords.size.width.toFloat()
            }
    ) {
        // Draw the Organic Liquid droplet behind active icons
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
        ) {
            if (barWidth > 0 && targetX > 0) {
                // Determine layout bounds for capsule
                val capWidth = animateWidth
                val capHeight = animateHeight
                
                // Draw liquid droplet capsule centering horizontally on elasticX
                val posX = elasticX - (capWidth / 2)
                val posY = (size.height - capHeight) / 2

                drawRoundRect(
                    color = indicatorColor,
                    topLeft = Offset(posX, posY),
                    size = Size(capWidth, capHeight),
                    cornerRadius = CornerRadius(capHeight / 2, capHeight / 2)
                )

                // Optional: liquid border tint accent
                drawRoundRect(
                    color = if (isDark) Color(0x33FFFFFF) else Color(0x22000000),
                    topLeft = Offset(posX, posY),
                    size = Size(capWidth, capHeight),
                    cornerRadius = CornerRadius(capHeight / 2, capHeight / 2),
                    style = Stroke(width = 1.dp.toPx())
                )
            }
        }

        // Layout rows
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val totalSlots = 4
            val displayDensity = LocalDensity.current

            // Slot 0: Schedule
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, 
                        onClick = { onTabSelected(0) }
                    )
                    .testTag("tab_schedule"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (selectedTab == 0) Icons.Filled.DateRange else Icons.Outlined.DateRange,
                        contentDescription = "Schedule",
                        tint = if (selectedTab == 0) activeTint else inactiveTint,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = Localization.get("app_title", lang),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selectedTab == 0) activeTint else inactiveTint,
                        maxLines = 1
                    )
                }
            }

            // Slot 1: Homework (ДЗ)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, 
                        onClick = { onTabSelected(1) }
                    )
                    .testTag("tab_homework"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (selectedTab == 1) Icons.Filled.AssignmentTurnedIn else Icons.Outlined.AssignmentTurnedIn,
                        contentDescription = "Homework",
                        tint = if (selectedTab == 1) activeTint else inactiveTint,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = Localization.get("homework", lang),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selectedTab == 1) activeTint else inactiveTint,
                        maxLines = 1
                    )
                }
            }

            // Slot 2: Large Floating Round "+" Plus Button (Calls Lesson Config dialog)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .testTag("tab_plus"),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(AuroraBlue, AuroraBurgundy)
                            )
                        )
                        .clickable(onClick = onPlusClicked)
                        .shadow(8.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add lesson",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Slot 3: Settings
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null, 
                        onClick = { onTabSelected(2) }
                    )
                    .testTag("tab_settings"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (selectedTab == 2) Icons.Filled.Settings else Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = if (selectedTab == 2) activeTint else inactiveTint,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = Localization.get("settings", lang),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selectedTab == 2) activeTint else inactiveTint,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// --- NEON PULSATING GLOW BORDER MODIFIER ---
@Composable
fun Modifier.neonGlowBorder(
    enabled: Boolean,
    shape: RoundedCornerShape
): Modifier {
    if (!enabled) return this

    val infiniteTransition = rememberInfiniteTransition(label = "NeonSparkle")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rot"
    )

    return this.drawBehind {
        val strokeWidth = 3.dp.toPx()
        val borderBrush = Brush.sweepGradient(
            colors = listOf(GlowBlue, GlowGreen, GlowCrimson, GlowBlue),
            center = Offset(size.width / 2, size.height / 2)
        )

        // Draw an ultra glowing wider blurred stroke or double layered border trace
        drawRoundRect(
            brush = borderBrush,
            size = size,
            cornerRadius = CornerRadius(shape.topStart.toPx(size, this), shape.topStart.toPx(size, this)),
            style = Stroke(width = strokeWidth + 1.dp.toPx())
        )
        // Draw inside secondary highlight border
        drawRoundRect(
            color = Color.White.copy(alpha = 0.35f),
            size = size,
            cornerRadius = CornerRadius(shape.topStart.toPx(size, this), shape.topStart.toPx(size, this)),
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

// --- APP ENTRY CHANGER ---
@Composable
fun ScheduleAppContent(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val profileState by viewModel.profileState.collectAsStateWithLifecycle()
    val lessonsState by viewModel.lessonsState.collectAsStateWithLifecycle()
    val homeworkState by viewModel.homeworkState.collectAsStateWithLifecycle()
    
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val scheduleDay by viewModel.scheduleDay.collectAsStateWithLifecycle()
    val homeworkDay by viewModel.homeworkDay.collectAsStateWithLifecycle()
    val selectedLessonId by viewModel.selectedLessonId.collectAsStateWithLifecycle()
    val showAddDialog by viewModel.showAddLessonDialog.collectAsStateWithLifecycle()

    val lang = profileState?.language ?: "RU"
    val isDark = profileState?.isDarkTheme ?: true

    Box(modifier = modifier.fillMaxSize()) {
        AuroraBackground(isDark = isDark) {
            if (profileState == null) {
                // First Onboarding Entry
                OnboardingScreen(
                    onSave = { name, surname, avatar ->
                        viewModel.saveUserProfile(name, surname, avatar, isDarkTheme = true, language = "RU")
                    }
                )
            } else {
                val profile = profileState!!
                // Main application scaffolding
                Scaffold(
                    containerColor = Color.Transparent,
                    bottomBar = {
                        LiquidBottomBar(
                            selectedTab = selectedTab,
                            onTabSelected = { viewModel.selectTab(it) },
                            onPlusClicked = { viewModel.setShowAddLessonDialog(true) },
                            lang = lang,
                            isDark = isDark
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        AnimatedContent(
                            targetState = selectedTab,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                            },
                            label = "screen_navigation"
                        ) { targetTab ->
                            when (targetTab) {
                                0 -> ScheduleTab(
                                    profile = profile,
                                    lessons = lessonsState,
                                    selectedDay = scheduleDay,
                                    selectedLessonId = selectedLessonId,
                                    onDaySelected = { viewModel.selectScheduleDay(it) },
                                    onLessonClicked = { viewModel.toggleLessonGlow(it) },
                                    onDeleteLesson = { viewModel.deleteLesson(it) },
                                    viewModel = viewModel,
                                    lang = lang,
                                    isDark = isDark
                                )
                                1 -> HomeworkTab(
                                    profile = profile,
                                    lessons = lessonsState,
                                    homeworkList = homeworkState,
                                    selectedDay = homeworkDay,
                                    onDaySelected = { viewModel.selectHomeworkDay(it) },
                                    onSaveHomework = { lessonNum, assigned, hwTomorrow, tDone, tmDone ->
                                        viewModel.saveHomework(homeworkDay, lessonNum, assigned, hwTomorrow, tDone, tmDone)
                                    },
                                    lang = lang,
                                    isDark = isDark
                                )
                                2 -> SettingsTab(
                                    profile = profile,
                                    onProfileUpdated = { name, surname, avatar ->
                                        viewModel.saveUserProfile(name, surname, avatar, profile.isDarkTheme, profile.language)
                                    },
                                    onThemeChanged = { viewModel.updateProfileTheme(it) },
                                    onLanguageChanged = { viewModel.updateProfileLanguage(it) },
                                    lang = lang,
                                    isDark = isDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // Add Lesson sheet Dialog overlay
        if (showAddDialog) {
            AddLessonDialog(
                lang = lang,
                isDark = isDark,
                onDismiss = { viewModel.setShowAddLessonDialog(false) },
                onAdd = { day, num, subject, start, end ->
                    viewModel.addLesson(day, num, subject, start, end)
                    viewModel.setShowAddLessonDialog(false)
                }
            )
        }
    }
}

// --- PRIMARY SCREEN MODULES ---

// 1. ONBOARDING SCREEN (FIRST RUN INITIALIZATION)
@Composable
fun OnboardingScreen(
    onSave: (name: String, surname: String, avatarId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf(0) }
    var errorMsg by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .shadow(24.dp, RoundedCornerShape(28.dp))
                .background(Color(0xBB0F0F14), RoundedCornerShape(28.dp))
                .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(28.dp))
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.school_diary_icon),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(80.dp)
                    .shadow(16.dp, CircleShape)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = Localization.get("onboarding_title", "RU"),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = Localization.get("onboarding_desc", "RU"),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Avatar circle grid
            Text(
                text = Localization.get("edit_avatar", "RU"),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AVATARS.take(6).forEach { preset ->
                    val isSelected = selectedAvatar == preset.id
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(preset.bgStart, preset.bgEnd)))
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) Color.White else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { selectedAvatar = preset.id },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(preset.emoji, fontSize = 22.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Inputs
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(Localization.get("first_name", "RU")) },
                textStyle = TextStyle(color = Color.White),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuroraBlue,
                    unfocusedBorderColor = Color(0x33FFFFFF),
                    focusedLabelColor = auroraBlueBrushColor(),
                    unfocusedLabelColor = Color(0x7Fffffff)
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_first_name_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(Localization.get("last_name", "RU")) },
                textStyle = TextStyle(color = Color.White),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AuroraBlue,
                    unfocusedBorderColor = Color(0x33FFFFFF),
                    focusedLabelColor = auroraBlueBrushColor(),
                    unfocusedLabelColor = Color(0x7Fffffff)
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_last_name_input")
            )

            if (errorMsg.isNotEmpty()) {
                Text(
                    text = errorMsg,
                    color = ErrorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (firstName.isNotBlank() && lastName.isNotBlank()) {
                        onSave(firstName.trim(), lastName.trim(), selectedAvatar)
                    } else {
                        errorMsg = Localization.get("invalid_fields", "RU")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("onboarding_save_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AuroraBlue)
            ) {
                Text(Localization.get("save", "RU"), color = Color.White, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

// 2. MAIN SCHEDULE SCREEN
@Composable
fun ScheduleTab(
    profile: UserProfile,
    lessons: List<Lesson>,
    selectedDay: Int,
    selectedLessonId: Int?,
    onDaySelected: (Int) -> Unit,
    onLessonClicked: (Int) -> Unit,
    onDeleteLesson: (Int) -> Unit,
    viewModel: MainViewModel,
    lang: String,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val preset = AVATARS.find { it.id == profile.avatarId } ?: AVATARS[0]

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = Localization.getFormatted("welcome", lang, profile.firstName),
                        style = MaterialTheme.typography.displayMedium,
                        color = if (isDark) Color.White else Color.Black
                    )
                }

                // Apple-style round avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(preset.bgStart, preset.bgEnd)))
                        .border(1.dp, Color(0x33FFFFFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(preset.emoji, fontSize = 24.sp)
                }
            }

            // Next Lesson Widget
            val nextStatus = remember(lessons, lang) { viewModel.getNextLessonStatus(lessons, lang) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .shadow(12.dp, RoundedCornerShape(18.dp))
                    .background(
                        if (isDark) Color(0x3D141419) else Color(0x90FFFFFF),
                        RoundedCornerShape(18.dp)
                    )
                    .border(
                        1.dp,
                        if (isDark) Color(0x19FFFFFF) else Color(0x22000000),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(colors = listOf(AuroraBlue, AuroraBurgundy))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Active", tint = Color.White)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = Localization.get("next_lesson", lang),
                            fontSize = 12.sp,
                            color = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold
                        )
                        if (nextStatus != null) {
                            Text(
                                text = nextStatus.lessonName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isDark) Color.White else Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            val statusStr = Localization.getFormatted(nextStatus.statusTextKey, lang, nextStatus.relativeMinutes)
                            Text(
                                text = "${nextStatus.timeLabel} • $statusStr",
                                fontSize = 13.sp,
                                color = if (isDark) GlowGreen else LightPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        } else {
                            Text(
                                text = Localization.get("no_next_lesson", lang),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDark) Color.White else Color.Black
                            )
                        }
                    }
                }
            }

            // Horizontal Day Tab Switcher (ПН-ПТ)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isDark) Color(0x1Affffff) else Color(0x11000000))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 1..5) {
                    val dayKey = when(i) {
                        1 -> "day_monday"
                        2 -> "day_tuesday"
                        3 -> "day_wednesday"
                        4 -> "day_thursday"
                        else -> "day_friday"
                    }
                    val isSelected = selectedDay == i
                    val dayLabel = Localization.get(dayKey, lang)
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(11.dp))
                            .background(
                                if (isSelected) {
                                    if (isDark) Color(0x35ffffff) else Color.White
                                } else {
                                    Color.Transparent
                                }
                            )
                            .clickable { onDaySelected(i) }
                            .padding(vertical = 8.dp)
                            .testTag("day_tab_$i"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayLabel,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isSelected) {
                                if (isDark) Color.White else LightPrimary
                            } else {
                                if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f)
                            }
                        )
                    }
                }
            }

            // Central Lesson List (1 to 8 classes)
            val dayLessons = lessons.filter { it.dayOfWeek == selectedDay }
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = Localization.get("hint_click_neon", lang),
                    fontSize = 11.sp,
                    color = if (isDark) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items((1..8).toList()) { slotNum ->
                        val lesson = dayLessons.find { it.lessonNumber == slotNum }
                        val isGlowing = selectedLessonId == lesson?.id && lesson != null

                        val cardShape = RoundedCornerShape(16.dp)
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(if (isGlowing) 16.dp else 4.dp, cardShape)
                                .neonGlowBorder(isGlowing, cardShape)
                                .background(
                                    if (isDark) Color(0x350A0A0E) else Color(0xB5FFFFFF),
                                    cardShape
                                )
                                .border(
                                    1.dp,
                                    if (isGlowing) Color.Transparent else {
                                        if (isDark) Color(0x1AFFFFFF) else Color(0x1F000000)
                                    },
                                    cardShape
                                )
                                .clickable {
                                    if (lesson != null) {
                                        onLessonClicked(lesson.id)
                                    }
                                }
                                .padding(14.dp)
                                .testTag("lesson_slot_$slotNum")
                        ) {
                            if (lesson != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (isDark) Color(0x1Fffffff) else Color(0x15007aff)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = slotNum.toString(),
                                                color = if (isDark) Color.White else LightPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = lesson.subjectName,
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDark) Color.White else Color.Black
                                            )
                                            Text(
                                                text = "${lesson.startTime} - ${lesson.endTime}",
                                                fontSize = 12.sp,
                                                color = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)
                                            )
                                        }
                                    }

                                    // Swipe margin delete action button
                                    IconButton(
                                        onClick = { onDeleteLesson(lesson.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = ErrorColor.copy(alpha = 0.8f),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            } else {
                                // Empty placeholder slot
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .border(
                                                    1.dp,
                                                    if (isDark) Color(0x33ffffff) else Color(0x22000000),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = slotNum.toString(),
                                                color = if (isDark) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.3f),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = Localization.get("empty_subject", lang),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Normal,
                                                color = if (isDark) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.3f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 3. HOMEWORK TAB
@Composable
fun HomeworkTab(
    profile: UserProfile,
    lessons: List<Lesson>,
    homeworkList: List<Homework>,
    selectedDay: Int,
    onDaySelected: (Int) -> Unit,
    onSaveHomework: (lessonNum: Int, assigned: String, hwTomorrow: String, todayDone: Boolean, tomorrowDone: Boolean) -> Unit,
    lang: String,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val preset = AVATARS.find { it.id == profile.avatarId } ?: AVATARS[0]
    val dayLessons = lessons.filter { it.dayOfWeek == selectedDay }
    val dayHomeworks = homeworkList.filter { it.dayOfWeek == selectedDay }

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = Localization.getFormatted("welcome", lang, profile.firstName),
                        style = MaterialTheme.typography.displayMedium,
                        color = if (isDark) Color.White else Color.Black
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(preset.bgStart, preset.bgEnd)))
                        .border(1.dp, Color(0x33FFFFFF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(preset.emoji, fontSize = 24.sp)
                }
            }

            // Horizontal Day Tab Switcher (ПН-ПТ)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isDark) Color(0x1Affffff) else Color(0x11000000))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (i in 1..5) {
                    val dayKey = when(i) {
                        1 -> "day_monday"
                        2 -> "day_tuesday"
                        3 -> "day_wednesday"
                        4 -> "day_thursday"
                        else -> "day_friday"
                    }
                    val isSelected = selectedDay == i
                    val dayLabel = Localization.get(dayKey, lang)
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(11.dp))
                            .background(
                                if (isSelected) {
                                    if (isDark) Color(0x35ffffff) else Color.White
                                } else {
                                    Color.Transparent
                                }
                            )
                            .clickable { onDaySelected(i) }
                            .padding(vertical = 8.dp)
                            .testTag("hw_day_tab_$i"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayLabel,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isSelected) {
                                if (isDark) Color.White else LightPrimary
                            } else {
                                if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.4f)
                            }
                        )
                    }
                }
            }

            // Scrollable list of homework classes 1 to 8
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items((1..8).toList()) { num ->
                    val lesson = dayLessons.find { it.lessonNumber == num }
                    val homework = dayHomeworks.find { it.lessonNumber == num }

                    HomeworkItemCard(
                        lessonNumber = num,
                        lesson = lesson,
                        homework = homework,
                        onSaved = { assigned, hwTomorrow, todayDone, tomorrowDone ->
                            onSaveHomework(num, assigned, hwTomorrow, todayDone, tomorrowDone)
                        },
                        lang = lang,
                        isDark = isDark
                    )
                }
            }
        }
    }
}

// Sub Card for individual homework controls
@Composable
fun HomeworkItemCard(
    lessonNumber: Int,
    lesson: Lesson?,
    homework: Homework?,
    onSaved: (assigned: String, hwTomorrow: String, todayDone: Boolean, tomorrowDone: Boolean) -> Unit,
    lang: String,
    isDark: Boolean
) {
    var isExpanded by remember { mutableStateOf(false) }

    // Text editors
    var textAssigned by remember(homework) { mutableStateOf(homework?.assignedToday ?: "") }
    var textTomorrow by remember(homework) { mutableStateOf(homework?.homeworkTomorrow ?: "") }

    // Checkboxes
    var isTodayCompleted by remember(homework) { mutableStateOf(homework?.isTodayCompleted ?: false) }
    var isTomorrowCompleted by remember(homework) { mutableStateOf(homework?.isTomorrowCompleted ?: false) }

    val coroutineScope = rememberCoroutineScope()
    val cardShape = RoundedCornerShape(18.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, cardShape)
            .background(
                if (isDark) Color(0x2E0E0E14) else Color(0xF2FFFFFF),
                cardShape
            )
            .border(
                1.dp,
                if (isDark) Color(0x19FFFFFF) else Color(0x1A000000),
                cardShape
            )
            .padding(14.dp)
            .animateContentSize()
            .testTag("hw_card_slot_$lessonNumber")
    ) {
        Column {
            // Header: Lesson number and Name
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isDark) Color(0x15ffffff) else Color(0x10007aff)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lessonNumber.toString(),
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color.White else LightPrimary,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = lesson?.subjectName ?: Localization.get("empty_subject", lang),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (isDark) Color.White else Color.Black,
                            textDecoration = if (isTodayCompleted && isTomorrowCompleted) TextDecoration.LineThrough else TextDecoration.None
                        )
                        if (lesson != null) {
                            Text(
                                text = "${lesson.startTime} - ${lesson.endTime}",
                                fontSize = 11.sp,
                                color = if (isDark) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f)
                            )
                        }
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)
                )
            }

            // Quick Status preview if not expanded
            if (!isExpanded) {
                if (textAssigned.isNotEmpty() || textTomorrow.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (textAssigned.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isTodayCompleted) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                                    contentDescription = "Done",
                                    tint = if (isTodayCompleted) SuccessColor else if (isDark) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.3f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = textAssigned,
                                    fontSize = 13.sp,
                                    color = if (isTodayCompleted) (if (isDark) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f)) else (if (isDark) Color.White else Color.Black),
                                    textDecoration = if (isTodayCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        if (textTomorrow.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isTomorrowCompleted) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                                    contentDescription = "Done",
                                    tint = if (isTomorrowCompleted) SuccessColor else if (isDark) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.3f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = textTomorrow,
                                    fontSize = 13.sp,
                                    color = if (isTomorrowCompleted) (if (isDark) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f)) else (if (isDark) Color.White else Color.Black),
                                    textDecoration = if (isTomorrowCompleted) TextDecoration.LineThrough else TextDecoration.None,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Expanded Rich Editors & checkmarks
            if (isExpanded) {
                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = if (isDark) Color(0x19FFFFFF) else Color(0x0F000000))
                Spacer(modifier = Modifier.height(12.dp))

                // Block 1: "Задали на сегодня"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = Localization.get("assigned_today", lang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f)
                    )

                    IconButton(
                        onClick = {
                            isTodayCompleted = !isTodayCompleted
                            onSaved(textAssigned, textTomorrow, isTodayCompleted, isTomorrowCompleted)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isTodayCompleted) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                            contentDescription = "Complete today",
                            tint = if (isTodayCompleted) SuccessColor else (if (isDark) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = textAssigned,
                    onValueChange = {
                        textAssigned = it
                        onSaved(textAssigned, textTomorrow, isTodayCompleted, isTomorrowCompleted)
                    },
                    placeholder = { Text(Localization.get("placeholder_assign", lang), fontSize = 13.sp) },
                    textStyle = TextStyle(
                        color = if (isDark) Color.White else Color.Black,
                        fontSize = 14.sp,
                        textDecoration = if (isTodayCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AuroraBlue,
                        unfocusedBorderColor = if (isDark) Color(0x1EFFFFFF) else Color(0x1F000000),
                        focusedPlaceholderColor = Color(0x66ffffff)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Block 2: "Домашка на завтра"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = Localization.get("dues_tomorrow", lang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f)
                    )

                    IconButton(
                        onClick = {
                            isTomorrowCompleted = !isTomorrowCompleted
                            onSaved(textAssigned, textTomorrow, isTodayCompleted, isTomorrowCompleted)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isTomorrowCompleted) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                            contentDescription = "Complete tomorrow",
                            tint = if (isTomorrowCompleted) SuccessColor else (if (isDark) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = textTomorrow,
                    onValueChange = {
                        textTomorrow = it
                        onSaved(textAssigned, textTomorrow, isTodayCompleted, isTomorrowCompleted)
                    },
                    placeholder = { Text(Localization.get("placeholder_assign", lang), fontSize = 13.sp) },
                    textStyle = TextStyle(
                        color = if (isDark) Color.White else Color.Black,
                        fontSize = 14.sp,
                        textDecoration = if (isTomorrowCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AuroraBlue,
                        unfocusedBorderColor = if (isDark) Color(0x1EFFFFFF) else Color(0x1F000000),
                        focusedPlaceholderColor = Color(0x66ffffff)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                )
            }
        }
    }
}

// 4. SETTINGS TAB (IPHONE STYLE GROUPED CARDS)
@Composable
fun SettingsTab(
    profile: UserProfile,
    onProfileUpdated: (name: String, surname: String, avatarId: Int) -> Unit,
    onThemeChanged: (Boolean) -> Unit,
    onLanguageChanged: (String) -> Unit,
    lang: String,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    var isProfileEditing by remember { mutableStateOf(false) }

    // Profil tmp edit state
    var editName by remember { mutableStateOf(profile.firstName) }
    var editSurname by remember { mutableStateOf(profile.lastName) }
    var editAvatar by remember { mutableStateOf(profile.avatarId) }

    val preset = AVATARS.find { it.id == profile.avatarId } ?: AVATARS[0]
    val blockShape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = Localization.get("settings", lang),
                style = MaterialTheme.typography.displayMedium,
                color = if (isDark) Color.White else Color.Black
            )

            // BLOCK 1: PROFILE BLOCK
            if (!isProfileEditing) {
                // Read-only profile card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, blockShape)
                        .background(
                            if (isDark) Color(0x35141419) else Color(0xB5FFFFFF),
                            blockShape
                        )
                        .border(
                            1.dp,
                            if (isDark) Color(0x19FFFFFF) else Color(0x1F000000),
                            blockShape
                        )
                        .clickable {
                            // Init editors
                            editName = profile.firstName
                            editSurname = profile.lastName
                            editAvatar = profile.avatarId
                            isProfileEditing = true
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(preset.bgStart, preset.bgEnd))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(preset.emoji, fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "${profile.firstName} ${profile.lastName}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = if (isDark) Color.White else Color.Black
                        )
                        Text(
                            text = Localization.get("edit_profile", lang),
                            fontSize = 12.sp,
                            color = if (isDark) GlowBlue else LightPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // Editing profile block inline
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, blockShape)
                        .background(
                            if (isDark) Color(0x4D0F0F14) else Color(0xEBFFFFFF),
                            blockShape
                        )
                        .border(
                            1.dp,
                            if (isDark) Color(0x33FFFFFF) else Color(0x2E000000),
                            blockShape
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = Localization.get("edit_profile", lang),
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black,
                        fontSize = 16.sp
                    )

                    // Avatar selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AVATARS.forEach { item ->
                            val isSelected = editAvatar == item.id
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(listOf(item.bgStart, item.bgEnd)))
                                    .border(
                                        width = if (isSelected) 3.dp else 0.dp,
                                        color = if (isSelected) (if (isDark) Color.White else LightPrimary) else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable { editAvatar = item.id },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(item.emoji, fontSize = 18.sp)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text(Localization.get("first_name", lang)) },
                        textStyle = TextStyle(color = if (isDark) Color.White else Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AuroraBlue,
                            unfocusedBorderColor = if (isDark) Color(0x1EFFFFFF) else Color(0x1F000000)
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editSurname,
                        onValueChange = { editSurname = it },
                        label = { Text(Localization.get("last_name", lang)) },
                        textStyle = TextStyle(color = if (isDark) Color.White else Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AuroraBlue,
                            unfocusedBorderColor = if (isDark) Color(0x1EFFFFFF) else Color(0x1F000000)
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { isProfileEditing = false }) {
                            Text(Localization.get("cancel", lang), color = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f))
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (editName.isNotBlank() && editSurname.isNotBlank()) {
                                    onProfileUpdated(editName.trim(), editSurname.trim(), editAvatar)
                                    isProfileEditing = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isDark) GlowGreen else LightPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(Localization.get("save", lang), color = if (isDark) Color.Black else Color.White)
                        }
                    }
                }
            }

            // BLOCK 2: INTERFACE & THEMES (Grouped iPhone menu)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, blockShape)
                    .background(
                        if (isDark) Color(0x35141419) else Color(0xB5FFFFFF),
                        blockShape
                    )
                    .border(
                        1.dp,
                        if (isDark) Color(0x19FFFFFF) else Color(0x1F000000),
                        blockShape
                    )
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                // Row 1: Theme Switcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BrightnessMedium,
                            contentDescription = "Theme",
                            tint = if (isDark) GlowGreen else LightPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = Localization.get("theme", lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (isDark) Color.White else Color.Black
                            )
                            Text(
                                text = if (isDark) Localization.get("theme_dark", lang) else Localization.get("theme_light", lang),
                                fontSize = 11.sp,
                                color = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)
                            )
                        }
                    }

                    Switch(
                        checked = isDark,
                        onCheckedChange = { onThemeChanged(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = if (isDark) GlowGreen else LightPrimary
                        ),
                        modifier = Modifier.testTag("theme_toggle")
                    )
                }

                Divider(
                    color = if (isDark) Color(0x0FFFFFFF) else Color(0x0A000000), 
                    modifier = Modifier.padding(horizontal = 14.dp)
                )

                // Row 2: Language Switcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Language",
                            tint = if (isDark) GlowBlue else AuroraBurgundy,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = Localization.get("language", lang),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (isDark) Color.White else Color.Black
                            )
                            Text(
                                text = if (lang == "UA") "Українська" else "Русский",
                                fontSize = 11.sp,
                                color = if (isDark) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.5f)
                            )
                        }
                    }

                    // Simple modern iOS styled selection pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isDark) Color(0x22FFFFFF) else Color(0x19000000))
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (lang == "RU") (if (isDark) Color(0x25ffffff) else Color.White) else Color.Transparent)
                                .clickable { onLanguageChanged("RU") }
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                                .testTag("lang_ru"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "RU", 
                                fontSize = 11.sp, 
                                fontWeight = FontWeight.Bold,
                                color = if (lang == "RU") (if (isDark) Color.White else LightPrimary) else (if (isDark) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f))
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (lang == "UA") (if (isDark) Color(0x25ffffff) else Color.White) else Color.Transparent)
                                .clickable { onLanguageChanged("UA") }
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                                .testTag("lang_ua"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "UA", 
                                fontSize = 11.sp, 
                                fontWeight = FontWeight.Bold,
                                color = if (lang == "UA") (if (isDark) Color.White else LightPrimary) else (if (isDark) Color.White.copy(alpha = 0.4f) else Color.Black.copy(alpha = 0.4f))
                            )
                        }
                    }
                }
            }
        }
    }
}

// 5. ADD LESSON SHEET MODAL COMPOSABLE
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLessonDialog(
    lang: String,
    isDark: Boolean,
    onDismiss: () -> Unit,
    onAdd: (day: Int, lessonNum: Int, subjectName: String, startTime: String, endTime: String) -> Unit
) {
    var selectedDay by remember { mutableStateOf(1) } // 1: ПН
    var selectedNum by remember { mutableStateOf(1) }

    var subjectName by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    var hasError by remember { mutableStateOf(false) }

    val daysText = listOf("day_monday", "day_tuesday", "day_wednesday", "day_thursday", "day_friday")

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .padding(16.dp)
            .shadow(24.dp, RoundedCornerShape(24.dp))
            .background(
                if (isDark) Color(0xEE0B0B0F) else Color(0xEEFDFDFD),
                RoundedCornerShape(24.dp)
            )
            .border(
                1.dp,
                if (isDark) Color(0x2BFFFFFF) else Color(0x1F000000),
                RoundedCornerShape(24.dp)
            )
            .testTag("add_lesson_dialog"),
        confirmButton = {
            Button(
                onClick = {
                    // Quick Validation
                    val cleanSubject = subjectName.trim()
                    val cleanStart = startTime.trim()
                    val cleanEnd = endTime.trim()
                    
                    if (cleanSubject.isNotEmpty() && cleanStart.isNotEmpty() && cleanEnd.isNotEmpty()) {
                        onAdd(selectedDay, selectedNum, cleanSubject, cleanStart, cleanEnd)
                    } else {
                        hasError = true
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AuroraBlue),
                modifier = Modifier.testTag("dialog_confirm_btn")
            ) {
                Text(Localization.get("add_lesson_btn", lang), color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("dialog_dismiss_btn")
            ) {
                Text(
                    text = Localization.get("cancel", lang),
                    color = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
                )
            }
        },
        title = {
            Text(
                text = Localization.get("add_lesson_title", lang),
                style = MaterialTheme.typography.titleLarge,
                color = if (isDark) Color.White else Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Day of Week
                Text(
                    text = Localization.get("choose_day", lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    daysText.forEachIndexed { index, key ->
                        val dayIndex = index + 1
                        val isSelected = selectedDay == dayIndex
                        val isDarkBorder = if (isDark) Color(0x19ffffff) else Color(0x1A000000)
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1.2f)
                                .padding(2.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) AuroraBlue else (if (isDark) Color(0x1Affffff) else Color(0x0F000000)))
                                .border(1.dp, if (isSelected) Color.Transparent else isDarkBorder, RoundedCornerShape(8.dp))
                                .clickable { selectedDay = dayIndex }
                                .testTag("dialog_day_select_$dayIndex"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = Localization.get(key, lang),
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else (if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)),
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Lesson Number
                Text(
                    text = Localization.get("choose_num", lang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f)
                )

                // Render numbers row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    (1..8).forEach { num ->
                        val isSelected = selectedNum == num
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) AuroraBurgundy else (if (isDark) Color(0x15ffffff) else Color(0x0F000000)))
                                .clickable { selectedNum = num }
                                .testTag("dialog_num_select_$num"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = num.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else (if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f))
                            )
                        }
                    }
                }

                Divider(color = if (isDark) Color(0x19ffffff) else Color(0x11000000))

                // Text fields
                OutlinedTextField(
                    value = subjectName,
                    onValueChange = { subjectName = it },
                    label = { Text(Localization.get("subject_name", lang)) },
                    textStyle = TextStyle(color = if (isDark) Color.White else Color.Black),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AuroraBlue,
                        unfocusedBorderColor = if (isDark) Color(0x2EFFFFFF) else Color(0x28000000)
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_field_subject")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text(Localization.get("start_time", lang)) },
                        placeholder = { Text("08:30") },
                        textStyle = TextStyle(color = if (isDark) Color.White else Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AuroraBlue,
                            unfocusedBorderColor = if (isDark) Color(0x2EFFFFFF) else Color(0x28000000)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_field_start")
                    )

                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text(Localization.get("end_time", lang)) },
                        placeholder = { Text("09:15") },
                        textStyle = TextStyle(color = if (isDark) Color.White else Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AuroraBlue,
                            unfocusedBorderColor = if (isDark) Color(0x2EFFFFFF) else Color(0x28000000)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dialog_field_end")
                    )
                }

                if (hasError) {
                    Text(
                        text = Localization.get("invalid_fields", lang),
                        color = ErrorColor,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

// Utility colors
@Composable
fun auroraBlueBrushColor(): Color = AuroraBlue
