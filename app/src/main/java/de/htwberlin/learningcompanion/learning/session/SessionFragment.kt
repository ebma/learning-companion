package de.htwberlin.learningcompanion.learning.session


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
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
import de.htwberlin.learningcompanion.util.*
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


        if (session.lightValues.isNotEmpty() && session.noiseValues.isNotEmpty()) {
            initBrightnessChart()
            initNoiseChart()
        } else {
            brightness_chart.visibility = View.GONE
            noise_chart.visibility = View.GONE

            tv_noise_value.text = session.noiseRating.name
            tv_brightness_value.text = session.brightnessRating.name
        }
    }

    private fun initBrightnessChart() {
        brightness_chart.setTouchEnabled(false)
        brightness_chart.description = Description().apply { text = "Brightness level" }

        var entries = arrayListOf<Entry>()
        for (i in 0 until session.lightValues.size) {
            entries.add(Entry(i.toFloat(), session.lightValues[i]))
        }

        var dataSet = LineDataSet(entries, "Measured Brightness in Lux")
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.MAGENTA

        val lineData = LineData(dataSet)
        brightness_chart.data = lineData
        brightness_chart.invalidate()

        brightness_chart.axisRight.isEnabled = false
        brightness_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        addLimitLinesToBrightnessChart()
    }

    private fun addLimitLinesToBrightnessChart() {
        val leftAxis = brightness_chart.axisLeft

        var ll = LimitLine(LIGHT_MEDIUM_THRESHOLD.toFloat(), "Medium Brightness Threshold")
        ll.lineColor = Color.DKGRAY
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)


        ll = LimitLine(LIGHT_LOW_THRESHOLD.toFloat(), "Low Brightness Threshold")
        ll.lineColor = Color.RED
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)


        ll = LimitLine(LIGHT_HIGH_THRESHOLD.toFloat(), "High Brightness Threshold")
        ll.lineColor = Color.GREEN
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)

        var averageBrightness = SessionEvaluator.calculateAverage(session.lightValues)

        ll = LimitLine(averageBrightness.toFloat(), "Your Average")
        ll.lineColor = Color.BLUE
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)

        leftAxis.axisMaximum = Math.max(averageBrightness.plus(10).toFloat(), LIGHT_MEDIUM_THRESHOLD.plus(LIGHT_MEDIUM_THRESHOLD / 10).toFloat())
    }

    private fun initNoiseChart() {
        noise_chart.setTouchEnabled(false)
        noise_chart.description = Description().apply { text = "Noise level" }

        var entries = arrayListOf<Entry>()
        for (i in 0 until session.noiseValues.size) {
            entries.add(Entry(i.toFloat(), session.noiseValues[i].toFloat()))
        }

        var dataSet = LineDataSet(entries, "Noise Values in dB")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.argb(255, 100, 149, 237)

        val lineData = LineData(dataSet)
        noise_chart.data = lineData
        noise_chart.invalidate()

        noise_chart.axisRight.isEnabled = false
        noise_chart.xAxis.position = XAxis.XAxisPosition.BOTTOM


        addLimitLinesToNoiseChart()
    }

    private fun addLimitLinesToNoiseChart() {
        val leftAxis = noise_chart.axisLeft

        var ll = LimitLine(NOISE_MEDIUM_THRESHOLD.toFloat(), "Medium Noise Threshold")
        ll.lineColor = Color.DKGRAY
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)

        ll = LimitLine(NOISE_LOW_THRESHOLD.toFloat(), "Low Noise Threshold")
        ll.lineColor = Color.GREEN
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)

        ll = LimitLine(NOISE_HIGH_THRESHOLD.toFloat(), "High Noise Threshold")
        ll.lineColor = Color.RED
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)


        var averageNoise = SessionEvaluator.calculateAverage(session.noiseValues)

        ll = LimitLine(averageNoise.toFloat(), "Your Average")
        ll.lineColor = Color.BLUE
        ll.lineWidth = 2f
        ll.textColor = Color.BLACK
        ll.textSize = 10f

        leftAxis.addLimitLine(ll)

        leftAxis.axisMaximum = Math.max(averageNoise.plus(10).toFloat(), NOISE_MEDIUM_THRESHOLD.plus(NOISE_MEDIUM_THRESHOLD / 10).toFloat())
    }
}
