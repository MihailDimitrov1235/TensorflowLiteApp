package com.example.tensorflowliteapp

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

fun getPermission(context: Context){
    val cameraPermision = android.Manifest.permission.CAMERA
    val audioRecordPermission = android.Manifest.permission.RECORD_AUDIO
    val storagePermission = android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
    val sensorsPermision = android.Manifest.permission.HIGH_SAMPLING_RATE_SENSORS
    if( (ContextCompat.checkSelfPermission(context,cameraPermision) != PackageManager.PERMISSION_GRANTED)
        || (ContextCompat.checkSelfPermission(context,audioRecordPermission) != PackageManager.PERMISSION_GRANTED)
        || (ContextCompat.checkSelfPermission(context,storagePermission) != PackageManager.PERMISSION_GRANTED)
        || (ContextCompat.checkSelfPermission(context,sensorsPermision) != PackageManager.PERMISSION_GRANTED)){
        requestPermissions(Activity(),arrayOf(cameraPermision,audioRecordPermission,storagePermission,sensorsPermision),101)
    }
}