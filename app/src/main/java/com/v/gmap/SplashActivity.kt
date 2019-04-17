package com.v.gmap

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.Gson
import com.v.gmap.service.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class SplashActivity : AppCompatActivity(){

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

        val locationsService = ApiFactory.locations
        GlobalScope.launch(Dispatchers.Main) {
            val locationsRequest = locationsService.getLocations(
                mode, ne_lat, ne_lng, sw_lat, sw_lng
            )
            try {
                val response = locationsRequest.await()
                val json = Gson().toJson(response)

                runActivity(json)

            }catch (e: Exception){
                /*
                progress.visibility = View.GONE
                progress_text.text = "Не получили данные, грузим из локального JSON"
                var json = loadJSONFromAsset()

                val locations = Gson().fromJson(json, Array<Locations>::class.java)
                         runActivity(loadJSONFromAsset())
                */
                Log.d("MainActivity ","response.errorBody().toString()")
            }
        }
    }


    private fun runActivity(obj: Parcelable){
        val intent = Intent(applicationContext, MapsActivity::class.java)
        intent.putExtra("LOCATIONS", obj);

        startActivity(intent)
        finish()
    }

    private fun runActivity(json: String?){
        val intent = Intent(applicationContext, MapsActivity::class.java)
        intent.putExtra("LOCATIONS", json);

        startActivity(intent)
        finish()
    }

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
}