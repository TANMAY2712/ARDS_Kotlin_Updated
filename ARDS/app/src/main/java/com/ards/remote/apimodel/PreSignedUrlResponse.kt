package com.ards.remote.apimodel

class PreSignedUrlResponse(
    val presigned_url: String,
    val resource_id: String,
    val statusCode: Int
)