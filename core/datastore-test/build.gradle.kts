plugins {
    alias(libs.plugins.pq.android.library)
    alias(libs.plugins.pq.android.hilt)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.wei.picquest.core.datastore.test"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(libs.hilt.android.testing)
    // DataStore
    implementation(libs.androidx.datastore)
    // Protobuf
    implementation(libs.protobuf.kotlin.lite)
}
