package com.example.tensorflowliteapp

import android.content.Context
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

class MainActivity : AppCompatActivity(), SensorEventListener {
    val paint = Paint()
    lateinit var objectDetector: ObjectDetector
    lateinit var handler: Handler
    lateinit var textureView:TextureView
    lateinit var cameraManager:CameraManager
    lateinit var cameraHandler: CameraHandler
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap
    lateinit var textView: TextView
    lateinit var txtKoltinAccelerometer : TextView
    private lateinit var sensorManager: SensorManager
    var sides : Float = 0.0f
    var updown : Float = 0.0f
    var thirdPostion : Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpSensorSuff()
        /*sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = AccelerometerSensor(sensorManager)
        accelerometerSensor.register()
        val acceleration = accelerometerSensor.getAcceleration()
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)*/

        val handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = (Handler(handlerThread.looper))
        textView = findViewById(R.id.textViewText)
        txtKoltinAccelerometer = findViewById(R.id.txtKoltinAccelerometer)
        imageView = findViewById(R.id.imageView)
        textureView = findViewById(R.id.textureView)

        getPermission()
        objectDetector = ObjectDetector(this)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraHandler = CameraHandler(imageView,textureView,handler,cameraManager)

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
                cameraHandler.openCamera()
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

                val bitmapHeight = mutableBitmap.height
                val bitmapWidth = mutableBitmap.width

                paint.textSize = bitmapHeight/15f
                paint.strokeWidth = bitmapWidth/85f

                var postProcessingObj = PostProcessing()


            }
        }

    }
    private fun setUpSensorSuff(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_FASTEST,SensorManager.SENSOR_DELAY_FASTEST)
        }

    }


    fun getPermission(){
        val cameraPermision = android.Manifest.permission.CAMERA
        val sensorsPermision = android.Manifest.permission.HIGH_SAMPLING_RATE_SENSORS
        if ((ContextCompat.checkSelfPermission(this,cameraPermision) != PackageManager.PERMISSION_GRANTED)
            || (ContextCompat.checkSelfPermission(this,sensorsPermision) != PackageManager.PERMISSION_GRANTED)){

            requestPermissions(arrayOf(cameraPermision,sensorsPermision),101)

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