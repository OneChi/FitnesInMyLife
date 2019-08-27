package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments.MapPage

//TODO : ПЕРЕДЕЛАТЬ ВСЕ НАХРЕН -> Добавить VIEWMODEL для карты

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.vanchikov.fitnesinmylife.data.ViewModels.MapPageViewModel
import ru.vanchikov.fitnesinmylife.data.ViewModels.NavigationViewModel
import ru.vanchikov.fitnesinmylife.util.makeToastShort


class MapFragment : Fragment(), com.google.android.gms.maps.OnMapReadyCallback, View.OnClickListener {
    private lateinit var navigationViewModel : NavigationViewModel
    private val LOG_TAG = "MAP_FRAGMENT_LOG"
    private lateinit var mapViewModel : MapPageViewModel
    private lateinit var mapView: MapView
    private lateinit var  googleMap : GoogleMap

    // SERVICE
    lateinit var serviceIntent: Intent



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(ru.vanchikov.fitnesinmylife.R.layout.fragment_map_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val floatButtonLeft = view.findViewById<FloatingActionButton>(ru.vanchikov.fitnesinmylife.R.id.fb_GetMyLoc)
        val floatButtonRight = view.findViewById<FloatingActionButton>(ru.vanchikov.fitnesinmylife.R.id.fb_addWayOnMap)

        mapViewModel = ViewModelProviders.of(this).get(MapPageViewModel::class.java)
        navigationViewModel =
            activity.let { ViewModelProviders.of(it!!).get(NavigationViewModel::class.java) }



        floatButtonLeft.setOnClickListener(this)
        floatButtonRight.setOnClickListener(this)

        mapView = view.findViewById(ru.vanchikov.fitnesinmylife.R.id.map_view) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()


    }

    fun setCurrentWayOnMap(){
        if(navigationViewModel?.currentWayLoadState ==true && (navigationViewModel?.currentWayOnMap != null)) {
            mapViewModel.currentWayState = true
            mapViewModel.currentWay = navigationViewModel?.currentWayOnMap
            navigationViewModel!!.allWayFixByWayId(mapViewModel.currentWay!!.wayId).observe(this, Observer { mapViewModel.currentWayFixList = it
                Log.d(LOG_TAG,"${it.size}")

                if (mapViewModel.currentWayState ==true) {
                    val polyline = PolylineOptions()

                    for (a in mapViewModel.currentWayFixList) {
                        polyline.add(LatLng(a.latitude, a.longtitude))

                        googleMap.addMarker(MarkerOptions().position(LatLng(a.latitude,a.longtitude)).title("Marker at Me"))
                    }
                    var newLoc: Location? = mapViewModel.getLastLoc()
                    polyline.color(Color.MAGENTA)
                    polyline.width(20f)
                    googleMap.addPolyline(polyline)
                }
            })
            //navigationViewModel?.getAllFixes()!!.observe(this, Observer { Log.d(LOG_TAG,"${it.size}") })
            //navigationViewModel?.allWayFixByWayId( mapViewModel.currentWay!!.wayId)!!.observe(this, Observer { })


        } else if (navigationViewModel?.currentWayLoadState == true && (navigationViewModel?.currentWayOnMap == null)){
            navigationViewModel?.currentWayLoadState = false
        }
    }

    override fun onResume() {
        mapView.getMapAsync(this)//when you already implement OnMapReadyCallback in your fragment
        startGpsService()
        super.onResume()
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            ru.vanchikov.fitnesinmylife.R.id.fb_GetMyLoc ->{
                try {
                    if (ActivityCompat.checkSelfPermission(this.activity!!.baseContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        makeToastShort("нет прав доступа")
                        return
                    }

                    var newLoc: Location? = mapViewModel.getLastLoc() //(activity as NavigationActivity).service?.getLocation()!!//locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                    val me = LatLng(newLoc!!.latitude, newLoc!!.longitude)
                    googleMap.clear()

                    googleMap.addMarker(MarkerOptions().position(me).title("Marker at Me"))
                    //googleMap.moveCamera(CameraUpdateFactory.newLatLng(me))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me,5f), 1000*2, null)



                } catch (ex: Exception){
                    Log.w(LOG_TAG, ex.toString())
                }
            }
            ru.vanchikov.fitnesinmylife.R.id.fb_addWayOnMap ->{

            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        setCurrentWayOnMap()

        /*
     .add(LatLng(-5.0, -30.0)).add(LatLng(-5.0, -20.0))
    .add(LatLng(5.0, -20.0)).add(LatLng(5.0, -30.0))
    .color(Color.MAGENTA).width(1f)
    */
    }





override fun onStop() {
    super.onStop()
    if (mapViewModel.getBinder() != null) {
        activity!!.unbindService(mapViewModel.getServiceConnection())
    }
}


private fun startGpsService() {
    serviceIntent = Intent(".GpsServiceApp")
    serviceIntent.setPackage("ru.vanchikov.fitnesinmylife")
    activity!!.startService(serviceIntent)
    activity!!.bindService(serviceIntent,  mapViewModel.getServiceConnection(), Context.BIND_AUTO_CREATE)
    /*
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        }
        else {
        startService(serviceIntent)
    }*/
    Log.w(LOG_TAG,"SERVICE_STARTED")
}








/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 *
 *
 * See the Android Training lesson [Communicating with Other Fragments]
 * (http://developer.android.com/training/basics/fragments/communicating.html)
 * for more information.
 */

}
