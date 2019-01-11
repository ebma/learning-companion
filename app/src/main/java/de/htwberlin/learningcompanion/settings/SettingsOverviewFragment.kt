package de.htwberlin.learningcompanion.settings

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import de.htwberlin.learningcompanion.MainActivity
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.util.SharedPreferencesHelper
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.toast

class SettingsOverviewFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var switchMicrophone: Switch
    private lateinit var switchCamera: Switch
    private lateinit var switchGps: Switch

    private lateinit var userNameEditText: EditText
    private lateinit var buddyNameEditText: EditText

    private lateinit var buddyMoodTextView: TextView

    private lateinit var intervalEditText: EditText
    private lateinit var frequencyEditText: EditText

    private lateinit var btn_buddyImage1: ImageButton
    private lateinit var btn_buddyImage2: ImageButton
    private lateinit var btn_buddyImage3: ImageButton
    private lateinit var btn_buddyImage4: ImageButton
    private lateinit var btn_buddyImage5: ImageButton

    private lateinit var btn_settings_save: Button

    private var charlieNumberChange = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_settings_overview, container, false)

        findViews()
        setSwitchStands()
        addClickListeners()

        setHints()

        return rootView
    }

    private fun findViews() {
        switchMicrophone = rootView.findViewById(R.id.sw_microphone)
        switchCamera = rootView.findViewById(R.id.sw_camera)
        switchGps = rootView.findViewById(R.id.sw_gps)

        userNameEditText = rootView.findViewById(R.id.input_user_name)
        buddyNameEditText = rootView.findViewById(R.id.input_buddy_name)
        buddyMoodTextView = rootView.findViewById(R.id.tv_settings_buddy_image_text)

        intervalEditText = rootView.findViewById(R.id.et_settings_interval)
        frequencyEditText = rootView.findViewById(R.id.et_settings_frequency)

        btn_buddyImage1 = rootView.findViewById(R.id.iv_charlie_1)
        btn_buddyImage2 = rootView.findViewById(R.id.iv_charlie_2)
        btn_buddyImage3 = rootView.findViewById(R.id.iv_charlie_3)
        btn_buddyImage4 = rootView.findViewById(R.id.iv_charlie_4)
        btn_buddyImage5 = rootView.findViewById(R.id.iv_charlie_5)

        btn_settings_save = rootView.findViewById(R.id.btn_settings_save)
    }

    private fun setHints() {
        userNameEditText.hint = SharedPreferencesHelper.get(context!!).getUserName()
        buddyNameEditText.hint = SharedPreferencesHelper.get(context!!).getBuddyName()

        buddyMoodTextView.text = SharedPreferencesHelper.get(context!!).getBuddyMood() + " " +
                    SharedPreferencesHelper.get(context!!).getBuddyName()

//        rootView.findViewById<TextView>(R.id.ti_settings_interval).hint =
//                SharedPreferencesHelper.get(context!!).getInterval().toString() + " minutes"
//        rootView.findViewById<TextView>(R.id.ti_settings_frequency).hint =
//                SharedPreferencesHelper.get(context!!).getInterval().toString() + " times"
    }

    private fun savePreferences() {
        var userName = SharedPreferencesHelper.get(context!!).getUserName()
        if (!userNameEditText.text.none()) {
            userName = userNameEditText.text.toString()
        }

        var buddyName = SharedPreferencesHelper.get(context!!).getBuddyName()
        if (!buddyNameEditText.text.none()) {
            buddyName = buddyNameEditText.text.toString()
        }

        var interval = SharedPreferencesHelper.get(context!!).getInterval()
        if (!intervalEditText.text.none()) {
            interval = intervalEditText.text.toString().toInt()
        }

        var frequency = SharedPreferencesHelper.get(context!!).getFrequency()
        if(!frequencyEditText.text.none()) {
            frequency = frequencyEditText.text.toString().toInt()
        }

        saveUserName(userName)
        saveBuddyName(buddyName)
        saveBuddyMood(charlieNumberChange)

        saveInterval(interval)
        saveFrequency(frequency)

        (activity as MainActivity).changeMainScreenMenuItemText()

        setHints()

        userNameEditText.setText("")
        buddyNameEditText.setText("")
        intervalEditText.setText("")
        frequencyEditText.setText("")
    }

    private fun saveUserName(userName: String) {
        SharedPreferencesHelper.get(context!!).setUserName(userName)
    }

    private fun saveBuddyName(buddyName: String) {
        SharedPreferencesHelper.get(context!!).setBuddyName(buddyName)
    }

    private fun saveBuddyMood(moodNumber: Int) {
        SharedPreferencesHelper.get(context!!).setBuddyMood(moodNumber)
    }

    private fun saveInterval(interval: Int) {
        SharedPreferencesHelper.get(context!!).setInterval(interval)
    }

    private fun saveFrequency(frequency: Int) {
        SharedPreferencesHelper.get(context!!).setFrequency(frequency)
    }

    private fun changeMoodText(newName: String) {
        rootView.findViewById<TextView>(R.id.tv_settings_buddy_image_text).text = newName + " " + SharedPreferencesHelper.get(context!!).getBuddyName()
    }

    private fun addClickListeners() {
        btn_buddyImage1.onClick {
            changeMoodText("gentle")
            charlieNumberChange = 1
        }
        btn_buddyImage2.onClick {
            changeMoodText("nerdy")
            charlieNumberChange = 2
        }
        btn_buddyImage3.onClick {
            changeMoodText("calm")
            charlieNumberChange = 3
        }
        btn_buddyImage4.onClick {
            changeMoodText("happy")
            charlieNumberChange = 4
        }
        btn_buddyImage5.onClick {
            changeMoodText("goofy")
            charlieNumberChange = 5
        }
        btn_settings_save.onClick {
            savePreferences()
            toast("settings were saved")
        }
    }

    private fun setSwitchStands() {
        switchMicrophone.isChecked = hasAudioPermission()
        switchGps.isChecked = hasLocationPermission()
        switchCamera.isChecked = hasWritePermission()   // ist es hier, oder ein anderes Befehl?
    }

    // check permissions with
    private fun hasWritePermission(): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun tintTextInputLayout(layout: TextInputLayout, errorTint: Boolean) {
        runOnUiThread {
            if (errorTint)
                layout.defaultHintTextColor = ColorStateList.valueOf(resources.getColor(android.R.color.holo_red_dark))
            else
                layout.defaultHintTextColor = ColorStateList.valueOf(resources.getColor(android.R.color.darker_gray))
        }
    }
}