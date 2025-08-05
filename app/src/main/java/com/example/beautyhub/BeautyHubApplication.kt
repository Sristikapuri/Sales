package com.example.beautyhub

import android.app.Application
import com.google.firebase.FirebaseApp

class BeautyHubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
    }
}
