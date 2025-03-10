package com.ards.remote.apimodel

import androidx.annotation.Keep

class SignInResponse(
    val ResponseCode: String = "",
    val SuccessMessage: String? = "",
    val Data: DataResponse
) {
    @Keep
    data class DataResponse(
        val AuthToken: String = "",
        val UserId: Int = 0,
        val RoleId: Int = 0,
        val Username: String = "",
        val CountryCode: String = "",
        val FullName: String = "",
        val FatherName: String = "",
        val DesignationId: Int = 0,
        val DesignationName: String = "",
        val PFNumber: String = "",
        val ProfileImage: String = "",
        val GradeId: Int = 0,
        val GradeName: String = "",
        val PayBandId: Int = 0,
        val PayBandName: String = "",
        val DepartmentId: Int = 0,
        val DepartmentName: String = "",
        val DOB: String = "",
        val DOJ: String = "",
        val IsAdmin: Int = 0,
        val SectorId: Int = 0,
        val Age: Int = 0,
        val ParentUnionId: Int = 0,
        val AdminUnionId: Int = 0,
        val ParentUnionName: String = "",
        val OfficeAddress: String = "",
        val OTP: String = "",
        val CouponCode: String = "",
        val CanPublishMedia: Boolean = false,
        val Quiz: Boolean = false,
        val FinancePerson: Boolean = false,
        val IsStudent: Int = 0,
        val BranchId: Int = 0,
        val DivisionId: Int = 0,
        val ZoneId: Int = 0,
        val NationalId: Int = 0,
        val QRCodePath: String = "",
        )
}
