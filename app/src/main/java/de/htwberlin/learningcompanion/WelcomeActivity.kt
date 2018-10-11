package de.htwberlin.learningcompanion

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        cl_welcome_activity.setOnClickListener {
            launchMainActivity()
        }
    }

    private fun launchMainActivity() {
        startActivity(Intent(this, MainActivity::class.java));
    }
}
