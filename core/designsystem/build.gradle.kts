plugins {
    alias(libs.plugins.voicenote.android.library)
    alias(libs.plugins.voicenote.android.library.compose)
}

android {
    namespace = "com.openai.voicenote.core.designsystem"
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.compose.ui.toolkit.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.ui.text.google.fonts)

    debugApi(libs.androidx.compose.ui.tooling)

    testImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.compose.ui.test.junit4)

}