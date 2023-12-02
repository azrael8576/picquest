import com.wei.picquest.PqBuildType

plugins {
    alias(libs.plugins.pq.android.application)
    alias(libs.plugins.pq.android.application.compose)
    alias(libs.plugins.pq.android.application.flavors)
    alias(libs.plugins.pq.android.hilt)
}

android {
    namespace = "com.wei.picquest"

    defaultConfig {
        applicationId = "com.wei.picquest"
        /**
         * Version Code: AABCXYZ
         *
         * AA: API Level (33)
         *
         * BC: Supported screen sizes for this APK.
         * 12: Small to Normal screens
         * 34: Large to X-Large screens
         *
         * XYZ: App version (050 for 0.5.0)
         */
        versionCode = 3414000
        /**
         * SemVer major.minor.patch
         */
        versionName = "0.0.0"

        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner = "com.wei.picquest.core.testing.PqTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = PqBuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            applicationIdSuffix = PqBuildType.RELEASE.applicationIdSuffix
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":feature:home"))
    implementation(project(":feature:photo"))
    implementation(project(":feature:contactme"))

    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    // TODO Wei
//    implementation(project(":core:datastore"))

    androidTestImplementation(project(":core:designsystem"))
//    androidTestImplementation(project(":core:datastore-test"))
    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.accompanist.testharness)
//    testImplementation(project(":core:datastore-test"))
    testImplementation(project(":core:testing"))
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.accompanist.testharness)
    debugImplementation(project(":ui-test-hilt-manifest"))
    debugImplementation(libs.androidx.compose.ui.testManifest)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    // LifeCycle
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    // Navigation
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Splashscreen
    implementation(libs.androidx.core.splashscreen)

    // Timber
    implementation(libs.timber)
}