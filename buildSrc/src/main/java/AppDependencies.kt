import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {
    // std lib
    private val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"

    // android ui
    val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
    private val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.support}"
    val material = "com.google.android.material:material:${Versions.material}"
    private val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"

    // room
    val room = "androidx.room:room-runtime:${Versions.room}"
    // To use Kotlin annotation processing tool (kapt)
    val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    // Optional - Kotlin Extensions and Coroutines support for Room
    val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    // optional - Test helpers
    val roomTesting = "androidx.room:room-testing:${Versions.room}"

    val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    private val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    // Lifecycle only (without ViewModel or LiveData)
    private val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    // Annotation processor
    private val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"

    // coroutines
    private val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    private val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    // work manger for background task
    val workManager = "androidx.work:work-runtime-ktx:${Versions.workManager}"
    private val workManagerTesting = "androidx.work:work-testing:${Versions.workManager}"

    private val multidex = "androidx.multidex:multidex:${Versions.multidex}"
    private val datastorePreferences =
        "androidx.datastore:datastore-preferences:${Versions.datastorePreferences}"
    private val palette = "androidx.palette:palette-ktx:${Versions.palette}"

    // google
    private val playCoreKtx = "com.google.android.play:core-ktx:${Versions.playCoreKtx}"
    private val playServicesLocation = "com.google.android.gms:play-services-location:${Versions.playServicesLocation}"
    // Import the BoM for the Firebase platform
    val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    // Firebase Cloud Messaging and Analytics libraries
    val firebaseMessaging = "com.google.firebase:firebase-messaging-ktx"
    val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"

    // 3rd party
    // Koin(dependency injector)
    val koin = "io.insert-koin:koin-android:${Versions.koin}"
    // Koin(dependency injector) testing
    val koinTest = "io.insert-koin:koin-test:${Versions.koin}"

    private val circularImageView = "com.mikhaellopez:circularimageview:${Versions.circularimageview}"
    private val coilImageLoader = "io.coil-kt:coil:${Versions.coil}"

    // networking
    private val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    private val retrofitAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    val retrofitConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    private val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

    // logger
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    // facebook shimmer layout
    private val shimmer = "com.facebook.shimmer:shimmer:${Versions.shimmer}"
    // web view
    private val agentweb = "com.github.Justson.AgentWeb:agentweb-core:${Versions.agentweb}"
    // animation loader
    private val lottie = "com.airbnb.android:lottie:${Versions.lottie}"

    // test libs
    private val junit = "junit:junit:${Versions.junit}"
    private val extJUnit = "androidx.test.ext:junit:${Versions.extJunit}"
    private val runner = "androidx.test:runner:${Versions.runner}"
    private val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"

    private val androidXLibraries = arrayListOf<String>().apply {
        add(kotlinStdLib)
        add(coreKtx)
        add(appcompat)
        add(constraintLayout)
        add(legacySupport)
        add(material)
        add(recyclerview)

        add(viewModel)

        add(retrofitConverter)

        add(coroutinesCore)
        add(coroutinesAndroid)

        add(multidex)

        add(datastorePreferences)

        add(palette)
    }

    private val googleLibraries = arrayListOf<String>().apply {
        add(playCoreKtx)
        add(playServicesLocation)
    }

    private val thirdPartyLibraries = arrayListOf<String>().apply {
        add(koin)
        add(circularImageView)
        add(coilImageLoader)

        add(timber)
        add(shimmer)
        add(agentweb)
        add(lottie)
    }

    // for app module
    val appLibraries = androidXLibraries + googleLibraries + thirdPartyLibraries

    // for network module
    val networkingLibraries = arrayListOf<String>().apply {
        add(retrofit)
        add(retrofitAdapter)
        add(retrofitConverter)
        add(okhttp)
        add(okhttpLogging)
    }

    val testLibraries = arrayListOf<String>().apply {
        add(junit)
        add(koinTest)
        add(roomTesting)
    }

    val androidTestLibraries = arrayListOf<String>().apply {
        add(extJUnit)
        add(runner)
        add(espressoCore)
    }
}

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.ksp(list: List<String>) {
    list.forEach { dependency ->
        add("ksp", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.implementationPlatform(list: List<String>) {
    list.forEach { dependency ->
        platform(dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}