package de.htwberlin.learningcompanion

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import de.htwberlin.learningcompanion.goaloverview.GoalOverviewFragment
import de.htwberlin.learningcompanion.mainscreen.MainScreenFragment
import de.htwberlin.learningcompanion.myplace.details.MyPlaceFragment
import de.htwberlin.learningcompanion.setgoal.GoalNavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_mainscreen)

        displaySelectedScreen(R.id.nav_mainscreen)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_setgoal -> {
                displaySelectedScreen(R.id.nav_setgoal)
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
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displaySelectedScreen(itemId: Int) {

        //creating fragment object
        var fragment: Fragment? = null

        //initializing the fragment object which is selected
        when (itemId) {
            R.id.nav_mainscreen -> fragment = MainScreenFragment()
            R.id.nav_myplace -> fragment = MyPlaceFragment()
            R.id.nav_overview -> fragment = GoalOverviewFragment()
            R.id.nav_setgoal -> fragment = GoalNavHostFragment()
        }

        //replacing the fragment
        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.content_main, fragment)
            ft.commit()
        }
    }
}
