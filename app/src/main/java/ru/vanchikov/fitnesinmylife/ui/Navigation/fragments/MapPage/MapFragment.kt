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
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_map_page.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.vanchikov.fitnesinmylife.data.ViewModels.MapPageViewModel
import ru.vanchikov.fitnesinmylife.data.ViewModels.NavigationViewModel
import ru.vanchikov.fitnesinmylife.data.model.UserWays
import ru.vanchikov.fitnesinmylife.data.model.WayFix
import ru.vanchikov.fitnesinmylife.util.makeToastShort


class MapFragment : Fragment(), com.google.android.gms.maps.OnMapReadyCallback, View.OnClickListener {
    private lateinit var navigationViewModel : NavigationViewModel
    private val LOG_TAG = "MapFragmentView"
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
        try {
            if(navigationViewModel?.currentWayLoadState ==true && (navigationViewModel?.currentWayOnMap != null)) {
                mapViewModel.currentWayState = true
                mapViewModel.currentWay = navigationViewModel?.currentWayOnMap
                navigationViewModel!!.getAllFixes().observe(this, Observer {
                    Log.d(LOG_TAG,"fixes all = ${it.size}")
                    for(a in it){
                        Log.d(LOG_TAG, "wayID = ${a.wayId} fixId = ${a.fixId}")
                    }

                })

                navigationViewModel!!.allWayFixByWayId(mapViewModel.currentWay!!.wayId).observe(this, Observer { mapViewModel.currentWayFixList = it
                    Log.d(LOG_TAG,"fixes = ${it.size}")

                    if (mapViewModel.currentWayState ==true) {
                        val polyline = PolylineOptions()
                        val FirstFix = LatLng(mapViewModel.currentWayFixList[0].latitude,mapViewModel.currentWayFixList[0].longtitude)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(FirstFix,30f),1000*2,null)
                        for (a in mapViewModel.currentWayFixList) {
                            polyline.add(LatLng(a.latitude, a.longtitude))

                            googleMap.addCircle(CircleOptions()
                                .center(LatLng(a.latitude, a.longtitude)).radius(0.5)
                                .fillColor(Color.BLUE).strokeColor(Color.DKGRAY)
                                .strokeWidth(1f))
                            //googleMap.addMarker(MarkerOptions().position(LatLng(a.latitude,a.longtitude)).title("Marker at Me"))
                        }
                        var newLoc: Location? = mapViewModel.getLocation()
                        polyline.color(Color.MAGENTA)
                        polyline.width(20f)
                        googleMap.addPolyline(polyline)

                    }
                })
            } else if (navigationViewModel?.currentWayLoadState == true && (navigationViewModel?.currentWayOnMap == null)){
                navigationViewModel?.currentWayLoadState = false
            }
        } catch (ex : java.lang.Exception){
            makeToastShort("${ex.toString()}")
            Log.d(LOG_TAG, ex.toString())
        }

    }

    override fun onResume() {
        startGpsService()
        Log.d(LOG_TAG,"StartService on resume")
        mapView.getMapAsync(this)
        super.onResume()
    }

    override fun onClick(v: View?) {
        if (ActivityCompat.checkSelfPermission(this.activity!!.baseContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            makeToastShort("нет прав доступа")
            return
        }
        when(v?.id) {
            ru.vanchikov.fitnesinmylife.R.id.fb_GetMyLoc ->{
                try {
                    if (ActivityCompat.checkSelfPermission(this.activity!!.baseContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        makeToastShort("нет прав доступа")
                        return
                    }
                    var newLoc: Location? = mapViewModel.getLocation()
                    val me = LatLng(newLoc!!.latitude, newLoc!!.longitude)
                    googleMap.clear()
                    googleMap.addMarker(MarkerOptions().position(me).title("Marker at Me"))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me,30f), 1000*2, null)
                    setCurrentWayOnMap()
                } catch (ex: Exception){
                    makeToastShort("${ex.toString()}")
                    Log.w(LOG_TAG, ex.toString())
                }
            }
            ru.vanchikov.fitnesinmylife.R.id.fb_addWayOnMap ->{
                try {
                    if(!mapViewModel.listeningWayState) {                                            /******************/

                        googleMap.clear()
                        var newLoc: Location? = mapViewModel.getLocation()
                        val me = LatLng(newLoc!!.latitude, newLoc!!.longitude)
                        googleMap.addMarker(MarkerOptions().position(me).title("Marker at Me"))                 //LISTENING WAY
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me,30f), 1000*2, null)
                        mapViewModel.startListeningLoc(
                            mapViewModel.MIN_TIME_BW_UPDATES,
                            mapViewModel.MIN_DISTANCE_CHANGE_FOR_UPDATES
                        )                                                                           /*******************/
                    } else {
                        mapViewModel.stopListeningLocUpdate()
                        var polygoneline = PolylineOptions()

                        var locData= mapViewModel.getLocationData()
                        mapViewModel.clearData()
                        for(a in locData)
                        {
                            polygoneline.add(LatLng(a.latitude, a.longitude))
                            //googleMap.addMarker(MarkerOptions().position(LatLng(a.latitude,a.longitude))
                            googleMap.addCircle(CircleOptions()
                                .center(LatLng(a.latitude, a.longitude)).radius(0.5)
                                .fillColor(Color.BLUE).strokeColor(Color.DKGRAY)
                                .strokeWidth(1f))

                        }

                        var newWay = UserWays(0,navigationViewModel.userAccount!!.userId,0,"newWay",locData[0].time)
                        try {
                            navigationViewModel.viewModelScope.launch {
                            var wayId: Long = navigationViewModel.insertWay(newWay)


                            var newFixList = emptyArray<WayFix>()
                            for (a in locData) {
                                var wayFix = mapViewModel.convertToWayFix(a, wayId)

                                navigationViewModel.insertWayFix(wayFix)
                            }

                        }
                        }catch (ex: java.lang.Exception){
                            makeToastShort("${ex.toString()}")
                            Log.d(LOG_TAG,"${ex.toString()}")
                        }
                    }
                } catch (ex: Exception){
                    makeToastShort("${ex.toString()}")
                    Log.w(LOG_TAG, ex.toString())
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        Log.d(LOG_TAG,"OnMapReady")
        googleMap = map
        //mapViewModel.currentPos = mapViewModel.getLocation()

        if(mapViewModel.currentPos == null) {
            val Moscow = LatLng(55.45, 37.37)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Moscow,5f))
        }  else {
            val myLoc = LatLng(mapViewModel.currentPos!!.latitude,mapViewModel.currentPos!!.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc))
            googleMap.addMarker(MarkerOptions().position(myLoc).title("Me"))
        }

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
