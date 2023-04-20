package com.example.tensorflowliteapp.message

import android.hardware.SensorEvent
import com.example.tensorflowliteapp.Result
import com.example.tensorflowliteapp.findObjectBG

class DetectionResultProcessor {

    val WAITING_MODE = 0
    val FIND_OBJECT_MODE = 1
    val FIND_ALL_OBJECTS_MODE = 2

    var sensorEvent: SensorEvent? = null


    fun processResult(mode: Int, outputs: MutableList<Result>, language: String, translator:Translator, word: String = ""): String{
        if (mode == FIND_OBJECT_MODE){
            if (language == "bg"){
                return findObjectBG(outputs,word,translator, sensorEvent)
            }
        }else if (mode == FIND_ALL_OBJECTS_MODE){
            if (language == "bg"){
                return findAllObjectsBG(outputs,translator)
            }
        }
        return ""
    }
}