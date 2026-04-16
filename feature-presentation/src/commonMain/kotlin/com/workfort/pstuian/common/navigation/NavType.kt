package com.workfort.pstuian.common.navigation

import androidx.navigation.NavType
import com.workfort.pstuian.featuredomain.model.FacultySelectionMode
import com.workfort.pstuian.featuredomain.model.ProfileEditMode
import com.workfort.pstuian.featuredomain.model.UserType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import androidx.core.bundle.Bundle

val UserTypeNavType = object : NavType<UserType>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): UserType? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }
    override fun put(bundle: Bundle, key: String, value: UserType) {
        bundle.putString(key, Json.encodeToString(value))
    }
    override fun parseValue(value: String): UserType = Json.decodeFromString(value)
    override fun serializeAsValue(value: UserType): String = Json.encodeToString(value)
    override val name: String = "UserType"
}

val ProfileEditModeNavType = object : NavType<ProfileEditMode>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ProfileEditMode? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }
    override fun put(bundle: Bundle, key: String, value: ProfileEditMode) {
        bundle.putString(key, Json.encodeToString(value))
    }
    override fun parseValue(value: String): ProfileEditMode = Json.decodeFromString(value)
    override fun serializeAsValue(value: ProfileEditMode): String = Json.encodeToString(value)
    override val name: String = "ProfileEditMode"
}

val FacultySelectionModeNavType = object : NavType<FacultySelectionMode>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): FacultySelectionMode? {
        return bundle.getString(key)?.let { Json.decodeFromString(it) }
    }
    override fun put(bundle: Bundle, key: String, value: FacultySelectionMode) {
        bundle.putString(key, Json.encodeToString(value))
    }
    override fun parseValue(value: String): FacultySelectionMode = Json.decodeFromString(value)
    override fun serializeAsValue(value: FacultySelectionMode): String = Json.encodeToString(value)
    override val name: String = "FacultySelectionMode"
}

val navTypeMap: Map<KType, NavType<*>> = mapOf(
    typeOf<UserType>() to UserTypeNavType,
    typeOf<ProfileEditMode>() to ProfileEditModeNavType,
    typeOf<FacultySelectionMode>() to FacultySelectionModeNavType,
)
