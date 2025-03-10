package com.ards.utils
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsOTPBRoadCast:BroadcastReceiver() {


    lateinit var smsBroadcastReceiverListener: SmsBroadcastReceiverListener


    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {

            val extras = intent.extras
            val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (smsRetrieverStatus.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    Log.d("dkdkdkkd","success")

                    extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT).also {
                        smsBroadcastReceiverListener.onSuccess(it)
                    }
                }

                CommonStatusCodes.TIMEOUT -> {
                    Log.d("dkdkdkkd","fail")
                    smsBroadcastReceiverListener.onFailure()
                }
            }
        }
    }


    interface SmsBroadcastReceiverListener {
        fun onSuccess(intent: Intent?)
        fun onFailure()
    }
}