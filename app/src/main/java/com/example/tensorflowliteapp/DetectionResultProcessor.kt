package com.example.tensorflowliteapp

import com.example.tensorflowliteapp.ml.EfficientdetLite2.Outputs

class DetectionResultProcessor {

    val WAITING_MODE = 0
    val FIND_OBJECT_MODE = 1
    val FIND_ALL_OBJECTS_MODE = 2


    fun processResult(mode: Int, outputs: Outputs, language: String, word: String): String{
        if (mode == FIND_OBJECT_MODE){
            if (language == "bg"){
                return findObjectBG(outputs,word)
            }
        }
        return ""
    }
}