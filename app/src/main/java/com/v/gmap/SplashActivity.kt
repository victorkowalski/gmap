package com.v.gmap

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.v.gmap.data.Locations
import com.v.gmap.service.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.IOException
import java.util.ArrayList
import com.google.gson.reflect.TypeToken




class SplashActivity : AppCompatActivity(){
    var isStarted = false
    var progressStatus = 0
    var handler: Handler? = null
    var secondaryHandler: Handler? = Handler()
    var primaryProgressStatus = 0
    var secondaryProgressStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        getLocations()
    }

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
                //this@MapsActivity.locations = response as ArrayList<Locations>

                //val json = Gson().toJson(response)
                //runActivity(Gson().toJson(response))
                runActivity(response as Parcelable)

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
                progress.visibility = View.GONE
                progress_text.text = "Не получили данные, грузим из локального JSON"
                var json = loadJSONFromAsset()

                val enums = Gson().fromJson(json, Array<Locations>::class.java)
                /*val collectionType = object : TypeToken<Collection<Locations>>() {

                }.type*/
                //val enums = Gson().fromJson(json, collectionType)
                //var locations = Gson().fromJson<ArrayList<Locations>>(json, com.v.gmap.data.Location::class.java!!)

                runActivity(loadJSONFromAsset())
                Log.d("MainActivity ","response.errorBody().toString()")
            }
        }
    }


    private fun runActivity(obj: Parcelable){
        val intent = Intent(applicationContext, MapsActivity::class.java)
        //intent.putExtra("LOCATIONS", json)
        intent.putExtra("LOCATIONS", obj);

        startActivity(intent)
        finish()
    }

    private fun runActivity(json: String?){
        val intent = Intent(applicationContext, MapsActivity::class.java)
        //intent.putExtra("LOCATIONS", json)
        intent.putExtra("LOCATIONS", json);

        startActivity(intent)
        finish()
    }
    /*
    fun loadJSONFromAsset(): String {
        try {
            val inputStream: InputStream = assets.open("news_data_file.json")
            val inputString = inputStream.bufferedReader().use { it.readText() }
            Log.d(TAG, inputString)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
        return inputString
    }*/

    fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val ins = assets.open("locations.json")
            val size = ins.available()
            val buffer = ByteArray(size)
            ins.read(buffer)
            ins.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler(Handler.Callback {
            if (isStarted) {
                progressStatus++
            }
            progressBarHorizontal.progress = progressStatus
            textViewHorizontalProgress.text = "${progressStatus}/${progressBarHorizontal.max}"
            handler?.sendEmptyMessageDelayed(0, 100)

            true
        })

        handler?.sendEmptyMessage(0)


        btnProgressBarSecondary.setOnClickListener {
            primaryProgressStatus = 0
            secondaryProgressStatus = 0

            Thread(Runnable {
                while (primaryProgressStatus < 100) {
                    primaryProgressStatus += 1

                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    startSecondaryProgress()
                    secondaryProgressStatus = 0

                    secondaryHandler?.post {
                        progressBarSecondary.progress = primaryProgressStatus
                        textViewPrimary.text = "Complete $primaryProgressStatus% of 100"

                        if (primaryProgressStatus == 100) {
                            textViewPrimary.text = "All tasks completed"
                        }
                    }
                }
            }).start()
        }

    }


    fun startSecondaryProgress() {
        Thread(Runnable {
            while (secondaryProgressStatus < 100) {
                secondaryProgressStatus += 1

                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                secondaryHandler?.post {
                    progressBarSecondary.setSecondaryProgress(secondaryProgressStatus)
                    textViewSecondary.setText("Current task progress\n$secondaryProgressStatus% of 100")

                    if (secondaryProgressStatus == 100) {
                        textViewSecondary.setText("Single task complete.")
                    }
                }
            }
        }).start()
    }

    fun horizontalDeterminate(view: View) {
        isStarted = !isStarted
    }
*/
}