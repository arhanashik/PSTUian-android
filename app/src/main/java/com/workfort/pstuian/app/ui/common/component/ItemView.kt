package com.workfort.pstuian.app.ui.common.component

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
import com.workfort.pstuian.R
import com.workfort.pstuian.model.FacultyEntity


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
                    .height(64.dp)
                    .padding(top = 8.dp),
                url = faculty.icon,
                placeholder = R.drawable.ic_education_gray,
                contentScale = ContentScale.FillHeight,
            )
            TitleTextSmall(
                modifier = Modifier.padding(vertical = 8.dp),
                text = faculty.shortTitle,
            )
        }
    }
}