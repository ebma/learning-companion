package de.htwberlin.learningcompanion

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView
import de.htwberlin.learningcompanion.buddy.Buddy
import de.htwberlin.learningcompanion.buddy.BuddyFaceHolder
import de.htwberlin.learningcompanion.goals.overview.GoalOverviewFragment
import de.htwberlin.learningcompanion.help.HelpOverview
import de.htwberlin.learningcompanion.learning.SessionHandler
import de.htwberlin.learningcompanion.learning.session.SessionOverviewFragment
import de.htwberlin.learningcompanion.mainscreen.MainScreenFragment
import de.htwberlin.learningcompanion.places.overview.PlaceOverviewFragment
import de.htwberlin.learningcompanion.recommendation.RecommendationFragment
import de.htwberlin.learningcompanion.settings.SettingsOverviewFragment
import de.htwberlin.learningcompanion.util.SharedPreferencesHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
                toggle.onDrawerStateChanged(newState)

                setUIElementsToCharlieFace()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                toggle.onDrawerSlide(drawerView, slideOffset)
            }

            override fun onDrawerClosed(drawerView: View) {
                toggle.onDrawerClosed(drawerView) //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDrawerOpened(drawerView: View) {
                toggle.onDrawerOpened(drawerView)


                if (SessionHandler.get(this@MainActivity).sessionRunning) {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    Buddy.get(applicationContext).showExitProhibitedMessage()
                }
            }

        })
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_mainscreen)


        SharedPreferencesHelper.get(applicationContext).buddyColorLiveData.observe(this, Observer {
            setUIElementsToCharlieFace()
        })

        fab_charlie.onClick {
            nav_view.setCheckedItem(R.id.nav_mainscreen)
            displaySelectedScreen(R.id.nav_mainscreen)
        }

        changeMainScreenMenuItemText()

        displaySelectedScreen(R.id.nav_mainscreen)

        setUIElementsToCharlieFace()
    }

    fun setUIElementsToCharlieFace() {
        runOnUiThread {
            fab_charlie.setImageDrawable(BuddyFaceHolder.get(applicationContext).getDefaultFace())
            drawer_layout.findViewById<ImageView>(R.id.nav_view_charlie_face)?.setImageDrawable(BuddyFaceHolder.get(applicationContext).getDefaultFace())
        }
    }

    fun changeMainScreenMenuItemText() {
        val menu = nav_view.menu
        val mainScreenMenuItem = menu.findItem(R.id.nav_mainscreen)
        mainScreenMenuItem.title = SharedPreferencesHelper.get(applicationContext!!).getBuddyName()
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
            R.id.nav_recommendation -> {
                displaySelectedScreen(R.id.nav_recommendation)
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
            R.id.nav_recommendation -> fragment = RecommendationFragment()
            R.id.nav_setting -> fragment = SettingsOverviewFragment()
            R.id.nav_help -> fragment = HelpOverview()
        }

        if (itemId == R.id.nav_mainscreen) {
            fab_charlie.hide()
        } else {
            fab_charlie.show()
        }

        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_main, fragment)
            ft.commit()
        }
    }
}
