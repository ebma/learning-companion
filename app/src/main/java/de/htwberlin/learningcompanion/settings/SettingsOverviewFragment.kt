package de.htwberlin.learningcompanion.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R

class SettingsOverviewFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var switchMicrophone: Switch
    private lateinit var switchCamera: Switch
    private lateinit var switchGps: Switch

    private lateinit var userNameEditText: EditText
    private lateinit var buddyNameEditText: EditText
    private lateinit var buddyImageEditText: EditText
    private lateinit var intervalEditText: EditText
    private lateinit var frequencyEditText: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_settings_overview, container, false)

        findViews()
        setSwitchStands()

        return rootView
    }

    private fun findViews() {
        switchMicrophone = rootView.findViewById(R.id.sw_microphone)
        switchCamera = rootView.findViewById(R.id.sw_camera)
        switchGps = rootView.findViewById(R.id.sw_gps)
        userNameEditText = rootView.findViewById(R.id.et_settings_user_name)
        buddyNameEditText = rootView.findViewById(R.id.et_settings_buddy_name)
//        buddyImageEditText = rootView.findViewById(R.id.tv_settings_buddy_image_text)
        intervalEditText = rootView.findViewById(R.id.et_settings_interval)
        frequencyEditText = rootView.findViewById(R.id.et_settings_frequency)
    }

    private fun setSwitchStands() {
        switchMicrophone.isChecked = hasAudioPermission()
        switchGps.isChecked = hasLocationPermission()
        switchCamera.isChecked = hasWritePermission()   // ist es hier, oder ein anderes Befehl?
    }


    // check permissions with
    fun hasWritePermission(): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}