plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    kotlin("android")
}

android {
    namespace = AppConfig.APP_ID
    compileSdk = AppConfig.COMPILE_SDK

    defaultConfig {
        applicationId = AppConfig.APP_ID
        minSdk = AppConfig.MIN_SDK
        targetSdk = AppConfig.TARGET_SDK
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME

        multiDexEnabled = true
        vectorDrawables {
            useSupportLibrary = true
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        buildTypes.forEach {
            it.buildConfigField("int", "VERSION_CODE_DB", AppConfig.VERSION_CODE_DB)
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                AppConfig.PROGUARD_RULES
            )
        }
    }

    flavorDimensions.add(AppConfig.DIMENSION)

    productFlavors {
        create("staging") {
            applicationIdSuffix = ".staging"
            dimension = AppConfig.DIMENSION
        }

        create("production") {
            dimension = AppConfig.DIMENSION
        }
    }

    viewBinding {
        android.buildFeatures.viewBinding = true
    }

    packaging {
        resources.excludes.add("META-INF/notice.txt")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // app libs
    implementation(AppDependencies.appLibraries)
    // room
    implementation(AppDependencies.room)
    ksp(listOf(AppDependencies.roomCompiler))
    implementation(AppDependencies.roomKtx)
    // sdk
    implementation(project(":appconstant"))
    implementation(project(":database"))
    implementation(project(":firebase"))
    implementation(project(":model"))
    implementation(project(":networking"))
    implementation(project(":repository"))
    implementation(project(":sharedpref"))
    implementation(project(":view"))
    implementation(project(":workmanager"))
    // test libs
    testImplementation(AppDependencies.testLibraries)
    androidTestImplementation(AppDependencies.androidTestLibraries)
}

