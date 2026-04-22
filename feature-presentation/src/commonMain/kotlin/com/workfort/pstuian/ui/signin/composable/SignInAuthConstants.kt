package com.workfort.pstuian.ui.signin.composable

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.ui.unit.dp

internal val SectionCornerRadius = 56.dp
internal val CompactHeaderHeight = 88.dp

// When not on Sign Up, the green hero header uses a fraction of screen height; Sign Up uses
// [CompactHeaderHeight] only (plus status bar inset where applied). Sign In uses a smaller green
// band so the form has more vertical space.
internal const val SignInGreenHeightFraction = 0.50f
internal const val ExpandedGreenHeightFraction = 0.58f

// Ease-out: responds quickly at the start then settles gently. Avoids the slight ease-in at the
// beginning of FastOutSlowIn, which can read as a bounce—especially when [totalHeight] jumps with
// the keyboard and the target green height is recomputed.
internal val SectionResizeEasing = LinearOutSlowInEasing
internal const val SectionResizeDurationMillis = 300
internal const val SignUpToggleResizeDurationMillis = 180
