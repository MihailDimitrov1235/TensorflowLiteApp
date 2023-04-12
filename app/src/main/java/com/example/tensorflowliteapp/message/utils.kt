package com.example.tensorflowliteapp.message

import com.example.tensorflowliteapp.ml.EfficientdetLite2

fun size(detectionResult : EfficientdetLite2.DetectionResult): Float {
    return Math.abs(detectionResult.locationAsRectF.left - detectionResult.locationAsRectF.right) * Math.abs(
        detectionResult.locationAsRectF.top - detectionResult.locationAsRectF.bottom
    )
}

fun oneOrManyBG(word: String, counter: Int, translator: Translator): String? {
    if (counter > 1){
        return translator.pluralBG[word]
    }
    return translator.translate(word,"bg")
}

fun count(arr : EfficientdetLite2.Outputs, word: String, threshold: Double, loc: String = "all"): Int {
    var counter = 0
    var gridCell : Array<Int?>
    var postProcessing = PostProcessing()
    arr.detectionResultList.forEachIndexed { index, detectionResult ->
        if (detectionResult.scoreAsFloat >= threshold) {
            gridCell = postProcessing.gridPostion(detectionResult)
            if (detectionResult.categoryAsString == word || word == "all") {
                if (loc == "left" && gridCell[0]!! <= 1) {
                    counter += 1
                } else if (loc == "mid" && gridCell[0] == 2) {
                    counter += 1
                } else if (loc == "right" && gridCell[0]!! >= 3) {
                    counter += 1
                } else if (loc == "all") {
                    counter += 1
                }
            }
        }
    }
    return counter
}

fun possitionBG(detectionResult: EfficientdetLite2.DetectionResult): String {
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