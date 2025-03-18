package com.ards.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ards.R


class PermissionManagerUtils {

    fun requestPermissionFromFragment(
        fragment: Fragment,
        callback: PermissionCallback
    ): ActivityResultLauncher<Array<String>> {
        return fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isAllGranted = permissions.all { it.value }
            if (isAllGranted && !fragment.isDetached) {
                callback.onPermissionCallback(permissions)
            } else {
                for (permission in permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            fragment.requireActivity(),
                            permission.key
                        )
                    ) {
                        //showPermissionRationaleDialog(permission.key, fragment.requireContext())
                    } else {
                        //showPermissionDeniedDialog(fragment.requireContext())
                    }
                }
            }
        }
    }

    fun requestPermissionFromActivity(
        activity: androidx.activity.ComponentActivity,
        callback: PermissionCallback,
        context: Context,
    ): ActivityResultLauncher<Array<String>> {
        return activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isAllGranted = permissions.all { it.value }
            if (isAllGranted && !activity.isDestroyed && !activity.isFinishing) {
                callback.onPermissionCallback(permissions)
            } else {
                for (permission in permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            permission.key
                        )
                    ) {
                        //showPermissionRationaleDialog(permission.key, context)
                    } else {
                        //showPermissionDeniedDialog(context)
                    }
                }
            }
        }
    }

    /*private fun showPermissionRationaleDialog(permission: String, context: Context) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.showPermissionDialog_title))
            .setMessage(message)
            .setPositiveButton(context.getString(R.string.showPermissionRationaleDialog_positiveButton)) { dialog, _ ->
                // Request the permission again
                when (permission) {
                    Manifest.permission.ACCESS_FINE_LOCATION -> {
                        requestFineLocationPermission(context)
                    }
                    Manifest.permission.ACCESS_COARSE_LOCATION -> {
                        requestCoarseLocationPermission(context)
                    }
                    Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                        requestExternalStoragePermission(context)
                    }
                    Manifest.permission.RECORD_AUDIO -> {
                        requestAudioPermission(context)
                    }
                    Manifest.permission.CAMERA -> {
                        requestCameraPermission(context)
                    }
                    Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        requestReadStoragePermission(context)
                    }
                    Manifest.permission.POST_NOTIFICATIONS -> {
                        requestNotificationPermission(context)
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton(context.getString(R.string.showPermissionDialog_negativeButton)) { dialog, _ ->
                // Handle cancel action, e.g. close the app or show an error message
                dialog.dismiss()
            }
            .show()
    }*/

    private fun requestExternalStoragePermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            EXTERNAL_STORAGE_PERMISSIONS_RESULT
        )
    }

    private fun requestAudioPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            RECORD_AUDIO_PERMISSIONS_RESULT
        )
    }

    private fun requestReadStoragePermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_STORAGE_PERMISSIONS_RESULT
        )
    }

    private fun requestFineLocationPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            FINE_LOCATION_PERMISSIONS_RESULT
        )
    }

    private fun requestNotificationPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATION_PERMISSIONS_RESULT
        )
    }

    private fun requestCoarseLocationPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            COARSE_LOCATION_PERMISSIONS_RESULT
        )
    }

    private fun requestCameraPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSIONS_RESULT
        )
    }

    /*private fun showPermissionDeniedDialog(context: Context) {
        // Show custom dialog to inform the user that permission is permanently denied
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.showPermissionDialog_title))
            .setMessage(message)
            .setPositiveButton(context.getString(R.string.showPermissionDeniedDialog_positiveButton)) { dialog, _ ->
                // Navigate to app settings
                openAppSettings(context)
                dialog.dismiss()
            }
            .setNegativeButton(context.getString(R.string.showPermissionDialog_negativeButton)) { dialog, _ ->
                // Handle cancel action, e.g. close the app or show an error message
                dialog.dismiss()
            }
            .show()
    }*/

    private fun openAppSettings(context: Context) {
        // Open app settings screen
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    interface PermissionCallback {
        fun onPermissionCallback(permissions: Map<String, Boolean>)
    }

    companion object {
        private const val EXTERNAL_STORAGE_PERMISSIONS_RESULT = 1067
        private const val FINE_LOCATION_PERMISSIONS_RESULT = 1068
        private const val COARSE_LOCATION_PERMISSIONS_RESULT = 1069
        private const val READ_STORAGE_PERMISSIONS_RESULT = 1070
        private const val CAMERA_PERMISSIONS_RESULT = 1071
        private const val RECORD_AUDIO_PERMISSIONS_RESULT = 1072
        var message = ""
        private const val NOTIFICATION_PERMISSIONS_RESULT = 1073
    }

    enum class CalledFrom{
        DOWNLOAD,
        UPLOAD_RESUME,
        UPLOAD_UT,
        UPLOAD_FILE,
        RECORD_AUDIO
    }
}