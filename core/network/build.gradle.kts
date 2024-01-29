plugins {
    alias(libs.plugins.pq.android.library)
    alias(libs.plugins.pq.android.hilt)
    alias(libs.plugins.secrets)
    id("kotlinx-serialization")
}

android {
    namespace = "com.wei.core.network"

    buildFeatures {
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

secrets {
    defaultPropertiesFileName = "secrets.defaults.properties"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)

    // KotlinxSerializationJson
    implementation(libs.kotlinx.serialization.json)
    // Okhttp Interceptor
    implementation(libs.okhttp.logging)
    // Retrofit2
    implementation(libs.retrofit.core)
    // RetrofitKotlinxSerializationJson
    implementation(libs.retrofit.kotlin.serialization)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
