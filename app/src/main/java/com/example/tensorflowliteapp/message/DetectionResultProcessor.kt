package com.example.tensorflowliteapp.message

import com.example.tensorflowliteapp.findObjectBG
import com.example.tensorflowliteapp.log
import com.example.tensorflowliteapp.ml.EfficientdetLite2.Outputs

class DetectionResultProcessor {

    val WAITING_MODE = 0
    val FIND_OBJECT_MODE = 1
    val FIND_ALL_OBJECTS_MODE = 2
    val threshold = 0.5


    fun processResult(mode: Int, outputs: Outputs, language: String, translator:Translator, word: String = ""): String{
        if (mode == FIND_OBJECT_MODE){
            if (language == "bg"){
                log("Find Object")
                return findObjectBG(outputs,word,translator,threshold)
            }
        }else if (mode == FIND_ALL_OBJECTS_MODE){
            if (language == "bg"){
                return findAllObjectsBG(outputs,translator,threshold)
            }
        }
        return ""
    }
}