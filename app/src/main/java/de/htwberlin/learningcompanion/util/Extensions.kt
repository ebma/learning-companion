package de.htwberlin.learningcompanion.util

import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.MainActivity

fun Fragment.setActivityTitle(title: String) {
    (activity as MainActivity).supportActionBar?.title = title
}