package com.workfort.pstuian.common.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun AnimatedListLoaderView(modifier: Modifier = Modifier)

@Composable
expect fun AnimatedEmptyView(modifier: Modifier = Modifier)

@Composable
expect fun AnimatedImagePlaceholderView(modifier: Modifier = Modifier)

@Composable
expect fun AnimatedErrorView(modifier: Modifier = Modifier)
