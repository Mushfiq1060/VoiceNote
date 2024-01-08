plugins {
    alias(libs.plugins.voicenote.android.feature)
    alias(libs.plugins.voicenote.android.library.compose)
}

android {
    namespace = "com.openai.voicenote.feature.label"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:common"))
}