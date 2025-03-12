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
import retrofit2.http.Url
import com.ards.remote.apimodel.ChartRequest
import com.ards.remote.apimodel.ChartResponse
import com.ards.remote.apimodel.MasterDataRequest
import com.ards.remote.apimodel.MasterDataResponse
import com.ards.remote.apimodel.NotificationFaultListRequest
import com.ards.remote.apimodel.NotificationFaultListResponse
import com.ards.remote.apimodel.NotificationListRequest
import com.ards.remote.apimodel.NotificationListResponse
import com.ards.remote.apimodel.SignInRequest
import com.ards.remote.apimodel.SignInResponse
import com.ards.remote.apimodel.VideoByCategoryRequest
import com.ards.remote.apimodel.VideoByCategoryResponse
import com.ards.utils.Constant

interface ArdsService {
    @POST(Constant.ApiEndPoint.GenrateOTP)
    fun sendOtp(@Body request: GenrateOTPRequest): Call<GenrateOTPResponse>

    @POST(Constant.ApiEndPoint.VerifyOTP)
    fun verifyOTP(@Body request: VerifyOtpRequest): Call<VerifyOtpResponse>

    @GET("videos") // Replace with actual endpoint
    fun getVideos(): Call<List<Playground>>
   /* @GET("model_inference?s3_path=uploads/65c612e57db39ec6eb44017c.mp4&train_no=0&station=0&rec_side=Right") // Replace with actual endpoint
    fun getPlayground(): Call<List<FaultResponse>>*/
   /* @GET
    fun getPlayground(@Url fullUrl: String): Call<List<FaultResponse>>*/
    @POST(Constant.ApiEndPoint.NotificationFault)
    fun getNotificationFaultList(@Body request: NotificationFaultListRequest): Call<NotificationFaultListResponse>

    @POST(Constant.ApiEndPoint.NotificationList)
    fun getAllNotification(@Body request: NotificationListRequest): Call<NotificationListResponse>

    @POST(Constant.ApiEndPoint.SignIn)
    fun signIn(@Body request: SignInRequest): Call<SignInResponse>

    @POST(Constant.ApiEndPoint.ChartByFaultType)
    fun chartByFaultType(@Body request: ChartRequest): Call<ChartResponse>

    @POST(Constant.ApiEndPoint.MasterData)
    fun masterData(@Body request: MasterDataRequest): Call<MasterDataResponse>

    @POST(Constant.ApiEndPoint.UserProfile)
    fun updateUserProfile(@Body request: MasterDataRequest): Call<MasterDataResponse>

    @POST(Constant.ApiEndPoint.VideoByCategory)
    fun getVideoByCategory(@Body request: VideoByCategoryRequest): Call<VideoByCategoryResponse>

}