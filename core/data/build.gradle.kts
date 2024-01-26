plugins {
    alias(libs.plugins.pq.android.library)
    alias(libs.plugins.pq.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.wei.picquest.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.model)
    implementation(projects.core.datastore)

    implementation(libs.kotlinx.serialization.json)
}