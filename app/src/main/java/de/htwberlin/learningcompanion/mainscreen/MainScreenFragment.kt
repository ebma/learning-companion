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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.*
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.myplace.details.MyPlaceFragment
import de.htwberlin.learningcompanion.sensors.LearningSessionEvaluator
import de.htwberlin.learningcompanion.sensors.SensorHandler
import de.htwberlin.learningcompanion.setgoal.GoalNavHostFragment
import de.htwberlin.learningcompanion.util.setActivityTitle
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast

class MainScreenFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var sensorHandler: SensorHandler

    private lateinit var btnStart: Button
    private lateinit var btnQuit: Button

    private lateinit var tvCharlieInfo: TextView
    private lateinit var tvLearningInfo: TextView

    private lateinit var ivPlaceBackground: ImageView

    private val INTERVAL_IN_SECONDS = 5 // maybe 1/min soon
    private var permissionToRecordAccepted = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_screen, container, false)
        setActivityTitle(getString(R.string.title_nav_menu_main_screen))

        sensorHandler = SensorHandler(activity!!.sensorManager)

        findViews()
        addClickListeners()
        addPermissionListener()
        setBackgroundPicture()
        showCharlieInfoText()
        return rootView
    }

    private fun setBackgroundPicture() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val currentPlace = PlaceRepository.get(context!!).getCurrentPlace()

            if (currentPlace != null) {
                val inputStream = activity!!.contentResolver.openInputStream(currentPlace.imageUri)
                val drawable = Drawable.createFromStream(inputStream, currentPlace.imageUri.toString())
                ivPlaceBackground.setImageDrawable(drawable)
            }
        } else {
            requestStoragePermission()
        }
    }

    private fun findViews() {
        btnStart = rootView.findViewById(R.id.btn_start)
        btnQuit = rootView.findViewById(R.id.btn_quit)
        tvCharlieInfo = rootView.findViewById(R.id.tv_charlie_info)
        tvLearningInfo = rootView.findViewById(R.id.tv_learning_info)
        ivPlaceBackground = rootView.findViewById(R.id.iv_place_background)
    }

    private fun addClickListeners() {
        btnStart.onClick {
            onStartButtonClick()
        }
        btnQuit.onClick {
            onQuitButtonClick()
        }
    }

    private fun onQuitButtonClick() {
        stopSensorHandler()
        startEvaluation()
    }

    private fun onStartButtonClick() {
        if (permissionToRecordAccepted) {
            val currentGoal = GoalRepository.get(context!!).getCurrentGoal()
            val currentPlace = PlaceRepository.get(context!!).getCurrentPlace()

            when {
                currentGoal == null -> showSelectGoalDialog()
                currentPlace == null -> showSelectPlaceDialog()
                else -> startSensorHandler()
            }
        } else {
            requestAudioPermission()
        }
    }

    private fun showCharlieInfoText() {
        val currentGoal = GoalRepository.get(context!!).getCurrentGoal()
        val currentPlace = PlaceRepository.get(context!!).getCurrentPlace()

        when {
            currentGoal == null -> showSelectGoalInfoText()
            currentPlace == null -> showSelectPlaceInfoText()
            else -> showStartLearningInfoText()
        }
    }

    private fun showSelectGoalInfoText() {
        tvCharlieInfo.text = "Please press \"Menu\" and go to \"My Goals\" to set the goal that you want to achieve."
    }

    private fun showSelectPlaceInfoText() {
        tvCharlieInfo.text = "Please press \"Menu\" and go to \"My places\" to set the place where you want to learn."
    }

    private fun showStartLearningInfoText() {
        tvCharlieInfo.text = "You can start your learning session by clicking on the \"Start\" Button below."
    }

    private fun startSensorHandler() {
        sensorHandler.clear()
        sensorHandler.start(INTERVAL_IN_SECONDS)
        toast("Learning session started")
    }

    private fun stopSensorHandler() {
        sensorHandler.stop()
        toast("Learning session stopped")
    }

    private fun startEvaluation() {
        val learningSessionEvaluator = LearningSessionEvaluator(sensorHandler.lightDataList, sensorHandler.noiseDataList)
        showSessionResultDialog(learningSessionEvaluator)
    }

    private fun showSessionResultDialog(learningSessionEvaluator: LearningSessionEvaluator) {
        alert {
            title = "Evaluation"
            positiveButton("Got it!") { }
            customView {
                verticalLayout {
                    textView("LightLevel: ${learningSessionEvaluator.evaluateLight()}")
                    textView("NoiseLevel: ${learningSessionEvaluator.evaluateNoise()}")
                    padding = dip(16)
                }
            }
        }.show()
    }

    private fun showSelectPlaceDialog() {
        alert("Please take your time and create one before starting your learning session.", "We detected that you did not yet create a place!") {
            yesButton { navigateToMyPlaceFragment() }
            noButton { }
        }.show()
    }

    private fun navigateToMyPlaceFragment() {
        val fragment = MyPlaceFragment()
        activity!!.supportFragmentManager.beginTransaction().addToBackStack("myplacefragment").replace(R.id.content_main, fragment).commit()
    }

    private fun showSelectGoalDialog() {
        alert("Please take your time and create one before starting your learning session.", "We detected that you did not yet create a goal!") {
            yesButton { navigateToGoalFragment() }
            noButton { }
        }.show()
    }

    private fun navigateToGoalFragment() {
        val fragment = GoalNavHostFragment()
        activity!!.supportFragmentManager.beginTransaction().addToBackStack("goalfragment").replace(R.id.content_main, fragment).commit()
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions: Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(activity!!, permissions, REQUEST_EXTERNAL_STORAGE_PERMISSION)
        }
    }

    private fun requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            val permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(activity!!, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
        } else {
            permissionToRecordAccepted = true
        }
    }

    private fun addPermissionListener() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionAccepted(permission: String) {
                if (permission == Manifest.permission.RECORD_AUDIO) {
                    permissionToRecordAccepted = true
                    onStartButtonClick()
                } else if (permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    setBackgroundPicture()
                }
            }

            override fun onPermissionRevoked(permission: String) {
                if (permission == Manifest.permission.RECORD_AUDIO) {
                    permissionToRecordAccepted = false
                    (activity as MainActivity).removePermissionListener(this)
                }
            }
        }

        (activity as MainActivity).addPermissionListener(permissionListener)
    }

}
