package com.v.gmap.service

import com.v.gmap.app.AppConstants

object ApiFactory{

    val locations : LocationsApi = RetrofitFactory.retrofit(AppConstants.BASE_URL).create(LocationsApi::class.java)

}