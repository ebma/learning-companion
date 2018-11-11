package de.htwberlin.learningcompanion.mainscreen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.myplace.details.MyPlaceFragment
import de.htwberlin.learningcompanion.sensors.LearningSessionEvaluator
import de.htwberlin.learningcompanion.sensors.SensorHandler
import de.htwberlin.learningcompanion.setgoal.GoalNavHostFragment
import de.htwberlin.learningcompanion.util.setActivityTitle
import org.jetbrains.anko.noButton
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sensorManager
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton

class MainScreenFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var sensorHandler: SensorHandler

    private lateinit var btnStart: Button
    private lateinit var btnQuit: Button

    private val INTERVAL_IN_SECONDS = 10

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
            stopSensorHandler()
        }
    }

    private fun onQuitButtonClick() {
        stopSensorHandler()
        startEvaluation()
    }

    private fun onStartButtonClick() {
        val goals = GoalRepository.get(context!!).goalsList
        val places = PlaceRepository.get(context!!).placesList

        if (goals != null && goals.isNotEmpty()) {
            if (places != null && places.isNotEmpty()) {
                startSensorHandler()
            } else {
                showSelectPlaceDialog()
            }
        } else {
            showSelectGoalDialog()
        }
    }

    private fun startSensorHandler() {
        sensorHandler.start(INTERVAL_IN_SECONDS)
    }

    private fun stopSensorHandler() {
        sensorHandler.stop()
    }

    private fun startEvaluation() {
        val learningSessionEvaluater = LearningSessionEvaluator(sensorHandler.dataList, arrayListOf<Float>())
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

}
