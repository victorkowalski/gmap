package com.v.gmap.data

data class Connector(
    val current: String,
    val fee_per_minute: FeePerMinute,
    val id: String,
    val location_id: String,
    val name: String,
    val power: Double,
    val station_id: String,
    val status: String
)