plugins {
    alias(libs.plugins.pq.android.library)
    alias(libs.plugins.pq.android.library.compose)
    alias(libs.plugins.pq.android.hilt)
}

android {
    namespace = "com.wei.picquest.core.common"

    defaultConfig {
        testInstrumentationRunner = "com.wei.picquest.core.testing.PqTestRunner"
    }
}

dependencies {
    // LifeCycle
    implementation(libs.androidx.lifecycle.runtimeCompose)
    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(projects.core.testing)
    // For flow test
    testImplementation(libs.turbine)

    androidTestImplementation(projects.core.testing)
}
