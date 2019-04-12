package com.v.gmap.service

import com.v.gmap.app.AppConstants
import com.v.gmap.data.Location
import com.v.gmap.data.Locations
import com.v.gmap.service.RetrofitFactory


object ApiFactory{

    val locations : LocationsApi = RetrofitFactory.retrofit(AppConstants.BASE_URL).create(LocationsApi::class.java)

    //val location : LocationApi = RetrofitFactory.retrofit(AppConstants.BASE_URL).create(LocationApi::class.java)

    /*
    https://api.tau.green/v1/locations
    ?mode=short
    &ne_lat=37.44446960614344
    &ne_lng=-122.06634320318699
    &sw_lat=37.3995197602049
    &sw_lng=-122.10165616124867
     */
}