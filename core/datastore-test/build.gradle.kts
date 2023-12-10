plugins {
    alias(libs.plugins.pq.android.library)
    alias(libs.plugins.pq.android.hilt)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.wei.picquest.core.datastore.test"
}

dependencies {
    api(project(":core:datastore"))
    implementation(project(":core:testing"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    // DataStore
    implementation(libs.androidx.datastore)

    // Protobuf
    implementation(libs.protobuf.kotlin.lite)
}