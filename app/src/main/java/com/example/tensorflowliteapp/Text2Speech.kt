package com.example.tensorflowliteapp

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class Text2Speech(context: Context) {

    val introductoryWords = "Добър ден Стартира се програма блайнд хелпър. Какво искате да " +
    "направя за вас. За да разберете повече, кажете думата Инструкции"

    val instrucionWords = "Ако искате да намерите даден предмет, трябва да кажете думата намери и" +
    " след нея да кажете обекта, който търсите. Например казвате Намери човек или " +
    "казвате търси котка. Друга функция на приложението е да ви навигира, тоест да" +
    "каже какво има пред вас и да ви предупреди за него. За да влезете в този реажим" +
    "кажете думата Навигация. В него например приложението ще Ви казва какво да " +
    "направите, ако има предмет пред вас, за да стигнете вървите безопасно напред."

    lateinit var textToSpeech: TextToSpeech
    init {
        textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS){
                var result = textToSpeech.setLanguage(Locale("bg-BG"))
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED){
                    Log.i("TTS", "Lang supported")
                    textToSpeech.speak(introductoryWords,TextToSpeech.QUEUE_FLUSH,null,"")
                }else{
                    Log.e("TTS", "Lang not supported")
                }
            }else{

            }
        })
    }
    fun speak(text:String){
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }
}