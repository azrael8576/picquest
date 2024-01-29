plugins {
    alias(libs.plugins.pq.android.library)
    alias(libs.plugins.pq.android.library.compose)
    alias(libs.plugins.pq.android.hilt)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.wei.picquest.core.designsystem"

    defaultConfig {
        testInstrumentationRunner = "com.wei.picquest.core.testing.PqTestRunner"
    }
}

dependencies {
    androidTestImplementation(projects.core.testing)

    // Material Design 3
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.iconsExtended)
    // main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.runtime)
    // Android Studio Preview support
    api(libs.androidx.compose.ui.tooling.preview)
    // Optional - Integration with window
    api(libs.androidx.window)
    // Optional - accompanist adaptive
    api(libs.accompanist.adaptive)
    // Coil
    api(libs.coil.kt.compose)
    api(libs.coil.kt.svg)

    debugApi(libs.androidx.compose.ui.tooling)

    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.accompanist.testharness)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)
    testImplementation(projects.core.testing)

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
