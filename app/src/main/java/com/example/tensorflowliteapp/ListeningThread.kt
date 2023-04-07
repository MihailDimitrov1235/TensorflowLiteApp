package com.example.tensorflowliteapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import org.tensorflow.lite.support.common.FileUtil
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.*

class ListeningThread(context: Context, text2Speech: Text2Speech, labels: List<String>) : Runnable{
    val context = context
    val text2Speech = text2Speech
    val labels = labels

    var look4object = ""
    var recognize = false
    var isRecognisable = false
    var language = "bg"


    override fun run() {
        val languageCommand =
            Command(arrayOf("смени език", " change language"))
        val stopCommand = Command(arrayOf("стоп", "спри", "стига", "stop"))
        val instructionsCommand =
            Command(arrayOf("инструкции", "помощ", "help", "instructions"))
        val findCommand =
            Command(arrayOf("намери", "къде e", "where", "find"))
        val findAllCommand = Command(arrayOf("навигирай", "navigate"))
        val exitCommand =
            Command(arrayOf("излез от програмата", "излез", "leave"))

        //Toast.makeText(this, "VLiza v startSpeechRecognition()", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "VLiza v startSpeechRecognition()", Toast.LENGTH_SHORT).show();
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        //Set up the intent for speech recognition
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
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
                val result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result!!.size == 0) return
                val lastCommand = result!![result!!.size - 1].lowercase(Locale.getDefault())

                // Влиза тук когато се каже човекът каже, че иска да му се намери даден обект
                if (findCommand.match(lastCommand)) {
                    log("ВЛИЗА В НАМЕРЕН ОБЕКТ------------- ")

                    look4object = lastCommand
//                    recognize = true
                }
                // Ако е казано "инструкции", казва на човека какви функционалности има приложението
                //log("/" + lastCommand + "/");
                if (instructionsCommand.match(lastCommand)) {
//                    captureRunning = false
                    //mode = 1
                    //                  log("instructions");
                    text2Speech.speak("",true)
                }
                // Спира приложението да снима

                //Toast.makeText(CapturePictureAutomatically.this, "Vliza v mode 2", Toast.LENGTH_SHORT).show();

                //Toast.makeText(CapturePictureAutomatically.this, "Vliza v mode 2", Toast.LENGTH_SHORT).show();
//                val assetManager = assets
                try {
                    for (i in labels){
                        if (result!!.contains("намери " + i)){
                            isRecognisable = true
                            break
                        }
                    }
                    //editTextTextMultiLine.setText("isrecognizable= "+isrecognizable);
                    if (isRecognisable == true) {
                        //soundForListen.interrupt();
                        //soundForListen.stop();
                        text2Speech.speak("Този предмет може да се открие. Почва търсене")
                        isRecognisable = false;
                        // Param is optional, to run task on UI thread.
//                        handler = Handler(Looper.getMainLooper())
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
//                        mode++
                        //speak("Този предмет може да се открие");
                    } else {
                        text2Speech.speak("Този предмет не може да се открие")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (stopCommand.match(lastCommand)) {
                    log("СТОП СПРИ ")
                    recognize = true
                    text2Speech.speak("Спряхте режима. Какво искате да направя за вас?")
                }

                // Влиза тук когато се каже думата навигация и започва да ориентира човека какво
                // има пред него
                if (findAllCommand.match(lastCommand)) {
                    log("НАВИГИРА МЕ НАВСЯКЪДЕ")
                    text2Speech.speak("Пускане на навигация.")
                    look4object = "all"
                    recognize = false
                }
                // Влиза тук когато потребителят иска да излезе от програмата
                if (exitCommand.match(lastCommand)) {
                    log("ИЗЛЕЗЕ ОТ ПРОГРАМАТА")
                    System.exit(0)
                }
                if (languageCommand.match(lastCommand)) {
                    text2Speech.speak("")
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
}