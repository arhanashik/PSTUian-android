package com.workfort.pstuian.ui.signin.composable

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.ui.unit.dp

internal val SectionCornerRadius = 56.dp
internal val CompactHeaderHeight = 88.dp

// White section height is driven by the form's measured content. If the form needs more than
// MAX_WHITE_FRACTION of the screen, the green header collapses to an app bar (CompactHeaderHeight)
// and the form takes the remainder; otherwise the green section fills whatever the form doesn't
// need so its size tracks the content.
internal const val MAX_WHITE_FRACTION = 0.75f
internal val WhiteSectionVerticalBuffer = 24.dp

// Ease-out: responds quickly at the start then settles gently. Avoids the slight ease-in at the
// beginning of FastOutSlowIn, which can read as a bounce—especially when [totalHeight] jumps with
// the keyboard and the target green height is recomputed.
internal val SectionResizeEasing = LinearOutSlowInEasing
internal const val SectionResizeDurationMillis = 300
