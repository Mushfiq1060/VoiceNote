plugins {
    alias(libs.plugins.voicenote.android.library)
    alias(libs.plugins.voicenote.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.openai.voicenote.core.data"
}

dependencies {
    api(project(":core:database"))
    api(project(":core:model"))
}