package ru.vanchikov.fitnesinmylife.ui.Navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_login.*
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser
import ru.vanchikov.fitnesinmylife.ui.login.LoginViewModel
import ru.vanchikov.fitnesinmylife.ui.login.LoginViewModelFactory
import android.app.Dialog
import android.widget.Toast
import ru.vanchikov.fitnesinmylife.data.UserAccount


class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var userInfo : LoggedInUser
    private lateinit var nav_header_login : TextView
    private lateinit var nav_header_email : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ru.vanchikov.fitnesinmylife.R.layout.activity_navigation)
        val toolbar: Toolbar = findViewById(ru.vanchikov.fitnesinmylife.R.id.toolbar)

        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(ru.vanchikov.fitnesinmylife.R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(ru.vanchikov.fitnesinmylife.R.id.drawer_layout)
        val navView: NavigationView = findViewById(ru.vanchikov.fitnesinmylife.R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, ru.vanchikov.fitnesinmylife.R.string.navigation_drawer_open, ru.vanchikov.fitnesinmylife.R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)




    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(ru.vanchikov.fitnesinmylife.R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(ru.vanchikov.fitnesinmylife.R.menu.navigation, menu)

        nav_header_login = findViewById<TextView>(R.id.nav_header_login)
        nav_header_email = findViewById<TextView>(R.id.nav_header_email)

        nav_header_login.text = UserAccount.user.displayName
        nav_header_email.text = UserAccount.user.email

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            ru.vanchikov.fitnesinmylife.R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            ru.vanchikov.fitnesinmylife.R.id.nav_home -> {
                // Handle the camera action
            }
            ru.vanchikov.fitnesinmylife.R.id.nav_ways -> {

            }
            ru.vanchikov.fitnesinmylife.R.id.nav_map -> {

            }
            ru.vanchikov.fitnesinmylife.R.id.nav_share -> {

            }
            ru.vanchikov.fitnesinmylife.R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(ru.vanchikov.fitnesinmylife.R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
