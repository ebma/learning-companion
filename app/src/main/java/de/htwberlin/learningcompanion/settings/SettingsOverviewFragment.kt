package de.htwberlin.learningcompanion.settings

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.htwberlin.learningcompanion.MainActivity
import de.htwberlin.learningcompanion.MyPreference
import de.htwberlin.learningcompanion.R
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
    //    private lateinit var buddyImageEditText: EditText
    private lateinit var intervalEditText: EditText
    private lateinit var frequencyEditText: EditText

    private lateinit var btn_buddyImage1: ImageButton
    private lateinit var btn_buddyImage2: ImageButton
    private lateinit var btn_buddyImage3: ImageButton
    private lateinit var btn_buddyImage4: ImageButton
    private lateinit var btn_buddyImage5: ImageButton

    private lateinit var btn_settings_save: Button

    private lateinit var sharedPref: SharedPreferences
    private var charlieNumberChange = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_settings_overview, container, false)

        findViews()
        setSwitchStands()
        addClickListeners()

        setDefaultPreferences()

        return rootView
    }

    private fun setDefaultPreferences() {
        sharedPref = activity!!.getPreferences(Context.MODE_PRIVATE) ?: return
//        val num = sharedPref.getCharlieNumber()
        val charlieNum = sharedPref?.getInt("CharlieNumber", 0)
        val userNamePref = sharedPref?.getString("UserName", "You")
        val buddyNamePref = sharedPref?.getString("BuddyName", "Charlie")
        rootView.findViewById<EditText>(R.id.input_user_name).hint = userNamePref
        rootView.findViewById<EditText>(R.id.input_buddy_name).hint = buddyNamePref
        when(charlieNum) {
            1 -> changeName("gentle Charlie")
            2 -> changeName("nerdy Charlie")
            3 -> changeName("calm Charlie")
            4 -> changeName("happy Charlie")
            5 -> changeName("goofy Charlie")
            else -> {
                changeName("gentle Charlie")
            }
        }
    }

    private fun savePreferences() {
        // muss noch ne Bedinguing hinzufügen, wenn die Input Felder nicht leer sind

        var uname = userNameEditText.text.toString()
        var bname = buddyNameEditText.text.toString()

        setUserName(uname)
        setBuddyName(bname)
        setCharlieName(charlieNumberChange)

        // change hint text in edit-fields
        rootView.findViewById<EditText>(R.id.input_user_name).hint = uname
        rootView.findViewById<EditText>(R.id.input_buddy_name).hint = bname
    }

    private fun setCharlieName(charlieNumber: Int) {
        val editor = sharedPref!!.edit()
        editor.putInt("CharlieNumber", charlieNumber)
        editor.apply()
    }

    private fun setUserName(userName: String) {
        val editor = sharedPref!!.edit()
        editor.putString("UserName", userName)
        editor.apply()
    }

    private fun setBuddyName(buddyName: String) {
        val editor = sharedPref!!.edit()
        editor.putString("BuddyName", buddyName)
        editor.apply()
    }

    private fun changeName(newName: String) {
        rootView.findViewById<TextView>(R.id.tv_settings_buddy_image_text).text = newName
    }

    private fun findViews() {
        switchMicrophone = rootView.findViewById(R.id.sw_microphone)
        switchCamera = rootView.findViewById(R.id.sw_camera)
        switchGps = rootView.findViewById(R.id.sw_gps)
        userNameEditText = rootView.findViewById(R.id.input_user_name)
        buddyNameEditText = rootView.findViewById(R.id.input_buddy_name)
        intervalEditText = rootView.findViewById(R.id.et_settings_interval)
        frequencyEditText = rootView.findViewById(R.id.et_settings_frequency)

        btn_buddyImage1 = rootView.findViewById(R.id.iv_charlie_1)
        btn_buddyImage2 = rootView.findViewById(R.id.iv_charlie_2)
        btn_buddyImage3 = rootView.findViewById(R.id.iv_charlie_3)
        btn_buddyImage4 = rootView.findViewById(R.id.iv_charlie_4)
        btn_buddyImage5 = rootView.findViewById(R.id.iv_charlie_5)

        btn_settings_save = rootView.findViewById(R.id.btn_settings_save)
    }

    private fun addClickListeners() {
        btn_buddyImage1.onClick {
            changeName("gentle Charlie")
            charlieNumberChange = 1
        }
        btn_buddyImage2.onClick {
            changeName("nerdy Charlie")
            charlieNumberChange = 2
        }
        btn_buddyImage3.onClick {
            changeName("calm Charlie")
            charlieNumberChange = 3
        }
        btn_buddyImage4.onClick {
            changeName("happy Charlie")
            charlieNumberChange = 4
        }
        btn_buddyImage5.onClick {
            changeName("goofy Charlie")
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
    fun hasWritePermission(): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    fun hasLocationPermission(): Boolean {
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