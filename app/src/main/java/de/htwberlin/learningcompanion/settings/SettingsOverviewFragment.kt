package de.htwberlin.learningcompanion.settings

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import de.htwberlin.learningcompanion.MyPreference
import de.htwberlin.learningcompanion.R
import org.jetbrains.anko.sdk27.coroutines.onClick

class SettingsOverviewFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var switchMicrophone: Switch
    private lateinit var switchCamera: Switch
    private lateinit var switchGps: Switch

    private lateinit var userNameEditText: EditText
    private lateinit var buddyNameEditText: EditText
    //    private lateinit var buddyImageEditText: EditText
    private lateinit var intervalEditText: EditText
    private lateinit var frequencyEditText: EditText

    private lateinit var btn_buddyImage1: ImageButton
    private lateinit var btn_buddyImage2: ImageButton
    private lateinit var btn_buddyImage3: ImageButton
    private lateinit var btn_buddyImage4: ImageButton
    private lateinit var btn_buddyImage5: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_settings_overview, container, false)

        findViews()
        setSwitchStands()
        addClickListeners()

//        val mypref = MyPreference()
//        val userName = mypref.getUserName()
//        val charlieNum = mypref.getCharlieNumber()
//        val buddyName = mypref.getBuddyName()

//        rootView.findViewById<TextView>(R.id.et_settings_user_name).text = userName

        return rootView
    }

//    private fun saveData() {
//        SharedPreferences pref = con
//    }

    private fun findViews() {
        switchMicrophone = rootView.findViewById(R.id.sw_microphone)
        switchCamera = rootView.findViewById(R.id.sw_camera)
        switchGps = rootView.findViewById(R.id.sw_gps)
        userNameEditText = rootView.findViewById(R.id.et_settings_user_name)
        buddyNameEditText = rootView.findViewById(R.id.et_settings_buddy_name)
//        buddyImageEditText = rootView.findViewById(R.id.tv_settings_buddy_image_text)
        intervalEditText = rootView.findViewById(R.id.et_settings_interval)
        frequencyEditText = rootView.findViewById(R.id.et_settings_frequency)

        btn_buddyImage1 = rootView.findViewById(R.id.iv_charlie_1)
        btn_buddyImage2 = rootView.findViewById(R.id.iv_charlie_2)
        btn_buddyImage3 = rootView.findViewById(R.id.iv_charlie_3)
        btn_buddyImage4 = rootView.findViewById(R.id.iv_charlie_4)
        btn_buddyImage5 = rootView.findViewById(R.id.iv_charlie_5)
    }

    private fun addClickListeners() {
        btn_buddyImage1.onClick {
            changeName("gentle Charlie")
        }
        btn_buddyImage2.onClick {
            changeName("nerdy Charlie")
        }
        btn_buddyImage3.onClick {
            changeName("calm Charlie")
        }
        btn_buddyImage4.onClick {
            changeName("happy Charlie")
        }
        btn_buddyImage5.onClick {
            changeName("goofy Charlie")
        }
    }

    private fun changeName(newName: String) {
        rootView.findViewById<TextView>(R.id.tv_settings_buddy_image_text).text = newName
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

    val sharedPref = activity?.getSharedPreferences("LearningCompanion", Context.MODE_PRIVATE)
}