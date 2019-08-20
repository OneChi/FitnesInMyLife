package ru.vanchikov.fitnesinmylife.ui.Navigation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation.*
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.data.DataViewModel
import ru.vanchikov.fitnesinmylife.data.UserAccount
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser


class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var userInfo : LoggedInUser
    private lateinit var nav_header_login : TextView
    private lateinit var nav_header_email : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)




        initMenu()

    }

    private lateinit var navController: NavController

    private fun initMenu() {

        val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navController = host.navController

        // обеспечивает поддержку логинап и почты в оглавлении бокового меню NavDrawer
        setSupportActionBar(toolbar)

        /*
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout = drawer_layout)



        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, ru.vanchikov.fitnesinmylife.R.string.navigation_drawer_open, ru.vanchikov.fitnesinmylife.R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        navView?.setupWithNavController(navController)

        toolbar.setupWithNavController(navController,appBarConfiguration)

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

        nav_header_login = findViewById<TextView>(ru.vanchikov.fitnesinmylife.R.id.nav_header_login)
        nav_header_email = findViewById<TextView>(ru.vanchikov.fitnesinmylife.R.id.nav_header_email)

        nav_header_login.text = UserAccount.user.displayName
        nav_header_email.text = UserAccount.user.email

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val navController = findNavController(R.id.nav_host_fragment)
        //NavigationUI.onNavDestinationSelected(item, navController)
        return when (item.itemId) {
            ru.vanchikov.fitnesinmylife.R.id.action_settings -> true

            else ->item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            ru.vanchikov.fitnesinmylife.R.id.userPage -> {
                //Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.userPage)
            }
            ru.vanchikov.fitnesinmylife.R.id.storyPage -> {
                //Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.mapPage)
            }
            ru.vanchikov.fitnesinmylife.R.id.mapPage -> {
                //Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.mapPage)
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



