package ru.vanchikov.fitnesinmylife.data.Repository

import android.location.Location
import androidx.lifecycle.LiveData
import ru.vanchikov.fitnesinmylife.Service.GpsServiceApp


class ServiceRepository(val service: GpsServiceApp) {



    fun startListeningLoc(timeBwUpdates : Long, distanceBwUpdates: Float){
        service.startListeningData(timeBwUpdates,distanceBwUpdates)
    }

    fun stopListeningLocUpdate(){
        service.stopListeningData()

    }

    fun getLocationData() : Array<Location>{
       return service.getData()
    }

    fun clearData(){
        service.clearData()
    }

    fun getLiveData(): LiveData<Array<Location>> {
       return service.getLiveData()
    }

    fun clearLiveData(){
       service.clearData()
    }

    fun getLastKnownLoc() : Location? {
        return service.getLocation()
    }

}