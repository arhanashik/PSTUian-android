plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = libs.versions.appId.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.appId.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        multiDexEnabled = true
        vectorDrawables {
            useSupportLibrary = true
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        buildTypes.forEach {
            it.buildConfigField("int", "VERSION_CODE_DB", libs.versions.versionCodeDb.get())
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                libs.versions.proguardRules.get()
            )
        }
    }

    flavorDimensions.add(libs.versions.dimension.get())

    productFlavors {
        create("staging") {
            applicationIdSuffix = ".staging"
            dimension = libs.versions.dimension.get()
        }

        create("production") {
            dimension = libs.versions.dimension.get()
        }
    }

    viewBinding {
        android.buildFeatures.viewBinding = true
    }

    packaging {
        resources.excludes.add("META-INF/notice.txt")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlin.stdlib)

    // compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.compose.runtime)
    implementation(libs.compose.navigation)
    implementation(libs.compose.viewmodel)

    // coroutine
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // datastore
    implementation(libs.androidx.datastore.preferences)

    // koin
    implementation(libs.koin.androidx.compose)

    // image loader
    implementation(libs.coil3.compose)
    implementation(libs.compose.components.resources)

    // animation loader
    implementation(libs.airbnb.android.lottie.compose)

    // multidex
    implementation(libs.androidx.multidex)

    // logger
    implementation(libs.jakewharton.timber)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.documentfile)

    // sdk
    implementation(project(":feature-domain"))
    implementation(project(":feature-presentation"))
    implementation(project(":util"))
    implementation(project(":workmanager"))

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)

    // test libs
    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.espresso.core)
}

