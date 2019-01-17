package de.htwberlin.learningcompanion.help

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.util.SharedPreferencesHelper
import de.htwberlin.learningcompanion.util.setActivityTitle

class HelpOverview : Fragment() {
    private lateinit var rootView : View

    private lateinit var tvHelpText1: TextView
    private lateinit var tvHelpText2: TextView
    private lateinit var tvHelpText3: TextView
    private lateinit var tvHelpText4: TextView
    private lateinit var tvHelpText5: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_help, container, false)

        setActivityTitle(getString(R.string.title_nav_menu_help))
        findViews()

        setHelpText()

        return rootView
    }

    private fun setHelpText() {

        val charlieName = SharedPreferencesHelper.get(context!!).getBuddyName()
        val text1 = " is designed to help you to set, monitor and review your learning goal and provide you with information on learning places with additional data such as brightness, noise level."
        val text2 = "Messages that "+ charlieName + " provide are based on volitional control strategies in addition to jokes and quotes. The source of the jokes and quotes are as follows: \\nhttps://www.brainyquote.com \\nhttps://www.lifehack.org\\nhttps://www.rd.com/jokes/funny-quotes/ \\nhttp://pun.me/pages/funny-quotes.php \\nhttp://coolfunnyquotes.com \\nhttp://www.laughfactory.com"
        val text3 = "When evaluating your set goal, " + charlieName + " will store data so that " + charlieName + " can recommend you the best place, best time or even best goal that you can achieve."
        val text4 = "You can personalize " + charlieName + " so that " + charlieName + " can call you by name and you can also call " + charlieName + " in your personal way."
        val text5 = "Colors scheme of " + charlieName + " are based on affective colors of trustworthy and playful to display positive affective state."


        val spannableString = SpannableString(charlieName + text1)

        tvHelpText1.text = spannableString.setSpan(StyleSpan(Typeface.BOLD), 0,  charlieName.length, 0).toString()



//        tvHelpText1.setText(text1)
        tvHelpText2.setText(text2)
        tvHelpText3.setText(text3)
        tvHelpText4.setText(text4)
        tvHelpText5.setText(text5)
    }

    private fun findViews() {
        tvHelpText1 = rootView.findViewById(R.id.tv_help_1)
        tvHelpText2 = rootView.findViewById(R.id.tv_help_2)
        tvHelpText3 = rootView.findViewById(R.id.tv_help_3)
        tvHelpText4 = rootView.findViewById(R.id.tv_help_4)
        tvHelpText5 = rootView.findViewById(R.id.tv_help_5)
    }
}