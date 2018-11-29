package de.htwberlin.learningcompanion.learning.session


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.LearningSessionRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.LearningSession
import de.htwberlin.learningcompanion.model.Place
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
    }
}
