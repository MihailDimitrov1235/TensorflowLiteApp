package com.example.tensorflowliteapp

import com.example.tensorflowliteapp.ml.EfficientdetLite2
import java.lang.Math.abs

class messageBG {
    val translator = Translator()
    fun messageBg(arr : EfficientdetLite2.Outputs, word: String): String {
        var result = "Има "
        var objects = listOf<String>()
        if(word == "all"){
            if(arr.detectionResultList.size == 0) {
                return "Няма намерени обекти"
            }
            result += arr.detectionResultList.size.toString() + " обекта намерни."
            arr.detectionResultList.forEachIndexed { index, detectionResult ->
                 if (!objects.contains(detectionResult.categoryAsString)){

                 }
                 objects += detectionResult.categoryAsString
                 var brL = count(arr,detectionResult.categoryAsString,"left")
                 var brM = count(arr,detectionResult.categoryAsString,"left")
                 var brR = count(arr,detectionResult.categoryAsString,"left")
                 if(brL > 0){
                     result += translator.number(brL,detectionResult.categoryAsString, "bg").toString() + " "
                     if(brL > 1){
                         result += translator.plural[detectionResult.categoryAsString]
                     }else{
                         result += translator.translateToBG[detectionResult.categoryAsString]
                     }
                     result += " вляво, "
                 }
                 if(brR > 0){
                     result += translator.number(brR,detectionResult.categoryAsString,"bg").toString() + " "
                     if(brR > 1){
                         result += translator.plural[detectionResult.categoryAsString]
                     }else{
                         result += translator.translateToBG[detectionResult.categoryAsString]
                     }
                     result += " вдясно, "
                 }
            }

        }else{
            if(arr.detectionResultList.size == 0){
                if(word == "person"){
                    return "Няма намерени хора"
                }
                return "Няма намерени " + translator.plural[word]
            }
            var br = count(arr,arr.detectionResultList[0].categoryAsString)
            val distanceCalulator = distanceCalulator()
            if(br > 1){
                result += br.toString() + " " +translator.plural[arr.detectionResultList[0].categoryAsString] + " пред телефона."

                arr.detectionResultList.forEachIndexed { index, detectionResult ->
                    result += translator.number(1,detectionResult.categoryAsString, "bg").toString() + " " +distanceCalulator.distance(detectionResult.categoryAsString, size(detectionResult),"bg") + " и е "
                    result += pos(detectionResult)
                }
            }else{
                result += translator.number(1,arr.detectionResultList[0].categoryAsString,"bg") + " " + translator.translateToBG[arr.detectionResultList[0].categoryAsString] + " намерен " + distanceCalulator.distance(arr.detectionResultList[0].categoryAsString,size(arr.detectionResultList[0]), "bg") + " и "
                result += pos(arr.detectionResultList[0])
            }

        }
        return result
    }
    fun size(detectionResult : EfficientdetLite2.DetectionResult): Float {
        return abs(detectionResult.locationAsRectF.left - detectionResult.locationAsRectF.right) * abs(detectionResult.locationAsRectF.top - detectionResult.locationAsRectF.bottom)
    }
    fun count(arr : EfficientdetLite2.Outputs, word: String, loc: String = "all"): Int {
        var br = 0
        var gridCell : Array<Int?>
        var postProcessing = PostProcessing()
        arr.detectionResultList.forEachIndexed { index, detectionResult ->
            gridCell = postProcessing.gridPostion(detectionResult)
            if (detectionResult.categoryAsString == word) {
                if(loc == "left" && gridCell[0]!! <= 1){
                    br += 1
                }else if(loc == "mid" && gridCell[0] == 2){
                    br += 1
                }else if(loc == "right" && gridCell[0]!! >= 3){
                    br += 1
                }else if(loc == "all"){
                    br += 1
                }
            }
        }


        return br
    }

    fun pos(detectionResult: EfficientdetLite2.DetectionResult): String {
        var postProcessing = PostProcessing()
        var gridCell : Array<Int?>
        gridCell = postProcessing.gridPostion(detectionResult)
        var result = ""
        if(gridCell[0] == 0){
            result += "вляво"
        }
        if(gridCell[0] == 1){
            result += "леко вляво"
        }
        if(gridCell[0] == 2){
            result += "по средата"
        }
        if(gridCell[0] == 3){
            result += "леко вдясно"
        }
        if(gridCell[0] == 4){
            result += "вдясно"
        }

        if(gridCell[1] == 0){
            result += " нагоре."
        }
        if(gridCell[1] == 1){
            result += " и леко нагоре. "
        }
        if(gridCell[1] == 2){
            result += ". "
        }
        if(gridCell[1] == 3){
            result += " и леко надолу. "
        }
        if(gridCell[1] == 4){
            result += " и долу. "
        }
        return result
    }
}