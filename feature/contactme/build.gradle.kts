plugins {
    alias(libs.plugins.pq.android.feature)
    alias(libs.plugins.pq.android.library.compose)
    alias(libs.plugins.pq.android.hilt)
}

android {
    namespace = "com.wei.picquest.feature.contactme"
}

dependencies {
    implementation(projects.core.data)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
