package com.example.tensorflowliteapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import java.io.IOException
import java.util.*

class ListeningThread(context: Context, text2Speech: Text2Speech, objectDetector: ObjectDetector, handler: Handler, labels: List<String>) : Runnable{

    // modes
    val WAITING_MODE = 0
    val FIND_OBJECT_MODE = 1
    val FIND_ALL_OBJECTS_MODE = 2


    val context = context
    val text2Speech = text2Speech
    val translator = Translator()
    val labels = labels

    var mode = 0
    var look4object = ""
    var language = "bg"

    val handler = handler
    val detectionThread = DetectionThread(objectDetector,text2Speech,mode)


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
            override fun onReadyForSpeech(bundle: Bundle) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray) {}
            override fun onEndOfSpeech() {
                log("EndOfSpeech")
            }
            override fun onError(i: Int) {
                log("SpeechError")
                speechRecognizer.startListening(recognizerIntent)
            }

            override fun onResults(results: Bundle) {
                val result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (result!!.size == 0) return
                val lastCommand = result!![result!!.size - 1].lowercase(Locale.getDefault())
                log("result")
                log(lastCommand)

                // Влиза тук когато се каже човекът каже, че иска да му се намери даден обект
                if (findCommand.match(lastCommand)) {
                    handler.removeCallbacks(detectionThread)
                    log("ВЛИЗА В НАМЕРЕН ОБЕКТ------------- ")
                    if (look4object!=null){
                        text2Speech.speak(translator.translate("This object can be found.",language))
                        text2Speech.speak(translator.translate("Starting search",language))

                        detectionThread.mode = FIND_OBJECT_MODE
                        detectionThread.look4object = look4object
                        detectionThread.run()
                    }else{
                        text2Speech.speak(translator.translate("This object can NOT be found. Try with something else",language))
                    }
                }
                // Ако е казано "инструкции", казва на човека какви функционалности има приложението
                //log("/" + lastCommand + "/");
                if (instructionsCommand.match(lastCommand)) {
                    handler.removeCallbacks(detectionThread)
                    text2Speech.speak("",true)
                }
                if (stopCommand.match(lastCommand)) {
                    log("СТОП СПРИ ")
                    handler.removeCallbacks(detectionThread)
                    text2Speech.speak(translator.translate("Mode is stopped. What is your next command?", language))
                }

                // Влиза тук когато се каже думата навигация и започва да ориентира човека какво
                // има пред него
                if (findAllCommand.match(lastCommand)) {
                    handler.removeCallbacks(detectionThread)
                    log("НАВИГИРА МЕ НАВСЯКЪДЕ")
                    text2Speech.speak(translator.translate("Starting navigation", language))
                    look4object = "all"
                }
                // Влиза тук когато потребителят иска да излезе от програмата
                if (exitCommand.match(lastCommand)) {
                    handler.removeCallbacks(detectionThread)
                    log("ИЗЛЕЗЕ ОТ ПРОГРАМАТА")
                    System.exit(0)
                }
                if (languageCommand.match(lastCommand)) {
                    handler.removeCallbacks(detectionThread)
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