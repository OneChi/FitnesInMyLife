package ru.vanchikov.fitnesinmylife.Service

//TODO : ПЕРЕДЕЛАТЬ ВСЕ НАХРЕН В СЕРВИСЕ


import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat

class GpsServiceApp: Service(),LocationListener {

    /*
    *               VARIABLES
    *
    * */

    val LOG_TAG = "MyGpsService"
    var binder = MyBinder()
    // GPS-related
    private val LOCATION_BUFFER = 5
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener : LocationListener
    private val TWO_MINUTES = 1000 * 60 * 2
    var currentBestLocation: Location? = null
    var canGetLocation = false
    val MIN_TIME_BW_UPDATES  : Long = 1000*3
    val MIN_DISTANCE_CHANGE_FOR_UPDATES : Float = 1f
    var dataSet : Array<Location> = emptyArray<Location>()
    private var currentProvider : Int = 0 // 0 - null 1 - GPS 2 - Network
    private var listeningLocationState : Boolean = false // 0 - not listening 1 - listening

    // IBinder
    inner class MyBinder : Binder() {
        val service: GpsServiceApp
            get() = this@GpsServiceApp
    }
    override fun onBind(intent: Intent): IBinder {
        Log.d(LOG_TAG, "MyService onBind")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.w(LOG_TAG, "SERVICE_CREATE")

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted // TODO: Сделать окно "дайте разрешения"
        }
        // Acquire a reference to the system Location Manager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w(LOG_TAG, "SERVICE_DESTROY")
    }

    fun task(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Permission is not granted",Toast.LENGTH_SHORT).show()
            return
        }
        Log.w(LOG_TAG, "SERVICE_INIT_TASK")
        currentBestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        Toast.makeText(this,"Lat = ${currentBestLocation?.latitude} lng = ${currentBestLocation?.longitude}",Toast.LENGTH_SHORT).show()
    }

    fun  getLocation() : Location? {
        var location : Location? = null
        try {
            // Acquire a reference to the system Location Manager
            //locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


            // getting GPS status
            var isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            var isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // location service disabled
            } else {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this)



                    Log.d(LOG_TAG, "GPS Enabled")

                    if (locationManager != null) {
                        location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        //updateGPSCoordinates()
                    }
                }

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d(LOG_TAG, "Network")

                        if (locationManager != null) {
                            location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            //updateGPSCoordinates();
                        }
                    }

                }
            }
        } catch (ex: SecurityException) {
            // e.printStackTrace();
            Log.w(LOG_TAG, "no rights to get location in getLocation func")
        }
        locationManager.removeUpdates(this)
        return location
    }
    //MIN_TIME_BW_UPDATES,
    //MIN_DISTANCE_CHANGE_FOR_UPDATES
    fun startListeningData(timeBwUpdates : Long, distanceBwUpdates: Float) {
        // getting GPS status
        try {
            var isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting network status
            var isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                // location service disabled
            } else {
                this.canGetLocation = true

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {

                   locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, timeBwUpdates,
                        distanceBwUpdates, this
                    )

                    Log.d(LOG_TAG, "GPS Enabled")

                } else if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        timeBwUpdates,
                        distanceBwUpdates, this
                    )

                    Log.d(LOG_TAG, "Network")


                } else {
                    throw Exception("unnable to connect with location")
                }
            }
        } catch(ex: SecurityException) {
            // e.printStackTrace();
            Log.w(LOG_TAG, "no rights to get location in getLocation func")
        }
        listeningLocationState = true
    }

    fun stopListeningData(){
        listeningLocationState = false
        locationManager.removeUpdates(this)
    }

    fun getData(): Array<Location>{
        return dataSet
    }

    fun clearData(){
       dataSet = emptyArray<Location>()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //Log.w(LOG_TAG, "ON START COMMAND $startId")

        return super.onStartCommand(intent, flags, startId)

    }

    override fun onProviderDisabled(provider: String?) {
        Log.w(LOG_TAG,"PROVIDER ${provider.toString()} DISABLED")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.w(LOG_TAG,"PROVIDER ${provider.toString()} ENABLED")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.w(LOG_TAG,"PROVIDER ${provider.toString()} STATUS CHANGED to $status")
    }

    override fun onLocationChanged(location: Location?) {
        Log.w(LOG_TAG,"LOCATION CHANGED PROVIDER= ${location?.provider.toString()}")
        if (listeningLocationState && location!=null) {
            dataSet = dataSet.plus(location!!)

        }
    }

}




/*
override fun onGpsStatusChanged(event: Int) {

}
*/


/*
       // Define a listener that responds to location updates
       locationListener = object : LocationListener {

           override fun onLocationChanged(location: Location) {
               // Called when a new location is found by the network location provider.
               //makeUseOfNewLocation(location)
               //Log.w("myApp","achive new location")
               iGpsSignificantReadings++
           }

           override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
           }

           override fun onProviderEnabled(provider: String) {
           }

           override fun onProviderDisabled(provider: String) {
           }
       }
       */