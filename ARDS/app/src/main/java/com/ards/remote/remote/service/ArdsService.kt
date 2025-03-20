package com.ards.remote.remote.service
import com.ards.domain.model.GenrateOTPRequest
import com.ards.model.Playground
import retrofit2.Call
import com.ards.remote.apimodel.VerifyOtpResponse
import com.ards.remote.apimodel.GenrateOTPResponse
import com.ards.remote.apimodel.VerifyOtpRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import com.ards.remote.apimodel.ChartRequest
import com.ards.remote.apimodel.ChartResponse
import com.ards.remote.apimodel.MasterDataRequest
import com.ards.remote.apimodel.MasterDataResponse
import com.ards.remote.apimodel.ModelInferenceResponse
import com.ards.remote.apimodel.NotificationFaultListRequest
import com.ards.remote.apimodel.NotificationFaultListResponse
import com.ards.remote.apimodel.NotificationListRequest
import com.ards.remote.apimodel.NotificationListResponse
import com.ards.remote.apimodel.PreSignedUrlResponse
import com.ards.remote.apimodel.SignInRequest
import com.ards.remote.apimodel.SignInResponse
import com.ards.remote.apimodel.UploadFileResponse
import com.ards.remote.apimodel.UserProfileRequest
import com.ards.remote.apimodel.UserProfileResponse
import com.ards.remote.apimodel.VideoByCategoryRequest
import com.ards.remote.apimodel.VideoByCategoryResponse
import com.ards.utils.ArdsConstant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.HeaderMap
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

interface ArdsService {
    @POST(ArdsConstant.ApiEndPoint.GenrateOTP)
    fun sendOtp(@Body request: GenrateOTPRequest): Call<GenrateOTPResponse>

    @POST(ArdsConstant.ApiEndPoint.VerifyOTP)
    fun verifyOTP(@Body request: VerifyOtpRequest): Call<VerifyOtpResponse>

    @GET("videos") // Replace with actual endpoint
    fun getVideos(): Call<List<Playground>>
   /* @GET("model_inference?s3_path=uploads/65c612e57db39ec6eb44017c.mp4&train_no=0&station=0&rec_side=Right") // Replace with actual endpoint
    fun getPlayground(): Call<List<FaultResponse>>*/
   /* @GET
    fun getPlayground(@Url fullUrl: String): Call<List<FaultResponse>>*/
    @POST(ArdsConstant.ApiEndPoint.NotificationFault)
    fun getNotificationFaultList(@Body request: NotificationFaultListRequest): Call<NotificationFaultListResponse>

    @POST(ArdsConstant.ApiEndPoint.NotificationList)
    fun getAllNotification(@Body request: NotificationListRequest): Call<NotificationListResponse>

    @POST(ArdsConstant.ApiEndPoint.SignIn)
    fun signIn(@Body request: SignInRequest): Call<SignInResponse>

    @POST(ArdsConstant.ApiEndPoint.ChartByFaultType)
    fun chartByFaultType(@Body request: ChartRequest): Call<ChartResponse>

    @POST(ArdsConstant.ApiEndPoint.MasterData)
    fun masterData(@Body request: MasterDataRequest): Call<MasterDataResponse>

    @POST(ArdsConstant.ApiEndPoint.UserProfile)
    fun updateUserProfile(@Body request: UserProfileRequest): Call<UserProfileResponse>

    @POST(ArdsConstant.ApiEndPoint.VideoByCategory)
    fun getVideoByCategory(@Body request: VideoByCategoryRequest): Call<VideoByCategoryResponse>

    // GET Pre-Signed URL
    @GET(ArdsConstant.ApiEndPoint.GetPresignedUrl)
    fun getPreSignedUrl(): Call<PreSignedUrlResponse>

    // Upload File to S3 (Presigned URL)
    @PUT
    fun uploadFileToS3(
        @Url url: String,
        @Body file: RequestBody,
        @HeaderMap headers: Map<String, String>
    ): Call<ResponseBody>

    // Perform Model Inference
    @GET(ArdsConstant.ApiEndPoint.ModelInference)
    fun performModelInference(
        @Query("train_no") trainNo: String,
        @Query("station") station: String,
        @Query("rec_side") recSide: String,
        @Query("s3_path") s3Path: String
    ): Call<ModelInferenceResponse>

    @Multipart
    @POST(ArdsConstant.ApiEndPoint.UploadFile)
    fun uploadfile(
        @Part UserImage: MultipartBody.Part,
        @Part(ArdsConstant.ApiEndPoint.APIKey) Id: RequestBody

    ): Call<UploadFileResponse>

}