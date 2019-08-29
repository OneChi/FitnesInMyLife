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
    lateinit var  service: GpsServiceApp
    private lateinit var  repository : ServiceRepository

    var listeningWayState = false
    var currentWayState : Boolean = false //загружен или нет
    var currentWay : UserWays? = null
    lateinit var currentWayFixList : List<WayFix>

    var currentPos : Location? = null
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

    fun convertToWayFix(loc : Location , wayId: Long): WayFix{
        var fix : WayFix = WayFix(
            wayId = wayId,
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
        listeningWayState = true
    }

    fun stopListeningLocUpdate(){
        repository.stopListeningLocUpdate()
        listeningWayState = false
    }

    fun getLocationData() : Array<Location>{
        return repository.getLocationData()
    }

    fun clearData(){
        repository.clearData()
    }

    fun getLiveData(): LiveData<Array<Location>> {
        return repository.getLiveData()
    }

    fun clearLiveData(){
        repository.clearData()
    }

    fun getLocation(): Location? {
        return repository.getLastKnownLoc()
    }

    fun getServiceConnection(): ServiceConnection {
        return serviceConnection
    }

    fun getBinder(): LiveData<MyBinder> {
        return mBinder
    }


}