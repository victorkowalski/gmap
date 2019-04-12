package com.v.gmap.service

import com.v.gmap.data.Locations
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationsApi {

    @GET("/locations")
    fun getLocations(
        @Query("mode") mode: String,
        @Query("ne_lat") ne_lat: String,
        @Query("ne_lng") ne_lng: String,
        @Query("sw_lat") sw_lat: String,
        @Query("sw_lng") sw_lng: String
    ): Deferred<List<Locations>>

}
/*
    @GET("json")
    fun requestCityAddressByName(
            @Query("address") address: String
    ): Single<LocationResponse>
 */
/*
?mode=short&ne_lat=37.44446960614344&ne_lng=-122.06634320318699&sw_lat=37.3995197602049&sw_lng=-122.10165616124867
 */