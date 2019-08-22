package ru.vanchikov.fitnesinmylife.ui.Navigation.fragments.MapPage


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapPage : Fragment(), com.google.android.gms.maps.OnMapReadyCallback {

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

        mapView = view.findViewById(ru.vanchikov.fitnesinmylife.R.id.map_view) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)//when you already implement OnMapReadyCallback in your fragment

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


}
