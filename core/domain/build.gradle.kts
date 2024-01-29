plugins {
    alias(libs.plugins.pq.android.library)
    alias(libs.plugins.pq.android.hilt)
}

android {
    namespace = "com.wei.picquest.core.domain"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)

    testImplementation(projects.core.testing)
}
