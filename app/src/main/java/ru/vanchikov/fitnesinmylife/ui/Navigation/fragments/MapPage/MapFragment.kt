package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments.MapPage


import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_map_page.view.*
import ru.vanchikov.fitnesinmylife.R
import ru.vanchikov.fitnesinmylife.ui.Navigation.NavigationActivity
import ru.vanchikov.fitnesinmylife.util.makeToastShort
import java.lang.Exception

class MapFragment : Fragment(), com.google.android.gms.maps.OnMapReadyCallback, View.OnClickListener {

    private val LOG_TAG = "MAP_FRAGMENT_LOG"

    private lateinit var mapView: MapView
    private lateinit var  googleMap : GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(ru.vanchikov.fitnesinmylife.R.layout.fragment_map_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val floatButtonLeft = view.findViewById<FloatingActionButton>(R.id.fb_GetMyLoc)
        val floatButtonRight = view.findViewById<FloatingActionButton>(R.id.fb_addWayOnMap)

        floatButtonLeft.setOnClickListener(this)
        floatButtonRight.setOnClickListener(this)

        mapView = view.findViewById(ru.vanchikov.fitnesinmylife.R.id.map_view) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        mapView.getMapAsync(this)//when you already implement OnMapReadyCallback in your fragment

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.fb_GetMyLoc ->{
                try {
                    if (ActivityCompat.checkSelfPermission(this.activity!!.baseContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        makeToastShort("не работает")
                        return;
                    }
                    var newLoc: Location = (activity as NavigationActivity).service?.getLocation()!!//locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                    val me = LatLng(newLoc.latitude, newLoc.longitude)
                    googleMap.clear()

                    googleMap.addMarker(MarkerOptions().position(me).title("Marker at Me"))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(me))
                } catch (ex: Exception){
                    Log.w(LOG_TAG, ex.localizedMessage)
                }
            }
            R.id.fb_addWayOnMap ->{

            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun initilizeMap() {

        /*
            val googlePlex = CameraPosition.builder()
                .target(LatLng(37.4219999, -122.0862462))
                .zoom(10f)
                .bearing(0f)
                .tilt(45f)
                .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null)

            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(37.4219999, -122.0862462))
                    .title("Spider Man")
            )


            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(37.4629101, -122.2449094))
                    .title("Iron Man")
                    .snippet("His Talent : Plenty of money")
            )

            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(37.3092293, -122.1136845))
                    .title("Captain America")
            )
        }

*/
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
