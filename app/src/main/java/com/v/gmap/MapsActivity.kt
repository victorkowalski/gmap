package com.v.gmap

import android.graphics.Color
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.widget.NestedScrollView
import android.util.Log
import android.view.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.v.gmap.service.ApiFactory
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import android.view.MotionEvent
import com.v.gmap.data.Locations
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken




class MapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener,OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    private lateinit var map: GoogleMap
    private var locations = ArrayList<com.v.gmap.data.Locations>()
    /** map to store place names and locations */
    private val places = mapOf(
        "BRISBANE" to LatLng(37.420819,-122.085259),
        "MELBOURNE" to LatLng(-37.81319, 144.96298)
    )

    private var mBottomSheetBehavior1: BottomSheetBehavior<NestedScrollView>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        OnMapAndViewReadyListener(mapFragment, this)

        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottom_sheet1)
        //mBottomSheetBehavior1?.setPeekHeight(0);
        //mBottomSheetBehavior1?.setHideable(true)
        mBottomSheetBehavior1?.setState(BottomSheetBehavior.STATE_HIDDEN)
        //getLocations()
        val json = intent.getStringExtra("LOCATIONS")
        val gson = Gson()

        locations = Gson().fromJson<ArrayList<com.v.gmap.data.Locations>>(json, com.v.gmap.data.Location::class.java!!)
        Log.d("MainActivity ","response.errorBody().toString()")
        //val things = Gson().fromJson<ArrayList<com.v.gmap.data.Locations>>(json, com.v.gmap.data.Location!!)
    }
/*
    private fun getLocations(){
        var mode = "short"
        var ne_lat = "37.44446960614344"
        var ne_lng = "-122.06634320318699"
        var sw_lat = "37.3995197602049"
        var sw_lng = "-122.10165616124867"
        //val places = mapOf<String, LatLng>()

        val locationsService = ApiFactory.locations
        GlobalScope.launch(Dispatchers.Main) {
            val locationsRequest = locationsService.getLocations(
                mode, ne_lat, ne_lng, sw_lat, sw_lng
            )
            try {
                val response = locationsRequest.await()
                this@MapsActivity.locations = response as ArrayList<Locations>
                //test
                /*
                response.forEach { place ->
                    Log.d("place", place.coordinates.lat.toString())
                    Log.d("place", place.coordinates.lng.toString())

                }
                response.forEach { place ->
                    boundsBuilder.include(LatLng(place.coordinates.lat, place.coordinates.lng))
                    Log.d("MainActivity ","response.errorBody().toString()")
                }*/


                /*if(response.isSuccessful){
                    val movieResponse = response.body() //This is single object Tmdb Movie response
                    val popularMovies = movieResponse?.results // This is list of TMDB Movie
                }else{
                    Log.d("MainActivity ",response.errorBody().toString())
                }*/
            }catch (e: Exception){
                Log.d("MainActivity ","response.errorBody().toString()")
            }
        }
    }
