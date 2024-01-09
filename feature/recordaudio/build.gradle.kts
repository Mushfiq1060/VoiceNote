plugins {
    alias(libs.plugins.voicenote.android.feature)
    alias(libs.plugins.voicenote.android.library.compose)
}

android {
    namespace = "com.openai.voicenote.feature.recordaudio"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:common"))
    implementation(libs.lottie.compose)
}