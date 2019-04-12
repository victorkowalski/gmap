package com.v.gmap.app

object AppConstants{
    const val JSON_PLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com"
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_PHOTO_URL = " http://image.tmdb.org/t/p/w185"
    //var tmdbApiKey = BuildConfig.TMDB_API_KEY

    const val BASE_URL = "https://api.tau.green/v1/"
    //https://api.tau.green/v1/locations?mode=short&ne_lat=37.44446960614344&ne_lng=-122.06634320318699&sw_lat=37.3995197602049&sw_lng=-122.10165616124867
    //https://api.tau.green/v1/locations/:locationId
}

/*
API Key (v3 auth)
72e7014245422dbaaa1528ae06805424
API Read Access Token (v4 auth)
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3MmU3MDE0MjQ1NDIyZGJhYWExNTI4YWUwNjgwNTQyNCIsInN1YiI6IjVjNzEyMzU1YzNhMzY4NWE0OTEyNDdmYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.qOMGjm6kEaHqW0Q2mldq9EpPuR62_o2be5rBg3gVm9A
Example API Request
https://api.themoviedb.org/3/movie/550?api_key=72e7014245422dbaaa1528ae06805424

 */