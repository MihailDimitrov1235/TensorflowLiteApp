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
    lateinit var editTextTextPersonName : TextView
    lateinit var textView: TextView
    lateinit var imageView: ImageView
    lateinit var textureView:TextureView

    //variables
    lateinit var labels:List<String>
    lateinit var bitmap: Bitmap
        //speechRecognition
        var language = "bg"
        var mode = 0
        var captureRunning = false
        var recognize = false
        var sides : Float = 0.0f
        var updown : Float = 0.0f
        var thirdPostion : Float = 0.0f

    //objects
    lateinit var objectDetector: ObjectDetector
    lateinit var handler: Handler
    lateinit var sensorManager: SensorManager
    lateinit var textToSpeech: Text2Speech
    lateinit var listeningThread: Runnable
    val paint = Paint()

        //camera
        lateinit var cameraManager:CameraManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mode = 1
        getPermission()
//        setUpSensorSuff()

        textView = findViewById(R.id.textViewText)
        txtKoltinAccelerometer = findViewById(R.id.txtKoltinAccelerometer)
        imageView = findViewById(R.id.imageView)
        textureView = findViewById(R.id.textureView)
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName)

        objectDetector = ObjectDetector(this)
        textToSpeech = Text2Speech(this)
        val handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = (Handler(handlerThread.looper))


        labels = FileUtil.loadLabels(this,"labels.txt")
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        listeningThread = ListeningThread(this, textToSpeech, labels)
        /*val x = acceleration[0]
        val y = acceleration[1]
        val z = acceleration[2]
        textView.text = "x="+x
        textView.text = "y="+y
        textView.text = "z="+z*/

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
            ) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

                bitmap = textureView.bitmap!!
                val outputs = objectDetector.detect(bitmap)

                var mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true)
                val canvas = Canvas(mutableBitmap)

                val bitmapHeight = bitmap.height
                val bitmapWidth = bitmap.width

                paint.textSize = bitmapHeight/15f
                paint.strokeWidth = bitmapWidth/85f

                val threshold = 0.1
                var postProcessingObj = PostProcessing();
            }
        }
        listeningThread.run();

    }
    private fun setUpSensorSuff(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_FASTEST,SensorManager.SENSOR_DELAY_FASTEST)
        }

    }

    fun getPermission(){
        val cameraPermision = android.Manifest.permission.CAMERA
        val audioRecordPermission = android.Manifest.permission.RECORD_AUDIO
        val storagePermission = android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
        val sensorsPermision = android.Manifest.permission.HIGH_SAMPLING_RATE_SENSORS
        if( (ContextCompat.checkSelfPermission(this,cameraPermision) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this,audioRecordPermission) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this,storagePermission) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this,sensorsPermision) != PackageManager.PERMISSION_GRANTED)){
            requestPermissions(
                arrayOf(
                    cameraPermision,
                    audioRecordPermission,
                    storagePermission,
                    sensorsPermision
                ),
                101
            )
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

    override fun onDestroy() {
        super.onDestroy()
        objectDetector.destroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val sides = event.values[0]
            val updown = event.values[1]
            val thirdPostion = event.values[2]
//            var postProcessingObj = PostProcessing();

            txtKoltinAccelerometer.text = "up/down ${updown.toInt()}\nleft/rigth ${sides.toInt()}\nthirdPOstion ${thirdPostion}"
            //postProcessingObj.determingPhonePostion(updown,sides,thirdPostion);
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }
}