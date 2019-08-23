package ru.vanchikov.fitnesinmylife.ui.Navigation

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.telecom.ConnectionService
import android.util.Log
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
import ru.vanchikov.fitnesinmylife.Service.GpsServiceApp
import ru.vanchikov.fitnesinmylife.data.UserAccount
import ru.vanchikov.fitnesinmylife.data.ViewModels.NavigationViewModel
import ru.vanchikov.fitnesinmylife.data.model.LoggedInUser


class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var userInfo: LoggedInUser
    private lateinit var nav_header_login: TextView
    private lateinit var nav_header_email: TextView
    private lateinit var navigationViewModel: NavigationViewModel
    private lateinit var navController: NavController
    // SERVICE
    var bound = false
    lateinit var serviceIntent: Intent
    lateinit var serviceConnection: ServiceConnection
    private var LOG_TAG = "MyGpsApp"
    private var interval : Long = 1000
    private var myServiceBinder : GpsServiceApp.MyBinder? = null
    var service: GpsServiceApp? = null

    //PERMISSIONS
    var MY_PERMISSIONS_REQUEST_ACC_FINE_LOC: Int = 1




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        userInfo = UserAccount.user
        val navigationViewModel = ViewModelProviders.of(this).get(NavigationViewModel::class.java)
        navigationViewModel.userAccount = userInfo

        initStartGPSService()
        initMenu()


    }

    private fun initStartGPSService(){
        //INITIALIZE SERVICE GPS
        serviceIntent = Intent(".GpsServiceApp")
        serviceIntent.setPackage("ru.vanchikov.fitnesinmylife")
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected")
                service = (binder as GpsServiceApp.MyBinder).service
                bound = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected")
                bound = false
            }
        }
        //END OF GPS SERVICE INIT.
        bindService(serviceIntent, serviceConnection, 0)

            startService(serviceIntent)


        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            }
            else {
        }*/
        Log.w(LOG_TAG,"SERVICE_STARTED")
        service?.task()

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

    override fun onStart() {
        super.onStart()
        //startService(serviceIntent)
        //service1 = GpsServiceApp().getInstance()
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
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onResume() {
        super.onResume()
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

    override fun onDestroy() {
        super.onDestroy()
        //stopService(serviceIntent)
        if (!bound) return
        Log.w(LOG_TAG,"SERVICE_STOPPED")
        unbindService(serviceConnection)
        bound = false
    }

}



