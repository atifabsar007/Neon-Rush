package com.neonrush.android

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.neonrush.NeonRushGame
import com.neonrush.service.GuestAuthService
import com.google.firebase.FirebaseApp

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val configuration = AndroidApplicationConfiguration().apply {
            useImmersiveMode = true
            useAccelerometer = false
            useCompass = false
        }
        val firebaseReady = FirebaseApp.initializeApp(this) != null
        initialize(NeonRushGame(if (firebaseReady) FirebaseAuthService(this) else GuestAuthService()), configuration)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        FirebaseAuthServiceHolder.service?.onActivityResult(requestCode, resultCode, data)
    }
}
