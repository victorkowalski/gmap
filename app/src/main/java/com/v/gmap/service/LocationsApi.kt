package com.v.gmap.service

import com.v.gmap.data.Locations
import com.v.gmap.data.Location
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationsApi {

    @GET("/v1/locations")
    fun getLocations(
        @Query("mode") mode: String,
        @Query("ne_lat") ne_lat: String,
        @Query("ne_lng") ne_lng: String,
        @Query("sw_lat") sw_lat: String,
        @Query("sw_lng") sw_lng: String
    ): Deferred<List<Locations>>

    @GET("/v1/locations/{id}")
    fun getLocation(
        @Path("id") id: String
    ): Deferred<Location>
}
