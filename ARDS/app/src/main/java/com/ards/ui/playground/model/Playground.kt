package com.ards.model

import com.google.common.base.Strings

data class Playground(
    val id: String,
    val train_name: String,
    val object_url: String,
    val fault_request: String,
    val thumbnail_url: String,
    val train_number: String,
    val station: String,
    val side: String,
    val date: String
)
