plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("org.jetbrains.kotlin.native.cocoapods")
}

compose.resources {
    publicResClass = true
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Shared module for PSTUian"
        homepage = "https://github.com/workfort/PSTUian-android"
        ios.deploymentTarget = "16.0"
        framework {
            baseName = "presentation"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":data"))
                api(project(":domain"))
                implementation(project(":util"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)

                // Compose
                implementation(libs.compose.ui)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.material.icons)
                implementation(libs.compose.runtime)
                implementation(libs.compose.animation)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.compose.viewmodel)
                implementation(libs.compose.navigation)

                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)
                implementation(libs.ktor.client.core)

                // Koin
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)

                // Animation
                implementation(libs.compottie)
                implementation(libs.compottie.resources)

                // Logging
                implementation(libs.napier)

                // ✅ GitLive Firebase (KMM wrapper)
                implementation(libs.firebase.kmm.auth)
                implementation(libs.firebase.kmm.firestore)
            }
        }
        commonMain.resources.srcDirs("src/commonMain/composeResources")
        val androidMain by getting {
            dependencies {
                api(libs.koin.android)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    namespace = "com.workfort.pstuian.presentation"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}
