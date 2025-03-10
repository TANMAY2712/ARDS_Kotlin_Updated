package com.ards.remote.apimodel

data class NotificationListRequest(
    val APIKey: String,
    val TrainName: String,
    val PageNo: Int,
    val UserId: Int
)
