package com.ards.remote.service

import com.ards.domain.model.GenrateOTPRequest
import com.ards.model.Playground
import com.ards.utils.Constant
import retrofit2.Call
import com.ards.remote.apimodel.VerifyOtpResponse
import com.ards.remote.apimodel.GenrateOTPResponse
import com.ards.remote.apimodel.VerifyOtpRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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

}