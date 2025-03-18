package com.ards.remote.apimodel
data class ModelInferenceRequest(
    val train_no: String,
    val station: String,
    val rec_side: String,
    val s3_path: String
)