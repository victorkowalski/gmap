package com.v.gmap

import android.graphics.Rect
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.v.gmap.data.Locations
import com.v.gmap.service.ApiFactory
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_maps.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener,OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    private lateinit var map: GoogleMap
    private var locations = ArrayList<Locations>()

    private var mBottomSheetBehavior: BottomSheetBehavior<NestedScrollView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        OnMapAndViewReadyListener(mapFragment, this)

        mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet1)
        mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_HIDDEN)

        val json = intent.getStringExtra("LOCATIONS")

        val listType = object : TypeToken<ArrayList<Locations>>() { }.type
        locations = Gson().fromJson<ArrayList<Locations>>(json, listType)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        val boundsBuilder = LatLngBounds.Builder()

        locations.forEach { place ->
            boundsBuilder.include(LatLng(place.coordinates.lat, place.coordinates.lng))
            Log.d("MainActivity ","response.errorBody().toString()")
        }

      val bounds = boundsBuilder.build()

      with(map) {
          uiSettings.isZoomControlsEnabled = true
          setOnMarkerClickListener(this@MapsActivity)
          setContentDescription("Map with lots of markers.")

          moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds(bounds, 150))
      }
      addMarkersToMap()
  }

  private fun addMarkersToMap() {

      locations.forEach { place ->
          map.addMarker(
              com.google.android.gms.maps.model.MarkerOptions()
                  .position(LatLng(place.coordinates.lat,place.coordinates.lng))
                  .title(place.id)
          )
      }
  }

  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
      if (event.action == MotionEvent.ACTION_DOWN) {
          if (mBottomSheetBehavior?.getState() === BottomSheetBehavior.STATE_EXPANDED) {

              val outRect = Rect()
              bottom_sheet1.getGlobalVisibleRect(outRect)

              if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt()))
                  mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
          }
      }

      return super.dispatchTouchEvent(event)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
      var id = marker.title

      if (mBottomSheetBehavior?.getState() != BottomSheetBehavior.STATE_EXPANDED) {
          mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
      } else if (mBottomSheetBehavior?.getState() == BottomSheetBehavior.STATE_EXPANDED){
          mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
          mBottomSheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
      }
      getLocation(id)
      return true
  }

    private fun getLocation(id: String){

        val locationsService = ApiFactory.locations
        GlobalScope.launch(Dispatchers.Main) {
            val locationRequest = locationsService.getLocation(id)
            try {
                val response = locationRequest.await()
                var location = response
                bottom_sheet1.text1.text = id
                bottom_sheet1.text2.text = location.city
                bottom_sheet1.text3.text = location.country
                bottom_sheet1.text4.text = location.company_name
            }catch (e: Exception){
                Log.d("MapsActivity ","error")
            }
        }
    }

}
