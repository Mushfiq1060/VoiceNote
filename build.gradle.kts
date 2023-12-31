// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.android.hilt) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}