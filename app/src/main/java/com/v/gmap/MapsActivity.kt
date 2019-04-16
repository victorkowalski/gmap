package com.v.gmap

import android.graphics.Color
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

class MapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener,OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    private lateinit var map: GoogleMap
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
        //mBottomSheetBehavior1?.setHideable(true) //Important to add
        mBottomSheetBehavior1?.setState(BottomSheetBehavior.STATE_HIDDEN); //Important to add


    }

    override fun onMapReady(googleMap: GoogleMap?) {
        var mode = "short"
        var ne_lat = "37.44446960614344"
        var ne_lng = "-122.06634320318699"
        var sw_lat = "37.3995197602049"
        var sw_lng = "-122.10165616124867"

        val locationsService = ApiFactory.locations
        GlobalScope.launch(Dispatchers.Main) {
            val locationsRequest = locationsService.getLocations(
                mode, ne_lat, ne_lng, sw_lat, sw_lng
            )
            try {
                val response = locationsRequest.await()
                Log.d("MainActivity ","response.errorBody().toString()")
                /*if(response.isSuccessful){
                    val movieResponse = response.body() //This is single object Tmdb Movie response
                    val popularMovies = movieResponse?.results // This is list of TMDB Movie
                }else{
                    Log.d("MainActivity ",response.errorBody().toString())
                }*/
            }catch (e: Exception){

            }
        }

        // return early if the map was not initialised properly
        map = googleMap ?: return

        // create bounds that encompass every location we reference
        val boundsBuilder = LatLngBounds.Builder()
        // include all places we have markers for on the map
        //test
        places.keys.map { place ->
            places.getValue(place)
            Log.d("place", place)
        }

        places.keys.map { place -> boundsBuilder.include(places.getValue(place)) }

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

            moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds(bounds, 250))
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

            // Uses a custom icon with the info window popping out of the center of the icon.
  /*          "SYDNEY" to PlaceDetails(
                position = places.getValue("SYDNEY"),
                title = "Sydney",
                snippet = "Population: 4,627,300",
                icon = BitmapDescriptorFactory.fromResource(R.drawable.arrow),
                infoWindowAnchorX = 0.5f,
                infoWindowAnchorY = 0.5f
            ),
*/
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

        // Creates a marker rainbow demonstrating how to create default marker icons of different
        // hues (colors).
       /*
        val numMarkersInRainbow = 12
        (0 until numMarkersInRainbow).mapTo(markerRainbow) {
            map.addMarker(MarkerOptions().apply{
                position(LatLng(
                    -30 + 10 * Math.sin(it * Math.PI / (numMarkersInRainbow - 1)),
                    135 - 10 * Math.cos(it * Math.PI / (numMarkersInRainbow - 1))))
                title("Marker $it")
                icon(
                    BitmapDescriptorFactory.defaultMarker((it * 360 / numMarkersInRainbow)
                        .toFloat()))
                flat(flatBox.isChecked)
                rotation(rotationBar.progress.toFloat())
            })
        }*/
    }

    override fun onMarkerClick(marker : Marker): Boolean {

        if(mBottomSheetBehavior1?.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior1?.setState(BottomSheetBehavior.STATE_EXPANDED);
            //mButton1.setText(R.string.collapse_button1);
        }
        else {
            mBottomSheetBehavior1?.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //mButton1.setText(R.string.button1);STATE_HIDDEN
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

//*****************************************
/*
LayoutInflater inflater = LayoutInflater.from(this);
final Dialog dialog1 = new Dialog(this);
Window window = dialog1.getWindow();
WindowManager.LayoutParams wlp = window.getAttributes();
wlp.gravity = Gravity.BOTTOM; // Here you can set window top or bottom
wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
window.setAttributes(wlp);
View view1 = inflater.inflate(R.layout.openYourWindow, null);
dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
dialog1.setContentView(view1);
dialog1.show();
 */