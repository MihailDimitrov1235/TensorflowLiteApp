package com.example.tensorflowliteapp

import com.example.tensorflowliteapp.message.*
import com.example.tensorflowliteapp.ml.EfficientdetLite2.Outputs

fun findObjectBG(outputs: Outputs, word: String, translator: Translator, threshold: Double): String{

    log(word)
    var result = "Има "
    var counter = count(outputs,word,threshold)
    if(counter == 0){
        if(word == "person"){
            return "Няма намерени хора"
        }
        return "Няма намерени " + translator.pluralBG[word]
    }
    val distanceCalulator = distanceCalulator()
        result += translator.number(counter,word,"bg") + " " + oneOrManyBG(word,counter,translator) + " пред телефона."
        outputs.detectionResultList.forEachIndexed { index, detectionResult ->
            if(detectionResult.categoryAsString.equals(word) && detectionResult.scoreAsFloat >= threshold) {
                result += translator.number(1, detectionResult.categoryAsString, "bg").toString() +
                        " " + distanceCalulator.distance(detectionResult.categoryAsString, size(detectionResult),"bg") + " и е "
                result += possitionBG(detectionResult)
            }
        }

    return result
}