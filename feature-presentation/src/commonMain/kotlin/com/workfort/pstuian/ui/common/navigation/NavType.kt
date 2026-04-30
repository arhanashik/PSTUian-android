package com.workfort.pstuian.ui.common.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.workfort.pstuian.featuredomain.model.FacultySelectionMode
import com.workfort.pstuian.featuredomain.model.UserType
import com.workfort.pstuian.util.deeplink.ResetPasswordParams
import kotlinx.serialization.json.Json
import kotlin.reflect.KType
import kotlin.reflect.typeOf

val UserTypeNavType = object : NavType<UserType>(isNullableAllowed = false) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: UserType,
    ) {
        bundle.write { putString(key, Json.encodeToString(value)) }
    }

    override fun get(
        bundle: SavedState,
        key: String,
    ): UserType? {
        return bundle.read { Json.decodeFromString<UserType?>(getString(key)) }
    }

    override fun parseValue(value: String): UserType = Json.decodeFromString(value)
    override fun serializeAsValue(value: UserType): String = Json.encodeToString(value)
    override val name: String = "UserType"
}

val FacultySelectionModeNavType = object : NavType<FacultySelectionMode>(isNullableAllowed = false) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: FacultySelectionMode,
    ) {
        bundle.write { putString(key, Json.encodeToString(value)) }
    }

    override fun get(
        bundle: SavedState,
        key: String,
    ): FacultySelectionMode? {
        return bundle.read { Json.decodeFromString<FacultySelectionMode?>(getString(key)) }
    }

    override fun parseValue(value: String): FacultySelectionMode = Json.decodeFromString(value)
    override fun serializeAsValue(value: FacultySelectionMode): String = Json.encodeToString(value)
    override val name: String = "FacultySelectionMode"
}

val ResetPasswordParamsNavType = object : NavType<ResetPasswordParams?>(isNullableAllowed = true) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: ResetPasswordParams?,
    ) {
        bundle.write {
            putString(key, value?.let { Json.encodeToString(it) } ?: "")
        }
    }

    override fun get(
        bundle: SavedState,
        key: String,
    ): ResetPasswordParams? {
        return bundle.read {
            getString(key).let { Json.decodeFromString<ResetPasswordParams?>(it) }
        }
    }

    override fun parseValue(value: String): ResetPasswordParams = Json.decodeFromString(value)
    override fun serializeAsValue(value: ResetPasswordParams?): String = Json.encodeToString(value)
    override val name: String = "ResetPasswordParams"
}

val navTypeMap: Map<KType, NavType<*>> = mapOf(
    typeOf<UserType>() to UserTypeNavType,
    typeOf<FacultySelectionMode>() to FacultySelectionModeNavType,
    typeOf<ResetPasswordParams?>() to ResetPasswordParamsNavType,
)
