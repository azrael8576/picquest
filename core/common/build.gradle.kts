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
    implementation(projects.core.model)

    // LifeCycle
    implementation(libs.androidx.lifecycle.runtimeCompose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
}