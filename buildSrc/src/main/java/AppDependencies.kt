object AppDependencies {
    // std lib
    val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"

    // android
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"

    // compose(need platform)
    val composeBom = "androidx.compose:compose-bom:${Versions.Compose.bom}"
    val composeMaterial3 = "androidx.compose.material3:material3"
    // Integration with activities
    val composeActivity = "androidx.activity:activity-compose:${Versions.Compose.activity}"
    // Integration with ViewModels
    val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Compose.viewModel}"
    // Navigation
    val composeNavigation = "androidx.navigation:navigation-compose:${Versions.Compose.navigation}"
    // Android Studio Preview support
    val composeToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    val composeTooling = "androidx.compose.ui:ui-tooling"

    // room
    val room = "androidx.room:room-runtime:${Versions.room}"
    // To use Kotlin annotation processing tool (kapt)
    val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    // Optional - Kotlin Extensions and Coroutines support for Room
    val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    // optional - Test helpers
    val roomTesting = "androidx.room:room-testing:${Versions.room}"

    // datastore
    val datastorePreferences = "androidx.datastore:datastore-preferences:${Versions.datastorePreferences}"

    // coroutines
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    // work manger for background task
    val workManager = "androidx.work:work-runtime-ktx:${Versions.workManager}"
    val workManagerTesting = "androidx.work:work-testing:${Versions.workManager}"

    val documentFile = "androidx.documentfile:documentfile:${Versions.documentFile}"

    val multidex = "androidx.multidex:multidex:${Versions.multidex}"

    // Import the BoM for the Firebase platform
    val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    // Firebase Cloud Messaging and Analytics libraries
    val firebaseMessaging = "com.google.firebase:firebase-messaging-ktx"
    val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"

    // 3rd party
    // Koin(dependency injector)
    val koin = "io.insert-koin:koin-androidx-compose:${Versions.koin}"
    // Koin(dependency injector) testing
    val koinTest = "io.insert-koin:koin-test:${Versions.koin}"

    // networking
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    val retrofitConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

    // image loader
    val coilCompose = "io.coil-kt:coil-compose:${Versions.coilCompose}"

    // logger
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    // animation loader
    val lottie = "com.airbnb.android:lottie-compose:${Versions.lottie}"

    // test libs
    val junit = "junit:junit:${Versions.junit}"
    val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    val runner = "androidx.test:runner:${Versions.runner}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}
