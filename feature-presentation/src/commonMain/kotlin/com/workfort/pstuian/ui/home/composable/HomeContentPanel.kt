package com.workfort.pstuian.ui.home.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.ErrorText
import com.workfort.pstuian.ui.common.theme.AppColors
import com.workfort.pstuian.ui.common.theme.TextStyle
import com.workfort.pstuian.ui.home.ActionItem
import com.workfort.pstuian.ui.home.informationItems
import com.workfort.pstuian.ui.home.optionsItems
import com.workfort.pstuian.ui.home.state.HomeUiEvent
import com.workfort.pstuian.ui.home.state.HomeUiState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pstuian.feature_presentation.generated.resources.Res
import pstuian.feature_presentation.generated.resources.about_it
import pstuian.feature_presentation.generated.resources.label_faculties
import pstuian.feature_presentation.generated.resources.label_information_corner
import pstuian.feature_presentation.generated.resources.label_options

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeContentPanel(
    uiState: HomeUiState.Content,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        // slider
        SliderViewWrapper(
            state = uiState.sliderState,
            onUiEvent = onUiEvent,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(top = 16.dp),
        )

        Spacer(Modifier.height(16.dp))

        // faculties
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(Res.string.label_faculties),
            style = TextStyle.title3.copy(color = AppColors.textPrimary),
        )

        FacultyViewWrapper(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = uiState.facultyState,
            onUiEvent = onUiEvent,
        )

        Spacer(Modifier.height(16.dp))

        // information corner
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(Res.string.label_information_corner),
            style = TextStyle.title3.copy(color = AppColors.textPrimary),
        )

        val informationRowSpacing = 16.dp
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            val informationItemWidth = (maxWidth - informationRowSpacing) / 2
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                informationItems.forEachIndexed { index, item ->
                    InformationCornerView(
                        item = item,
                        isLeftItem = index % 2 == 0,
                        onClick = { onUiEvent(HomeUiEvent.ActionItemClicked(item)) },
                        modifier = Modifier
                            .width(informationItemWidth)
                            .height(84.dp)
                            .padding(vertical = 8.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // options
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(Res.string.label_options),
            style = TextStyle.title3.copy(color = AppColors.textPrimary),
        )

        val optionsRowSpacing = 16.dp
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            val optionsItemWidth = (maxWidth - optionsRowSpacing * 2) / 3
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.spacedBy(optionsRowSpacing),
            ) {
                optionsItems.forEach { item ->
                    OptionView(
                        item = item,
                        onClick = { onUiEvent(HomeUiEvent.ActionItemClicked(item)) },
                        modifier = Modifier
                            .width(optionsItemWidth)
                            .height(84.dp)
                            .padding(vertical = 8.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        AboutCard()

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun AboutCard() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Text(
            text = stringResource(Res.string.about_it),
            style = TextStyle.body1,
            color = AppColors.textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
        )
    }
}

@Composable
private fun SliderViewWrapper(
    state: HomeUiState.SliderState,
    onUiEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        when (state) {
            is HomeUiState.SliderState.None -> Unit
            is HomeUiState.SliderState.Loading -> {
                SliderShimmer()
            }
            is HomeUiState.SliderState.Available -> {
                SliderView(
                    sliders = state.sliders,
                    scrollPosition = state.scrollPosition,
                    onScrollSlider = {
                        onUiEvent(HomeUiEvent.ScrollSlider(it))
                    },
                    onClickSlider = {
                        onUiEvent(HomeUiEvent.SliderClicked(it))
                    },
                )
            }
            is HomeUiState.SliderState.Error -> {
                AnimatedErrorView(modifier = Modifier.width(150.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FacultyViewWrapper(
    modifier: Modifier,
    state: HomeUiState.FacultyState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    when (state) {
        is HomeUiState.FacultyState.None -> Unit
        is HomeUiState.FacultyState.Loading -> {
            Box(modifier = modifier.padding(top = 8.dp)) {
                FacultyShimmer()
            }
        }
        is HomeUiState.FacultyState.Available -> {
            val facultyRowSpacing = 16.dp
            BoxWithConstraints(modifier = modifier) {
                val itemWidth = (maxWidth - facultyRowSpacing * 2) / 3
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.spacedBy(facultyRowSpacing),
                ) {
                    state.faculties.forEach { item ->
                        FacultyView(
                            modifier = Modifier
                                .width(itemWidth)
                                .padding(vertical = 8.dp),
                            faculty = item,
                            onClick = { onUiEvent(HomeUiEvent.FacultyClicked(item)) },
                        )
                    }
                }
            }
        }
        is HomeUiState.FacultyState.Error -> {
            Row(modifier = modifier.padding(top = 8.dp)) {
                ErrorText(text = state.message)
            }
        }
    }
}

@Composable
private fun InformationCornerView(
    item: ActionItem,
    isLeftItem: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    val cardShape = RoundedCornerShape(16.dp)
    ElevatedCard(
        modifier = modifier
            .clip(cardShape)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = cardShape,
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isLeftItem) {
                Text(
                    text = stringResource(item.title),
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle.body2.copy(
                        color = AppColors.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    ),
                )
                InformationItemIcon(item = item)
            } else {
                InformationItemIcon(item = item)
                Text(
                    text = stringResource(item.title),
                    modifier = Modifier
                        .weight(0.6f)
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle.body2.copy(
                        color = AppColors.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    ),
                )
            }
        }
    }
}

@Composable
private fun RowScope.InformationItemIcon(item: ActionItem) {
    val iconContainerShape = RoundedCornerShape(16.dp)
    val iconContainerModifier = Modifier
        .fillMaxHeight()
        .weight(0.4f)
        .padding(4.dp)
        .clip(iconContainerShape)
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
            shape = iconContainerShape,
        )

    val icon = item.icon
    Box(
        modifier = iconContainerModifier,
        contentAlignment = Alignment.Center,
    ) {
        when (icon) {
            is DrawableResource -> {
                Image(
                    painter = painterResource(icon),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            is ImageVector -> {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    tint = AppColors.textPrimary,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            else -> Unit
        }
    }
}

@Composable
private fun OptionView(
    item: ActionItem,
    onClick: () -> Unit,
    modifier: Modifier,
) {
    val cardShape = RoundedCornerShape(16.dp)
    ElevatedCard(
        modifier = modifier
            .clip(cardShape)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = cardShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                text = stringResource(item.title),
                style = TextStyle.label1.copy(
                    color = AppColors.textPrimary,
                    fontWeight = FontWeight.Medium
                ),
            )
            val icon = item.icon
            if (icon is DrawableResource) {
                Image(
                    painterResource(icon),
                    contentDescription = "",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .weight(0.3f)
                        .size(24.dp),
                )
            } else if (icon is ImageVector) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .weight(0.3f)
                        .size(24.dp),
                )
            }
        }
    }
}
