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
    implementation(project(":core:common"))

    // Okhttp Interceptor
    implementation(libs.okhttp.logging)

    // Retrofit2
    implementation(libs.retrofit.core)

    // RetrofitKotlinxSerializationJson
    implementation(libs.retrofit.kotlin.serialization)

    // KotlinxSerializationJson
    implementation(libs.kotlinx.serialization.json)
}