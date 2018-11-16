package de.htwberlin.learningcompanion.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R

class SettingsOverviewFragment : Fragment() {

    private lateinit var rootView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_settings_overview, container, false)

        return rootView
    }





        /*
    private var action: String? = null
    private var amount: String? = null
    private var discipline: String? = null
    private var medium: String? = null
    private var timestamp: String? = null


    private lateinit var rootView: View
    private lateinit var goalTextTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_summary, container, false)

        initVariablesFromArguments()
        findViews()
        setGoalTextInTextView()
        return rootView
    }

    private fun findViews() {
        goalTextTextView = rootView.findViewById(R.id.tv_goal_text)
    }

    private fun initVariablesFromArguments() {
        amount = arguments?.getString("amount")
        action = arguments?.getString("action")
        discipline = arguments?.getString("discipline")
        medium = arguments?.getString("medium")
        timestamp = arguments?.getString("timestamp")
    }

    private fun setGoalTextInTextView() {
        val goalText = "${action} ${discipline} ${medium} ${amount} for/until ${timestamp}"

        goalTextTextView.text = goalText
    }

    ----------------------------

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            rootView = inflater.inflate(R.layout.fragment_goal_overview, container, false)

            updateHeaderLayout()
            return rootView
        }


        private lateinit var rootView: View
        private lateinit var sensorHandler: SensorHandler

        private lateinit var btnStart: Button
        private lateinit var btnQuit: Button

        private lateinit var tvCharlieInfo: TextView

        private val INTERVAL_IN_SECONDS = 5
        private var permissionToRecordAccepted = false

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            rootView = inflater.inflate(R.layout.fragment_main_screen, container, false)
            setActivityTitle(getString(R.string.title_nav_menu_main_screen))

            sensorHandler = SensorHandler(activity!!.sensorManager)

            findViews()
            addClickListeners()
            showCharlieInfoText()
            return rootView
        }
        */

}