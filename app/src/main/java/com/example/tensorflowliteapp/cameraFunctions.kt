package com.example.tensorflowliteapp

import android.annotation.SuppressLint
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.view.Surface
import android.view.TextureView

@SuppressLint("MissingPermission")
fun openCamera(cameraManager: CameraManager, textureView: TextureView, handler: Handler){
    cameraManager.openCamera(cameraManager.cameraIdList[0],object: CameraDevice.StateCallback(){
        override fun onOpened(camera: CameraDevice) {
            val cameraDevice = camera
            val surfaceTexture = textureView.surfaceTexture
            val surface = Surface(surfaceTexture)

            val captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
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