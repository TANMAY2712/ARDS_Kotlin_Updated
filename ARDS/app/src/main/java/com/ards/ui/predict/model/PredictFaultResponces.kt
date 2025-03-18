package com.ards.ui.predict.model

import org.json.JSONObject

data class PredictFaultResponces(val data: PredictFaultData, val statusCode: Int) {
    constructor(json: JSONObject) : this(
        PredictFaultData(json.optJSONObject("data") ?: JSONObject()),
        json.optInt("statusCode", 0) // ✅ Default value if statusCode is missing
    )
}

data class PredictFaultData(
    val faults: List<PredictFault>,
    val outputUrl: String?,
    val progress: String?
) {
    constructor(json: JSONObject) : this(
        json.optJSONArray("faults")?.let { array ->
            List(array.length()) { index -> PredictFault(array.getJSONObject(index)) }
        } ?: emptyList(), // ✅ Handles missing "faults"
        json.optString("output_url", null), // ✅ Extract output_url
        json.optString("progress", null) // ✅ Extract progress status
    )
}

data class PredictFault(
    val coachNumber: String?,
    val coachPosition: Int?,
    val faultInfo: String,
    val faultProbability: Double,
    val uploadedTime: String,
    val imageUrl: String?,
    val partName: String?,
    val recordedSide: String?,
    val stationCode: String?,
    val stationName: String?,
    val trainName: String?,
    val trainNumber: String?,
    val faultTimestamp: String? // ✅ New field added for fault timestamp
) {
    constructor(json: JSONObject) : this(
        json.optString("coach_number", null),
        if (json.has("coach_position")) json.getInt("coach_position") else null,
        json.optString("fault_info", "Unknown"),
        json.optDouble("fault_probability", 0.0),
        json.optString("uploaded_time", ""),
        json.optString("image_url", null), // ✅ Extract image_url
        json.optString("part_name", null),
        json.optString("recorded_side", null),
        json.optString("station_code", null),
        json.optString("station_name", null),
        json.optString("train_name", null),
        json.optString("train_number", null),
        json.optString("fault_timestamp", null) // ✅ Extract fault_timestamp if present
    )
}
