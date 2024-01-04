plugins {
    alias(libs.plugins.voicenote.android.library)
    alias(libs.plugins.voicenote.android.hilt)
}

android {
    namespace = "com.openai.voicenote.core.common"
}

dependencies {
    implementation(libs.converter.gson)
}