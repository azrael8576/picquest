plugins {
    alias(libs.plugins.pq.android.library)
    alias(libs.plugins.pq.android.hilt)
}

android {
    namespace = "com.wei.picquest.core.data.test"
}

dependencies {
    api(projects.core.data)

    implementation(libs.hilt.android.testing)
}
