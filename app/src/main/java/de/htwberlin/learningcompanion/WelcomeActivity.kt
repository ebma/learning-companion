package de.htwberlin.learningcompanion

import android.app.Activity
import android.os.Bundle
import de.htwberlin.learningcompanion.R

class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }
}
