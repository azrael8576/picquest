plugins {
    alias(libs.plugins.pq.android.library)
    alias(libs.plugins.pq.android.hilt)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.wei.picquest.core.datastore.test"
}

dependencies {
    api(projects.core.datastore)
    implementation(projects.core.testing)
    implementation(projects.core.common)
    implementation(projects.core.model)

    // DataStore
    implementation(libs.androidx.datastore)

    // Protobuf
    implementation(libs.protobuf.kotlin.lite)
}