package com.example.tensorflowliteapp

import android.annotation.SuppressLint
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
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.TextureView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.*

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
    lateinit var text2speech: Text2Speech
    lateinit var txtKoltinAccelerometer : TextView
    lateinit var editTextTextPersonName : TextView
    private lateinit var sensorManager: SensorManager
    private var mode = 0
    private var textToSpeech: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    private var captureRunning = false
    var result: ArrayList<String>? = null
    //var isrecognizable = false
    var recognize = false
    var sides : Float = 0.0f
    var updown : Float = 0.0f
    var thirdPostion : Float = 0.0f
    var introductoryWords: String? = null
    var instrucionWords: String? = null
    var mediaPlayer: MediaPlayer? = null
    var `object`: String? = null
    var language: String? = null
    var mPermissionResultLauncher: ActivityResultLauncher<Array<String>>? = null
    var isrecognizable = false
    private var isAudioRecordPermissionGranted = false
    private var isCameraPermissionGranted = false
    private var isStoragePermissionGranted = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text2speech = Text2Speech(this)
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

        labels = FileUtil.loadLabels(this,"labels.txt")
        model0 = EfficientdetLite0.newInstance(this)
        model1 = EfficientdetLite1.newInstance(this)
        model2 = EfficientdetLite2.newInstance(this)
        model = Mobilenetv1.newInstance(this)
        imageProcessor = ImageProcessor.Builder().add(ResizeOp(300,300, ResizeOp.ResizeMethod.BILINEAR)).build()
        textureView = findViewById(R.id.textureView)

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
                //postProcessingObj.postProccessingInfo(outputs,textView);


            }
        }


        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager


    }
    private fun setUpSensorSuff(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_FASTEST,SensorManager.SENSOR_DELAY_FASTEST)
        }

    }
    private fun startSpeechRecognition() {
        editTextTextPersonName.setText("mode=$mode")
        //Toast.makeText(this, "VLiza v startSpeechRecognition()", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "VLiza v startSpeechRecognition()", Toast.LENGTH_SHORT).show();
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        //Set up the intent for speech recognition
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        recognizerIntent!!.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        recognizerIntent!!.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)


        // Start listening for speech
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {
                //editTextTextMultiLine.setText("Pochva da slusha");
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray) {}
            override fun onEndOfSpeech() {
                // editTextTextMultiLine.setText("END");
            }

            override fun onError(i: Int) {
                //editTextShowText.setText("Error " + i);
                speechRecognizer.startListening(recognizerIntent)
                //speechRecognizer.startListening(recognizerIntent);
            }

            override fun onResults(results: Bundle) {
                result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result!!.size == 0) return
                val lastCommand = result!![result!!.size - 1].lowercase(Locale.getDefault())

                // Влиза тук когато се каже човекът каже, че иска да му се намери даден обект
                if (commandFind.match(lastCommand)) {
                    log("ВЛИЗА В НАМЕРЕН ОБЕКТ------------- ")

                    `object` = lastCommand
                    recognize = true
                }
                // Ако е казано "инструкции", казва на човека какви функционалности има приложението
                //log("/" + lastCommand + "/");
                if (commandInstructions.match(lastCommand)) {
                    captureRunning = false
                    //mode = 1
                    //                  log("instructions");
                    speak(instrucionWords)
                }
                // Спира приложението да снима

                //Toast.makeText(CapturePictureAutomatically.this, "Vliza v mode 2", Toast.LENGTH_SHORT).show();

                //Toast.makeText(CapturePictureAutomatically.this, "Vliza v mode 2", Toast.LENGTH_SHORT).show();
                val assetManager = assets
                try {
                    val inputStream = assetManager.open("recognizableoObjects-bg.txt")
                    val reader = BufferedReader(
                        InputStreamReader(
                            inputStream,
                            Charset.forName("windows-1251")
                        )
                    )
                    var line: String = ""
                    while (reader.readLine()?.also { line = it } != null) {
                        // do something with the line
                        //result.contains("намери "+ line)
                        if (result!!.contains("намери $line")) {
                            isrecognizable = true
                            break
                        }
                        //editTextTextMultiLine.setText(line+" ");

                        //Toast.makeText(this, "lin="+line, Toast.LENGTH_SHORT).show();
                    }
                    //editTextTextMultiLine.setText("isrecognizable= "+isrecognizable);
                    if (isrecognizable == true) {
                        //soundForListen.interrupt();
                        //soundForListen.stop();
                        mediaPlayer!!.isLooping = false
                        speak("Този предмет може да се открие. Почва търсене")
                        isrecognizable = false;
                        // Param is optional, to run task on UI thread.
                        handler = Handler(Looper.getMainLooper())
                        /*runnable = object : Runnable {
                            override fun run() {
                                // Do the task...
                                capturePhoto()
                                Toast.makeText(
                                    this@CapturePictureAutomatically,
                                    "Започна да прави снимки",
                                    Toast.LENGTH_SHORT
                                ).show()
                                handler.postDelayed(this, 5000)
                                // Optional, to repeat the task
                            }
                        }
                        handler.postDelayed(runnable, 5000)*/
                        //                            capturePhoto();
                        mode++
                        //speak("Този предмет може да се открие");
                    } else {
                        speak("Този предмет не може да се открие")
                    }
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (commandStop.match(lastCommand)) {
                    log("СТОП СПРИ ")
                    recognize = true
                    captureRunning = false
                    speak("Спряхте режима. Какво искате да направя за вас?")
                }

                // Влиза тук когато се каже думата навигация и започва да ориентира човека какво
                // има пред него
                if (commandFindAll.match(lastCommand)) {
                    log("НАВИГИРА МЕ НАВСЯКЪДЕ")
                    speak("Пускане на навигация.")
                    captureRunning = true
                    `object` = "all"
                    recognize = false
                }
                // Влиза тук когато потребителят иска да излезе от програмата
                if (commandExit.match(lastCommand)) {
                    log("ИЗЛЕЗЕ ОТ ПРОГРАМАТА")
                    System.exit(0)
                }
                if (commandLanguage.match(lastCommand)) {
                    speak("")
                    if (result!!.contains("български")) {
                        language = "bg"
                    } else if (result!!.contains("български")) {
                        language = "en"
                    }
                }
                speechRecognizer.startListening(recognizerIntent)
            }

            override fun onPartialResults(bundle: Bundle) {
                
            }

            override fun onEvent(i: Int, bundle: Bundle) {}
        })
        speechRecognizer.startListening(recognizerIntent)
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