package com.example.tensorflowliteapp.message

import com.example.tensorflowliteapp.distanceCalulator
import com.example.tensorflowliteapp.ml.EfficientdetLite2

class messageBG {
    val translator = Translator()
    fun messageBg(arr: EfficientdetLite2.Outputs, word: String): String {
        var result = "Има "
        var objects = listOf<String>()
        if (word == "all") {
            if (arr.detectionResultList.size == 0) {
                return "Няма намерени обекти"
            }
            result += arr.detectionResultList.size.toString() + " обекта намерни."
            arr.detectionResultList.forEachIndexed { index, detectionResult ->
                if (!objects.contains(detectionResult.categoryAsString)) {

                }
                objects += detectionResult.categoryAsString
                var brL = count(arr, detectionResult.categoryAsString, "left")
                var brM = count(arr, detectionResult.categoryAsString, "left")
                var brR = count(arr, detectionResult.categoryAsString, "left")
                if (brL > 0) {
                    result += translator.number(brL, detectionResult.categoryAsString, "bg")
                        .toString() + " "
                    if (brL > 1) {
                        result += translator.plural[detectionResult.categoryAsString]
                    } else {
                        result += translator.translateToBG[detectionResult.categoryAsString]
                    }
                    result += " вляво, "
                }
                if (brR > 0) {
                    result += translator.number(brR, detectionResult.categoryAsString, "bg")
                        .toString() + " "
                    if (brR > 1) {
                        result += translator.plural[detectionResult.categoryAsString]
                    } else {
                        result += translator.translateToBG[detectionResult.categoryAsString]
                    }
                    result += " вдясно, "
                }
            }

        }
        return result
    }

}