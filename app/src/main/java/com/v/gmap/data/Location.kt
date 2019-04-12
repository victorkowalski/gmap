package com.v.gmap.data

data class Location(
    val avatar: Any,
    val city: String,
    val company_name: String,
    val connectors: List<Connector>,
    val coordinates: Coordinates,
    val country: String,
    val created_at: String,
    val id: String,
    val intervals: Intervals,
    val manager_id: String,
    val name: String,
    val opening_hours: OpeningHours,
    val photos: List<Photo>,
    val postcode: String,
    val rating: Int,
    val street: String
)