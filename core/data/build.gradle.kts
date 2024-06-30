import java.util.Properties

plugins {
    alias(libs.plugins.voicenote.android.library)
    alias(libs.plugins.voicenote.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.openai.voicenote.core.data"

    defaultConfig {
        val localProperties = project.rootProject.file("local.properties")
        val properties = Properties()
        properties.load(localProperties.inputStream())
        buildConfigField("String", "OPEN_AI_API_KEY", properties.getProperty("OPEN_AI_API_KEY") ?: "")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(project(":core:database"))
    api(project(":core:model"))
    api(project(":core:datastore"))
    api(project(":core:common"))
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.converter.scalars)
    implementation(libs.play.services.wearable)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.play.services)
}