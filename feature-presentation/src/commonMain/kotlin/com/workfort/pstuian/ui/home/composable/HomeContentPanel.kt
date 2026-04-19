package com.workfort.pstuian.ui.home.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.workfort.pstuian.ui.common.composable.AnimatedErrorView
import com.workfort.pstuian.ui.common.composable.ErrorText
import com.workfort.pstuian.ui.common.composable.FacultyView
import com.workfort.pstuian.ui.common.composable.ShimmerBox
import com.workfort.pstuian.ui.common.composable.SliderView
import com.workfort.pstuian.ui.common.composable.TitleTextMedium
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .layout { measurable, constraints ->
                    // removing the horizontal padding set by parent
                    val horizontalPadding = 16.dp.roundToPx()
                    // measure the composable with the padding*2 (left+right)
                    val placeable = measurable.measure(
                        constraints.offset(horizontal = horizontalPadding * 2),
                    )
                    // reset the width by removing the padding*2
                    layout(placeable.width - horizontalPadding * 2, placeable.height) {
                        // place the composable
                        placeable.place(-horizontalPadding, 0)
                    }
                }
                .padding(top = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            SliderViewWrapper(uiState.sliderState, onUiEvent)
        }

        Spacer(Modifier.height(16.dp))

        // faculties
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(Res.string.label_faculties),
            style = TextStyle.title3.copy(color = AppColors.textPrimary),
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            FacultyViewWrapper(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(vertical = 8.dp),
                state = uiState.facultyState,
                onUiEvent = onUiEvent,
            )
        }

        Spacer(Modifier.height(16.dp))

        // information corner
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(Res.string.label_information_corner),
            style = TextStyle.title3.copy(color = AppColors.textPrimary),
        )

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            informationItems.forEach { item ->
                InformationCornerView(
                    item = item,
                    modifier = Modifier
                        .fillMaxWidth(0.48f)
                        .height(84.dp)
                        .padding(vertical = 8.dp)
                        .clickable {
                            onUiEvent(HomeUiEvent.ActionItemClicked(item))
                        },
                )
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

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            optionsItems.forEach { item ->
                OptionView(
                    item = item,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(84.dp)
                        .padding(vertical = 8.dp)
                        .clickable {
                            onUiEvent(HomeUiEvent.ActionItemClicked(item))
                        },
                )
            }
        }
    }
}

@Composable
private fun SliderViewWrapper(
    state: HomeUiState.SliderState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    when (state) {
        is HomeUiState.SliderState.None -> Unit
        is HomeUiState.SliderState.Loading -> {
            ShimmerBox()
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

@Composable
private fun FacultyViewWrapper(
    modifier: Modifier,
    state: HomeUiState.FacultyState,
    onUiEvent: (HomeUiEvent) -> Unit,
) {
    when (state) {
        is HomeUiState.FacultyState.None -> Unit
        is HomeUiState.FacultyState.Loading -> {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Loading, please wait...")
            }
        }
        is HomeUiState.FacultyState.Available -> {
            state.faculties.forEach { item ->
                FacultyView(
                    modifier = modifier
                        .clickable {
                            onUiEvent(HomeUiEvent.FacultyClicked(item))
                        },
                    faculty = item,
                )
            }
        }
        is HomeUiState.FacultyState.Error -> {
            Row(modifier = Modifier.fillMaxWidth()) {
                ErrorText(text = state.message)
            }
        }
    }
}

@Composable
private fun InformationCornerView(item: ActionItem, modifier: Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(item.title),
                modifier = Modifier
                    .weight(0.6f)
                    .padding(start = 8.dp),
                style = TextStyle.body2.copy(color = AppColors.textSecondary),
            )
            val icon = item.icon
            if (icon is DrawableResource) {
                Image(
                    painterResource(icon),
                    contentDescription = "",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f),
                )
            } else if (icon is ImageVector) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.4f)
                        .padding(8.dp),
                )
            }
        }
    }
}

@Composable
private fun OptionView(item: ActionItem, modifier: Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                text = stringResource(item.title),
                style = TextStyle.body2.copy(color = AppColors.textSecondary),
            )
            val icon = item.icon
            if (icon is DrawableResource) {
                Image(
                    painterResource(icon),
                    contentDescription = "",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .weight(0.3f),
                )
            } else if (icon is ImageVector) {
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .weight(0.3f),
                )
            }
        }
    }
}
