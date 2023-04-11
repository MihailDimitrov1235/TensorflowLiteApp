package com.example.tensorflowliteapp.message

import com.example.tensorflowliteapp.ml.EfficientdetLite2.Outputs

fun findAllObjectsBG(outputs: Outputs, translator: Translator): String{
    var result = "Има "
    var objects = listOf<String>()
        if (outputs.detectionResultList.size == 0) {
            return "Няма намерени обекти"
        }
        result += outputs.detectionResultList.size.toString() + " обекта намерни."
        outputs.detectionResultList.forEachIndexed { index, detectionResult ->
            if (!objects.contains(detectionResult.categoryAsString)) {

            }
            objects += detectionResult.categoryAsString
            var brL = count(outputs, detectionResult.categoryAsString, "left")
            var brM = count(outputs, detectionResult.categoryAsString, "left")
            var brR = count(outputs, detectionResult.categoryAsString, "left")
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

    return result
}