//version constants for the Kotlin DSL dependencies
object Versions {
    //app level
    const val GRADLE = "8.3.1"
    const val KOTLIN = "1.9.23"
    const val KSP = "1.9.23-1.0.19"
    const val GOOGLE_SERVICES = "4.4.1"

    object Compose {
        val bom = "2024.03.00"
        val activity = "1.8.2"
        val viewModel = "2.6.1"
        val navigation = "2.5.3"
    }

    //libs
    val coreKtx = "1.2.0"
    val room = "2.6.1"
    val coroutines = "1.6.0"
    val multidex = "2.0.1"
    val datastorePreferences = "1.0.0"
    val workManager = "2.9.0"

    val firebaseBom = "32.7.4"

    val koin = "3.1.2"
    val coilCompose = "2.6.0"
    val retrofit = "2.9.0"
    val okhttp = "4.12.0"
    val timber = "4.7.1"
    val lottie = "4.0.0"
    val documentFile = "1.0.1"

    //test
    val junit = "4.12"
    val extJunit = "1.1.1"
    val espresso = "3.2.0"
    val runner = "1.5.2"
}