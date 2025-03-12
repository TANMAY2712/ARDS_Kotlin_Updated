import org.json.JSONObject

data class FaultResponses(val data: FaultData, val statusCode: Int) {
    constructor(json: JSONObject) : this(
        FaultData(json.optJSONObject("data") ?: JSONObject()),
        json.optInt("statusCode", 0) // ✅ Default value if statusCode is missing
    )
}

data class FaultData(
    val faults: List<Fault>,
    val outputUrl: String?,
    val progress: String?
) {
    constructor(json: JSONObject) : this(
        json.optJSONArray("faults")?.let { array ->
            List(array.length()) { index -> Fault(array.getJSONObject(index)) }
        } ?: emptyList(), // ✅ Handles missing "faults"
        json.optString("output_url", null), // ✅ Extract output_url
        json.optString("progress", null) // ✅ Extract progress status
    )
}

data class Fault(
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
