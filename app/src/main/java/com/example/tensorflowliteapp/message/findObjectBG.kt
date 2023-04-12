package com.example.tensorflowliteapp

import com.example.tensorflowliteapp.message.Translator
import com.example.tensorflowliteapp.message.count
import com.example.tensorflowliteapp.message.possitionBG
import com.example.tensorflowliteapp.message.size
import com.example.tensorflowliteapp.ml.EfficientdetLite2.Outputs

fun findObjectBG(outputs: Outputs, word: String, translator: Translator, threshold: Double): String{

    var result = "Има "

    if(outputs.detectionResultList.size == 0){
        if(word == "person"){
            return "Няма намерени хора"
        }
        return "Няма намерени " + translator.plural[word]
    }
    var br = count(outputs,outputs.detectionResultList[0].categoryAsString)
    val distanceCalulator = distanceCalulator()
    if(br > 1){
        result += br.toString() + " " +translator.plural[outputs.detectionResultList[0].categoryAsString] + " пред телефона."

        outputs.detectionResultList.forEachIndexed { index, detectionResult ->
            if(detectionResult.categoryAsString.equals(word) && detectionResult.scoreAsFloat >= threshold) {
                result += translator.number(1, detectionResult.categoryAsString, "bg")
                    .toString() + " " + distanceCalulator.distance(detectionResult.categoryAsString, size(detectionResult),"bg") + " и е "
                result += possitionBG(detectionResult)
            }
        }
    }else{
        if (outputs.detectionResultList[0].categoryAsString.equals(word) && outputs.detectionResultList[0].scoreAsFloat >= threshold) {
            result += translator.number(
                1,
                outputs.detectionResultList[0].categoryAsString,
                "bg"
            ) + " " + translator.translateToBG[outputs.detectionResultList[0].categoryAsString] + " намерен " + distanceCalulator.distance(
                outputs.detectionResultList[0].categoryAsString,
                size(outputs.detectionResultList[0]),
                "bg"
            ) + " и "
            result += possitionBG(outputs.detectionResultList[0])
        }
    }

    return result

}