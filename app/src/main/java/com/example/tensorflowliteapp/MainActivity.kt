package com.example.tensorflowliteapp

import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPermission()

        val textureView = findViewById<TextureView>(R.id.textureView)
//        textureView.surfaceTextureListener = object:TextureView.SurfaceTextureListener{
//            override fun onSurfaceTextureAvailable(
//                surface: SurfaceTexture,
//                width: Int,
//                height: Int
//            ) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onSurfaceTextureSizeChanged(
//                surface: SurfaceTexture,
//                width: Int,
//                height: Int
//            ) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
//                TODO("Not yet implemented")
//            }
//        }

    }

    fun getPermission(){
        val cameraPermision = android.Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(this,cameraPermision) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(cameraPermision),101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
            getPermission();
        }
    }

}