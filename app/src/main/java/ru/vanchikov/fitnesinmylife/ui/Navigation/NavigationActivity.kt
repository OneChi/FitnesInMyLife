package ru.vanchikov.fitnesinmylife.ui.Navigation

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import ru.vanchikov.fitnesinmylife.data.UserAccount
import ru.vanchikov.fitnesinmylife.data.ViewModels.NavigationViewModel
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser


class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val LOG_TAG = "NAVIGATION"
    private lateinit var userInfo: LoggedInUser
    private lateinit var nav_header_login: TextView
    private lateinit var nav_header_email: TextView
    private lateinit var navigationViewModel: NavigationViewModel
    private lateinit var navController: NavController

    //PERMISSIONS
    var MY_PERMISSIONS_REQUEST_ACC_FINE_LOC: Int = 1




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        userInfo = UserAccount.user
        val navigationViewModel = ViewModelProviders.of(this).get(NavigationViewModel::class.java)
        navigationViewModel.userAccount = userInfo
        initMenu()

    }

    fun initPermissions(){

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACC_FINE_LOC)
            }
        } else {
            // Permission has already been granted
        }
    }

    override fun onResume() {
        super.onResume()
        initPermissions()
    }

    private fun initMenu() {

        val host =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
                ?: return

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navController = host.navController

        // обеспечивает поддержку логина и почты в оглавлении бокового меню NavDrawer
        setSupportActionBar(toolbar)



        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val appBarConfiguration =
            AppBarConfiguration(navController.graph, drawerLayout = drawer_layout)


        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            ru.vanchikov.fitnesinmylife.R.string.navigation_drawer_open,
            ru.vanchikov.fitnesinmylife.R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        navView?.setupWithNavController(navController)

        toolbar.setupWithNavController(navController, appBarConfiguration)

    }


    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)

        nav_header_login = findViewById(R.id.nav_header_login)
        nav_header_email = findViewById(R.id.nav_header_email)

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
            R.id.action_settings -> true

            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.userPage -> {
                //Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.userPage)
            }
            R.id.storyPage -> {
                //Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.mapPage)
            }
            R.id.mapPage -> {
                //Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.mapPage)
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACC_FINE_LOC -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            } else -> {
            // Ignore all other requests.
        }
        }
    }


}



