package com.example.tensorflowliteapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.TextureView
import com.example.tensorflowliteapp.message.DetectionResultProcessor
import com.example.tensorflowliteapp.message.Translator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class ListeningThread(context: Context, text2Speech: Text2Speech, objectDetector: ObjectDetector, handler: Handler, textureView: TextureView, labels: List<String>) : Runnable{

    // modes
    val WAITING_MODE = 0
    val FIND_OBJECT_MODE = 1
    val FIND_ALL_OBJECTS_MODE = 2


    val context = context
    val text2Speech = text2Speech
    val detectionResultProcessor = DetectionResultProcessor()
    val translator = Translator()
    val objectDetector = objectDetector
    val textureView = textureView
    val labels = labels

    var mode = 0
    var look4object = ""
    var language = "bg"

    val handler = handler


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
            Command(arrayOf("излез", "leave", "exit"))

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
                log(translator.recognizeObject(lastCommand).toString())
                look4object = translator.recognizeObject(lastCommand).toString()

                // Влиза тук когато се каже човекът каже, че иска да му се намери даден обект
                if (findCommand.match(lastCommand)) {
                    log("ВЛИЗА В НАМЕРЕН ОБЕКТ------------- ")
                    if (look4object!=null){
                        text2Speech.speak(translator.translate("This object can be found.",language))
                        text2Speech.speak(translator.translate("Starting search",language))

                        mode = FIND_OBJECT_MODE
                        GlobalScope.launch {
                            while (mode == FIND_OBJECT_MODE) {
                                if (!text2Speech.isSpeaking()){
                                    val outputs = async { objectDetector.detect(textureView.bitmap!!) }
                                    log(look4object)
                                    text2Speech.speak(detectionResultProcessor.processResult(mode,outputs.await(),language, translator, look4object))
                                }
                            }
                        }

                    }else{
                        text2Speech.speak(translator.translate("This object can NOT be found. Try with something else",language))
                    }
                }
                // Ако е казано "инструкции", казва на човека какви функционалности има приложението
                //log("/" + lastCommand + "/");
                if (instructionsCommand.match(lastCommand)) {
                    mode = WAITING_MODE
                    text2Speech.speak("",true)
                }
                if (stopCommand.match(lastCommand)) {
                    log("СТОП СПРИ ")
                    mode = WAITING_MODE
                    text2Speech.speak(translator.translate("Mode is stopped. What is your next command?", language))
                }

                // Влиза тук когато се каже думата навигация и започва да ориентира човека какво
                // има пред него
                if (findAllCommand.match(lastCommand)) {
                    mode = FIND_ALL_OBJECTS_MODE
                    log("НАВИГИРА МЕ НАВСЯКЪДЕ")
                    text2Speech.speak(translator.translate("Starting navigation", language))
                    look4object = "all"
                    GlobalScope.launch {
                        while (mode == FIND_ALL_OBJECTS_MODE) {
                            if (!text2Speech.isSpeaking()){
                                val outputs = async { objectDetector.detect(textureView.bitmap!!) }
                                text2Speech.speak(detectionResultProcessor.processResult(mode,outputs.await(),language,translator))
                            }
                        }
                    }
                }
                // Влиза тук когато потребителят иска да излезе от програмата
                if (exitCommand.match(lastCommand)) {
                    mode = WAITING_MODE
                    log("ИЗЛЕЗЕ ОТ ПРОГРАМАТА")
                    System.exit(0)
                }
                if (languageCommand.match(lastCommand)) {
                    mode = WAITING_MODE
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