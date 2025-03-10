package com.ards.remote.apimodel

import androidx.annotation.Keep

class GenrateOTPResponse(
    val ResponseCode: String = "",
    val SuccessMessage: String? = "",
    val Data: DataResponse
) {
    @Keep
    data class DataResponse(
        val status: String = "",
        val lstPayload: List<Plans> )
    {
        @Keep
        data class Plans(
            val loanType: String = "",
            val VendorName: String = "",
            val interestRate: String = "",
            val processingFee: String = "",
            val maxLoanEligibilityAmount: String = "",
            val minLoanEligibilityAmount: String = "",
            val noOfInstallments: String = "",
            val upfrontInterestDeductionPercentage: String = "",
        )
    }
}
