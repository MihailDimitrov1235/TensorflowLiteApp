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
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
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
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tensorflowliteapp.ml.EfficientdetLite0
import com.example.tensorflowliteapp.ml.EfficientdetLite1
import com.example.tensorflowliteapp.ml.EfficientdetLite2
import com.example.tensorflowliteapp.ml.Mobilenetv1
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
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
    lateinit var txtKoltinAccelerometer : TextView
    private lateinit var sensorManager: SensorManager
    private var mode = 0
    private var textToSpeech: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null
    var result: ArrayList<String>? = null
    var isrecognizable = false
    var sides : Float = 0.0f
    var updown : Float = 0.0f
    var thirdPostion : Float = 0.0f
    var introductoryWords: String? = null
    var instrucionWords: String? = null
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mode = 1
        introductoryWords = "Добър ден Стартира се програма блайнд хелпър. Какво искате да " +
                "направя за вас. За да разберете повече, кажете думата Инструкции"
        instrucionWords = "Ако искате да намерите даден предмет, трябва да кажете думата намери и" +
                " след нея да кажете обекта, който търсите. Например казвате Намери човек или " +
                "казвате търси котка. Друга функция на приложението е да ви навигира, тоест да" +
                "каже какво има пред вас и да ви предупреди за него. За да влезете в този реажим" +
                "кажете думата Навигация. В него например приложението ще Ви казва какво да " +
                "направите, ако има предмет пред вас, за да стигнете вървите безопасно напред."
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech!!.setLanguage(Locale.forLanguageTag("bg-BG"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported")
                } else {
                    Log.i("TTS", "TextToSpeech initialized")
                }
                speak(introductoryWords)
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.beep)
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
                //postProcessingObj.postProccessingInfo(outputs,textView);


            }
        }
    }
    private fun setUpSensorSuff(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_FASTEST,SensorManager.SENSOR_DELAY_FASTEST)
        }

    }
    private fun startSpeechRecognition() {
        editTextTextPersonName!!.setText("mode=$mode")
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
                //editTextShowText.setText(" ");
                // editTextTextMultiLine.setText("");
                result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                //editTextTextMultiLine.setText("f"+ result.get(result.size() - 1));
                //File file = new File("res/");
                //if (file.exists()) {
                /*try {
                    InputStream inputStream = getAssets().open("recognizableoObjects-en.txt");
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    inputStream.close();
                    String fileContents = new String(buffer);
                    Toast.makeText(MainActivity.this, "fileContents="+fileContents, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // handle error
                }*/
                //}
                Toast.makeText(
                    this@MainActivity,
                    "Vliza v mode 1$result",
                    Toast.LENGTH_SHORT
                ).show()
                // Влиза, когато са се прочели въвеждащите думи
                if (mode == 1) {
                    //mediaPlayer.start();
                    Toast.makeText(
                        this@MainActivity,
                        "Vliza v mode 1",
                        Toast.LENGTH_SHORT
                    ).show()
                    //soundForListen.start();
                    if (result!!.contains("инструкции")) {
                        Toast.makeText(
                            this@MainActivity,
                            "vliza v proverka",
                            Toast.LENGTH_SHORT
                        ).show()
                        speak(instrucionWords)
                        //
                        //Toast.makeText(CapturePictureAutomatically.this, "Vliza v razpoznat", Toast.LENGTH_SHORT).show();
                        //speak("Здравейте, почвам да ви слушам");
                        editTextTextPersonName!!.setText("result$result")
                        //mode++;
                    }

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
                        var line: String
                        while (reader.readLine().also { line = it } != null) {
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
                            // Param is optional, to run task on UI thread.
                            handler = Handler(Looper.getMainLooper())
                            runnable = object : Runnable {
                                override fun run() {
                                    // Do the task...
                                    capturePhoto()
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Започна да прави снимки",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    handler!!.postDelayed(this, 5000)
                                    // Optional, to repeat the task
                                }
                            }
                            handler!!.postDelayed(runnable as Runnable, 5000)
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

                    //speak("Здравейте, почвам да ви слушам");
                    //speak("Този предмет може да се открие");
                }
                speechRecognizer.startListening(recognizerIntent)
                //Toast.makeText(MainActivity.this, "Tova e rezultata: "+result.get(0), Toast.LENGTH_SHORT).show();
            }

            override fun onPartialResults(bundle: Bundle) {
                val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null) {
                    val match = matches[matches.size - 1]
                    //if(match.length() > 12) match = match.substring(match.length() - 12);
                    // editTextTextMultiLine.setText("p"+ match);
/*                    Toast.makeText(MainActivity.this, "Vliza v onPartialResults", Toast.LENGTH_SHORT).show();
                    if(mode == 1){
                        Toast.makeText(MainActivity.this, "Vliza v mode 1", Toast.LENGTH_SHORT).show();
                        if(matches.contains("хей помощник") || matches.contains("hey pomoshtnik")){
                            Toast.makeText(MainActivity.this, "Vliza v razpoznat", Toast.LENGTH_SHORT).show();
                            speak("Здравейте, почвам да ви слушам");
                            mode++;
                        }
                    }else if (mode == 2){
                        Toast.makeText(MainActivity.this, "Vliza v mode 2", Toast.LENGTH_SHORT).show();
                        boolean isrecognizable = false;
                        AssetManager assetManager = getAssets();
                        try{
                            InputStream inputStream = assetManager.open("recognizableoObjects-bg.txt");
                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,Charset.forName("windows-1251")));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                // do something with the line
                                if(matches.contains(line)){
                                    isrecognizable = true;
                                    break;

                                }
                                editTextTextMultiLine.setText(line+" ");
                                //Toast.makeText(this, "lin="+line, Toast.LENGTH_SHORT).show();
                            }
                            editTextTextMultiLine.setText("isrecognizable="+isrecognizable);
                            if(isrecognizable){
                                textToSpeech.speak("Този предмет може да се открие", TextToSpeech.QUEUE_FLUSH, null);
                                mode++;
                                //speak("Този предмет може да се открие");
                            }else{
                                textToSpeech.speak("Този предмет не може да се открие", TextToSpeech.QUEUE_FLUSH, null);
                                //speak("Този предмет не може да се открие");
                            }
                            reader.close();
                        }catch(IOException e){
                            e.printStackTrace();
                        }


                        speak("Здравейте, почвам да ви слушам");

                    }


                    / *for(String s : matches) {
                        editTextShowText.setText("word: "+s);
                    }*/
                }
            }

            override fun onEvent(i: Int, bundle: Bundle) {}
        })
        speechRecognizer.startListening(recognizerIntent)
    }

    private fun speak(text: String?) {
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
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