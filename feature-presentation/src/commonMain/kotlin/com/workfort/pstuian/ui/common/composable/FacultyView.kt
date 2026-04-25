package com.workfort.pstuian.ui.common.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.featuredomain.model.Faculty
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.common.theme.bgCircle
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.ic_education_gray

@Composable
fun FacultyView(modifier: Modifier, faculty: Faculty) {
    ElevatedCard(
        modifier = modifier.clip(RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .bgCircle()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                LoadAsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    url = faculty.icon,
                    placeholder = Res.drawable.ic_education_gray,
                    contentScale = ContentScale.Fit,
                )
            }
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = faculty.shortTitle,
                style = TextStyle.label1.copy(
                    color = AppColors.textSecondary,
                    fontWeight = FontWeight.Bold
                ),
            )
        }
    }
}
