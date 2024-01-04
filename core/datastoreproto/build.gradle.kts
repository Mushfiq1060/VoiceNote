plugins {
    alias(libs.plugins.voicenote.android.library)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.openai.voicenote.core.datastoreproto"
}

// Setup protobuf configuration, generating lite Java and Kotlin classes
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.4"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    api(libs.protobuf.java.lite)
}