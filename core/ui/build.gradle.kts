plugins {
    alias(libs.plugins.voicenote.android.library)
    alias(libs.plugins.voicenote.android.library.compose)
}

android {
    namespace = "com.openai.voicenote.core.ui"
}

dependencies {
    implementation(project(":core:designsystem"))
    api(project(":core:model"))
}