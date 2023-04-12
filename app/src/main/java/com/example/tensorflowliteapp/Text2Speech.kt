package com.example.tensorflowliteapp

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class Text2Speech(context: Context) {

    val introductoryWordsBG = "Добър ден Стартира се програма блайнд хелпър. Какво искате да " +
    "направя за вас. За да разберете повече, кажете думата Инструкции"

    val introductoryWordsEN = "not " +
            "implemented"

    val instrucionWordsBG = "Ако искате да намерите даден предмет, трябва да кажете думата намери и" +
    " след нея да кажете обекта, който търсите. Например казвате Намери човек или " +
    "казвате търси котка. Друга функция на приложението е да ви навигира, тоест да" +
    "каже какво има пред вас и да ви предупреди за него. За да влезете в този реажим" +
    "кажете думата Навигация. В него например приложението ще Ви казва какво да " +
    "направите, ако има предмет пред вас, за да стигнете вървите безопасно напред."

    val instrucionWordsEN = "not" +
            "implemented"

    lateinit var textToSpeech: TextToSpeech
    var lang:String = "bg"

    fun setLanguage(lang:String){
        this.lang = lang
    }
    init {
        textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS){
                var result = textToSpeech.setLanguage(Locale("bg-BG"))
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED){
                    Log.i("TTS", "Lang supported")
                    if (lang == "bg"){
                        textToSpeech.speak(introductoryWordsBG,TextToSpeech.QUEUE_FLUSH,null,"")
                    }else{
                        textToSpeech.speak(introductoryWordsEN,TextToSpeech.QUEUE_FLUSH,null,"")
                    }
                }else{
                    Log.e("TTS", "Lang not supported")
                }
            }else{

            }
        })
    }
    fun speak(text: String?, instructions: Boolean = false){
        if (instructions){
            if (lang == "bg") {
                textToSpeech.speak(instrucionWordsBG, TextToSpeech.QUEUE_FLUSH, null, "")
            }else{
                textToSpeech.speak(instrucionWordsEN, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    fun isSpeaking():Boolean{
        return textToSpeech.isSpeaking
    }
}