plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "repository"
            export(project(":appconstant"))
            export(project(":model"))
            export(project(":networking"))
            export(project(":sharedpref"))
            export(project(":database"))
            export(project(":util"))
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":appconstant"))
                implementation(project(":model"))
                implementation(project(":networking"))
                implementation(project(":sharedpref"))
                implementation(project(":database"))
                implementation(project(":util"))
                implementation(libs.koin.core)
            }
        }
        val androidMain by getting
        val iosMain by creating {
            dependsOn(commonMain)
        }
    }
}

android {
    namespace = "com.workfort.pstuian.repository"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
}
