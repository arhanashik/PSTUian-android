package com.workfort.pstuian.ui.signin.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.ic_logo

/**
 * Extracted so that only [BoxScope] is in scope at the call site. When this was inlined inside the
 * green `Box { ... }` (which itself lives inside a `Column { ... }`), Kotlin resolved the call to
 * [ColumnScope.AnimatedVisibility] because the outer column scope was still visible, and then
 * failed to supply its implicit receiver.
 */
@Composable
internal fun BoxScope.SignInHeroReveal(visible: Boolean) {
    AnimatedVisibility(
        visible = visible,
        // Wait for the green section to finish growing, then slowly reveal the hero.
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 220,
                delayMillis = SectionResizeDurationMillis,
                easing = SectionResizeEasing,
            ),
        ),
        // Mirror of the reveal: fade out at the same pace as the reveal runs in, so the hero
        // gently dissolves at the start of the white section's expand rather than snapping away.
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 220,
                easing = SectionResizeEasing,
            ),
        ),
        modifier = Modifier.align(Alignment.BottomCenter),
        label = "signInHeroReveal",
    ) {
        SignInHero()
    }
}

@Composable
internal fun SignInHero() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .padding(bottom = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Welcome Back!",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}
