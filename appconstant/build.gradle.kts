plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.workfort.pstuian.appconstant"
    compileSdk = AppConfig.COMPILE_SDK

    defaultConfig {
        minSdk = AppConfig.MIN_SDK

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        buildTypes.forEach {
            it.buildConfigField(
                "String", "APPLICATION_ID", "\"${AppConfig.APP_ID}\""
            )
        }
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

}