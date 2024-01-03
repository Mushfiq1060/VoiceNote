plugins {
    alias(libs.plugins.voicenote.android.feature)
    alias(libs.plugins.voicenote.android.library.compose)
}

android {
    namespace = "com.openai.voicenote.feature.home"
}

dependencies {
    implementation(project(":core:data"))
}