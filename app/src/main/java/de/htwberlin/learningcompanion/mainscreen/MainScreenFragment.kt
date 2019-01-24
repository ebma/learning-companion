package de.htwberlin.learningcompanion.mainscreen


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.library.bubbleview.BubbleTextView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.buddy.Buddy
import de.htwberlin.learningcompanion.buddy.BuddyFaceHolder
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.LearningSessionRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.learning.SessionHandler
import de.htwberlin.learningcompanion.learning.evaluation.EvaluationNavHostFragment
import de.htwberlin.learningcompanion.model.LearningSession
import de.htwberlin.learningcompanion.util.SharedPreferencesHelper
import de.htwberlin.learningcompanion.util.setActivityTitle
import org.jetbrains.anko.noButton
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton

class MainScreenFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var sessionHandler: SessionHandler

    private lateinit var btnStart: Button
    private lateinit var btnQuit: Button

    private lateinit var tvCharlieInfo: TextView
    private lateinit var tvLearningInfo: TextView
    private lateinit var tvCharlieText: BubbleTextView

    private lateinit var ivPlaceBackground: ImageView
    private lateinit var ivCharlieFace: ImageView

    private var permissionToRecordAccepted = false
    private var waitingForPermissionToStartSession = false

    private lateinit var buddy: Buddy

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_screen, container, false)
        setActivityTitle(SharedPreferencesHelper.get(context!!).getBuddyName())

        sessionHandler = SessionHandler.get(activity!!)
        buddy = Buddy.get(context!!)

        findViews()
        addClickListeners()
        setBackgroundPicture()
        showCharlieInfoText()

        observeBuddyLiveData()

        ivCharlieFace.setImageDrawable(BuddyFaceHolder.get(context!!).getDefaultFace())

        if (sessionHandler.sessionRunning) {
            showSessionInfoInBox()

            btnStart.visibility = View.INVISIBLE
            btnQuit.visibility = View.VISIBLE
        }

        return rootView
    }

    private fun observeBuddyLiveData() {
        buddy.drawableLiveData.observe(this, Observer<Drawable> { drawable -> ivCharlieFace.setImageDrawable(drawable) })
        buddy.speechLiveData.observe(this, Observer<String> { text ->
            run {
                if (text.isEmpty()) {
                    tvCharlieText.visibility = View.INVISIBLE
                    tvCharlieText.text = ""
                } else {
                    tvCharlieText.visibility = View.VISIBLE
                    tvCharlieText.text = text
                }
            }
        })

        ivCharlieFace.onClick {
            if (buddy.isInDefaultState) {
                if (SessionHandler.get(activity!!).sessionRunning) {
                    buddy.setNewRandomBuddyLearningText()
                } else {
                    buddy.setNewRandomBuddyBeforeLearningText()
                }
            }
        }
    }

    private fun setBackgroundPicture() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val currentPlace = PlaceRepository.get(context!!).getCurrentPlace()

            if (currentPlace != null) {
                if (currentPlace.imageUri != null) {
                    val inputStream = activity!!.contentResolver.openInputStream(currentPlace.imageUri)
                    val drawable = Drawable.createFromStream(inputStream, currentPlace.imageUri.toString())
                    ivPlaceBackground.setImageDrawable(drawable)
                }
            }
        } else {
            requestAllPermissions()
        }
    }

    private fun findViews() {
        btnStart = rootView.findViewById(R.id.btn_start)
        btnQuit = rootView.findViewById(R.id.btn_quit)
        tvCharlieInfo = rootView.findViewById(R.id.tv_charlie_info)
        tvCharlieText = rootView.findViewById(R.id.tv_charlie_text)
        tvLearningInfo = rootView.findViewById(R.id.tv_learning_info)
        ivPlaceBackground = rootView.findViewById(R.id.iv_place_background)
        ivCharlieFace = rootView.findViewById(R.id.iv_charlie)
    }

    private fun addClickListeners() {
        btnStart.onClick {
            onStartButtonClick()
        }
        btnQuit.onClick {
            openQuitDialog()
        }
    }

    private fun openQuitDialog() {
        alert("We can finish this goal together!\n" +
                "Do you REALLY want to quit?", "Quit learning session") {
            yesButton {
                finishLearningSession()
            }
            noButton {
                toast("Good :)")
            }
        }.show()
    }

    private fun finishLearningSession() {
        sessionHandler.stopLearningSession()
        createNewSessionEntity()
        navigateToEvaluateFragment()
        btnStart.visibility = View.VISIBLE
        btnQuit.visibility = View.INVISIBLE
    }

    private fun createNewSessionEntity() {
        val goalID = GoalRepository.get(context!!).getCurrentGoal()!!.id
        val placeID = PlaceRepository.get(context!!).getCurrentPlace()!!.id

        val session = LearningSession(placeID, goalID).apply {
            brightnessRating = sessionHandler.getLightLevel()
            noiseRating = sessionHandler.getNoiseLevel()
            noiseValues = sessionHandler.getNoiseValues()
            lightValues = sessionHandler.getLightValues()
        }

        LearningSessionRepository.get(context!!).insertLearningSession(session)
    }

    private fun navigateToEvaluateFragment() {
        val fragment = EvaluationNavHostFragment()

        activity!!.supportFragmentManager.beginTransaction().addToBackStack("evaluation").replace(R.id.content_main, fragment).commit()
    }

    private fun onStartButtonClick() {
        if (canStartSession()) {

            waitingForPermissionToStartSession = true
            requestAudioPermission()

            // this will be set immediately if permission is already granted
            if (permissionToRecordAccepted) {
                startLearningSession()
            }
        } else {
            buddy.setInstructionText()
        }
    }

    private fun startLearningSession() {
        btnStart.visibility = View.INVISIBLE
        btnQuit.visibility = View.VISIBLE
        if (permissionToRecordAccepted) {
            sessionHandler.startLearningSessionWithMeasuringSensors()
        } else {
            sessionHandler.startLearningSessionWithoutMeasuringSensors()
        }

        showSessionInfoInBox()
    }

    private fun showSessionInfoInBox() {
        if (sessionHandler.sessionRunning) {
            sessionHandler.observe(object : SessionHandler.LearningSessionObserver {
                override fun onUpdate(millisUntilFinished: Long) {
                    tvLearningInfo.text = sessionHandler.getSessionInfo()
                }

                override fun onFinish() {
                    tvLearningInfo.text = "Learning session over"
                    finishLearningSession()
                }
            })
        }
    }

    private fun canStartSession(): Boolean {
        return sessionHandler.canStartLearningSession()
    }

    private fun showCharlieInfoText() {
        tvCharlieInfo.text = buddy.getInfoText()
    }

    private fun requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
            requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        } else {
            permissionToRecordAccepted = true
        }
    }

    private fun requestAllPermissions() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permissions, REQUEST_ALL_PERMISSION)
        } else {
            permissionToRecordAccepted = true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

            if (waitingForPermissionToStartSession) {
                startLearningSession()
            }
        } else if (requestCode == REQUEST_ALL_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

            if (waitingForPermissionToStartSession) {
                startLearningSession()
            }
        }
    }

    companion object {
        const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        const val REQUEST_EXTERNAL_STORAGE_PERMISSION = 201
        const val REQUEST_ALL_PERMISSION = 202
    }
}
