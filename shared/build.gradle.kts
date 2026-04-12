plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
            // Export modules to make them visible in Swift
            export(project(":model"))
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":appconstant"))
                implementation(project(":database"))
                api(project(":model"))
                implementation(project(":networking"))
                implementation(project(":repository"))
                implementation(project(":sharedpref"))
                implementation(project(":util"))
                implementation(libs.kotlinx.coroutines.core)

                api(libs.koin.core)
                implementation(libs.compose.ui)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.material.icons)
                implementation(libs.compose.runtime)
                implementation(libs.compose.animation)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.viewmodel)
                implementation(libs.compose.navigation)
                implementation(libs.coil3.compose)
            }
        }
        commonMain.resources.srcDirs("src/commonMain/composeResources")
        val androidMain by getting {
            dependencies {
                api(libs.koin.android)
                implementation(project(":firebase"))
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
    namespace = "com.workfort.pstuian.shared"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
