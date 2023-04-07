package com.example.tensorflowliteapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.io.IOException
import java.util.*

class ListeningThread(context: Context, text2Speech: Text2Speech, labels: List<String>) : Runnable{
    val context = context
    val text2Speech = text2Speech
    val translator = Translator()
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

                    look4object = translator.recognizeObject(lastCommand).toString()
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
                        if (look4object!=null){
                            isRecognisable = true
                        }
                    //editTextTextMultiLine.setText("isrecognizable= "+isrecognizable);
                    if (isRecognisable == true) {
                        //soundForListen.interrupt();
                        //soundForListen.stop();
                        text2Speech.speak(translator.translate("This object can be found. Starting search",language))
                        isRecognisable = false;
                        //                            capturePhoto();
//                        mode++
                        //speak("Този предмет може да се открие");
                    } else {
                        text2Speech.speak(translator.translate("This object can be found.", language))
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (stopCommand.match(lastCommand)) {
                    log("СТОП СПРИ ")
                    recognize = true
                    text2Speech.speak(translator.translate("Mode is stopped. What is your next command?", language))
                }

                // Влиза тук когато се каже думата навигация и започва да ориентира човека какво
                // има пред него
                if (findAllCommand.match(lastCommand)) {
                    log("НАВИГИРА МЕ НАВСЯКЪДЕ")
                    text2Speech.speak(translator.translate("Starting navigation", language))
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
                    if (lastCommand.contains("българ") || lastCommand.contains("bulgarian")) {
                        language = "bg"
                        text2Speech.speak("Сменяне на езика на български")
                    } else if (lastCommand.contains("англи") || lastCommand.contains("english")) {
                        language = "en"
                        text2Speech.speak("Changing language to english")
                    }
                }
                speechRecognizer.startListening(recognizerIntent)
            }

            override fun onPartialResults(bundle: Bundle) {
                val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            }

            override fun onEvent(i: Int, bundle: Bundle) {}
        })
        speechRecognizer.startListening(recognizerIntent)
    }
}