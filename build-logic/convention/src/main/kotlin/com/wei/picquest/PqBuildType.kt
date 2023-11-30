package com.wei.picquest

/**
 * This is shared between :app module to provide configurations type safety.
 */
enum class PqBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE
}
