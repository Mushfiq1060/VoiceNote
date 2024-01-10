plugins {
    alias(libs.plugins.voicenote.android.application)
    alias(libs.plugins.voicenote.android.application.compose)
    alias(libs.plugins.voicenote.android.hilt)
}

android {
    namespace = "com.openai.voicenote"

    defaultConfig {
        applicationId = "com.openai.voicenote"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":feature:notes"))
    implementation(project(":feature:noteedit"))
    implementation(project(":feature:labeledit"))
    implementation(project(":feature:label"))
    implementation(project(":feature:recordaudio"))
    implementation(project(":feature:notelabel"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
//    implementation(libs.bundles.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    implementation(libs.androidx.core.splash.screen)

    // Google Accompanist
    implementation(libs.accompanist.systemuicontroller)

    // Compose View Model
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

}