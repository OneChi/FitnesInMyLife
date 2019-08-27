package ru.vanchikov.fitnesinmylife.data.ViewModels

import android.app.Application
import android.content.ComponentName
import android.content.ServiceConnection
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.vanchikov.fitnesinmylife.Service.GpsServiceApp
import ru.vanchikov.fitnesinmylife.Service.GpsServiceApp.MyBinder
import ru.vanchikov.fitnesinmylife.data.Repository.ServiceRepository
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.WayFix

// Request permissions = https://developer.android.com/training/permissions/requesting.html


class MapPageViewModel(application: Application) : AndroidViewModel(application){
    private val LOG_TAG="MapViewModel"
    val MIN_TIME_BW_UPDATES  : Long = 1000*3
    val MIN_DISTANCE_CHANGE_FOR_UPDATES : Float = 1f
    private lateinit var  service: GpsServiceApp
    private lateinit var  repository : ServiceRepository

    var currentWayState : Boolean = false
    var currentWay : UserWays? = null
    lateinit var currentWayFixList : List<WayFix>

    private val mBinder = MutableLiveData<GpsServiceApp.MyBinder>()

    // Keeping this in here because it doesn't require a context
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, iBinder: IBinder) {
            Log.d(LOG_TAG, "ServiceConnection: connected to service.")
            // We've bound to MyService, cast the IBinder and get MyBinder instance
            val binder = iBinder as GpsServiceApp.MyBinder
            service = binder.service
            repository = ServiceRepository(service)
            mBinder.postValue(binder)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d(LOG_TAG, "ServiceConnection: disconnected from service.")
            mBinder.postValue(null)
        }
    }

    fun convertToWayFix(loc : Location): WayFix{
        var fix : WayFix = WayFix(
            wayId = 0,
            altitude = loc.altitude,
            latitude = loc.latitude,
            longtitude = loc.longitude,
            time = loc.time,
            speed = loc.speed,
            provider = loc.provider,
            fixId = 0
        )
        return fix
    }


    fun startListeningLoc(timeBwUpdates : Long, distanceBwUpdates: Float){
        repository.startListeningLoc(timeBwUpdates,distanceBwUpdates)
    }

    fun stopListeningLocUpdate(){
        repository.stopListeningLocUpdate()
    }

    fun getLocationData() : Array<Location>{
        return repository.getLocationData()
    }

    fun clearData(){
        repository.clearData()
    }


    fun getLastLoc(): Location? {
        return repository.getLastKnownLoc()
    }

    fun getServiceConnection(): ServiceConnection {
        return serviceConnection
    }

    fun getBinder(): LiveData<MyBinder> {
        return mBinder
    }


}