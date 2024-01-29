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
    api(projects.core.common)
    api(projects.core.network)
    api(projects.core.model)
    api(projects.core.datastore)
}
