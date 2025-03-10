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
    /*@POST(Constant.ApiEndPoint.SignOut)
    suspend fun getPlans(
        @Body planListRequest: PlanListRequest
    ): PlansListResponse*/

    /*@POST(Constant.ApiEndPoint.MobileSignup)
    suspend fun getStateList(
        @Body stateListRequest: StateListRequest
    ): StateListResponse*/

    /*@POST(Constant.ApiEndPoint.DALogin)
    suspend fun getCityList(
        @Body cityListRequest: CityListRequest
    ): CityListResponse*/

    /*@POST(Constant.ApiEndPoint.IfUserExists)
    suspend fun preApproval(
        @Body userDetailsRequest: UserDetailsRequest
    ): LoanDetailResponse*/

    /*@POST(Constant.ApiEndPoint.GenrateOTP)
    suspend fun genrateOTP(
        @Body loanListRequest: GenrateOTPRequest
    ): GenrateOTPResponse

    @POST(Constant.ApiEndPoint.VerifyOTP)
    suspend fun verifyOTP(
        @Body pincodeRequest: VerifyOtpRequest
    ): VerifyOtpResponse*/

    /*@POST(Constant.ApiEndPoint.MobileSignup)
    suspend fun getLoanDetails(
        @Body loanDetailRequest: LoanDetailRequest
    ): LoanDetailResponse*/

    /*@Multipart
    @POST("Loan/UploadFile")
    suspend fun uploadLoanDocumentFile(
        @Part UserDocs: MultipartBody.Part,
        @Part(Constants.Loan.APIKey) APIKey: RequestBody,
        @Part(Constants.Loan.DocId) DocumentName: RequestBody,
        @Part(Constants.Loan.VendorId) VendorId: RequestBody,
        @Part(Constants.Loan.ReferenceNo) ReferenceNo: RequestBody,
        @Part(Constants.Loan.DocPassword) DocPassword: RequestBody?,
        @Part(Constants.Loan.UserId) UserId: RequestBody,
        @Part(Constants.Loan.IsSubDoc) IsSubDoc: RequestBody,
        @Part(Constants.Loan.UserLoanProfileId) UserLoanProfileId: Int
    ):DocumentsUploadResponse

    @POST("Master/GetMasterData")
    suspend fun getMasterList(
        @Body masterListRequest: MasterListRequest
    ): MasterListResponse
    @POST("Loan/GetSubDocumentType")
    suspend fun getSubDocumentList(
        @Body subDocumentTypeRequest: SubDocumentTypeRequest
    ): SubDocumentTypeResponse*/

    @POST(Constant.ApiEndPoint.GenrateOTP)
    fun sendOtp(@Body request: GenrateOTPRequest): Call<GenrateOTPResponse>

    @POST(Constant.ApiEndPoint.VerifyOTP)
    fun verifyOTP(@Body request: VerifyOtpRequest): Call<VerifyOtpResponse>

    @GET("videos") // Replace with actual endpoint
    fun getVideos(): Call<List<Playground>>
    @GET
    fun getPlayground(@Url fullUrl: String): Call<List<Playground>>

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