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
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.charlie.Charlie
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.learning.SessionHandler
import de.htwberlin.learningcompanion.learning.evaluation.EvaluationNavHostFragment
import de.htwberlin.learningcompanion.util.setActivityTitle
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainScreenFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var sessionHandler: SessionHandler

    private lateinit var btnStart: Button
    private lateinit var btnQuit: Button

    private lateinit var tvCharlieInfo: TextView
    private lateinit var tvLearningInfo: TextView

    private lateinit var ivPlaceBackground: ImageView

    private var permissionToRecordAccepted = false

    private lateinit var charlie: Charlie

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_screen, container, false)
        setActivityTitle(getString(R.string.title_nav_menu_main_screen))

        sessionHandler = SessionHandler(activity!!)
        charlie = Charlie(context!!)

        findViews()
        addClickListeners()
        setBackgroundPicture()
        showCharlieInfoText()
        return rootView
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
            btnStart.visibility = View.INVISIBLE
            btnQuit.visibility = View.VISIBLE
        }
        btnQuit.onClick {
            onQuitButtonClick()
            btnStart.visibility = View.VISIBLE
            btnQuit.visibility = View.INVISIBLE
        }
    }

    private fun onQuitButtonClick() {
        // sessionHandler.stopLearningSession()
        navigateToEvaluateFragment()
    }

    private fun navigateToEvaluateFragment() {
        val fragment = EvaluationNavHostFragment()

        activity!!.supportFragmentManager.beginTransaction().addToBackStack("evaluation").replace(R.id.content_main, fragment).commit()
    }

    private fun onStartButtonClick() {
        requestAudioPermission()

        if (permissionToRecordAccepted) {
            if (sessionHandler.canStartLearningSession()) {
                sessionHandler.startLearningSession()

                sessionHandler.observe(object : SessionHandler.LearningSessionObserver {
                    override fun onUpdate(millisUntilFinished: Long) {
                        tvLearningInfo.text = sessionHandler.getSessionInfo()
                    }

                    override fun onFinish() {
                        tvLearningInfo.text = "Learning session over"
                    }

                })
            }
        }
    }

    private fun showCharlieInfoText() {
        tvCharlieInfo.text = charlie.getInfoText()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        const val REQUEST_RECORD_AUDIO_PERMISSION = 200
        const val REQUEST_EXTERNAL_STORAGE_PERMISSION = 201
    }
}