*/
    override fun onMapReady(googleMap: GoogleMap?) {

        // return early if the map was not initialised properly
        map = googleMap ?: return

        // create bounds that encompass every location we reference
        val boundsBuilder = LatLngBounds.Builder()




      // include all places we have markers for on the map
      //test
/*      places.keys.map { place ->
          places.getValue(place)
          Log.d("place", place)
      }
*/
        locations.forEach { place ->
            boundsBuilder.include(LatLng(place.coordinates.lat, place.coordinates.lng))
            Log.d("MainActivity ","response.errorBody().toString()")
        }
      //places.keys.map { place -> boundsBuilder.include(places.getValue(place)) }

      //val sydney = LatLng(-34.0, 151.0)

      val bounds = boundsBuilder.build()

      with(map) {
          // Hide the zoom controls as the button panel will cover it.
          uiSettings.isZoomControlsEnabled = true

          // Setting an info window adapter allows us to change the both the contents and
          // look of the info window.
          //setInfoWindowAdapter(CustomInfoWindowAdapter())

          // Set listeners for marker events.  See the bottom of this class for their behavior.
          setOnMarkerClickListener(this@MapsActivity)

          //setOnInfoWindowClickListener(this@MarkerDemoActivity)
          //setOnMarkerDragListener(this@MarkerDemoActivity)
          //setOnInfoWindowCloseListener(this@MarkerDemoActivity)
          //setOnInfoWindowLongClickListener(this@MarkerDemoActivity)

          // Override the default content description on the view, for accessibility mode.
          // Ideally this string would be localised.
          setContentDescription("Map with lots of markers.")

          moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds(bounds, 50))
      }

      // Add lots of markers to the googleMap.
      addMarkersToMap()

  }

  /**
   * Show all the specified markers on the map
   */
  private fun addMarkersToMap() {

      val placeDetailsMap = mutableMapOf(
          // Uses a coloured icon
          "BRISBANE" to PlaceDetails(
              position = places.getValue("BRISBANE"),
              title = "Brisbane",
              snippet = "Population: 2,074,200",
              icon = BitmapDescriptorFactory
                  .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
          ),

          // Will create a draggable marker. Long press to drag.
          "MELBOURNE" to PlaceDetails(
              position = places.getValue("MELBOURNE"),
              title = "Melbourne",
              snippet = "Population: 4,137,400",
              draggable = true
          )

          // Use a vector drawable resource as a marker icon.
/*          "ALICE_SPRINGS" to PlaceDetails(
              position = places.getValue("ALICE_SPRINGS"),
              title = "Alice Springs",
              icon = vectorToBitmap(
                  R.drawable.ic_android, Color.parseColor("#A4C639"))
          ),
*/
          // More markers for good measure
/*
          "PERTH" to PlaceDetails(
              position = places.getValue("PERTH"),
              title = "Perth",
              snippet = "Population: 1,738,800"
          ),

          "ADELAIDE" to PlaceDetails(
              position = places.getValue("ADELAIDE"),
              title = "Adelaide",
              snippet = "Population: 1,213,000"
          )
*/
      )

      // add 4 markers on top of each other in Darwin with varying z-indexes
      /*
      (0 until 4).map {
          placeDetailsMap.put(
              "DARWIN ${it + 1}", PlaceDetails(
                  position = places.getValue("DARWIN"),
                  title = "Darwin Marker ${it + 1}",
                  snippet = "z-index initially ${it + 1}",
                  zIndex = it.toFloat()
              )
          )
      }
      */

      // place markers for each of the defined locations
      placeDetailsMap.keys.map {
          with(placeDetailsMap.getValue(it)) {
              map.addMarker(
                  com.google.android.gms.maps.model.MarkerOptions()
                      .position(position)
                      .title(title)
                      .snippet(snippet)
                      .icon(icon)
                      .infoWindowAnchor(infoWindowAnchorX, infoWindowAnchorY)
                      .draggable(draggable)
                      .zIndex(zIndex))

          }
      }
  }

  override fun dispatchTouchEvent(event: MotionEvent): Boolean {
      if (event.action == MotionEvent.ACTION_DOWN) {
          if (mBottomSheetBehavior1?.getState() === BottomSheetBehavior.STATE_EXPANDED) {

              val outRect = Rect()
              bottom_sheet1.getGlobalVisibleRect(outRect)

              if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt()))
                  mBottomSheetBehavior1?.setState(BottomSheetBehavior.STATE_COLLAPSED)
          }
      }

      return super.dispatchTouchEvent(event)
  }

  override fun onMarkerClick(marker: Marker): Boolean {

      if (mBottomSheetBehavior1?.getState() != BottomSheetBehavior.STATE_EXPANDED) {
          mBottomSheetBehavior1?.setState(BottomSheetBehavior.STATE_EXPANDED)
      } else if (mBottomSheetBehavior1?.getState() == BottomSheetBehavior.STATE_EXPANDED){
          mBottomSheetBehavior1?.setState(BottomSheetBehavior.STATE_COLLAPSED)
          mBottomSheetBehavior1?.setState(BottomSheetBehavior.STATE_EXPANDED)
      }
      return true
  }
}


/**
* This stores the details of a place that used to draw a marker
*/
class PlaceDetails(
  val position: LatLng,
  val title: String = "Marker",
  val snippet: String? = null,
  val icon: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker(),
  val infoWindowAnchorX: Float = 0.5F,
  val infoWindowAnchorY: Float = 0F,
  val draggable: Boolean = false,
  val zIndex: Float = 0F)
