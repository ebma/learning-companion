package de.htwberlin.learningcompanion.mainscreen


import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.MainActivity
import de.htwberlin.learningcompanion.PermissionListener
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.REQUEST_RECORD_AUDIO_PERMISSION
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

    private val INTERVAL_IN_SECONDS = 5
    private var permissionToRecordAccepted = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_screen, container, false)
        setActivityTitle(getString(R.string.title_nav_menu_main_screen))

        sensorHandler = SensorHandler(activity!!.sensorManager)

        findViews()
        addClickListeners()
        return rootView
    }

    private fun findViews() {
        btnStart = rootView.findViewById(R.id.btn_start)
        btnQuit = rootView.findViewById(R.id.btn_quit)
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
                //currentGoal == null -> showSelectGoalDialog()
                currentPlace == null -> showSelectPlaceDialog()
                else -> startSensorHandler()
            }
        } else {
            requestPermissionsAndRetryStart()
        }
    }

    private fun startSensorHandler() {
        sensorHandler.start(INTERVAL_IN_SECONDS)
    }


    private fun stopSensorHandler() {
        sensorHandler.stop()
    }

    private fun startEvaluation() {
        val learningSessionEvaluator = LearningSessionEvaluator(sensorHandler.lightDataList, sensorHandler.noiseDataList)
        showSessionResultDialog(learningSessionEvaluator)
    }

    private fun showSessionResultDialog(learningSessionEvaluator: LearningSessionEvaluator) {
        alert {
            title = "Evaluation"
            positiveButton("Got it!") { toast("Yes!") }
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
            noButton { toast("Learning session aborted") }
        }.show()
    }

    private fun navigateToMyPlaceFragment() {
        val fragment = MyPlaceFragment()
        activity!!.supportFragmentManager.beginTransaction().addToBackStack("myplacefragment").replace(R.id.content_main, fragment).commit()
    }

    private fun showSelectGoalDialog() {
        alert("Please take your time and create one before starting your learning session.", "We detected that you did not yet create a goal!") {
            yesButton { navigateToGoalFragment() }
            noButton { toast("Learning session aborted") }
        }.show()
    }

    private fun navigateToGoalFragment() {
        val fragment = GoalNavHostFragment()
        activity!!.supportFragmentManager.beginTransaction().addToBackStack("goalfragment").replace(R.id.content_main, fragment).commit()
    }

    private fun requestPermissionsAndRetryStart() {
        (activity as MainActivity).addPermissionListener(object : PermissionListener {
            override fun onPermissionAccepted(permission: String) {
                if (permission == Manifest.permission.RECORD_AUDIO) {
                    permissionToRecordAccepted = true
                    onStartButtonClick()
                }
            }

            override fun onPermissionRevoked(permission: String) {
                if (permission == Manifest.permission.RECORD_AUDIO)
                    permissionToRecordAccepted = false
            }

        })

        val permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
        ActivityCompat.requestPermissions(activity!!, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

}
