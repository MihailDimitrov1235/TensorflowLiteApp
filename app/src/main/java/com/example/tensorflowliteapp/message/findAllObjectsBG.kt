package com.example.tensorflowliteapp.message

import android.annotation.SuppressLint
import com.example.tensorflowliteapp.Result

@SuppressLint("SuspiciousIndentation")
fun findAllObjectsBG(outputs: MutableList<Result>, translator: Translator): String{
    var result = "Има "
    var objects = listOf<String>()
    val counter = count(outputs,"all")
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
        outputs.forEach { detectionResult ->
            if (!objects.contains(detectionResult.getCategory())) {
                objects += detectionResult.getCategory()
                val brL = count(outputs, detectionResult.getCategory(), "left")
                val brM = count(outputs, detectionResult.getCategory(), "mid")
                val brR = count(outputs, detectionResult.getCategory(), "right")
                if (brL > 0) {
                    result += translator.number(brL, detectionResult.getCategory(), "bg")
                        .toString() + " " + oneOrManyBG(detectionResult.getCategory(), brL, translator) + " вляво, "
                }
                if (brM > 0) {
                    result += translator.number(brM, detectionResult.getCategory(), "bg")
                        .toString() + " " + oneOrManyBG(detectionResult.getCategory(), brM, translator) + " посредата, "
                }
                if (brL > 0) {
                    result += translator.number(brR, detectionResult.getCategory(), "bg")
                        .toString() + " " + oneOrManyBG(detectionResult.getCategory(), brR, translator) + " вдясно, "
                }
            }

        }

    return result
}