package com.workfort.pstuian.common.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.ic_education_gray
import com.workfort.pstuian.model.FacultyEntity
import com.workfort.pstuian.common.theme.bgCircle


@Composable
fun FacultyView(modifier: Modifier, faculty: FacultyEntity) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoadAsyncImage(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .bgCircle()
                    .padding(12.dp)
                    .height(64.dp),
                url = faculty.icon,
                placeholder = Res.drawable.ic_education_gray,
                contentScale = ContentScale.FillHeight,
            )
            TitleTextSmall(
                modifier = Modifier.padding(vertical = 8.dp),
                text = faculty.shortTitle,
            )
        }
    }
}
