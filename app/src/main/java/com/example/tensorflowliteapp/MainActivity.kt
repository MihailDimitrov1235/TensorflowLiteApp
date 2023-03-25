package com.example.tensorflowliteapp

import android.annotation.SuppressLint
import android.content.AsyncQueryHandler
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    lateinit var cameraDevice: CameraDevice
    lateinit var handler: Handler
    lateinit var textureView:TextureView
    lateinit var cameraManager:CameraManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = (Handler(handlerThread.looper))

        getPermission()

        textureView = findViewById(R.id.textureView)
        textureView.surfaceTextureListener = object:TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            }
        }

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    @SuppressLint("MissingPermission")
    fun openCamera(){
        cameraManager.openCamera(cameraManager.cameraIdList[0],object:CameraDevice.StateCallback(){
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                var surfaceTexture = textureView.surfaceTexture
                var surface = Surface(surfaceTexture)

                var captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequest.addTarget(surface)

                cameraDevice.createCaptureSession(listOf(surface), object: CameraCaptureSession.StateCallback(){
                    override fun onConfigured(session: CameraCaptureSession) {
                        session.setRepeatingRequest(captureRequest.build(),null,null)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        TODO("Not yet implemented")
                    }
                }, handler)
            }

            override fun onDisconnected(camera: CameraDevice) {
                TODO("Not yet implemented")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                TODO("Not yet implemented")
            }
        },handler)
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