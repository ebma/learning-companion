package de.htwberlin.learningcompanion.learning.session


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.squareup.picasso.Picasso
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.LearningSessionRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.learning.SessionEvaluator
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.LearningSession
import de.htwberlin.learningcompanion.model.Place
import de.htwberlin.learningcompanion.util.LIGHT_MEDIUM_THRESHOLD
import de.htwberlin.learningcompanion.util.NOISE_MEDIUM_THRESHOLD
import de.htwberlin.learningcompanion.util.setActivityTitle
import kotlinx.android.synthetic.main.fragment_session.*


class SessionFragment : Fragment() {

    private lateinit var session: LearningSession

    private lateinit var goal: Goal
    private lateinit var place: Place

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setActivityTitle("Watch session")

        loadSession()
        initLayoutWithSession()
    }

    private fun loadSession() {
        val id = arguments!!.getLong("ID")

        session = LearningSessionRepository.get(context!!).getLearningSessionByID(id)
        goal = GoalRepository.get(context!!).getGoalByID(session.goalID)
        place = PlaceRepository.get(context!!).getPlaceByID(session.placeID)
    }

    private fun initLayoutWithSession() {
        speedView.speedPercentTo(session.userRating, 2000)

        tv_goal.text = goal.getGoalText()
        tv_place.text = place.name
        tv_address.text = place.addressString
        Picasso.get().load(place.imageUri).fit().into(iv_place_preview)


        initBrightnessChart()
        initNoiseChart()
    }

    private fun initBrightnessChart() {
        brightness_chart.setTouchEnabled(false)
        brightness_chart.description = Description().apply { text = "Brightness level" }

        var entries = arrayListOf<Entry>()
        for (i in 0 until session.lightValues.size) {
            entries.add(Entry(i.toFloat(), session.lightValues[i]))
        }

        var dataSet = LineDataSet(entries, "Measured Brightness")
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.MAGENTA

        val lineData = LineData(dataSet)
        brightness_chart.data = lineData
        brightness_chart.invalidate()

        addLimitLinesToBrightnessChart()
    }

    private fun addLimitLinesToBrightnessChart() {
        val leftAxis = brightness_chart.axisLeft

        var ll = LimitLine(LIGHT_MEDIUM_THRESHOLD.toFloat(), "Expected Average")
        ll.lineColor = Color.RED
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)

        var averageBrightness = SessionEvaluator.calculateAverage(session.lightValues)

        ll = LimitLine(averageBrightness.toFloat(), "Your Average")
        ll.lineColor = Color.RED
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)
    }

    private fun initNoiseChart() {
        noise_chart.setTouchEnabled(false)
        noise_chart.description = Description().apply { text = "Noise level" }

        var entries = arrayListOf<Entry>()
        for (i in 0 until session.noiseValues.size) {
            entries.add(Entry(i.toFloat(), session.noiseValues[i].toFloat()))
        }

        var dataSet = LineDataSet(entries, "Noise Amplitudes")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.CYAN

        val lineData = LineData(dataSet)
        noise_chart.data = lineData
        noise_chart.invalidate()

        addLimitLinesToNoiseChart()
    }

    private fun addLimitLinesToNoiseChart() {
        val leftAxis = noise_chart.axisLeft

        var ll = LimitLine(NOISE_MEDIUM_THRESHOLD.toFloat(), "Expected Average")
        ll.lineColor = Color.RED
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)

        var averageNoise = SessionEvaluator.calculateAverage(session.noiseValues)

        ll = LimitLine(averageNoise.toFloat(), "Your Average")
        ll.lineColor = Color.RED
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)
    }
}
