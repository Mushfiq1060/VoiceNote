plugins {
    alias(libs.plugins.voicenote.android.feature)
    alias(libs.plugins.voicenote.android.library.compose)
}

android {
    namespace = "com.opanai.voicenote.feature.notelabel"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:common"))
}