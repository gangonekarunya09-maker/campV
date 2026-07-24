package com.example.campv.firebase

import android.content.Context
import com.google.firebase.FirebaseApp

object FirebaseManager {
    fun initialize(context: Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
    }
}
