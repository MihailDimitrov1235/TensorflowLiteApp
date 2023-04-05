package com.example.tensorflowliteapp

import android.Manifest
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    lateinit var labels:List<String>
    lateinit var imageProcessor: ImageProcessor
    lateinit var cameraDevice: CameraDevice
    lateinit var handler: Handler
    lateinit var textureView:TextureView
    lateinit var cameraManager:CameraManager
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap
    lateinit var model0: EfficientdetLite0
    lateinit var model1: EfficientdetLite1
    lateinit var model2: EfficientdetLite2
    lateinit var model: Mobilenetv1
    lateinit var number : Number
    lateinit var textView: TextView
    lateinit var txtKoltinAccelerometer : TextView
    lateinit var editTextTextPersonName : TextView
    private lateinit var sensorManager: SensorManager
    private var mode = 0
    private var textToSpeech: TextToSpeech? = null
    private lateinit var speechRecognizer: SpeechRecognizer
    private var recognizerIntent: Intent? = null
    private var captureRunning = false
    private var isAudioRecordPermissionGranted = false
    private var isCameraPermissionGranted = false
    private var isStoragePermissionGranted = false
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mode = 1
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName)
        mPermissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            isAudioRecordPermissionGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: isAudioRecordPermissionGranted
            isCameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: isCameraPermissionGranted
            isStoragePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isStoragePermissionGranted

        }
        requestPermission()
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

                bitmap = textureView.bitmap!!

                var image = TensorImage.fromBitmap(bitmap)
                image = imageProcessor.process(image)

                val outputs = model2.process(image)
                val locations = outputs.locationAsTensorBuffer.floatArray
//                val detectionResult = outputs.detectionResultList.get(0)
//                val location = detectionResult.locationAsRectF
//                val category = detectionResult.categoryAsString
//                val score = detectionResult.scoreAsFloat

                var mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true)

                val canvas = Canvas(mutableBitmap)

                val bitmapHeight = 1
                val bitmapWidth = 1

                paint.textSize = bitmapHeight/15f
                paint.strokeWidth = bitmapWidth/85f

                var idx = 0
                val threshold = 0.1
                var postProcessingObj = PostProcessing();
                outputs.detectionResultList.forEachIndexed { index, detectionResult ->
                    idx = index*4
                    val location = detectionResult.locationAsRectF
                    val category = detectionResult.categoryAsString
                    val score = detectionResult.scoreAsFloat


                    if (score >= threshold){

                        Log.d("LOCATION",location.left.toString())
                        Log.d("LOCATION",location.right.toString())
                        Log.d("LOCATION",location.top.toString())
                        Log.d("LOCATION",location.bottom.toString())
                        Log.d("CATEGORY",category)
                        Log.d("SCORE",score.toString())
//                        paint.setColor(Color.BLUE)
//                        paint.style = Paint.Style.STROKE
//                        canvas.drawRect(RectF(locations.get(idx+1)*bitmapWidth, locations.get(idx)*bitmapHeight, locations.get(idx+3)*bitmapWidth, locations.get(idx+2)*bitmapHeight),paint)
//                        paint.style = Paint.Style.FILL
//                        canvas.drawText(category,location.left*bitmapWidth, location.top*bitmapHeight,paint)

                    }

                }


            }
        }


        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        startSpeechRecognition();

    }


    private fun requestPermission() {
        isAudioRecordPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        isStoragePermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val permissionRequest: MutableList<String> = ArrayList()
        if (!isAudioRecordPermissionGranted) {
            permissionRequest.add(Manifest.permission.RECORD_AUDIO)
        }
        if (!isCameraPermissionGranted) {
            permissionRequest.add(Manifest.permission.CAMERA)
        }
        if (!isStoragePermissionGranted) {
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!permissionRequest.isEmpty()) {
            mPermissionResultLauncher!!.launch(permissionRequest.toTypedArray())
        }
    }

    fun log(text: String?) {
        //editTextTextMultiLine.setText(text + " "+ editTextTextMultiLine.getText());
    }
    private fun setUpSensorSuff(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(this,it,SensorManager.SENSOR_DELAY_FASTEST,SensorManager.SENSOR_DELAY_FASTEST)
        }

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
    private class Command     //        public String param;
        (val templates: Array<String>) {
        fun match(text: String): Boolean {
            for (template in templates) {
                if (text.contains(template)) {
                    return true
                }
            }
            return false
        }
    }
    companion object {
        // Това са шаблони за какви команди могат да се разпознаят
        private val commandLanguage = Command(arrayOf("смени език", " change language"))
        private val commandStop = Command(arrayOf("стоп", "спри", "стига", "stop"))
        private val commandInstructions =
            Command(arrayOf("инструкции", "помощ", "help", "instructions"))
        private val commandFind = Command(arrayOf("намери", "къде e", "where", "find"))
        private val commandFindAll = Command(arrayOf("навигирай", "navigate"))
        private val commandExit = Command(arrayOf("излез от програмата", "излез", "leave"))
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

    override fun onDestroy() {
        super.onDestroy()
        model.close()
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