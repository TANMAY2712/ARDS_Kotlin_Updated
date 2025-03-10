package com.ards.ui.history.model

data class Recent(
    val trainName: String,
    val trainNo: String,
    val from: String,
    val departure: String,
    val to: String,
    val arrival: String,
    val faults: Int
)
