plugins {
    alias(libs.plugins.voicenote.android.library)
    alias(libs.plugins.voicenote.android.hilt)
}

android {
    namespace = "com.openai.voicenote.core.datastore"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api(libs.androidx.dataStore.core)
    api(project(":core:datastoreproto"))
}