package com.example.tensorflowliteapp

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.SurfaceTexture
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.TextureView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.tensorflow.lite.support.common.FileUtil
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    //views
    lateinit var txtKoltinAccelerometer : TextView
    lateinit var imageView: ImageView
    lateinit var textureView:TextureView

    //variables
    lateinit var labels:List<String>
    var request = true

    //objects
    lateinit var objectDetector: ObjectDetector
    lateinit var handler: Handler
    lateinit var sensorManager: SensorManager
    lateinit var textToSpeech: Text2Speech
    lateinit var listeningThread: Runnable

        //camera
        lateinit var cameraManager:CameraManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPermission()
        setUpSensorStuff()

        imageView = findViewById(R.id.imageView)
        textureView = findViewById(R.id.textureView)

        objectDetector = ObjectDetector(this)
        textToSpeech = Text2Speech(this)
        val handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = (Handler(handlerThread.looper))


        labels = FileUtil.loadLabels(this,"labels.txt")
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        listeningThread = ListeningThread(this, textToSpeech, objectDetector, handler, textureView, labels)

        textureView.surfaceTextureListener = object:TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                openCamera(cameraManager,textureView,handler)
            }

            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {}

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
        }
        listeningThread.run()

    }

    private fun setUpSensorStuff(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        try {
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
                sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_FASTEST,SensorManager.SENSOR_DELAY_FASTEST)
            }
        }catch (e: java.lang.Exception){
            Log.e("Accelerometer error", e.toString())
        }


    }

    fun getPermission(){
        val cameraPermission = android.Manifest.permission.CAMERA
        val audioRecordPermission = android.Manifest.permission.RECORD_AUDIO


        while( (ContextCompat.checkSelfPermission(this,cameraPermission) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this,audioRecordPermission) != PackageManager.PERMISSION_GRANTED)){
            if (request) {
                requestPermissions(
                    arrayOf(
                        cameraPermission,
                        audioRecordPermission,
                    ),
                    101
                )
                request = false
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
            request = true
            getPermission()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        objectDetector.destroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
//            val sides = event.values[0]
//            val updown = event.values[1]
//            val thirdPostion = event.values[2]
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }
}