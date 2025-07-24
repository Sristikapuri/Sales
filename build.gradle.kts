plugins {
    id("com.android.application") version "8.9.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
}

// Add the Google Services classpath for Firebase
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}
