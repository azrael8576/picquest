plugins {
    alias(libs.plugins.pq.android.feature)
    alias(libs.plugins.pq.android.library.compose)
    alias(libs.plugins.pq.android.hilt)
}

android {
    namespace = "com.wei.picquest.feature.video"
}

dependencies {
    // Navigation
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.exoplayer.dash)
    implementation(libs.media3.ui)
}