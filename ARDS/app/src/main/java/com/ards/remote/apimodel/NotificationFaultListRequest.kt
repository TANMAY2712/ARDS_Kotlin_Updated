package com.ards.remote.apimodel

data class NotificationFaultListRequest(
    val APIKey: String,
    val TrainName: String,
    val PartName: String,
    val PageNo: Int,
    val NotificationId: Int
)
