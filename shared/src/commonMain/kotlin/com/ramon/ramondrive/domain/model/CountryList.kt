package com.ramon.ramondrive.domain.model

data class CountryList(
    val countries: List<Country>,
    val detected: Country?
)
