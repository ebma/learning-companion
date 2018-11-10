package de.htwberlin.learningcompanion.mainscreen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.sensors.LearningSessionEvaluator
import de.htwberlin.learningcompanion.sensors.SensorHandler
import de.htwberlin.learningcompanion.util.setActivityTitle
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sensorManager

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
            startSensorHandler()
        }
        btnQuit.onClick {
            stopSensorHandler()
        }
    }

    private fun startSensorHandler() {
        sensorHandler.start(INTERVAL_IN_SECONDS)
    }

    private fun stopSensorHandler() {
        sensorHandler.stop()
        startEvaluation()
    }

    private fun startEvaluation() {
        val learningSessionEvaluater = LearningSessionEvaluator(sensorHandler.dataList, arrayListOf<Float>())
    }

}
