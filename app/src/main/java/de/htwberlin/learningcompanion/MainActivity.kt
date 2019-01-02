package de.htwberlin.learningcompanion

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import de.htwberlin.learningcompanion.goals.overview.GoalOverviewFragment
import de.htwberlin.learningcompanion.help.HelpOverview
import de.htwberlin.learningcompanion.learning.session.SessionOverviewFragment
import de.htwberlin.learningcompanion.mainscreen.MainScreenFragment
import de.htwberlin.learningcompanion.places.overview.PlaceOverviewFragment
import de.htwberlin.learningcompanion.settings.SettingsOverviewFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_mainscreen)

        displaySelectedScreen(R.id.nav_mainscreen)

        val mypref = MyPreference(this)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        clearBackstack()

        when (item.itemId) {
            R.id.nav_mygoal -> {
                displaySelectedScreen(R.id.nav_mygoal)
            }
            R.id.nav_myplace -> {
                displaySelectedScreen(R.id.nav_myplace)
            }
            R.id.nav_mainscreen -> {
                displaySelectedScreen(R.id.nav_mainscreen)
            }
            R.id.nav_overview -> {
                displaySelectedScreen(R.id.nav_overview)
            }
            R.id.nav_setting -> {
                displaySelectedScreen(R.id.nav_setting)
            }
            R.id.nav_help -> {
                displaySelectedScreen(R.id.nav_help)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun clearBackstack() {
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun displaySelectedScreen(itemId: Int) {
        var fragment: Fragment? = null

        when (itemId) {
            R.id.nav_mainscreen -> fragment = MainScreenFragment()
            R.id.nav_myplace -> fragment = PlaceOverviewFragment()
            R.id.nav_overview -> fragment = SessionOverviewFragment()
            R.id.nav_mygoal -> fragment = GoalOverviewFragment()
            R.id.nav_setting -> fragment = SettingsOverviewFragment()
            R.id.nav_help -> fragment = HelpOverview()
        }

        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_main, fragment)
            ft.commit()
        }
    }
}
