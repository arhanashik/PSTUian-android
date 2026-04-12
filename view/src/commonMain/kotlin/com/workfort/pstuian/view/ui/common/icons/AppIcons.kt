package com.workfort.pstuian.view.ui.common.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object AppIcons {
    val BloodDrop: ImageVector
        get() = ImageVector.Builder(
            name = "BloodDrop",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(12f, 18f)
            curveTo(12f, 18.7f, 12.12f, 19.36f, 12.34f, 20f)
            curveTo(12.23f, 20f, 12.12f, 20f, 12f, 20f)
            curveTo(8.69f, 20f, 6f, 17.31f, 6f, 14f)
            curveTo(6f, 10f, 12f, 3.25f, 12f, 3.25f)
            reflectiveCurveTo(16.31f, 8.1f, 17.62f, 12f)
            curveTo(14.5f, 12.22f, 12f, 14.82f, 12f, 18f)
            moveTo(19f, 17f)
            verticalLineTo(14f)
            horizontalLineTo(17f)
            verticalLineTo(17f)
            horizontalLineTo(14f)
            verticalLineTo(19f)
            horizontalLineTo(17f)
            verticalLineTo(22f)
            horizontalLineTo(19f)
            verticalLineTo(19f)
            horizontalLineTo(22f)
            verticalLineTo(17f)
            horizontalLineTo(19f)
            close()
        }.build()

    val Newspaper: ImageVector
        get() = ImageVector.Builder(
            name = "Newspaper",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(20f, 3f)
            horizontalLineTo(4f)
            curveTo(2.89f, 3f, 2f, 3.89f, 2f, 5f)
            verticalLineTo(19f)
            curveTo(2f, 20.11f, 2.89f, 21f, 4f, 21f)
            horizontalLineTo(20f)
            curveTo(21.11f, 21f, 22f, 20.11f, 22f, 19f)
            verticalLineTo(5f)
            curveTo(22f, 3.89f, 21.11f, 3f, 20f, 3f)
            moveTo(5f, 7f)
            horizontalLineTo(10f)
            verticalLineTo(13f)
            horizontalLineTo(5f)
            verticalLineTo(7f)
            moveTo(19f, 17f)
            horizontalLineTo(5f)
            verticalLineTo(15f)
            horizontalLineTo(19f)
            verticalLineTo(17f)
            moveTo(19f, 13f)
            horizontalLineTo(12f)
            verticalLineTo(11f)
            horizontalLineTo(19f)
            verticalLineTo(13f)
            moveTo(19f, 9f)
            horizontalLineTo(12f)
            verticalLineTo(7f)
            horizontalLineTo(19f)
            verticalLineTo(9f)
            close()
        }.build()

    val StarFace: ImageVector
        get() = ImageVector.Builder(
            name = "StarFace",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(12f, 2.5f)
            lineTo(8.42f, 8.06f)
            lineTo(2f, 9.74f)
            lineTo(6.2f, 14.88f)
            lineTo(5.82f, 21.5f)
            lineTo(12f, 19.09f)
            lineTo(18.18f, 21.5f)
            lineTo(17.8f, 14.88f)
            lineTo(22f, 9.74f)
            lineTo(15.58f, 8.06f)
            lineTo(12f, 2.5f)
            moveTo(9.38f, 10.5f)
            curveTo(10f, 10.5f, 10.5f, 11f, 10.5f, 11.63f)
            arcTo(1.12f, 1.12f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9.38f, 12.75f)
            curveTo(8.75f, 12.75f, 8.25f, 12.25f, 8.25f, 11.63f)
            curveTo(8.25f, 11f, 8.75f, 10.5f, 9.38f, 10.5f)
            moveTo(14.63f, 10.5f)
            curveTo(15.25f, 10.5f, 15.75f, 11f, 15.75f, 11.63f)
            arcTo(1.12f, 1.12f, 0f, isMoreThanHalf = false, isPositiveArc = true, 14.63f, 12.75f)
            curveTo(14f, 12.75f, 13.5f, 12.25f, 13.5f, 11.63f)
            curveTo(13.5f, 11f, 14f, 10.5f, 14.63f, 10.5f)
            moveTo(9f, 15f)
            horizontalLineTo(15f)
            curveTo(14.5f, 16.21f, 13.31f, 17f, 12f, 17f)
            curveTo(10.69f, 17f, 9.5f, 16.21f, 9f, 15f)
            close()
        }.build()

    val BellBadgeFilled: ImageVector
        get() = ImageVector.Builder(
            name = "BellBadgeFilled",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(21f, 6.5f)
            curveTo(21f, 8.43f, 19.43f, 10f, 17.5f, 10f)
            reflectiveCurveTo(14f, 8.43f, 14f, 6.5f)
            reflectiveCurveTo(15.57f, 3f, 17.5f, 3f)
            reflectiveCurveTo(21f, 4.57f, 21f, 6.5f)
            moveTo(19f, 11.79f)
            curveTo(18.5f, 11.92f, 18f, 12f, 17.5f, 12f)
            curveTo(14.47f, 12f, 12f, 9.53f, 12f, 6.5f)
            curveTo(12f, 5.03f, 12.58f, 3.7f, 13.5f, 2.71f)
            curveTo(13.15f, 2.28f, 12.61f, 2f, 12f, 2f)
            curveTo(10.9f, 2f, 10f, 2.9f, 10f, 4f)
            verticalLineTo(4.29f)
            curveTo(7.03f, 5.17f, 5f, 7.9f, 5f, 11f)
            verticalLineTo(17f)
            lineTo(3f, 19f)
            verticalLineTo(20f)
            horizontalLineTo(21f)
            verticalLineTo(19f)
            lineTo(19f, 17f)
            verticalLineTo(11.79f)
            moveTo(12f, 23f)
            curveTo(13.11f, 23f, 14f, 22.11f, 14f, 21f)
            horizontalLineTo(10f)
            curveTo(10f, 22.11f, 10.9f, 23f, 12f, 23f)
            close()
        }.build()

    val CheckIn: ImageVector
        get() = ImageVector.Builder(
            name = "CheckIn",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(9f, 11.5f)
            arcTo(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 11.5f, 9f)
            arcTo(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9f, 6.5f)
            arcTo(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.5f, 9f)
            arcTo(2.5f, 2.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9f, 11.5f)
            moveTo(9f, 2f)
            curveTo(12.86f, 2f, 16f, 5.13f, 16f, 9f)
            curveTo(16f, 14.25f, 9f, 22f, 9f, 22f)
            curveTo(9f, 22f, 2f, 14.25f, 2f, 9f)
            arcTo(7f, 7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9f, 2f)
            moveTo(15f, 17f)
            horizontalLineTo(18f)
            verticalLineTo(14f)
            horizontalLineTo(20f)
            verticalLineTo(17f)
            horizontalLineTo(23f)
            verticalLineTo(19f)
            horizontalLineTo(20f)
            verticalLineTo(22f)
            horizontalLineTo(18f)
            verticalLineTo(19f)
            horizontalLineTo(15f)
            verticalLineTo(17f)
            close()
        }.build()

    val SeatOutline: ImageVector
        get() = ImageVector.Builder(
            name = "SeatOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(15f, 5f)
            verticalLineTo(12f)
            horizontalLineTo(9f)
            verticalLineTo(5f)
            horizontalLineTo(15f)
            moveTo(15f, 3f)
            horizontalLineTo(9f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 7f, 5f)
            verticalLineTo(14f)
            horizontalLineTo(17f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15f, 3f)
            moveTo(22f, 10f)
            horizontalLineTo(19f)
            verticalLineTo(13f)
            horizontalLineTo(22f)
            verticalLineTo(10f)
            moveTo(5f, 10f)
            horizontalLineTo(2f)
            verticalLineTo(13f)
            horizontalLineTo(5f)
            verticalLineTo(10f)
            moveTo(20f, 15f)
            horizontalLineTo(4f)
            verticalLineTo(21f)
            horizontalLineTo(6f)
            verticalLineTo(17f)
            horizontalLineTo(18f)
            verticalLineTo(21f)
            horizontalLineTo(20f)
            verticalLineTo(15f)
            close()
        }.build()

    val AccountBoxOutline: ImageVector
        get() = ImageVector.Builder(
            name = "AccountBoxOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(19f, 19f)
            horizontalLineTo(5f)
            verticalLineTo(5f)
            horizontalLineTo(19f)
            moveTo(19f, 3f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 3f, 5f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 5f, 21f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 21f, 19f)
            verticalLineTo(5f)
            curveTo(21f, 3.89f, 20.1f, 3f, 19f, 3f)
            moveTo(16.5f, 16.25f)
            curveTo(16.5f, 14.75f, 13.5f, 14f, 12f, 14f)
            curveTo(10.5f, 14f, 7.5f, 14.75f, 7.5f, 16.25f)
            verticalLineTo(17f)
            horizontalLineTo(16.5f)
            moveTo(12f, 12.25f)
            arcTo(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 14.25f, 10f)
            arcTo(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 7.75f)
            arcTo(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9.75f, 10f)
            arcTo(2.25f, 2.25f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 12.25f)
            close()
        }.build()

    val SignIn: ImageVector
        get() = ImageVector.Builder(
            name = "SignIn",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(19f, 3f)
            horizontalLineTo(5f)
            curveTo(3.89f, 3f, 3f, 3.89f, 3f, 5f)
            verticalLineTo(9f)
            horizontalLineTo(5f)
            verticalLineTo(5f)
            horizontalLineTo(19f)
            verticalLineTo(19f)
            horizontalLineTo(5f)
            verticalLineTo(15f)
            horizontalLineTo(3f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 5f, 21f)
            horizontalLineTo(19f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 21f, 19f)
            verticalLineTo(5f)
            curveTo(21f, 3.89f, 20.1f, 3f, 19f, 3f)
            moveTo(10.08f, 15.58f)
            lineTo(11.5f, 17f)
            lineTo(16.5f, 12f)
            lineTo(11.5f, 7f)
            lineTo(10.08f, 8.41f)
            lineTo(12.67f, 11f)
            horizontalLineTo(3f)
            verticalLineTo(13f)
            horizontalLineTo(12.67f)
            lineTo(10.08f, 15.58f)
            close()
        }.build()

    val SignOut: ImageVector
        get() = ImageVector.Builder(
            name = "SignOut",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(14.08f, 15.59f)
            lineTo(16.67f, 13f)
            horizontalLineTo(7f)
            verticalLineTo(11f)
            horizontalLineTo(16.67f)
            lineTo(14.08f, 8.41f)
            lineTo(15.5f, 7f)
            lineTo(20.5f, 12f)
            lineTo(15.5f, 17f)
            lineTo(14.08f, 15.59f)
            moveTo(19f, 3f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 21f, 5f)
            verticalLineTo(9.67f)
            lineTo(19f, 7.67f)
            verticalLineTo(5f)
            horizontalLineTo(5f)
            verticalLineTo(19f)
            horizontalLineTo(19f)
            verticalLineTo(16.33f)
            lineTo(21f, 14.33f)
            verticalLineTo(19f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 19f, 21f)
            horizontalLineTo(5f)
            curveTo(3.89f, 21f, 3f, 20.1f, 3f, 19f)
            verticalLineTo(5f)
            curveTo(3f, 3.89f, 3.89f, 3f, 5f, 3f)
            horizontalLineTo(19f)
            close()
        }.build()

    val FileEditOutline: ImageVector
        get() = ImageVector.Builder(
            name = "FileEditOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(8f, 12f)
            horizontalLineTo(16f)
            verticalLineTo(14f)
            horizontalLineTo(8f)
            verticalLineTo(12f)
            moveTo(10f, 20f)
            horizontalLineTo(6f)
            verticalLineTo(4f)
            horizontalLineTo(13f)
            verticalLineTo(9f)
            horizontalLineTo(18f)
            verticalLineTo(12.1f)
            lineTo(20f, 10.1f)
            verticalLineTo(8f)
            lineTo(14f, 2f)
            horizontalLineTo(6f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4f, 4f)
            verticalLineTo(20f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6f, 22f)
            horizontalLineTo(10f)
            verticalLineTo(20f)
            moveTo(8f, 18f)
            horizontalLineTo(12.1f)
            lineTo(13f, 17.1f)
            verticalLineTo(16f)
            horizontalLineTo(8f)
            verticalLineTo(18f)
            moveTo(20.2f, 13f)
            curveTo(20.3f, 13f, 20.5f, 13.1f, 20.6f, 13.2f)
            lineTo(21.9f, 14.5f)
            curveTo(22.1f, 14.7f, 22.1f, 15.1f, 21.9f, 15.3f)
            lineTo(20.9f, 16.3f)
            lineTo(18.8f, 14.2f)
            lineTo(19.8f, 13.2f)
            curveTo(19.9f, 13.1f, 20f, 13f, 20.2f, 13f)
            moveTo(20.2f, 16.9f)
            lineTo(14.1f, 23f)
            horizontalLineTo(12f)
            verticalLineTo(20.9f)
            lineTo(18.1f, 14.8f)
            lineTo(20.2f, 16.9f)
            close()
        }.build()

    val PencilBoxOutline: ImageVector
        get() = ImageVector.Builder(
            name = "PencilBoxOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(19f, 19f)
            verticalLineTo(5f)
            horizontalLineTo(5f)
            verticalLineTo(19f)
            horizontalLineTo(19f)
            moveTo(19f, 3f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 21f, 5f)
            verticalLineTo(19f)
            curveTo(21f, 20.11f, 20.1f, 21f, 19f, 21f)
            horizontalLineTo(5f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 3f, 19f)
            verticalLineTo(5f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 3f)
            horizontalLineTo(19f)
            moveTo(16.7f, 9.35f)
            lineTo(15.7f, 10.35f)
            lineTo(13.65f, 8.3f)
            lineTo(14.65f, 7.3f)
            curveTo(14.86f, 7.08f, 15.21f, 7.08f, 15.42f, 7.3f)
            lineTo(16.7f, 8.58f)
            curveTo(16.92f, 8.79f, 16.92f, 9.14f, 16.7f, 9.35f)
            moveTo(7f, 14.94f)
            lineTo(13.06f, 8.88f)
            lineTo(15.12f, 10.94f)
            lineTo(9.06f, 17f)
            horizontalLineTo(7f)
            verticalLineTo(14.94f)
            close()
        }.build()

    val PencilCircularOutline: ImageVector
        get() = ImageVector.Builder(
            name = "PencilCircularOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(7f, 14.94f)
            lineTo(13.06f, 8.88f)
            lineTo(15.12f, 10.94f)
            lineTo(9.06f, 17f)
            horizontalLineTo(7f)
            verticalLineTo(14.94f)
            moveTo(12f, 20f)
            arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 20f, 12f)
            arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 4f)
            arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 4f, 12f)
            arcTo(8f, 8f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 20f)
            moveTo(16.7f, 9.35f)
            lineTo(15.7f, 10.35f)
            lineTo(13.65f, 8.3f)
            lineTo(14.65f, 7.3f)
            curveTo(14.86f, 7.08f, 15.21f, 7.08f, 15.42f, 7.3f)
            lineTo(16.7f, 8.58f)
            curveTo(16.92f, 8.79f, 16.92f, 9.14f, 16.7f, 9.35f)
            moveTo(12f, 2f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22f, 12f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 22f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 12f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 2f)
            close()
        }.build()

    val AlertCircleFill: ImageVector
        get() = ImageVector.Builder(
            name = "AlertCircleFill",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(13f, 13f)
            horizontalLineTo(11f)
            verticalLineTo(7f)
            horizontalLineTo(13f)
            moveTo(13f, 17f)
            horizontalLineTo(11f)
            verticalLineTo(15f)
            horizontalLineTo(13f)
            moveTo(12f, 2f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 12f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 22f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 22f, 12f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 2f)
            close()
        }.build()

    val HelpFill: ImageVector
        get() = ImageVector.Builder(
            name = "HelpFill",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(15.07f, 11.25f)
            lineTo(14.17f, 12.17f)
            curveTo(13.45f, 12.89f, 13f, 13.5f, 13f, 15f)
            horizontalLineTo(11f)
            verticalLineTo(14.5f)
            curveTo(11f, 13.39f, 11.45f, 12.39f, 12.17f, 11.67f)
            lineTo(13.41f, 10.41f)
            curveTo(13.78f, 10.05f, 14f, 9.55f, 14f, 9f)
            curveTo(14f, 7.89f, 13.1f, 7f, 12f, 7f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = false, 10f, 9f)
            horizontalLineTo(8f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 5f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 16f, 9f)
            curveTo(16f, 9.88f, 15.64f, 10.67f, 15.07f, 11.25f)
            moveTo(13f, 19f)
            horizontalLineTo(11f)
            verticalLineTo(17f)
            horizontalLineTo(13f)
            moveTo(12f, 2f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 12f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 22f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 22f, 12f)
            curveTo(22f, 6.47f, 17.5f, 2f, 12f, 2f)
            close()
        }.build()

    val HelpOutline: ImageVector
        get() = ImageVector.Builder(
            name = "HelpOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(11f, 18f)
            horizontalLineTo(13f)
            verticalLineTo(16f)
            horizontalLineTo(11f)
            verticalLineTo(18f)
            moveTo(12f, 2f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 2f, 12f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 22f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 22f, 12f)
            arcTo(10f, 10f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 2f)
            moveTo(12f, 20f)
            curveTo(7.59f, 20f, 4f, 16.41f, 4f, 12f)
            curveTo(4f, 7.59f, 7.59f, 4f, 12f, 4f)
            curveTo(16.41f, 4f, 20f, 7.59f, 20f, 12f)
            curveTo(20f, 16.41f, 16.41f, 20f, 12f, 20f)
            moveTo(12f, 6f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 8f, 10f)
            horizontalLineTo(10f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12f, 8f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 14f, 10f)
            curveTo(14f, 12f, 11f, 11.75f, 11f, 15f)
            horizontalLineTo(13f)
            curveTo(13f, 12.75f, 16f, 12.5f, 16f, 10f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 12f, 6f)
            close()
        }.build()

    val AccountHeartOutline: ImageVector
        get() = ImageVector.Builder(
            name = "AccountHeartOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(5f, 15f)
            lineTo(4.4f, 14.5f)
            curveTo(2.4f, 12.6f, 1f, 11.4f, 1f, 9.9f)
            curveTo(1f, 8.7f, 2f, 7.7f, 3.2f, 7.7f)
            curveTo(3.9f, 7.7f, 4.6f, 8f, 5f, 8.5f)
            curveTo(5.4f, 8f, 6.1f, 7.7f, 6.8f, 7.7f)
            curveTo(8f, 7.7f, 9f, 8.6f, 9f, 9.9f)
            curveTo(9f, 11.4f, 7.6f, 12.6f, 5.6f, 14.5f)
            lineTo(5f, 15f)
            moveTo(15f, 4f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 11f, 8f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15f, 12f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 19f, 8f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = false, 15f, 4f)
            moveTo(15f, 10.1f)
            arcTo(2.1f, 2.1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 12.9f, 8f)
            arcTo(2.1f, 2.1f, 0f, isMoreThanHalf = false, isPositiveArc = true, 15f, 5.9f)
            curveTo(16.16f, 5.9f, 17.1f, 6.84f, 17.1f, 8f)
            curveTo(17.1f, 9.16f, 16.16f, 10.1f, 15f, 10.1f)
            moveTo(15f, 13f)
            curveTo(12.33f, 13f, 7f, 14.33f, 7f, 17f)
            verticalLineTo(20f)
            horizontalLineTo(23f)
            verticalLineTo(17f)
            curveTo(23f, 14.33f, 17.67f, 13f, 15f, 13f)
            moveTo(21.1f, 18.1f)
            horizontalLineTo(8.9f)
            verticalLineTo(17f)
            curveTo(8.9f, 16.36f, 12f, 14.9f, 15f, 14.9f)
            curveTo(17.97f, 14.9f, 21.1f, 16.36f, 21.1f, 17f)
            verticalLineTo(18.1f)
            close()
        }.build()

    val DatabaseRemoveOutline: ImageVector
        get() = ImageVector.Builder(
            name = "DatabaseRemoveOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(20f, 13.09f)
            verticalLineTo(7f)
            curveTo(20f, 4.79f, 16.42f, 3f, 12f, 3f)
            reflectiveCurveTo(4f, 4.79f, 4f, 7f)
            verticalLineTo(17f)
            curveTo(4f, 19.21f, 7.59f, 21f, 12f, 21f)
            curveTo(12.46f, 21f, 12.9f, 21f, 13.33f, 20.94f)
            curveTo(13.12f, 20.33f, 13f, 19.68f, 13f, 19f)
            verticalLineTo(18.95f)
            curveTo(12.68f, 19f, 12.35f, 19f, 12f, 19f)
            curveTo(8.13f, 19f, 6f, 17.5f, 6f, 17f)
            verticalLineTo(14.77f)
            curveTo(7.61f, 15.55f, 9.72f, 16f, 12f, 16f)
            curveTo(12.65f, 16f, 13.27f, 15.96f, 13.88f, 15.89f)
            curveTo(14.93f, 14.16f, 16.83f, 13f, 19f, 13f)
            curveTo(19.34f, 13f, 19.67f, 13.04f, 20f, 13.09f)
            moveTo(18f, 12.45f)
            curveTo(16.7f, 13.4f, 14.42f, 14f, 12f, 14f)
            reflectiveCurveTo(7.3f, 13.4f, 6f, 12.45f)
            verticalLineTo(9.64f)
            curveTo(7.47f, 10.47f, 9.61f, 11f, 12f, 11f)
            reflectiveCurveTo(16.53f, 10.47f, 18f, 9.64f)
            verticalLineTo(12.45f)
            moveTo(12f, 9f)
            curveTo(8.13f, 9f, 6f, 7.5f, 6f, 7f)
            reflectiveCurveTo(8.13f, 5f, 12f, 5f)
            reflectiveCurveTo(18f, 6.5f, 18f, 7f)
            reflectiveCurveTo(15.87f, 9f, 12f, 9f)
            moveTo(20.41f, 19f)
            lineTo(22.54f, 21.12f)
            lineTo(21.12f, 22.54f)
            lineTo(19f, 20.41f)
            lineTo(16.88f, 22.54f)
            lineTo(15.47f, 21.12f)
            lineTo(17.59f, 19f)
            lineTo(15.47f, 16.88f)
            lineTo(16.88f, 15.47f)
            lineTo(19f, 17.59f)
            lineTo(21.12f, 15.47f)
            lineTo(22.54f, 16.88f)
            lineTo(20.41f, 19f)
            close()
        }.build()

    val AccountQuestionOutline: ImageVector
        get() = ImageVector.Builder(
            name = "AccountQuestionOutline",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(20.5f, 14.5f)
            verticalLineTo(16f)
            horizontalLineTo(19f)
            verticalLineTo(14.5f)
            horizontalLineTo(20.5f)
            moveTo(18.5f, 9.5f)
            horizontalLineTo(17f)
            verticalLineTo(9f)
            arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20f, 6f)
            arcTo(3f, 3f, 0f, isMoreThanHalf = false, isPositiveArc = true, 23f, 9f)
            curveTo(23f, 9.97f, 22.5f, 10.88f, 21.71f, 11.41f)
            lineTo(21.41f, 11.6f)
            curveTo(20.84f, 12f, 20.5f, 12.61f, 20.5f, 13.3f)
            verticalLineTo(13.5f)
            horizontalLineTo(19f)
            verticalLineTo(13.3f)
            curveTo(19f, 12.11f, 19.6f, 11f, 20.59f, 10.35f)
            lineTo(20.88f, 10.16f)
            curveTo(21.27f, 9.9f, 21.5f, 9.47f, 21.5f, 9f)
            arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 20f, 7.5f)
            arcTo(1.5f, 1.5f, 0f, isMoreThanHalf = false, isPositiveArc = false, 18.5f, 9f)
            verticalLineTo(9.5f)
            moveTo(9f, 13f)
            curveTo(11.67f, 13f, 17f, 14.34f, 17f, 17f)
            verticalLineTo(20f)
            horizontalLineTo(1f)
            verticalLineTo(17f)
            curveTo(1f, 14.34f, 6.33f, 13f, 9f, 13f)
            moveTo(9f, 4f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 13f, 8f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9f, 12f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 8f)
            arcTo(4f, 4f, 0f, isMoreThanHalf = false, isPositiveArc = true, 9f, 4f)
            moveTo(9f, 14.9f)
            curveTo(6.03f, 14.9f, 2.9f, 16.36f, 2.9f, 17f)
            verticalLineTo(18.1f)
            horizontalLineTo(15.1f)
            verticalLineTo(17f)
            curveTo(15.1f, 16.36f, 11.97f, 14.9f, 9f, 14.9f)
            moveTo(9f, 5.9f)
            arcTo(2.1f, 2.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 6.9f, 8f)
            arcTo(2.1f, 2.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9f, 10.1f)
            arcTo(2.1f, 2.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 11.1f, 8f)
            arcTo(2.1f, 2.1f, 0f, isMoreThanHalf = false, isPositiveArc = false, 9f, 5.9f)
            close()
        }.build()

    val ArrowBack: ImageVector get() = Icons.AutoMirrored.Filled.ArrowBack
    val Edit: ImageVector get() = Icons.Default.Edit
    val Visibility: ImageVector get() = Icons.Default.Visibility
    val VisibilityOff: ImageVector get() = Icons.Default.VisibilityOff
    val Web: ImageVector get() = Icons.Default.Language
    val Settings: ImageVector get() = Icons.Default.Settings
    val Camera: ImageVector get() = Icons.Default.PhotoCamera
    val Call: ImageVector get() = Icons.Default.Call
    val Close: ImageVector get() = Icons.Default.Close
    val Email: ImageVector get() = Icons.Default.Email
    val Reload: ImageVector get() = Icons.Default.Refresh
    val DeleteOutline: ImageVector get() = Icons.Default.DeleteOutline
    val KeyboardArrowDown: ImageVector get() = Icons.Default.KeyboardArrowDown
    val BellFilled: ImageVector get() = Icons.Default.Notifications
    val CheckCircleFill: ImageVector get() = Icons.Default.CheckCircle

    val Export: ImageVector
        get() = ImageVector.Builder(
            name = "Export",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(12f, 1f)
            lineTo(8f, 5f)
            horizontalLineTo(11f)
            verticalLineTo(14f)
            horizontalLineTo(13f)
            verticalLineTo(5f)
            horizontalLineTo(16f)
            moveTo(18f, 23f)
            horizontalLineTo(6f)
            curveTo(4.89f, 23f, 4f, 22.1f, 4f, 21f)
            verticalLineTo(9f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 6f, 7f)
            horizontalLineTo(9f)
            verticalLineTo(9f)
            horizontalLineTo(6f)
            verticalLineTo(21f)
            horizontalLineTo(18f)
            verticalLineTo(9f)
            horizontalLineTo(15f)
            verticalLineTo(7f)
            horizontalLineTo(18f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20f, 9f)
            verticalLineTo(21f)
            arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 18f, 23f)
            close()
        }.build()

    val ArrowUp: ImageVector
        get() = ImageVector.Builder(
            name = "ArrowUp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(4.08f, 11.92f)
            lineTo(12f, 4f)
            lineTo(19.92f, 11.92f)
            lineTo(18.5f, 13.33f)
            lineTo(13f, 7.83f)
            verticalLineTo(22f)
            horizontalLineTo(11f)
            verticalLineTo(7.83f)
            lineTo(5.5f, 13.33f)
            lineTo(4.08f, 11.92f)
            moveTo(12f, 4f)
            horizontalLineTo(22f)
            verticalLineTo(2f)
            horizontalLineTo(2f)
            verticalLineTo(4f)
            horizontalLineTo(12f)
            close()
        }.build()

    val ArrowRightLarge: ImageVector
        get() = ImageVector.Builder(
            name = "ArrowRightLarge",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(22f, 12f)
            lineTo(18f, 8f)
            verticalLineTo(11f)
            horizontalLineTo(3f)
            verticalLineTo(13f)
            horizontalLineTo(18f)
            verticalLineTo(16f)
            lineTo(22f, 12f)
            close()
        }.build()

    val ArrowDown: ImageVector
        get() = ImageVector.Builder(
            name = "ArrowDown",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(19.92f, 12.08f)
            lineTo(12f, 20f)
            lineTo(4.08f, 12.08f)
            lineTo(5.5f, 10.67f)
            lineTo(11f, 16.17f)
            verticalLineTo(2f)
            horizontalLineTo(13f)
            verticalLineTo(16.17f)
            lineTo(18.5f, 10.66f)
            lineTo(19.92f, 12.08f)
            moveTo(12f, 20f)
            horizontalLineTo(2f)
            verticalLineTo(22f)
            horizontalLineTo(22f)
            verticalLineTo(20f)
            horizontalLineTo(12f)
            close()
        }.build()

    val HandHeart: ImageVector
        get() = ImageVector.Builder(
            name = "HandHeart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).path(
            fill = SolidColor(Color.Black),
        ) {
            moveTo(20f, 17f)
            quadTo(20.86f, 17f, 21.45f, 17.6f)
            reflectiveQuadTo(22.03f, 19f)
            lineTo(14f, 22f)
            lineTo(7f, 20f)
            verticalLineTo(11f)
            horizontalLineTo(8.95f)
            lineTo(16.22f, 13.69f)
            quadTo(17f, 14f, 17f, 14.81f)
            quadTo(17f, 15.28f, 16.66f, 15.63f)
            reflectiveQuadTo(15.8f, 16f)
            horizontalLineTo(13f)
            lineTo(11.25f, 15.33f)
            lineTo(10.92f, 16.27f)
            lineTo(13f, 17f)
            horizontalLineTo(20f)
            moveTo(16f, 3.23f)
            quadTo(17.06f, 2f, 18.7f, 2f)
            quadTo(20.06f, 2f, 21f, 3f)
            reflectiveQuadTo(22f, 5.3f)
            quadTo(22f, 6.33f, 21f, 7.76f)
            reflectiveQuadTo(19.03f, 10.15f)
            lineTo(16f, 13f)
            quadTo(13.92f, 11.11f, 12.94f, 10.15f)
            reflectiveQuadTo(10.97f, 7.76f)
            lineTo(10f, 5.3f)
            quadTo(10f, 3.94f, 10.97f, 3f)
            reflectiveQuadTo(13.31f, 2f)
            quadTo(14.91f, 2f, 16f, 3.23f)
            moveTo(0.984f, 11f)
            horizontalLineTo(5f)
            verticalLineTo(22f)
            horizontalLineTo(0.984f)
            verticalLineTo(11f)
            close()
        }.build()
}
