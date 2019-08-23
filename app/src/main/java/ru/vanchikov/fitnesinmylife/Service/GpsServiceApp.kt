package ru.vanchikov.fitnesinmylife.Service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.room.Room
import ru.vanchikov.fitnesinmylife.data.Database.UsersRoomDatabase
import java.util.*

class GpsServiceApp : Service(), SensorEventListener, LocationListener {

    /*
    *               VARIABLES
    *
    * */

    val LOG_TAG = "MyGpsService"
    var timer: Timer? = null
    var tTask: TimerTask? = null
    var interval: Long = 1000
    var binder = MyBinder()
    // INTERNAL STATE
    private var moving = false

    // Accelerometer-related

    private var iGpsSignificantReadings : Int = 0
    private var iAccelTimestamp: Long = 0
    private lateinit var mSensorManager: SensorManager

    // GPS-related
    private val LOCATION_BUFFER = 5
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener : LocationListener
    //private static final int BETWEEN_GPS = 30;
    private val TWO_MINUTES = 1000 * 60 * 2
    var currentBestLocation: Location? = null
    private var lGPSTimestamp: Long = 0
    private var mLocationManager: LocationManager? = null
    private val bestLocation: Location? = null
    private var pi: PendingIntent? = null
    //private var locations: CircularBuffer? = null
    private var cellOnly = false
    private var lowestAccuracy: Float = 0.toFloat()
    var canGetLocation = false
    val MIN_TIME_BW_UPDATES  : Long = 0
    val MIN_DISTANCE_CHANGE_FOR_UPDATES : Float = 0f

    // Singleton instance
    private var INSTANCE: GpsServiceApp? = null
        get()  {return INSTANCE ?: synchronized(this) {
            val instance = GpsServiceApp()
            INSTANCE = instance
            // return instance
            instance
        }
    }



    // IBinder
    inner class MyBinder : Binder() {
        val service: GpsServiceApp
            get() = this@GpsServiceApp
    }

    lateinit var dataSet : Array<Pair<Double,Double>>


    // ON CREATE
    override fun onCreate() {
        super.onCreate()
        startAccelerometer()
        Log.w(LOG_TAG, "SERVICE_CREATE")

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }

        // Acquire a reference to the system Location Manager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Register the listener with the Location Manager to receive location updates
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)


        } catch (exception:Exception)
        {
            Log.w(LOG_TAG,"${exception.toString()}" )
            //Toast.makeText(this,"${exception.toString()}",Toast.LENGTH_LONG).show()
        }


    }
    // ON DESTROY
    override fun onDestroy() {
        super.onDestroy()
        stopAccelerometer()
        Log.w(LOG_TAG, "SERVICE_DESTROY")
    }
    // ACCELEROMETER METHODS
    fun startAccelerometer() {
        iAccelTimestamp = System.currentTimeMillis()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopAccelerometer() {
        mSensorManager.unregisterListener(this)
    }

    fun task(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Permission is not granted",Toast.LENGTH_SHORT).show()
            return
        }

        Log.w(LOG_TAG, "SERVICE_INIT_TASK")


        currentBestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


        // if (locationManager != null) {
        // currentBestLocation = getLocation()
        //locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)}
        //   else {return}
        //currentBestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


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
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("GPS Enabled", "GPS Enabled");

                    if (locationManager != null) {
                        location = locationManager
                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        //updateGPSCoordinates();
                    }
                }

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("Network", "Network");

                        if (locationManager != null) {
                            location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            //updateGPSCoordinates();
                        }
                    }

                }
            }
        } catch (ex: SecurityException) {
            // e.printStackTrace();
            Log.w(LOG_TAG, "no rights to get location in getLocation func")
        }

        return location
    }

    override fun onSensorChanged(event: SensorEvent) {
        var accel:Double
        var x : Double
        var y: Double
        var z:Double
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) {
            Log.w(LOG_TAG, "not ACCELEROMETER")
            return
        }


        x = event.values[0].toDouble()
        y = event.values[1].toDouble()
        z = event.values[2].toDouble()

        //accel = abs(sqrt(x.pow(2.0) + y.pow(2.0) + z.pow(2.0)))

        accel = Math.abs(
            Math.sqrt(
                Math.pow(x, 2.0)
                        +
                        Math.pow(y, 2.0)
                        +
                        Math.pow(z, 2.0)
            )
        )
        // Was 0.6. Lowered to 0.3 (plus gravity) to account for smooth motion from Portland Streetcar
        if (accel >10.3)
        {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                Log.w(LOG_TAG, "no permissions")
                return
            }
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)

                var longtitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).longitude
                var latitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).latitude
                var pair = Pair<Double,Double>(first = longtitude, second = latitude)

                dataSet += pair

                // Log.w(LOG_TAG,"++++++++++++++++++")
                // Log.w(LOG_TAG,  " PROVIDER= ${locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).provider}")
                // Log.w(LOG_TAG, "longitude ${locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).longitude}")
                // Log.w(LOG_TAG, "latitude ${locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).latitude}")
            } catch (exception:Exception)
            {
                Log.w(LOG_TAG,"${exception.toString()}" )
            }

        }


        locationManager.removeUpdates(this)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    fun getData(): Array<Pair<Double,Double>>{
        return dataSet
    }

    fun clearData(){

        //for (i in dataSet.size .. 0 ){
        //  dataSet.drop(i)
        dataSet.dropLast(dataSet.size)
        //}
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.w(LOG_TAG, "ON START COMMAND $startId")

        return super.onStartCommand(intent, flags, startId)

    }

    override fun onBind(intent: Intent): IBinder {
        Log.d(LOG_TAG, "MyService onBind")
        return binder
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
        iGpsSignificantReadings++
        Log.w(LOG_TAG,"LOCATION CHANGED PROVIDER= ${location?.provider.toString()}")

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