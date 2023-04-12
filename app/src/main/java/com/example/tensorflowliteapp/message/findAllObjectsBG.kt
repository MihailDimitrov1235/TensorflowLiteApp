package com.example.tensorflowliteapp.message

import android.annotation.SuppressLint
import com.example.tensorflowliteapp.ml.EfficientdetLite2.Outputs

@SuppressLint("SuspiciousIndentation")
fun findAllObjectsBG(outputs: Outputs, translator: Translator, threshold: Double): String{
    var result = "Има "
    var objects = listOf<String>()
    val counter = count(outputs,"all",threshold)
        if (counter == 0) {
            return "Няма намерени обекти"
        }
        if (counter == 1){
            result += "един обект намерен."
        }else if(counter == 2){
            result += "два обекта намерени."
        }else {
            result += counter.toString() + " обекта намерени."
        }
        outputs.detectionResultList.forEachIndexed { index, detectionResult ->
            if (!objects.contains(detectionResult.categoryAsString) && detectionResult.scoreAsFloat >= threshold) {
                objects += detectionResult.categoryAsString
                val brL = count(outputs, detectionResult.categoryAsString, threshold, "left")
                val brM = count(outputs, detectionResult.categoryAsString, threshold, "mid")
                val brR = count(outputs, detectionResult.categoryAsString, threshold, "right")
                if (brL > 0) {
                    result += translator.number(brL, detectionResult.categoryAsString, "bg")
                        .toString() + " " + oneOrManyBG(detectionResult.categoryAsString, brL, translator) + " вляво, "
                }
                if (brM > 0) {
                    result += translator.number(brM, detectionResult.categoryAsString, "bg")
                        .toString() + " " + oneOrManyBG(detectionResult.categoryAsString, brM, translator) + " посредата, "
                }
                if (brL > 0) {
                    result += translator.number(brR, detectionResult.categoryAsString, "bg")
                        .toString() + " " + oneOrManyBG(detectionResult.categoryAsString, brR, translator) + " вдясно, "
                }
            }

        }

    return result
}