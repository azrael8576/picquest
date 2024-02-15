import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.wei.picquest.configureFlavors
import com.wei.picquest.configureGradleManagedDevices
import com.wei.picquest.configureKotlinAndroid
import com.wei.picquest.configurePrintApksTask
import com.wei.picquest.disableUnnecessaryAndroidTests
import com.wei.picquest.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("pq.android.lint")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
                configureFlavors(this)
                configureGradleManagedDevices(this)
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                configurePrintApksTask(this)
                disableUnnecessaryAndroidTests(target)
            }
            dependencies {
                add("testImplementation", kotlin("test"))

                // Paging 3
                add("implementation", libs.findLibrary("paging-runtime").get())
                add("implementation", libs.findLibrary("paging-compose").get())
                add("testImplementation", libs.findLibrary("paging-common").get())

                // Timber
                add("implementation", libs.findLibrary("timber").get())
            }
        }
    }
}
