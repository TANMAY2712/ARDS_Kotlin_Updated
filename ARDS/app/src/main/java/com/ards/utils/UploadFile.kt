package com.ards.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.UnsupportedEncodingException


class UploadFile(private val context: Context) {

    private val TAG = "AzureBlobUploader"
    private var requestQueue: RequestQueue = Volley.newRequestQueue(context)


    fun uploadFileToBlob(
        fileUri: Uri,
        trainNo: String,
        station: String,
        recSide: String,
        context: Context,
        successListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        // Step 1: Make a GET request to retrieve the pre-signed URL
        val getRequest = JsonObjectRequest(
            Request.Method.GET,
            "http://98.70.56.87:8085/get_presigned_url",
            null,
            { response ->
                val preSignedUrl = response.getString("presigned_url")
                val s3Path = response.getString("resource_id")
                Log.d("URI", "uploadFileToBlob:$preSignedUrl ")
                Log.d("S3PATH", "uploadFileToBlob: $s3Path")
                Log.d(TAG, "uploadFileToBlob: $response")
                // Step 2: Use the retrieved pre-signed URL to upload the file
                val customHeaders = mapOf(
                    "Content-Type" to "video/mp4",
                    "x-amz-acl" to "public-read"
                )
                uploadFileToS3(
                    preSignedUrl,
                    s3Path,
                    fileUri,
                    trainNo,
                    station,
                    recSide,
                    context = context,
                    customHeaders,
                    successListener,
                    errorListener
                )
            },
            { error ->
                // Handle error in retrieving the pre-signed URL
                Log.d(TAG, "Error:ist me${error.localizedMessage} ")
                errorListener.onErrorResponse(error)
                Toast.makeText(context , "${error.localizedMessage}" , Toast.LENGTH_SHORT).show()

            }
        )

        // Add the GET request to the Volley request queue
        requestQueue.add(getRequest)
    }


    private fun uploadFileToS3(
        url: String,
        s3path: String,
        fileUri: Uri,
        trainNo: String,
        station: String,
        recSide: String,
        context: Context,
        headers: Map<String, String>,
        successListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        Log.e(TAG, "uploadFileToS3: $url")

        val fileInputStream = context.contentResolver.openInputStream(fileUri)

        fileInputStream?.use { inputStream ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            val dataOutputStream = DataOutputStream(byteArrayOutputStream)
            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                dataOutputStream.write(buffer, 0, len)
            }
            dataOutputStream.flush()

            val multipartRequest = object : JsonObjectRequest(
                Method.PUT,
                url,
                null,
                { response ->


                },
                errorListener
            ) {
                override fun getBody(): ByteArray {
                    return byteArrayOutputStream.toByteArray()
                }

                override fun getBodyContentType(): String {
                    return "video/mp4"
                }

                override fun getHeaders(): Map<String, String> {
                    return headers
                }


                override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                    Log.d(TAG, "parseNetworkResponse: ${response?.statusCode}")
                    if (response?.statusCode == 200) {
                        Log.d(TAG, "parseNetworkResponse: $s3path")
                        performModelInferenceRequest(
                            s3Path = s3path,
                            trainNo = trainNo ,
                            station = station,
                            recSide = recSide,
                            successListener = successListener,
                            errorListener = errorListener
                        )
                    }
                    else{
                        Toast.makeText(context , "Error" , Toast.LENGTH_LONG).show()
                        Log.e(TAG, "Error: ${response?.statusCode} ", )
                    }
                    return try {
                        val jsonString = String(
                            response?.data ?: ByteArray(0),
                            charset(HttpHeaderParser.parseCharset(response?.headers))
                        )
                        Response.success(
                            JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response)
                        )
                    } catch (e: UnsupportedEncodingException) {
                        Response.error(ParseError(e))
                    } catch (e: JSONException) {
                        Response.error(ParseError(e))
                    }
                }
            }

            multipartRequest.retryPolicy = DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            requestQueue.add(multipartRequest)
        }
    }



    fun performModelInferenceRequest(
        s3Path: String  ,
//        s3Path :String= "uploads%2F658184713f579aabe404f60d.mp4",
        trainNo: String,
        station: String,
        recSide: String,
        successListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        val url =
            "http://98.70.56.87:8085/model_inference?s3_path=$s3Path&train_no=$trainNo&station=$station&rec_side=$recSide"

        val headers = mapOf(
            "Accept" to "*/*" // Adjust as per your requirements
            // Add more headers if needed
        )

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            url,
            null,
            { response ->
//                Log.d(TAG, "performModelInferenceRequest: $response")
//                if (response.toString().contains("completed")) {
//                    Log.d(
//                        TAG,
//                        "performModelInferenceRequest: ${
//                            response.getJSONObject("data").getString("output_url")
//                        }"
//                    )
//                    successListener.onResponse(response)
                if (response.has("progress") && response.getString("progress") == "error") {
                    // Stop API call and show error
                    Log.e(TAG, "Model inference progress marked as error")
                    Toast.makeText(context, "Model inference failed", Toast.LENGTH_SHORT).show()
                    errorListener.onErrorResponse(VolleyError("Model inference failed"))
                } else if (response.toString().contains("completed")) {
                    Log.d(
                        TAG,
                        "performModelInferenceRequest: ${
                            response.getJSONObject("data").getString("output_url")
                        }"
                    )
                    successListener.onResponse(response)
                } else {

                    // Retry after a delay if needed
                    MainScope().launch {
                        delay(5000)
                        performModelInferenceRequest(
                            s3Path,
                            trainNo,
                            station,
                            recSide,
                            successListener,
                            errorListener
                        )
                    }
                }
            },
            errorListener
        ) {
            override fun getHeaders(): Map<String, String> {
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }
}